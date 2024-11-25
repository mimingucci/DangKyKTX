package com.example.quanlyktx.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {

    private List<String> rentList;

    public CustomAdapter(Context context, List<String> rentList) {
        super(context, 0, rentList);
        this.rentList = rentList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String rentInfo = rentList.get(position);
        String[] parts = rentInfo.split("\n");
        String status = parts[2].split(":")[1].trim(); // Assuming status is the third line, adjust as needed

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView textView = customView.findViewById(android.R.id.text1);
        textView.setText(rentInfo);

        // Set background color based on status
        if (status.equals("chưa duyệt")) {
            textView.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_orange_light));
        } else if (status.equals("thất bại")) {
            textView.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_red_light));
        } else if (status.equals("thành công")) {
            textView.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_green_light));
        } else {
            // Default color if status doesn't match expected values
            textView.setBackgroundColor(getContext().getResources().getColor(android.R.color.white));
        }

        return customView;
    }
}