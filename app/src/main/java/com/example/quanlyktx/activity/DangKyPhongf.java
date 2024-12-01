package com.example.quanlyktx.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlyktx.R;
import com.example.quanlyktx.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DangKyPhongf extends AppCompatActivity {
    private ListView listViewRooms;
    private DatabaseHelper databaseHelper;
    private EditText editTextKhuVuc;
    private Button btnTimKiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky_phongf);

        listViewRooms = findViewById(R.id.lvDsPhong);
        editTextKhuVuc = findViewById(R.id.editTextText);
        btnTimKiem = findViewById(R.id.btn_Tim);

        databaseHelper = new DatabaseHelper(this);

        List<String> roomList = getAllRooms(null); // Initial load with no filter
        CustomAdapter adapter = new CustomAdapter(this, roomList);

        listViewRooms.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String khuVuc = editTextKhuVuc.getText().toString().trim();
                List<String> filteredRooms = getAllRooms(khuVuc);
                CustomAdapter newAdapter = new CustomAdapter(DangKyPhongf.this, filteredRooms);
                listViewRooms.setAdapter(newAdapter);
                newAdapter.notifyDataSetChanged();
            }
        });

        listViewRooms.setOnItemClickListener((parent, view, position, id) -> {
            // Lấy thông tin từ item được click

            String selectedItem = roomList.get(position);
            String[] parts = selectedItem.split("\n");
            String roomStatus = parts[5].split(":")[1].trim();
            // Lấy tình trạng phòng từ selectedItem


            // Kiểm tra nếu phòng đã đầy, hiển thị thông báo và không chuyển sang trang đăng ký
            if (roomStatus.equals("full")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DangKyPhongf.this);
                builder.setTitle("Thông báo")
                        .setMessage("Phòng này đã đầy. Không thể đăng ký.")
                        .setPositiveButton("OK", null)
                        .show();
            } else {

            }
            // Nếu phòng chưa đầy, chuyển sang activity TaoPhieuDangKy
            Intent intent = new Intent(DangKyPhongf.this, TaoPhieuDangKy.class);
            intent.putExtra("selectedItem", selectedItem);
            startActivity(intent);
        });
    }

    private List<String> getAllRooms(String khuVuc) {
        List<String> roomList = new ArrayList<>();
        Cursor cursor;

        if (khuVuc == null || khuVuc.isEmpty()) {
            cursor = databaseHelper.getReadableDatabase().query(
                    DatabaseHelper.ROOM_TABLE_NAME,
                    null, null, null, null, null, null
            );
        } else {
            cursor = databaseHelper.getReadableDatabase().query(
                    DatabaseHelper.ROOM_TABLE_NAME,
                    null,
                    DatabaseHelper.COLUMN_AREA + " = ?",
                    new String[]{khuVuc},
                    null, null, null
            );
        }

        if (cursor != null) {
            int dem = 0;
            while (cursor.moveToNext()) {
                dem++;
                String roomNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOMNUMBER));
                String area = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AREA));
                String floor = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FLOOR));
                String roomType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOMTYPE));
                String roomPrice = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOMPRICE));

                String roomStatus = checkFullRoom(roomNumber, area);

                String roomInfo = dem + ".                    Phòng: " + roomNumber +
                        "\n                            Khu vực: " + area +
                        "\n                            Tầng: " + floor +
                        "\n                            Loại phòng: " + roomType +
                        "\n                            Giá: " + roomPrice+

                        "\n                                                  tình trạng : " + roomStatus;
                roomList.add(roomInfo);
            }
            cursor.close();
        }
        return roomList;
    }

    private String checkFullRoom(String roomNumber,String area  ) {
        int countRooms = databaseHelper.countPeopleByRoomNumberAndArea(roomNumber,area);
        String tst="";
        if(countRooms<5){
            tst=countRooms+"/5";
        }
        else{
            tst="full";
        }

        return tst;

    }
}
