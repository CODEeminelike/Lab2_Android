const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
const PORT = 3000;

// Middleware
app.use(cors());
app.use(bodyParser.json());

// Dữ liệu mẫu
let orders = [];
let orderIdCounter = 1;

// API Endpoints

// Nhận đơn hàng từ app Android
app.post('/api/orders', (req, res) => {
    const { items, tableNumber } = req.body;
    
    if (!items || !tableNumber) {
        return res.status(400).json({ error: 'Thiếu thông tin đơn hàng' });
    }
    
    const newOrder = {
        id: orderIdCounter++,
        items,
        tableNumber,
        status: 'Đang xử lý',
        createdAt: new Date()
    };
    
    orders.push(newOrder);
    
    // Giới hạn orderIdCounter từ 1 đến 1000
    if (orderIdCounter > 1000) {
        orderIdCounter = 1;
    }
    
    // Gửi thông báo đến app Android
    // Trong thực tế, bạn có thể sử dụng Firebase Cloud Messaging hoặc WebSocket
    // Ở đây chỉ trả về ID đơn hàng
    
    res.json({ 
        orderId: newOrder.id,
        message: `Đơn hàng ${newOrder.id} của bạn đang xử lý.`
    });
});

// Cập nhật trạng thái đơn hàng từ web app
app.put('/api/orders/:id/status', (req, res) => {
    const { id } = req.params;
    const { status } = req.body;
    
    const orderIndex = orders.findIndex(order => order.id === parseInt(id));
    
    if (orderIndex === -1) {
        return res.status(404).json({ error: 'Đơn hàng không tồn tại' });
    }
    
    orders[orderIndex].status = status;
    
    // Nếu trạng thái là "xong", xóa đơn hàng khỏi danh sách
    if (status === 'xong') {
        orders.splice(orderIndex, 1);
    }
    
    // Gửi thông báo đến app Android
    // Trong thực tế, bạn cần triển khai push notification ở đây
    
    res.json({ 
        success: true,
        message: `Đơn hàng ${id} đã được cập nhật trạng thái thành ${status}`
    });
});

// Lấy danh sách đơn hàng cho web app
app.get('/api/orders', (req, res) => {
    res.json(orders);
});

// Web App Routes

// Trang chủ giới thiệu nhà hàng
app.get('/', (req, res) => {
    res.sendFile(__dirname + '/public/index.html');
});

// Trang quản lý đơn hàng
app.get('/orders', (req, res) => {
    res.sendFile(__dirname + '/public/orders.html');
});

// Deep link cho app Android
app.get('/deep-link', (req, res) => {
    // Trong thực tế, bạn sẽ redirect đến intent của Android app
    res.json({ 
        message: 'Mở ứng dụng Food Ordering',
        intent: 'foodordering://menu'
    });
});

// Khởi động server
app.listen(PORT, () => {
    console.log(`Server đang chạy trên port ${PORT}`);
});