package com.main;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.main.Caller.ShiftInfo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ShiftAdaptor extends ArrayAdapter{

    private final Context context;
    private final List<ShiftInfo> shift;

    public ShiftAdaptor(Context context, List<ShiftInfo> shifts){
        super(context, -1, shifts);
        this.context = context;
        this.shift = shifts;
    }

    @Override
    public int getCount(){
        return shift.size();
    }

    @Override
    public ShiftInfo getItem(int position){
        return shift.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View shiftView = inflater.inflate(R.layout.shift_view, parent, false);
        ShiftInfo shiftPos = getItem(position);

        TextView shiftDateType = (TextView) shiftView.findViewById(R.id.shiftViewDateType);
        TextView shiftStaff1 = (TextView) shiftView.findViewById(R.id.shiftViewStaff1);
        TextView shiftStaff2 = (TextView) shiftView.findViewById(R.id.shiftViewStaff2);
        TextView shiftStaff3 = (TextView) shiftView.findViewById(R.id.shiftViewStaff3);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy EEEE ", Locale.CANADA);
        shiftDateType.setText(sdf.format(shiftPos.date) + shiftPos.shiftType);
        shiftStaff1.setText("Staff 1: " + shiftPos.staff1);
        shiftStaff2.setText("Staff 2: " + shiftPos.staff2);
        shiftStaff3.setText("Staff 3: " + shiftPos.staff3);

        return shiftView;
    }
}
