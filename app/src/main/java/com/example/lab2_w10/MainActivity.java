package com.example.lab2_w10;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kiểm tra phiên bản Android để đảm bảo rằng shortcut có thể được tạo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            createAppShortcut();
        }

        // Chạy hoạt động khác
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    // Tạo shortcut cho Activity "Về chúng tôi"
    private void createAppShortcut() {
        // Intent mở Activity "Về chúng tôi"
        Intent shortcutIntent = new Intent(getApplicationContext(), AboutUsActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        // Chỉ tạo shortcut nếu phiên bản Android >= Android 8 (API 26)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.content.pm.ShortcutInfo shortcut = new android.content.pm.ShortcutInfo.Builder(this, "id_about_us")
                    .setShortLabel("Về chúng tôi")  // Tiêu đề của shortcut
                    .setLongLabel("Tìm hiểu về chúng tôi")  // Mô tả dài của shortcut
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_about)) // Chọn icon cho shortcut
                    .setIntent(shortcutIntent)  // Gắn Intent cho shortcut
                    .build();

            android.content.pm.ShortcutManager shortcutManager = getSystemService(android.content.pm.ShortcutManager.class);
            if (shortcutManager != null) {
                // Đăng ký yêu cầu pin shortcut vào màn hình chính
                shortcutManager.requestPinShortcut(shortcut, null);
            } else {
                Toast.makeText(this, "Không thể tạo shortcut", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Phiên bản hệ điều hành không hỗ trợ", Toast.LENGTH_SHORT).show();
        }
    }
}
