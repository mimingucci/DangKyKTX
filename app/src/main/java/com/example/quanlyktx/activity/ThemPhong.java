package com.example.uddd_nhom11.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uddd_nhom11.R;
import com.example.uddd_nhom11.database.DatabaseHelper;
import com.example.uddd_nhom11.entity.Room;

import java.util.Arrays;
import java.util.List;

public class ThemPhong extends AppCompatActivity {

    private EditText edtTPTenPhong, edtTPGiaPhong, edtTPKhu, edtTPTang;
    private Spinner spnTPLoaiPhong;
    private Button btnTPThem;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quantri_them_phong);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các view
        edtTPTenPhong = findViewById(R.id.edtTPTenPhong);
        edtTPGiaPhong = findViewById(R.id.edtTPGiaPhong);
        edtTPKhu = findViewById(R.id.edtTPKhu);
        edtTPTang = findViewById(R.id.edtTPTang);
        spnTPLoaiPhong = findViewById(R.id.spnTPLoaiPhong);
        btnTPThem = findViewById(R.id.btnTPThem);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Thiết lập Spinner cho loại phòng
        List<String> roomTypes = Arrays.asList("1", "2", "3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roomTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTPLoaiPhong.setAdapter(adapter);

        // Sự kiện khi bấm nút Thêm
        btnTPThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenPhong = edtTPTenPhong.getText().toString().trim();
                String giaPhongStr = edtTPGiaPhong.getText().toString().trim();
                String khu = edtTPKhu.getText().toString().trim();
                String tang = edtTPTang.getText().toString().trim();
                String loaiPhongStr = spnTPLoaiPhong.getSelectedItem().toString();

                // Kiểm tra dữ liệu đầu vào
                if (tenPhong.isEmpty() || giaPhongStr.isEmpty() || khu.isEmpty() || tang.isEmpty() || loaiPhongStr.isEmpty()) {
                    Toast.makeText(ThemPhong.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                int giaPhong;
                try {
                    giaPhong = Integer.parseInt(giaPhongStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(ThemPhong.this, "Giá phòng phải là số", Toast.LENGTH_SHORT).show();
                    return;
                }

                int loaiPhong;
                try {
                    loaiPhong = Integer.parseInt(loaiPhongStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(ThemPhong.this, "Loại phòng không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo đối tượng Room
                Room newRoom = new Room();
                newRoom.setRoomnumber(tenPhong);
                newRoom.setFloor(tang);
                newRoom.setArea(khu);
                newRoom.setRoomtype(loaiPhong);
                newRoom.setRoomprice(giaPhong);

                // Thêm phòng vào cơ sở dữ liệu
                if(dbHelper.checkRoomExist(tenPhong,khu)){
                    Toast.makeText(ThemPhong.this, "Phòng đã tồn tại", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        dbHelper.addRoom(newRoom);
                        Toast.makeText(ThemPhong.this, "Thêm phòng thành công", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(ThemPhong.this, "Thêm phòng thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}