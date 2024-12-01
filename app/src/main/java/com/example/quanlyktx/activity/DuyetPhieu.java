package com.example.uddd_nhom11.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uddd_nhom11.R;
import com.example.uddd_nhom11.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DuyetPhieu extends AppCompatActivity {
    CustomAdapter adapter;
    private static final int REQUEST_UPDATE_LIST = 101;
    private ListView listViewRents;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duyet_phieu);

        listViewRents= findViewById(R.id.listViewRents);
        databaseHelper = new DatabaseHelper(this);

        List<String> rentList = getAllRents();
        adapter= new CustomAdapter(this, rentList);
        listViewRents.setAdapter(adapter);
        onResume();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



            listViewRents.setOnItemClickListener((parent, view, position, id) -> {
                // Lấy thông tin từ item được click
                String selectedItem = rentList.get(position);

                // Tạo một Intent để chuyển sang activity XacNhanDuyet và nhận lại kết quả
                Intent intent = new Intent(DuyetPhieu.this, XacNhanDuyet.class);
                intent.putExtra("selectedItem", selectedItem);
                startActivity(intent);
            });

    }
    protected void onResume(CustomAdapter adapter) {
        super.onResume();
        // Load lại dữ liệu khi Activity được khởi động lại
        List<String> rentList = getAllRents();
        adapter.clear(); // Xóa dữ liệu cũ trong adapter
        adapter.addAll(rentList); // Thêm dữ liệu mới vào adapter
        adapter.notifyDataSetChanged(); // Cập nhật ListView
    }
    private List<String> getAllRents() {
        List<String> rentList = new ArrayList<>();
        Cursor cursor = databaseHelper.getReadableDatabase().query(
                DatabaseHelper.REQUEST_TABLE_NAME,
                null, null, null, null, null, null
        );

        if (cursor != null) {
            int dem=0;
            while (cursor.moveToNext()) {
                dem++;
                String requestusername = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));
                String requestname = databaseHelper.getNameByUsername(requestusername);
                String requestId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REQUESTID));
                String requestType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REQUESTTYPE));
                String requestStatus = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REQUESTSTATUS));
                String loai = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOMNUMBER));
                String khu = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AREA));
                String rtype="";
                if(requestType.equals("0")){
                    rtype="Phiếu đăng ký";

                }
                else {
                    rtype="Phiếu gia hạn";
                }
                String status="";
                if(requestStatus.equals("0")){
                    status="chưa duyệt";
                }
                else if(requestStatus.equals("-1")){
                    status="thất bại";
                }
                else{
                    status=" thành công";
                }
                String rentInfo = "                    ID Phiếu: "+requestId+
                        "\n                    Loại phiếu: " +rtype +
                        "\n"+dem+ "                 Trạng Thái : "+status +
                        "\n                    Người tạo : "+requestname+
                        "\n                    phòng : "+loai+
                        "\n                    khu : "+khu+
                        "\n                    usr: "+requestusername;
                rentList.add(rentInfo);
            }
            cursor.close();
        }
        return rentList;
    }
}