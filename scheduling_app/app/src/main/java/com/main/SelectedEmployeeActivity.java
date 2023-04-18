package com.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.main.Caller.LocalDB;
import com.main.Caller.ShiftInfo;
import com.main.databinding.FragmentSelectedEmployeeBinding;

import java.util.Date;
import java.util.List;


/*
    Project:    Scheduling App
    Authors:    Taylor Bennett
                Noman Khan
                Mitch Driedger
                Nick Hogan

    File:       SelectedEmployeeActivity
    Purpose:    Activity to display an employee selected by the user from the employee fragment.
                Contains information about employee arranged in text boxes and lists.
*/
public class SelectedEmployeeActivity extends AppCompatActivity {

    /*
    Used for displaying this fragment.
     */
    private @NonNull FragmentSelectedEmployeeBinding binding;


    /*
    Function:       onCreate
    Parameters:     Bundle savedInstanceState
    Returns:        void
    Purpose:        - to handle the initialization of the selected employee activity
                    - handles the selected employee logic

    Author:         Mitchell Driedger
    Log (2021-10-28) DRIEDGER:  - Created the activity class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_selected_employee);
        binding = FragmentSelectedEmployeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.selectedToolbar.setTitle("STAFF INFORMATION");
        binding.selectedToolbar.setTitleTextColor(getResources().getColor(R.color.design_default_color_on_primary));

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        String employed = intent.getExtras().getString("employed");
        String trained_open = intent.getExtras().getString("trained_open");
        String trained_close = intent.getExtras().getString("trained_close");
        String phone = intent.getExtras().getString("phone");
        String email = intent.getExtras().getString("email");
        List<String> upcoming = intent.getExtras().getStringArrayList("upcoming");

        // find name, age, gender, email, and phone number text views
        TextView nameLine = (TextView) findViewById(R.id.nameLine);
        //TextView employedLine = (TextView) findViewById(R.id.secondAgeLine);
        TextView trained_open_line = (TextView) findViewById(R.id.secondTrainedToOpenLine);
        TextView trained_close_line = (TextView) findViewById(R.id.secondTrainedToCloseLine);
        TextView phoneLine = (TextView) findViewById(R.id.secondPhoneLine);
        TextView emailLine = (TextView) findViewById(R.id.secondEmailLine);

        // set name, age, gender, email, and phone number text views
        nameLine.setText(name);
        //employedLine.setText(employed);
        trained_open_line.setText(trained_open);
        trained_close_line.setText(trained_close);

        //trainedLine.setText(trained);
        phoneLine.setText(phone);
        emailLine.setText(email);

        //upcoming shifts
        ListView upcomingList = (ListView) findViewById(R.id.notif_upcoming_events_list);
        ArrayAdapter<String> upcomingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, upcoming);
        upcomingList.setAdapter(upcomingAdapter);

        // handle the back button
        Button back = (Button) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                returnToMainActivity();
            }
        });

        // handle the edit button
        Button edit = (Button) findViewById(R.id.editButton);
        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                Intent intent_edit_staff = new Intent(getBaseContext(), EditStaffActivity.class);
                String[] broken_name = name.split(" ");
                intent_edit_staff.putExtra("fname", broken_name[0]);
                intent_edit_staff.putExtra("lname", broken_name[1]);
                intent_edit_staff.putExtra("employed", employed);
                intent_edit_staff.putExtra("trained_open", trained_open);
                intent_edit_staff.putExtra("trained_close", trained_close);
                intent_edit_staff.putExtra("phone", phone);
                intent_edit_staff.putExtra("email", email);
                startActivity(intent_edit_staff);
            }
        });
    }


    /*
     Function:       returnToMainActivity
     Parameters:     None
     Returns:        None
     Purpose:        - navigates back to the mainActivity (actually parent activity)
     Author:         Mitchell Driedger
     Log (2021-10-28) DRIEDGER:   Created this function
     */
    public void returnToMainActivity(){
        NavUtils.navigateUpFromSameTask(this);
    }
}