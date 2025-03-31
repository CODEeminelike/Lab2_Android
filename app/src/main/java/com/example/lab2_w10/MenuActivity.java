package com.example.lab2_w10;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DishAdapter adapter;
    private List<Dish> dishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // 1. Ánh xạ RecyclerView từ layout
        recyclerView = findViewById(R.id.recyclerView);

        // 2. Thiết lập LayoutManager (Grid 2 cột)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // 3. Khởi tạo danh sách món ăn
        dishList = new ArrayList<>();
        dishList.add(new Dish("Phở", 50000, R.drawable.pho));
        dishList.add(new Dish("Bún bò", 45000, R.drawable.bunbo));
        dishList.add(new Dish("Bánh mì", 20000, R.drawable.banhmi));
        dishList.add(new Dish("Cơm tấm", 60000, R.drawable.comtam));
        dishList.add(new Dish("Hủ tiếu", 25000, R.drawable.hutieu));
        dishList.add(new Dish("Gà rán", 75000, R.drawable.garan));

        // 4. Tạo Adapter và gắn vào RecyclerView
        adapter = new DishAdapter(this, dishList);
        recyclerView.setAdapter(adapter);

        // 5. (Tùy chọn) Thêm divider giữa các item
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}