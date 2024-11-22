package com.example.uddd_nhom11.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uddd_nhom11.R;
import com.example.uddd_nhom11.database.DatabaseHelper;
import com.example.uddd_nhom11.entity.Account;
import com.example.uddd_nhom11.entity.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChiTietPhongQT extends AppCompatActivity {
    private EditText edtCTTenPhong;
    private EditText edtCTGiaPhong;
    private Spinner spnCTLoaiPhong;
    private EditText edtCTKhu;
    private EditText edtCTTang;
    private Button btnCTSuaPhong;
    private ListView lvSinhVienO;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> registrantsAdapter;
    private List<Account> registrants;
    private int selectedItemPosition;
    String roomNumber;
    String roomArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantri_chi_tiet_phong);

        edtCTTenPhong = findViewById(R.id.edtCTTenPhong);
        edtCTGiaPhong = findViewById(R.id.edtCTGiaPhong);
        spnCTLoaiPhong = findViewById(R.id.spnCTLoaiPhong);
        edtCTKhu = findViewById(R.id.edtCTKhu);
        edtCTTang = findViewById(R.id.edtCTTang);
        btnCTSuaPhong = findViewById(R.id.btnCTSuaPhong);
        lvSinhVienO = findViewById(R.id.lvSinhVienO);

        dbHelper = new DatabaseHelper(this);

        List<String> roomTypes = Arrays.asList("1", "2", "3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roomTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCTLoaiPhong.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null) {
            roomNumber = intent.getStringExtra("roomNumber");
            String roomPrice = intent.getStringExtra("roomPrice");
            String roomType = intent.getStringExtra("roomType");
            roomArea = intent.getStringExtra("roomArea");
            String roomFloor = intent.getStringExtra("roomFloor");

            edtCTTenPhong.setText(roomNumber);
            edtCTGiaPhong.setText(roomPrice);
            setSpinnerValue(spnCTLoaiPhong, roomType);
            edtCTKhu.setText(roomArea);
            edtCTTang.setText(roomFloor);

            edtCTTenPhong.setEnabled(false);
            edtCTKhu.setEnabled(false);
            edtCTTang.setEnabled(false);

            // Load and display registrants
            loadAndDisplayRegistrants(roomNumber, roomArea);
        }

        btnCTSuaPhong.setOnClickListener(v -> {
            String tenPhong = edtCTTenPhong.getText().toString().trim();
            String giaPhongStr = edtCTGiaPhong.getText().toString().trim();
            String khu = edtCTKhu.getText().toString().trim();
            String tang = edtCTTang.getText().toString().trim();
            String loaiPhongStr = spnCTLoaiPhong.getSelectedItem().toString();

            if (tenPhong.isEmpty() || giaPhongStr.isEmpty() || khu.isEmpty() || tang.isEmpty() || loaiPhongStr.isEmpty()) {
                Toast.makeText(ChiTietPhongQT.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int giaPhong;
            try {
                giaPhong = Integer.parseInt(giaPhongStr);
            } catch (NumberFormatException e) {
                Toast.makeText(ChiTietPhongQT.this, "Giá phòng phải là số", Toast.LENGTH_SHORT).show();
                return;
            }

            int loaiPhong;
            try {
                loaiPhong = Integer.parseInt(loaiPhongStr);
            } catch (NumberFormatException e) {
                Toast.makeText(ChiTietPhongQT.this, "Loại phòng không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            Room newRoom = new Room();
            newRoom.setRoomnumber(tenPhong);
            newRoom.setFloor(tang);
            newRoom.setArea(khu);
            newRoom.setRoomtype(loaiPhong);
            newRoom.setRoomprice(giaPhong);

            try {
                dbHelper.updateRoom(newRoom);
                Toast.makeText(ChiTietPhongQT.this, "Sửa phòng thành công", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } catch (Exception e) {
                Toast.makeText(ChiTietPhongQT.this, "Sửa phòng thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        // Đăng ký context menu cho ListView
        registerForContextMenu(lvSinhVienO);

        // Xử lý sự kiện nhấn giữ
        lvSinhVienO.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItemPosition = position;
                return false; // Trả về false để hiển thị context menu
            }
        });
    }

    @SuppressLint("Range")
    private void loadAndDisplayRegistrants(String roomNumber, String area) {
        registrants = dbHelper.getRegistrantsByRoom(roomNumber, area);
        List<String> names = new ArrayList<>();
        for (Account registrant : registrants) {
            Cursor cursor = dbHelper.getProfileInfo(registrant.getUsername());
            String n = "";
            if (cursor.moveToFirst()) n = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            names.add(n);
        }

        registrantsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        lvSinhVienO.setAdapter(registrantsAdapter);
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
            if (spinner.getAdapter().getItem(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_chi_tiet_phong, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ctmnXoaNguoiDangKy) {
            // Hiển thị cảnh báo xác nhận
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa người đăng ký này không?")
                    .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @SuppressLint("Range")
                        public void onClick(DialogInterface dialog, int which) {
                            Account accountToRemove = registrants.get(selectedItemPosition);
                            // Xóa người đăng ký khỏi database
                            dbHelper.removeRegistrant(accountToRemove);
                            // Xóa người đăng ký khỏi danh sách và cập nhật ListView
                            registrants.remove(selectedItemPosition);
                            List<String> names = new ArrayList<>();
                            for (Account registrant : registrants) {
                                Cursor cursor = dbHelper.getProfileInfo(registrant.getUsername());
                                String n = "";
                                if (cursor.moveToFirst()) n = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                                names.add(n);
                            }
                            registrantsAdapter = new ArrayAdapter<>(ChiTietPhongQT.this, android.R.layout.simple_list_item_1, names);
                            lvSinhVienO.setAdapter(registrantsAdapter);
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
