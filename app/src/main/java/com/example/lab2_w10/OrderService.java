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
    private static final String SERVER_URL = "http://10.0.2.2:3000/api/order";

    // Hằng số cho broadcast
    public static final String ACTION_ORDER_RESPONSE = "com.example.lab2_w10.ORDER_RESPONSE";
    public static final String EXTRA_ORDER_ID = "order_id";
    public static final String EXTRA_DISH_NAME = "dish_name";

    private RequestQueue queue; // Khai báo RequestQueue ở cấp class

    public OrderService() {
        super("OrderService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(this); // Khởi tạo queue trong onCreate
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

            sendOrderToServer(orderData, dishName); // Truyền thêm dishName
        }
    }

    private void sendOrderToServer(JSONObject orderData, String dishName) {
        Log.d(TAG, "Dữ liệu gửi đi: " + orderData.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                SERVER_URL,
                orderData,
                response -> {
                    Log.d(TAG, "Phản hồi từ server: " + response.toString());
                    try {
                        int orderId = response.getInt("order_id");
                        // Gửi broadcast với cả order_id và dish_name
                        Intent broadcastIntent = new Intent(ACTION_ORDER_RESPONSE);
                        broadcastIntent.putExtra(EXTRA_ORDER_ID, orderId);
                        broadcastIntent.putExtra(EXTRA_DISH_NAME, dishName);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (queue != null) {
            queue.cancelAll(TAG); // Hủy các request khi service bị hủy
        }
    }
}