package com.example.uddd_nhom11.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.uddd_nhom11.R;
import com.example.uddd_nhom11.database.DatabaseHelper;
import com.example.uddd_nhom11.entity.Account;
import com.example.uddd_nhom11.entity.Room;

import java.util.List;

public class PhongAdapter extends ArrayAdapter<Room> {
    private Context context;
    private int resource;
    private DatabaseHelper databaseHelper;
    public PhongAdapter(Context context, int resource, List<Room> objects, DatabaseHelper databaseHelper){
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Room room = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView tvLVTenPhong = convertView.findViewById(R.id.tvLVTenPhong);
        TextView tvLVTang = convertView.findViewById(R.id.tvLVTang);
        TextView tvLVKhu = convertView.findViewById(R.id.tvLVKhu);
        TextView tvLVLoaiPhong = convertView.findViewById(R.id.tvLVLoaiPhong);
        TextView tvLVGia = convertView.findViewById(R.id.tvLVGia);
        TextView tvLVDaDat = convertView.findViewById(R.id.tvLVDaDat);

        tvLVTenPhong.setText(room.getRoomnumber());
        tvLVTang.setText(String.valueOf(room.getFloor()));
        tvLVKhu.setText(room.getArea());
        tvLVLoaiPhong.setText(String.valueOf(room.getRoomtype()));
        tvLVGia.setText(String.valueOf((int)room.getRoomprice()));

        List<Account> registrants = databaseHelper.getRegistrantsByRoom(room.getRoomnumber(), room.getArea());
        tvLVDaDat.setText(registrants.size() + "/5");

        return convertView;
    }
}