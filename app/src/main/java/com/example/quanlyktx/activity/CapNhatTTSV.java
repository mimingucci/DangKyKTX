package com.example.uddd_nhom11.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uddd_nhom11.R;
import com.example.uddd_nhom11.database.DatabaseHelper;
import com.example.uddd_nhom11.entity.Account;
import com.example.uddd_nhom11.entity.Profile;

public class CapNhatTTSV extends AppCompatActivity {

    EditText edtName, edtEmail, edtPhone, edtOldPassword, edtPassword, edtConfirmPassword;
    Button btnUpdate;
    String username, name, email, sdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_ttsv);
        getWidget();
    }
    @SuppressLint("Range")
    public void getWidget(){
        edtName = findViewById(R.id.edtName); edtName.setEnabled(false);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnUpdate = findViewById(R.id.btnUpdate);

        try {
            DatabaseHelper db = new DatabaseHelper(this);

            Cursor usrCur = db.getSession();
            if (usrCur.moveToFirst()) username = usrCur.getString(usrCur.getColumnIndex(DatabaseHelper.COLUMN_USERNAME));

            Cursor profileCur = db.getProfileInfo(username);
            if (profileCur.moveToFirst()) {
                name = profileCur.getString(profileCur.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                email = profileCur.getString(profileCur.getColumnIndex(DatabaseHelper.COLUMN_EMAIL));
                sdt = profileCur.getString(profileCur.getColumnIndex(DatabaseHelper.COLUMN_SDT));
                edtName.setText(name);
                edtEmail.setText(email);
                edtPhone.setText(sdt);
            }
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cursor accCur = db.getAccountByUsernameCursor(username);
                    String actualold = "";
                    if (accCur.moveToFirst()) actualold = accCur.getString(accCur.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD));
                    String oldpass = edtOldPassword.getText().toString(),
                            pass = edtPassword.getText().toString(),
                            confirmpass = edtConfirmPassword.getText().toString(),
                            email = edtEmail.getText().toString(),
                            sdt = edtPhone.getText().toString();

                    if (oldpass.isEmpty() || pass.isEmpty() || confirmpass.isEmpty() || email.isEmpty() || sdt.isEmpty()) {
                        Toast.makeText(CapNhatTTSV.this, "Vui lòng nhập đầy đủ các trường thông tin!", Toast.LENGTH_LONG).show();
                    }
                    else if (!oldpass.equals(actualold)) {
                        Toast.makeText(CapNhatTTSV.this, "Mật khẩu cũ đã nhập không đúng!", Toast.LENGTH_LONG).show();
                    }
                    else if (!pass.equals(confirmpass)) {
                        Toast.makeText(CapNhatTTSV.this, "Mật khẩu mới không khớp!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        AlertDialog.Builder b = new AlertDialog.Builder(CapNhatTTSV.this);
                        b.setTitle("Xác nhận cập nhật thông tin?");
                        b.setMessage("Bạn có chắc muốn cập nhật thông tin không?");
                        b.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.updateProfile(new Profile(username, sdt, email, name));
                                db.updateAccount(new Account(username, pass, 0));
                                Toast.makeText(CapNhatTTSV.this, "Cập nhật thông tin thành công !", Toast.LENGTH_LONG).show();
                            }
                        });
                        b.setNegativeButton("Hủy bỏ", null);
                        b.create();
                        b.show();
                    }
                }
            });
            db.close();
        }
        catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}