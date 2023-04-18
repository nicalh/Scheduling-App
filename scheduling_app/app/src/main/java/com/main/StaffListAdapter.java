package com.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.main.Caller.StaffInfo;
import com.main.R;

import java.util.List;

public class StaffListAdapter extends RecyclerView.Adapter<StaffListAdapter.StaffListHolder> {
    private final LayoutInflater mInflater;
    private List<StaffInfo> mStaff;

    StaffListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public StaffListHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View staffView = mInflater.inflate(R.layout.employee_layout, parent, false);
        return new StaffListHolder(staffView);
    }

    @Override
    public void onBindViewHolder(StaffListHolder holder, int position){
        for(int i=0; i < getItemCount(); i++){
            StaffInfo current = mStaff.get(i);
            String fullName = current.getFName() + " " + current.getLName();
            holder.nameTextView.setText(fullName);
            holder.phoneTextView.setText(current.getPhoneNum());
            holder.infoButton.setId(i);
    }}

    void setStaff(List<StaffInfo> staffs){
        mStaff = staffs;
        notifyDataSetChanged();
    }

    //This is used to get the count of how many staff exist.
    //Is null at start but we can't return null so it returns 0
    @Override
    public int getItemCount(){
        if(mStaff != null)
            return mStaff.size();
        else return 0;
    }

    class StaffListHolder extends RecyclerView.ViewHolder{
        //private final TextView staffItemView;
        TextView nameTextView;
        TextView phoneTextView;
        ImageView faceImageView;
        ImageButton infoButton;

        private StaffListHolder(View staffView){
            super(staffView);
            nameTextView = (TextView) staffView.findViewById(R.id.firstLine);
            phoneTextView = (TextView) staffView.findViewById(R.id.secondLine);
            faceImageView = (ImageView) staffView.findViewById(R.id.face);
            infoButton = (ImageButton) staffView.findViewById(R.id.imageButton);
            //staffItemView = staffView.findViewById(R.id.staffView);

        }
    }
}
