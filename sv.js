const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const { v4: uuidv4 } = require('uuid');

const app = express();
const PORT = 3000;

// Middleware
app.use(cors());
app.use(bodyParser.json());

// Database mẫu (trong thực tế nên dùng database thật)
let orders = {};
let orderStatus = {};

// API endpoints
app.post('/api/order', (req, res) => {
    const { tableId, items } = req.body;
    
    if (!tableId || !items || items.length === 0) {
        return res.status(400).json({ error: 'Thiếu thông tin đơn hàng' });
    }

    // Tạo ID đơn hàng ngẫu nhiên từ 1-1000
    const orderId = Math.floor(Math.random() * 1000) + 1;
    
    // Lưu đơn hàng
    orders[orderId] = {
        tableId,
        items,
        createdAt: new Date()
    };
    
    // Đặt trạng thái ban đầu
    orderStatus[orderId] = 'Đang xử lý';
    
    res.json({ 
        success: true,
        orderId,
        message: `Đơn hàng ${orderId} của bạn đang xử lý.`
    });
});

app.get('/api/order/status/:orderId', (req, res) => {
    const { orderId } = req.params;
    
    if (!orders[orderId]) {
        return res.status(404).json({ error: 'Không tìm thấy đơn hàng' });
    }
    
    res.json({
        orderId,
        status: orderStatus[orderId]
    });
});

app.put('/api/order/update-status/:orderId', (req, res) => {
    const { orderId } = req.params;
    const { status } = req.body;
    
    if (!orders[orderId]) {
        return res.status(404).json({ error: 'Không tìm thấy đơn hàng' });
    }
    
    const validStatuses = ['Đang xử lý', 'Sắp xong', 'Xong'];
    if (!validStatuses.includes(status)) {
        return res.status(400).json({ error: 'Trạng thái không hợp lệ' });
    }
    
    orderStatus[orderId] = status;
    
    res.json({
        success: true,
        message: `Cập nhật trạng thái đơn hàng ${orderId} thành ${status}`
    });
});

// API cho thông tin nhóm
app.get('/api/about-us', (req, res) => {
    res.json({
        team: [
            { studentId: 'MSSV1', name: 'Họ tên thành viên 1' },
            { studentId: 'MSSV2', name: 'Họ tên thành viên 2' },
            { studentId: 'MSSV3', name: 'Họ tên thành viên 3' }
        ],
        contact: 'foodorder@cntt.io'
    });
});

// Khởi động server
app.listen(PORT, () => {
    console.log(`Server đang chạy trên cổng ${PORT}`);
});