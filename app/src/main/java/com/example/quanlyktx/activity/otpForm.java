package com.example.uddd_nhom11.activity;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import com.example.uddd_nhom11.R;
import com.example.uddd_nhom11.entity.Account;

import java.util.ArrayList;
import java.util.Random;
public class otpForm extends AppCompatActivity {
    String phoneNumber;
    int idAcc, maOtp;
    Account acc;
    Button btnXacNhanOTP;
    EditText edtOTP;
    private static final int SMS_PERMISSION_REQUEST_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_form);

        // Nhận Intent
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("myPackage");
        assert bundle != null;
        phoneNumber = bundle.getString("phoneNumber");
        idAcc = bundle.getInt("id");
        acc = (Account)bundle.getSerializable("account");
        //otp
        maOtp = generateOTP();
        Log.d("Mã otp: ", String.valueOf(maOtp) );

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Quyền chưa được cấp, yêu cầu người dùng cấp quyền
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        } else {
            // Quyền đã được cấp, gửi OTP
            sendOTP();
        }

        btnXacNhanOTP = findViewById(R.id.btnXacNhanOTP);
        edtOTP = findViewById(R.id.edtOTP);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnXacNhanOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpNhap = edtOTP.getText().toString();
                if(otpNhap.length() == 0)
                {
                    Toast.makeText(otpForm.this, "Vui lòng nhập otp", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (otpNhap.equals(String.valueOf(maOtp)))
                    {
                        Intent intent = new Intent(otpForm.this, xacNhanMatKhau.class);
                        Bundle myBundle = new Bundle();
                        myBundle.putInt("id", idAcc);
                        myBundle.putSerializable("account", acc);
                        intent.putExtra("myPackage", myBundle);
                        startActivity(intent);

                    }
                    else
                    {
                        Toast.makeText(otpForm.this, "otp không chính xác", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                sendOTP();

            }else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void sendOTP() {

        Log.d(TAG, "sendOTP: " + phoneNumber);
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(maOtp + " " + " là mã otp của ban.");
        smsManager.sendMultipartTextMessage(phoneNumber,null,parts,null,null);

    }
    public  int generateOTP() {

        Random random = new Random();
        int otp = 1000 + random.nextInt(9000); // Tạo số ngẫu nhiên từ 1000 đến 9999
        return otp;
    }


}