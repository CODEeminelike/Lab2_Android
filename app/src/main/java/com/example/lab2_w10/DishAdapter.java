package com.example.lab2_w10;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// Adapter cho RecyclerView để hiển thị danh sách món ăn
public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {
    private List<Dish> dishList;
    private Context context;

    // Constructor: Khởi tạo adapter với context và danh sách món ăn
    public DishAdapter(Context context, List<Dish> dishList) {
        this.context = context;
        this.dishList = dishList;
    }

    // Tạo ViewHolder mới cho mỗi mục trong danh sách
    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp layout item_dish.xml cho mỗi món ăn
        View view = LayoutInflater.from(context).inflate(R.layout.item_dish, parent, false);
        return new DishViewHolder(view);
    }

    // Gán dữ liệu món ăn vào ViewHolder tại vị trí position
    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        // Lấy món ăn từ danh sách
        Dish dish = dishList.get(position);
        // Đặt hình ảnh món ăn
        holder.imgDish.setImageResource(dish.getImageRes());
        // Đặt tên món ăn
        holder.tvDishName.setText(dish.getName());
        // Đặt giá món ăn với định dạng số
        holder.tvPrice.setText(String.format("%,d đ", dish.getPrice()));

        // Xử lý sự kiện nhấn nút đặt món
        holder.btnOrder.setOnClickListener(v -> {
            // Hiển thị thông báo xác nhận đặt món
            Toast.makeText(context, "Đã đặt món: " + dish.getName(), Toast.LENGTH_SHORT).show();

            // Gửi Intent tới OrderService để gửi đơn hàng tới server
            Intent serviceIntent = new Intent(context, OrderService.class);
            serviceIntent.putExtra("dish_name", dish.getName());
            serviceIntent.putExtra("price", dish.getPrice());
            context.startService(serviceIntent);
        });
    }

    // Trả về số lượng món ăn trong danh sách
    @Override
    public int getItemCount() {
        return dishList != null ? dishList.size() : 0;
    }

    // ViewHolder lưu trữ các thành phần giao diện của một món ăn
    public static class DishViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDish;
        TextView tvDishName, tvPrice;
        Button btnOrder;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.imgDish);
            tvDishName = itemView.findViewById(R.id.tvDishName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnOrder = itemView.findViewById(R.id.btnOrder);
        }
    }
}