package com.example.lab2_w10;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

// Activity hiển thị menu món ăn và xử lý deep link
public class MenuActivity extends AppCompatActivity {
    private RecyclerView recyclerView; // RecyclerView hiển thị danh sách món ăn
    private DishAdapter adapter; // Adapter cho danh sách món ăn
    private List<Dish> dishList; // Danh sách món ăn
    private DrawerLayout drawerLayout; // DrawerLayout cho menu điều hướng
    private BroadcastReceiver orderReceiver; // Receiver cho broadcast từ OrderService

    private static final String CHANNEL_ID = "order_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Xử lý Deep Link trước khi khởi tạo giao diện
        handleDeepLink(getIntent());

        // Khởi tạo Toolbar làm ActionBar
        initToolbar();

        // Thiết lập Navigation Drawer
        initNavigationDrawer();

        // Khởi tạo RecyclerView để hiển thị danh sách món ăn
        initRecyclerView();

        // Tạo Notification Channel cho thông báo
        createNotificationChannel();

        // Đăng ký BroadcastReceiver để nhận order_id từ OrderService
        orderReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int orderId = intent.getIntExtra(OrderService.EXTRA_ORDER_ID, -1);
                if (orderId != -1) {
                    showOrderNotification(orderId);
                } else {
                    Toast.makeText(context, "Lỗi nhận ID đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }
        };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(orderReceiver, new IntentFilter(OrderService.ACTION_ORDER_RESPONSE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy đăng ký BroadcastReceiver để tránh rò rỉ bộ nhớ
        LocalBroadcastManager.getInstance(this).unregisterReceiver(orderReceiver);
    }

    // Tạo Notification Channel cho Android 8.0 trở lên
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Order Notifications";
            String description = "Thông báo trạng thái đơn hàng";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Hiển thị thông báo với order_id
    private void showOrderNotification(int orderId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_menu) // Thay bằng icon của bạn nếu có
                .setContentTitle("Xác nhận đơn hàng")
                .setContentText("Đơn hàng " + orderId + " của bạn đang xử lý.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    // region Helper Methods
    // Xử lý Deep Link từ web (foodordering://menu)
    private void handleDeepLink(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getData() != null) {
            Uri data = intent.getData();
            try {
                if ("foodordering".equals(data.getScheme()) && "menu".equals(data.getHost())) {
                    Toast.makeText(this, "Mở menu từ liên kết web", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Liên kết không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi xử lý liên kết", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Thiết lập Toolbar và nút menu
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
            }
        }
    }

    // Thiết lập Navigation Drawer và xử lý sự kiện chọn menu
    private void initNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if (drawerLayout != null && navigationView != null) {
            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();

                if (id == R.id.nav_feedback) {
                    openFeedbackEmail();
                } else if (id == R.id.nav_about) {
                    openAboutUs();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            });
        }
    }

    // Khởi tạo RecyclerView và danh sách món ăn
    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

            dishList = new ArrayList<>();
            dishList.add(new Dish("Phở", 50000, R.drawable.pho));
            dishList.add(new Dish("Bún bò", 45000, R.drawable.bunbo));
            dishList.add(new Dish("Bánh mì", 20000, R.drawable.banhmi));
            dishList.add(new Dish("Cơm tấm", 60000, R.drawable.comtam));
            dishList.add(new Dish("Hủ tiếu", 25000, R.drawable.hutieu));
            dishList.add(new Dish("Gà rán", 75000, R.drawable.garan));

            adapter = new DishAdapter(this, dishList);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }
    }

    // Mở ứng dụng email để gửi phản hồi
    private void openFeedbackEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:foodorder@cntt.io"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Phản hồi về ứng dụng Menu Đặt Món");
        try {
            startActivity(Intent.createChooser(emailIntent, "Gửi phản hồi qua email"));
        } catch (Exception e) {
            Toast.makeText(this, "Không tìm thấy ứng dụng email", Toast.LENGTH_SHORT).show();
        }
    }

    // Chuyển đến activity AboutUs
    private void openAboutUs() {
        startActivity(new Intent(MenuActivity.this, AboutUsActivity.class));
    }
    // endregion

    // region Override Methods
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout != null) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleDeepLink(intent);
    }
    // endregion
}