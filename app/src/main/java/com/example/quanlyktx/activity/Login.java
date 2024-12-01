package com.example.uddd_nhom11.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uddd_nhom11.R;
import com.example.uddd_nhom11.database.DatabaseHelper;
import com.example.uddd_nhom11.entity.Account;
import com.example.uddd_nhom11.entity.Profile;
import com.example.uddd_nhom11.entity.Rent;
import com.example.uddd_nhom11.entity.Room;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
    TextView forgotPasswordTextView;
    EditText edtUsername, edtPassword;
    Button btnDangNhap;
    CheckBox ckbLuuThongTin;
    private int role, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        initAccountsDatabase();
        initRoomsDatabase();
        addSomeFakeRent();
        addSession();
        addSomeProfiles();
        addTerm();
        changeTerm();
        getWidget();
    }
    @Override
    protected void onPause() {
        super.onPause();
        saveLoginState();
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        boolean save = preferences.getBoolean("save", false);
        if (save) {
            edtUsername.setText(preferences.getString("tenDangNhap", ""));
            edtPassword.setText(preferences.getString("matKhau", ""));
            ckbLuuThongTin.setChecked(preferences.getBoolean("save", false));
        }
    }
    public void saveLoginState() {
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("tenDangNhap", edtUsername.getText().toString());
        editor.putString("matKhau", edtPassword.getText().toString());
        editor.putBoolean("save", ckbLuuThongTin.isChecked());
        editor.apply();
    }
    @SuppressLint("WrongViewCast")
    public void getWidget(){
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        ckbLuuThongTin = findViewById(R.id.ckbLuuThongTin);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        btnDangNhap.setOnClickListener(v -> {
            saveLoginState();
            String username = edtUsername.getText()+"";
            String password = edtPassword.getText()+"";
            if (authenticateUser(username, password)) {
                if (role == 0) {
                    saveSession(username);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putInt("id", id);
                    Intent intent = new Intent(Login.this, MenuSinhVien.class);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                }
                if (role == 1) {
                    Intent intent = new Intent(Login.this, MenuQuanTri.class);
                    startActivity(intent);
                }
            }
            else {
                AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setTitle("Lỗi");
                b.setMessage("Thông tin đăng nhập không hợp lệ!");
                b.setNegativeButton("Cancel", null);
                b.create();
                b.show();
                
            }
        });
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, QuenMatKhau.class);
                startActivity(intent);
            }
        });
    }
    @SuppressLint("Range")
    private boolean authenticateUser(String username, String password) {
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(this);

            Cursor cursor = dbHelper.getAccountByUsernameAndPasswordCursor(username, password);
            int count = cursor.getCount();
            if (count > 0){
                if (cursor.moveToFirst()) {
                    this.role = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ROLE));
                    this.id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                }
            }

            cursor.close();
            dbHelper.close();

            return count > 0;
        } catch (Exception e){
            Toast.makeText(Login.this, e.toString(), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void initAccountsDatabase() {
        DatabaseHelper db = new DatabaseHelper(this);

        db.addAccountToDatabase(new Account("a", "a", 0));
        db.addAccountToDatabase(new Account("b", "b",  1));
        db.addAccountToDatabase(new Account("admin", "123456", 1));
        db.addAccountToDatabase(new Account("e", "e", 0));
        db.addAccountToDatabase(new Account("khanh", "12345", 0));
        db.addAccountToDatabase(new Account("khai", "12345", 1));
        db.addAccountToDatabase(new Account("2021604440", "f", 0));

        db.close();
    }

    public void initRoomsDatabase () {
        DatabaseHelper db = new DatabaseHelper(this);

        for (int i = 501; i <= 510; i++) {
            db.addRoomToDatabase(new Room(i+"", "A", 5+"", 800000, 4));
        }
        for (int i = 511; i <= 520; i++) {
            db.addRoomToDatabase(new Room(i+"", "B", 5+"", 800000, 4));
        }
        for (int i = 521; i <= 530; i++) {
            db.addRoomToDatabase(new Room(i+"", "C", 5+"", 800000, 4));
        }
        db.close();
    }

    public void addSomeFakeRent() {
        DatabaseHelper db = new DatabaseHelper(this);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        db.addARentToDatabase(new Rent("a", 501+"", "A", "1", year+""));
        db.addARentToDatabase(new Rent("2021604440", 501+"", "A", "2", year+""));
        db.close();
    }


    public void addSession() {
        DatabaseHelper db = new DatabaseHelper(this);
        db.addSession("k");
        db.close();
    }
    public void saveSession(String username) {
        DatabaseHelper db = new DatabaseHelper(this);
        db.changeSession(username);
        db.close();
    }
    public void addTerm() {
        DatabaseHelper db = new DatabaseHelper(this);
        db.addTerm("1","2024");
        db.close();
    }
    public void changeTerm () {
        DatabaseHelper db = new DatabaseHelper(this);
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        String y2 = y+"";
        String s = y2+"-06-01";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long d1 = dateFormat.parse(s).getTime();
            long d2 = System.currentTimeMillis();
            long d = d2-d1;
            long dd = TimeUnit.DAYS.convert(d, TimeUnit.MILLISECONDS);
            String ky = "", nam = y2;
            if (dd <= 0) ky = "1";
            else ky = "2";
            db.changeTerm(ky, nam);
//            Toast.makeText(this, dd+"", Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        db.close();
    }
    public void addSomeProfiles() {
        DatabaseHelper db = new DatabaseHelper(this);
        db.addAProfile(new Profile("a", "0838388833", "uwa@gmail.com", "Tạ Thị Lạng"));
        db.addAProfile(new Profile("2021604440", "0838388833", "uwa@gmail.com", "Trần Trí Trung"));
        db.addAProfile(new Profile("khanh", "0393511358", "uwa@gmail.com", "Đặng Văn Khanh"));
        db.addAProfile(new Profile("khai", "0385946895", "uwa@gmail.com", "Đặng Văn Khải"));
        db.close();
    }

}