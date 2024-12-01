
package com.example.uddd_nhom11.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uddd_nhom11.R;
import com.example.uddd_nhom11.database.DatabaseHelper;
import com.example.uddd_nhom11.entity.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class GiaHanPhong extends AppCompatActivity {

    TextView tvNguoiLamPhieu, tvSoPhong, tvGiaPhong, tvLoaiPhong, tvKhu, tvTang, tvKyHoc;
    CheckBox cbDieuHoa, cbBinhNongLanh, cbMayGiat;
    EditText edtNgayGiaHan;
    Button btnChonNgay;
    FloatingActionButton btnFl;
    Spinner spnDoiTuongUT;
    ArrayList<String> spnList = new ArrayList<>();
    ArrayAdapter<String> spnAdapter;
    String username, roomnumber, roomarea, kyhoc, namhoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gia_han_phong);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWidget();
        registerForContextMenu(btnFl);
    }
    @SuppressLint("Range")
    public void getWidget() {
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
//        edtNgayGiaHan = findViewById(R.id.edtNgayGiaHan);
//        btnChonNgay = findViewById(R.id.btnChonNgay);
        btnFl = findViewById(R.id.floatingActionButton);
        btnFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContextMenu(btnFl);
            }
        });
        SpinnerConfig();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        DatabaseHelper db = new DatabaseHelper(this);
        assert bundle != null;
        Cursor cursor = db.getRentByUsername(bundle.getString("username"));
        if (cursor.moveToFirst()) {
            //Lấy thông tin sinh viên từ bảng profile nhờ cột username của bảng rentlist
            username = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME));
            Cursor cursor1 = db.getProfileInfo(username);
            if (cursor1.moveToFirst()) {
                String name = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                tvNguoiLamPhieu.setText(name);
            }
            //Lấy thông tin phòng từ bảng rentlist cột roomnumber và area
            roomnumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ROOMNUMBER));
            roomarea = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_AREA));
            Cursor cursor2 = db.getRoomByRoomNumberAndAreaCursor(roomnumber, roomarea);
            if (cursor2.moveToFirst()) {
                tvSoPhong.setText(roomnumber);
                long roomprice = cursor2.getLong(cursor2.getColumnIndex(DatabaseHelper.COLUMN_ROOMPRICE));
                tvGiaPhong.setText(String.valueOf(roomprice));
                int roomtype = cursor2.getInt(cursor2.getColumnIndex(DatabaseHelper.COLUMN_ROOMTYPE));
                tvLoaiPhong.setText(String.valueOf(roomtype));
                int floor = cursor2.getInt(cursor2.getColumnIndex(DatabaseHelper.COLUMN_FLOOR));
                tvTang.setText(String.valueOf(floor));
                tvKhu.setText(roomarea);
//                kyhoc = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_KYHOC));
                kyhoc = db.getCurrentTerm();
//                namhoc = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAMHOC));
                namhoc = db.getCurrentYear();
                String kh = kyhoc + " - " + namhoc;
                tvKyHoc.setText(kh);
                if (roomprice >= 700000) {
                    cbBinhNongLanh.setChecked(true);
                }
                if (roomprice >= 800000) {
                    cbDieuHoa.setChecked(true);
                    cbBinhNongLanh.setChecked(true);
                }
                if (roomprice >= 1200000) {
                    cbDieuHoa.setChecked(true);
                    cbMayGiat.setChecked(true);
                    cbBinhNongLanh.setChecked(true);
                }
            }
        }
        db.close();
    }
    public void SpinnerConfig() {
        spnDoiTuongUT = findViewById(R.id.spnDoiTuongUT);
        spnAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spnList);
        spnDoiTuongUT.setAdapter(spnAdapter);
        spnList.add("Không");
        spnList.add("Hộ nghèo");
        spnList.add("Gia đình chính sách");
        spnAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.giahanmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.gui) {
            DatabaseHelper db = new DatabaseHelper(this);
            Cursor cursor = db.getSession();
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") Cursor cursorRequest = db.getRenewRequestByUsername(cursor.getString(cursor.getColumnIndex("username")));
                if (cursorRequest.getCount() > 0) {

                    Toast.makeText(this, "Yêu cầu đã được gửi đi", Toast.LENGTH_LONG).show();
                }
                else {
                    AlertDialog.Builder b = new AlertDialog.Builder(this);
                    b.setTitle("Xác nhận");
                    b.setMessage("Bạn có chắc muốn gửi yêu cầu gia hạn?");
                    b.setPositiveButton("Gửi", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.addARenewRequest(new Request(username, roomnumber, roomarea, kyhoc, namhoc, 1, 0));
                            AlertDialog.Builder c = new AlertDialog.Builder(GiaHanPhong.this);
                            c.setMessage("Yêu cầu gia hạn đã được gửi đi!");
                            c.setTitle("Thành công.");
                            c.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            c.create();
                            c.show();
                            Toast.makeText(GiaHanPhong.this, "Đã gửi yêu cầu gia hạn.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    b.setNegativeButton("Hủy", null);
                    b.setIcon(android.R.drawable.ic_dialog_alert);
                    b.create();
                    b.show();
                }
            }

        }
        return super.onContextItemSelected(item);
    }
}
