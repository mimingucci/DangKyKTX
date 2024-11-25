package com.example.quanlyktx.activity;

import static com.example.quanlyktx.database.DatabaseHelper.COLUMN_USERNAME;
import static com.example.quanlyktx.database.DatabaseHelper.PROFILE_TABLE_NAME;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlyktx.R;
import com.example.quanlyktx.database.DatabaseHelper;
import com.example.quanlyktx.entity.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TaoPhieuDangKy extends AppCompatActivity {
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    TextView tvNguoiLamPhieu, tvSoPhong, tvGiaPhong, tvLoaiPhong, tvKhu, tvTang, tvKyHoc;
    CheckBox cbDieuHoa, cbBinhNongLanh, cbMayGiat;
    FloatingActionButton btnFl;
    private int requestId;
    String username, ki, nam;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tao_phieu_dang_ky);

        String selectedItem = getIntent().getStringExtra("selectedItem");

        // Parse the selectedItem to extract the details
        String[] parts = selectedItem.split("\n");

        String roomname = parts[0].split(":")[1].trim();
        String khu = parts[1].split(":")[1].trim();
        String tang = parts[2].split(":")[1].trim();
        String loai = parts[3].split(":")[1].trim();
        String gia = parts[4].split(":")[1].trim();
        String tinhtrang = parts[5].split(":")[1].trim();

        tvNguoiLamPhieu = findViewById(R.id.tvNguoiLamPhieu);
        tvSoPhong = findViewById(R.id.tvSoPhong);
        tvGiaPhong = findViewById(R.id.tvGiaPhong);
        tvLoaiPhong = findViewById(R.id.tvLoaiPhong);
        tvKhu = findViewById(R.id.tvKhu);
        tvTang = findViewById(R.id.tvTang);
        tvKyHoc = findViewById(R.id.tvKyHoc);
        cbDieuHoa = findViewById(R.id.cbDieuHoa); cbDieuHoa.setEnabled(false);
        cbBinhNongLanh = findViewById(R.id.cbBinhNongLanh); cbBinhNongLanh.setEnabled(false);
        cbMayGiat = findViewById(R.id.cbMayGiat); cbMayGiat.setEnabled(false);

        tvNguoiLamPhieu.setText(" ");
        tvSoPhong.setText(roomname);
        tvKhu.setText(khu);
        int tanga = Integer.parseInt(tang);
        tanga = tanga / 100;
        tvTang.setText(tanga + " ");
        tvGiaPhong.setText(gia);
        tvLoaiPhong.setText(loai);
        if (gia.equals("700000")) {
            cbBinhNongLanh.setChecked(true);
        }
        if (gia.equals("800000")) {
            cbBinhNongLanh.setChecked(true);
            cbDieuHoa.setChecked(true);
        }
        if (gia.equals("1200000")) {
            cbBinhNongLanh.setChecked(true);
            cbDieuHoa.setChecked(true);
            cbMayGiat.setChecked(true);
        }

        // Lấy username từ phiên làm việc
        Cursor sessionCursor =  dbHelper.getSession();
        if (sessionCursor != null && sessionCursor.moveToFirst()) {
            username = sessionCursor.getString(sessionCursor.getColumnIndex("username"));
            sessionCursor.close();
        }

        // Sử dụng username để truy vấn thông tin khác
        Cursor cursor = dbHelper.getRentByUsername(username);
        if (cursor.moveToFirst()) {
            Cursor cursor1 = dbHelper.getProfileInfo(username);
            ki = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_KYHOC));
            nam = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAMHOC));
            String kh = ki + " - " + nam;
            tvKyHoc.setText(kh);
            if (cursor1.moveToFirst()) {
                String name = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                tvNguoiLamPhieu.setText(name);
            }
        }

        btnFl = findViewById(R.id.floatingActionButton);
        btnFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một đối tượng Request mới và thêm vào database
                Request request = new Request(username, roomname, khu, ki, nam, 1, 0);
                dbHelper.addARegisterRequest(request);

                // Hiển thị thông báo hoặc thực hiện các hành động khác sau khi thêm request thành công
                Toast.makeText(TaoPhieuDangKy.this, "Đã thêm phiếu đăng ký thành công", Toast.LENGTH_SHORT).show();

                // Đóng activity hiện tại và quay về activity trước đó (nếu cần)
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}