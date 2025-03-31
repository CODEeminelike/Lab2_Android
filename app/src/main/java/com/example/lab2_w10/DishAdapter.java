package com.example.lab2_w10;

import android.content.Context;
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

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {
    private List<Dish> dishList;
    private Context context;

    public DishAdapter(Context context, List<Dish> dishList) {
        this.context = context;
        this.dishList = dishList;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dish, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Dish dish = dishList.get(position);
        holder.imgDish.setImageResource(dish.getImageRes());
        holder.tvDishName.setText(dish.getName());
        holder.tvPrice.setText(String.format("%,d đ", dish.getPrice()));

        holder.btnOrder.setOnClickListener(v -> {
            Toast.makeText(context, "Đã đặt món: " + dish.getName(), Toast.LENGTH_SHORT).show();
            // Gửi order đến server (sẽ triển khai sau)
        });
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

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