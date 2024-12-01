package com.example.uddd_nhom11.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uddd_nhom11.R;
import com.example.uddd_nhom11.database.DatabaseHelper;
import com.example.uddd_nhom11.entity.Account;


public class xacNhanMatKhau extends AppCompatActivity {
    int idAcc;
    Account acc;
    EditText edtMKM1, edtMKM2;
    Button btnXacNhan;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xac_nhan_mat_khau);
        // anh xa thuoc tinh
        edtMKM1 = findViewById(R.id.edtMKM1);
        edtMKM2 = findViewById(R.id.edtMKM2);
        btnXacNhan = findViewById(R.id.btnXacNhan);

        // lay intent
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("myPackage");
        assert bundle != null;
        idAcc = bundle.getInt("id");
        acc = (Account)bundle.getSerializable("account");
        //Toast.makeText(xacNhanMatKhau.this, "id la:" + idAcc + " account: " + acc.getUsername() + " " + acc.getPassword(), Toast.LENGTH_LONG).show();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edt1 = edtMKM1.getText().toString();
                String edt2 = edtMKM2.getText().toString();

                if(edt1.equals(edt2) == true )
                {
                    if(edt1.length() < 1)
                    {
                        Toast.makeText(xacNhanMatKhau.this, "Vui lòng điền thông tin!", Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        try {

                            // liệt kê tên các bảng
//                        List<String> tableNames = new ArrayList<>();
//                        DatabaseHelper helper = new DatabaseHelper(xacNhanMatKhau.this);
//                        SQLiteDatabase db = helper.getReadableDatabase();
//                        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//                        if (cursor.moveToFirst()) {
//                            do {
//                                String tableName = cursor.getString(0);
//                                tableNames.add(tableName);
//                                Log.d("DatabaseHelper", "Table name: " + tableName); // Log table name
//                            } while (cursor.moveToNext());
//                        }
//                        cursor.close();
                            //


                            DatabaseHelper dbHelper = new DatabaseHelper(xacNhanMatKhau.this);
                            Cursor cursor = dbHelper.getAccountInfoById(idAcc);
                            if (cursor != null) {
                                // Kiểm tra xem Cursor có chứa bất kỳ hàng nào không
                                if (cursor.moveToFirst()) {
                                    //doi mat khau
                                    String newPass = edtMKM1.getText().toString();
                                    acc.setPassword(newPass);
                                    Log.d(TAG, "new pass: " + acc.getPassword());
                                    int check = dbHelper.updatePasswordAccount(acc, idAcc);
                                    if(check == 1)
                                    {
                                        Toast.makeText(xacNhanMatKhau.this, "Đổi mật khẩu thành công  " , Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(xacNhanMatKhau.this, Login.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(xacNhanMatKhau.this, "Đổi mật khẩu thất bại  " , Toast.LENGTH_LONG).show();

                                    }
                                    //Toast.makeText(xacNhanMatKhau.this, "Đổi mật khẩu thành công", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(xacNhanMatKhau.this, "Không tìm thấy tài khoản trên hệ thống!", Toast.LENGTH_LONG).show();
                                }

                                cursor.close();
                                dbHelper.close();
                            }
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(xacNhanMatKhau.this, e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("Lỗi đa này", e.toString());

                        }
                    }

                    // thay doi mk

                }
                else
                {
                    Toast.makeText(xacNhanMatKhau.this, "Xác nhận mật khẩu không trùng khớp.", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}