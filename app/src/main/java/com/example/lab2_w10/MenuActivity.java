package com.example.lab2_w10;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DishAdapter adapter;
    private List<Dish> dishList;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // 1. Khởi tạo Toolbar và thiết lập nó làm ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu); // Đảm bảo bạn có biểu tượng này
        }

        // 2. Thiết lập Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Xử lý khi người dùng chọn một mục trong Navigation Drawer
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_feedback) {
                // Mở Intent email để gửi phản hồi
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:foodorder@cntt.io"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Phản hồi về ứng dụng Menu Đặt Món");
                startActivity(Intent.createChooser(emailIntent, "Gửi phản hồi qua email"));
            } else if (id == R.id.nav_about) {
                // Mở Activity "About Us"
                startActivity(new Intent(MenuActivity.this, AboutUsActivity.class));
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // 3. Ánh xạ RecyclerView từ layout
        recyclerView = findViewById(R.id.recyclerView);

        // 4. Thiết lập LayoutManager (Grid với 2 cột)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // 5. Khởi tạo danh sách món ăn
        dishList = new ArrayList<>();
        dishList.add(new Dish("Phở", 50000, R.drawable.pho));
        dishList.add(new Dish("Bún bò", 45000, R.drawable.bunbo));
        dishList.add(new Dish("Bánh mì", 20000, R.drawable.banhmi));
        dishList.add(new Dish("Cơm tấm", 60000, R.drawable.comtam));
        dishList.add(new Dish("Hủ tiếu", 25000, R.drawable.hutieu));
        dishList.add(new Dish("Gà rán", 75000, R.drawable.garan));

        // 6. Tạo Adapter và gắn vào RecyclerView
        adapter = new DishAdapter(this, dishList);
        recyclerView.setAdapter(adapter);

        // 7. (Tùy chọn) Thêm divider giữa các item trong RecyclerView
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Xử lý khi người dùng nhấn các mục trong ActionBar
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START); // Mở Drawer khi nhấn vào icon menu
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Nếu Drawer đang mở, đóng nó khi người dùng nhấn nút Back
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed(); // Nếu Drawer đóng, thực hiện hành động mặc định của nút Back
        }
    }
}
