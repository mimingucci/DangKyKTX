package com.example.uddd_nhom11.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uddd_nhom11.R;
import com.example.uddd_nhom11.adapter.PhongAdapter;
import com.example.uddd_nhom11.database.DatabaseHelper;
import com.example.uddd_nhom11.entity.Room;

import java.util.ArrayList;
import java.util.List;

public class QuanLyPhong extends AppCompatActivity {

    Spinner spnLoc;
    ListView lvPhong;
    Button btnThemPhong;
    List<Room> roomList;
    PhongAdapter phongAdapter;
    DatabaseHelper db;
    private static final int ADD_ROOM_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_phong);

        spnLoc = findViewById(R.id.spnLoc);
        lvPhong = findViewById(R.id.lvPhong);
        btnThemPhong = findViewById(R.id.btnThemPhong);
        db = new DatabaseHelper(this);

        getWidget();
        registerForContextMenu(lvPhong); // Register context menu for ListView

        // Handle item click event
        lvPhong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Room selectedRoom = (Room) phongAdapter.getItem(position);
                Intent intent = new Intent(QuanLyPhong.this, ChiTietPhongQT.class);
                intent.putExtra("roomNumber", selectedRoom.getRoomnumber());
                intent.putExtra("roomPrice", String.valueOf(selectedRoom.getRoomprice()));
                long i = selectedRoom.getRoomprice();
                intent.putExtra("roomType", String.valueOf(selectedRoom.getRoomtype()));
                intent.putExtra("roomArea", selectedRoom.getArea());
                intent.putExtra("roomFloor", selectedRoom.getFloor());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load lại danh sách phòng mỗi khi activity được kích hoạt lại
        loadRooms();
    }

    private void getWidget() {
        try {
            ArrayList<String> spnList = new ArrayList<>();
            spnList.add("Tất cả");
            spnList.add("A");
            spnList.add("B");
            spnList.add("C");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    QuanLyPhong.this,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    spnList);
            adapter.setDropDownViewResource(
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spnLoc.setAdapter(adapter);

            spnLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedArea = parent.getItemAtPosition(position).toString();
                    if (selectedArea.equals("Tất cả")) {
                        List<Room> allRooms = db.getRoom();
                        updateRoomList(allRooms);
                    } else {
                        List<Room> filteredRooms = db.getRoomByArea(selectedArea);
                        updateRoomList(filteredRooms);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        btnThemPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLyPhong.this, ThemPhong.class);
                startActivityForResult(intent, ADD_ROOM_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ROOM_REQUEST && resultCode == RESULT_OK) {
            loadRooms();
        }
    }

    private void loadRooms() {
        try {
            List<Room> rooms = db.getRoom();
            updateRoomList(rooms);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tải danh sách phòng", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateRoomList(List<Room> rooms) {
        phongAdapter = new PhongAdapter(this, R.layout.custom_layout_phong, rooms, db);
        lvPhong.setAdapter(phongAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_quantri_phong, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        Room selectedRoom = (Room) phongAdapter.getItem(position);
        int id = item.getItemId();
        if (id == R.id.ctmnXoaPhong) {
            showDeleteConfirmationDialog(selectedRoom);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    private void showDeleteConfirmationDialog(Room room) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa phòng này?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            db.deleteRoomFromDatabase(room.getRoomnumber(), room.getArea());
                            loadRooms();
                            Toast.makeText(QuanLyPhong.this, "Xóa phòng thành công", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(QuanLyPhong.this, "Xóa phòng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}