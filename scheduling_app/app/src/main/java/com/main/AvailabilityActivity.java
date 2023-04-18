package com.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.main.Caller.LocalDB;
import com.main.Caller.StaffInfo;
import com.main.databinding.ActivityAvailabilityBinding;
import java.util.ArrayList;


/*
    Project:    Scheduling App
    Authors:    Taylor Bennett
                Noman Khan
                Mitch Driedger
                Nick Hogan

    File:       AvailabilityActivity.java
    Purpose:    Activity to set the availability for a given staff in the staff database.
*/
public class AvailabilityActivity extends AppCompatActivity{
    /*
        Used for displaying this fragment.
        */
    private @NonNull ActivityAvailabilityBinding binding;
    /*
        A list of the spinners in this activity.
        */
    private ArrayList<Spinner> SpinnerList = new ArrayList<Spinner>();


    /*
    Function:       onCreate
    Parameters:     Bundle savedInstanceState
    Returns:        void
    Purpose:        - to handle the initialization of the availability activity
                    - handles the availability logic

    Author:         Mitchell Driedger
    Log (2021-11-25) DRIEDGER:   - created this class
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        binding = ActivityAvailabilityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // edit the toolbar title
        binding.availabilityToolbar.setTitle("SET AVAILABILITY");
        binding.availabilityToolbar.setTitleTextColor(getResources().getColor(R.color.design_default_color_on_primary));

        // locate staff with UID
        Intent intent = getIntent();
        int UID = intent.getExtras().getInt("UID");
        StaffInfo staffmem = LocalDB.getDatabase(getApplicationContext())
                .getStaffDao().findByUID(UID);

        // populate spinner list
        SpinnerList.add(findViewById(R.id.sunday_spinner));
        SpinnerList.add(findViewById(R.id.monday_spinner));
        SpinnerList.add(findViewById(R.id.tuesday_spinner));
        SpinnerList.add(findViewById(R.id.wednesday_spinner));
        SpinnerList.add(findViewById(R.id.thursday_spinner));
        SpinnerList.add(findViewById(R.id.friday_spinner));
        SpinnerList.add(findViewById(R.id.saturday_spinner));

        // spin up spinners
        spinUpSpinner(R.array.weekend_availability_type, R.id.sunday_spinner);
        spinUpSpinner(R.array.availability_type, R.id.monday_spinner);
        spinUpSpinner(R.array.availability_type, R.id.tuesday_spinner);
        spinUpSpinner(R.array.availability_type, R.id.wednesday_spinner);
        spinUpSpinner(R.array.availability_type, R.id.thursday_spinner);
        spinUpSpinner(R.array.availability_type, R.id.friday_spinner);
        spinUpSpinner(R.array.weekend_availability_type, R.id.saturday_spinner);

        // fill spinners with current availability
        int[] availability = staffmem.getAvailability();
        if (availability[0] == 0) {
            SpinnerList.get(0).setSelection(0);
        }
        else {
            SpinnerList.get(0).setSelection(1);
        }
        for (int i=1; i<6; i++) {
            SpinnerList.get(i).setSelection(availability[i]);
        }
        if (availability[6] == 0) {
            SpinnerList.get(6).setSelection(0);
        }
        else {
            SpinnerList.get(6).setSelection(1);
        }

        // handle the submit button
        Button submit = (Button) findViewById(R.id.availability_submit_button);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                // find input from spinners
                for (int i=0; i<7; i++) {
                    if (SpinnerList.get(i).getSelectedItem().toString().equals("Unavailable")) {
                        availability[i] = 0;
                    }
                    else if (SpinnerList.get(i).getSelectedItem().toString().equals("Morning")) {
                        availability[i] = 1;
                    }
                    else if (SpinnerList.get(i).getSelectedItem().toString().equals("Afternoon")) {
                        availability[i] = 2;
                    }
                    else {
                        availability[i] = 3;
                    }
                }

                // set staffmem to reflect selections
                staffmem.setAvailability(availability[0], availability[1], availability[2],
                        availability[3], availability[4], availability[5], availability[6]);
                LocalDB.getDatabase(getApplicationContext()).getStaffDao().updateUser(staffmem);

                // actions completed, return to main activity
                returnToMainActivity();
            }
        });

        // handle the skip button
        Button skip = (Button) findViewById(R.id.availability_skip_button);
        skip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                returnToMainActivity();
            }
        });
    }


    /*
     Function:       spinUpSpinner()
     Parameters:     int textArrayResId, int spinnerId
     Returns:        None
     Purpose:        - takes the text array id and finds it for the array adapter
                     - takes the spinnerID and applies the adapter to it
                     - basically this function takes the spinner information and
                     populates the spinner with the information provided.

     Author:         Taylor Bennett
     Log (2021-10-28) BENNETT:   Created this function
     */
    public void spinUpSpinner(int textArrayResId, int spinnerId){
        Spinner spinner = (Spinner) findViewById(spinnerId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                textArrayResId, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    /*
     Function:       returnToMainActivity
     Parameters:     None
     Returns:        void
     Purpose:        - navigates back to the mainActivity (actually parent activity)

     Author:         Taylor Bennett
     Log (2021-10-27) BENNETT:   Created this function
     */
    public void returnToMainActivity(){
        NavUtils.navigateUpFromSameTask(this);
    }
}