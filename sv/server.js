const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
const PORT = 3000;

// Middleware
app.use(cors());
app.use(bodyParser.json());

// Database tạm thời (lưu trong RAM)
let orders = [];
let orderIdCounter = 1;

// API Endpoints
// 1. Tạo đơn hàng mới (App Android gọi)
app.post('/api/orders', (req, res) => {
    const { items } = req.body; // Danh sách món ăn từ app

    const newOrder = {
        id: orderIdCounter++,
        items: items,
        status: 'Đang xử lý',
        createdAt: new Date()
    };

    orders.push(newOrder);
    res.json({ 
        success: true,
        orderId: newOrder.id,
        message: `Đơn hàng ${newOrder.id} đang xử lý.`
    });
});

// 2. Lấy danh sách đơn hàng (Web App gọi)
app.get('/api/orders', (req, res) => {
    res.json({
        success: true,
        orders: orders.filter(order => order.status !== 'Xong') // Ẩn đơn đã hoàn thành
    });
});

// 3. Cập nhật trạng thái đơn (Web App gọi)
app.put('/api/orders/:id', (req, res) => {
    const { id } = req.params;
    const { status } = req.body;

    const order = orders.find(order => order.id === Number(id));
    if (!order) {
        return res.status(404).json({ success: false, message: 'Đơn hàng không tồn tại' });
    }

    order.status = status;
    res.json({ 
        success: true,
        message: `Đơn hàng ${id} đã chuyển sang trạng thái: ${status}`
    });

    // Gửi thông báo đến app Android ở đây (sẽ triển khai sau)
});

// Khởi động server
app.listen(PORT, () => {
    console.log(`Server đang chạy tại http://localhost:${PORT}`);
});