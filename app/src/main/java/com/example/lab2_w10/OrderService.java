package com.example.lab2_w10;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderService extends IntentService {
    private static final String TAG = "OrderService";
    // URL server: dùng 10.0.2.2 cho emulator, hoặc IP máy tính cho thiết bị thật
    private static final String SERVER_URL = "http://10.0.2.2:3000/api/order"; // Cập nhật nếu dùng thiết bị thật

    // Hằng số cho broadcast
    public static final String ACTION_ORDER_RESPONSE = "com.example.lab2_w10.ORDER_RESPONSE";
    public static final String EXTRA_ORDER_ID = "order_id";

    public OrderService() {
        super("OrderService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String dishName = intent.getStringExtra("dish_name");
            int price = intent.getIntExtra("price", 0);

            JSONObject orderData = new JSONObject();
            try {
                orderData.put("dish_name", dishName);
                orderData.put("price", price);
            } catch (JSONException e) {
                Log.e(TAG, "Lỗi tạo JSON: " + e.getMessage());
                return;
            }

            sendOrderToServer(orderData);
        }
    }

    private void sendOrderToServer(JSONObject orderData) {
        Log.d(TAG, "Dữ liệu gửi đi: " + orderData.toString());
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                SERVER_URL,
                orderData,
                response -> {
                    Log.d(TAG, "Phản hồi từ server: " + response.toString());
                    try {
                        int orderId = response.getInt("order_id");
                        // Gửi broadcast với order_id
                        Intent broadcastIntent = new Intent(ACTION_ORDER_RESPONSE);
                        broadcastIntent.putExtra(EXTRA_ORDER_ID, orderId);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
                    } catch (JSONException e) {
                        Log.e(TAG, "Lỗi parse response: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e(TAG, "Lỗi gửi request: " + (error.getMessage() != null ? error.getMessage() : "Không có thông tin lỗi"));
                }
        );
        queue.add(jsonObjectRequest);
    }
}