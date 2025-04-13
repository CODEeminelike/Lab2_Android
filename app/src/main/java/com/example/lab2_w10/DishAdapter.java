package com.example.lab2_w10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {
    private List<Dish> dishList;
    private Context context;
    private BroadcastReceiver orderResponseReceiver;

    public DishAdapter(Context context, List<Dish> dishList) {
        this.context = context;
        this.dishList = dishList;
        setupOrderResponseReceiver();
    }

    private void setupOrderResponseReceiver() {
        orderResponseReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (OrderService.ACTION_ORDER_RESPONSE.equals(intent.getAction())) {
                    int orderId = intent.getIntExtra(OrderService.EXTRA_ORDER_ID, -1);
                    String dishName = intent.getStringExtra(OrderService.EXTRA_DISH_NAME);

                    if (orderId != -1) {
                        Toast.makeText(context,
                                "Đã đặt món " + dishName + " thành công. Mã đơn: " + orderId,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(
                orderResponseReceiver,
                new IntentFilter(OrderService.ACTION_ORDER_RESPONSE)
        );
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dish, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Dish dish = dishList.get(position);
        holder.imgDish.setImageResource(dish.getImageRes());
        holder.tvDishName.setText(dish.getName());
        holder.tvPrice.setText(String.format("%,d đ", dish.getPrice()));

        holder.btnOrder.setOnClickListener(v -> {
            // Hiển thị thông báo đang xử lý
            Toast.makeText(context, "Đang xử lý đặt món: " + dish.getName(), Toast.LENGTH_SHORT).show();

            Intent serviceIntent = new Intent(context, OrderService.class);
            serviceIntent.putExtra("dish_name", dish.getName());
            serviceIntent.putExtra("price", dish.getPrice());
            context.startService(serviceIntent);
        });
    }

    @Override
    public int getItemCount() {
        return dishList != null ? dishList.size() : 0;
    }

    // Thêm phương thức để hủy đăng ký BroadcastReceiver
    public void unregisterReceiver() {
        if (orderResponseReceiver != null) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(orderResponseReceiver);
        }
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