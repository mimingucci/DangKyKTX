package com.example.uddd_nhom11.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uddd_nhom11.R;
import com.example.uddd_nhom11.database.DatabaseHelper;

public class MenuSinhVien extends AppCompatActivity {

    Button btnDangKyPhong, btnGiaHanPhong, btnCapNhatTTSV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_sinh_vien);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWidget();
    }
    @SuppressLint("Range")
    public void getWidget(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        btnDangKyPhong = findViewById(R.id.btnDangKyPhong);
        btnGiaHanPhong = findViewById(R.id.btnGiaHanPhong);
        btnCapNhatTTSV = findViewById(R.id.btnCapNhatTTSV);
        btnGiaHanPhong.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            assert bundle != null;
            Cursor cursor = db.query(DatabaseHelper.RENTLIST_TABLE_NAME, null, DatabaseHelper.COLUMN_USERNAME + " = ?", new String[] {bundle.getString("username")}, null, null, null);
            if (cursor.moveToFirst()) {
                int kythue = Integer.parseInt(cursor.getString(cursor.getColumnIndex("kyhoc")));
                int namthue = Integer.parseInt(cursor.getString(cursor.getColumnIndex("namhoc")));
                int kyhientai = Integer.parseInt(dbHelper.getCurrentTerm());
                int namhientai = Integer.parseInt(dbHelper.getCurrentYear());
                if (kythue < kyhientai || namthue < namhientai) {
                    Intent intent1 = new Intent(MenuSinhVien.this, GiaHanPhong.class);
                    intent1.putExtra("bundle", bundle);
                    startActivity(intent1);
//                Toast.makeText(MenuSinhVien.this, cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ROOMNUMBER)), Toast.LENGTH_LONG).show();
                }
                else {
                    AlertDialog.Builder b = new AlertDialog.Builder(this);
                    b.setMessage("Chưa đến thời điểm gia hạn!");
                    b.setNegativeButton("Đóng", null);
                    b.create();
                    b.show();
                }

            }
            else {
                Toast.makeText(MenuSinhVien.this, "Chưa có", Toast.LENGTH_LONG).show();
            }
            cursor.close();
            db.close();
        });
        btnCapNhatTTSV.setOnClickListener(v -> {
            Intent intent2 = new Intent(MenuSinhVien.this, CapNhatTTSV.class);
            startActivity(intent2);
        });
        btnDangKyPhong.setOnClickListener(v -> {
            Intent intent3 = new Intent(MenuSinhVien.this, DangKyPhongf.class);
            startActivity(intent3);
        });
    }

}