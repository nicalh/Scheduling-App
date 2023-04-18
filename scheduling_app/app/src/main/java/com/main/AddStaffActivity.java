package com.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.main.Caller.LocalDB;
import com.main.Caller.StaffInfo;
import com.main.databinding.ActivityAddStaffBinding;


/*
    Project:    Scheduling App
    Authors:    Taylor Bennett
                Noman Khan
                Mitch Driedger
                Nick Hogan

    File:       AddStaffActivity.java
    Purpose:    Activity to add new staff to the staff database.
*/
public class AddStaffActivity extends AppCompatActivity {
    /*
        Used for displaying this fragment.
        */
    private @NonNull ActivityAddStaffBinding binding;
    /*
        Editable text of first name.
        */
    private EditText FName;
    /*
        Editable text of last name.
        */
    private EditText LName;
    /*
        Editable text of phone number.
        */
    private EditText PhoneNum;
    /*
        Editable text of email address.
        */
    private EditText Email;
    /*
        Editable spinner of opening experience.
        */
    private Spinner Exp_open;
    /*
        Editable spinner of closing experience.
        */
    private Spinner Exp_close;
    /*
        Boolean to represent if an issue exists with the input of the user.
        */
    private boolean issue_exists = false;


    /*
    Function:       onCreate
    Parameters:     Bundle savedInstanceState
    Returns:        void
    Purpose:        - to handle the initialization of the add staff activity
                    - handles the add staff logic

    Author:         Taylor Bennett
    Log (2021-10-27) BENNETT:   - Created the activity class
                                - added the toolbar edits
                                - added listener for the cancel button
                                - return to mainActivity function
    Log (2021-10-29) BENNETT:   added the submit button handling area
    Log (2021-11-01) NOMAN, MITCH: - Updated the class
                                - Added add_staff functionality
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        binding = ActivityAddStaffBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // edit the toolbar title
        binding.addstaffToolbar.setTitle(R.string.title_add_staff);
        binding.addstaffToolbar.setTitleTextColor(getResources().getColor(R.color.design_default_color_on_primary));

        // handle the spinners
        spinUpSpinner(R.array.experience_type, R.id.addstaff_trained_to_open_spinner);
        spinUpSpinner(R.array.experience_type, R.id.addstaff_trained_to_close_spinner);

        // handle the next button
        Button next = (Button) findViewById(R.id.addstaff_nextbutton);

        // warning text
        TextView warning = (TextView)findViewById(R.id.addstaff_conflictdisplay);

        //Handles inputs for first name, last name, phone, email
        FName = findViewById(R.id.inputFName);
        LName = findViewById(R.id.inputLName);
        PhoneNum = findViewById(R.id.inputPhoneNum);
        Email = findViewById(R.id.inputEmail);
        Exp_open = findViewById(R.id.addstaff_trained_to_open_spinner);
        Exp_close = findViewById(R.id.addstaff_trained_to_close_spinner);

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                String fname = FName.getText().toString();
                String lname = LName.getText().toString();
                String phoneNum = PhoneNum.getText().toString();
                String email = Email.getText().toString();
                String exp_open = Exp_open.getSelectedItem().toString();
                String exp_close = Exp_close.getSelectedItem().toString();

                // ensure no spaces in staff names
                if (!fname.contains(" ") && !lname.contains(" ")) {
                    // ensure all text inputs are filled
                    if (!fname.matches("") && !lname.matches("") && !phoneNum.matches("") && !email.matches("")) {

                        // check for staff with same name
                        StaffInfo currentStaff = LocalDB.getDatabase(getApplicationContext())
                                .getStaffDao().findByName(fname, lname);
                        if (currentStaff != null) {
                            if (currentStaff.employed.equals("Current Staff")) {
                                warning.setText("Staff with name: "+fname+" "+lname+" already exists!");
                                warning.setVisibility(View.VISIBLE);
                            }
                        }

                        // add staff to DB
                        else {
                            warning.setVisibility(View.INVISIBLE);
                            StaffInfo staff = new StaffInfo(fname, lname, phoneNum, email, exp_open, exp_close);
                            // set to current employee by default
                            staff.setEmployed(getResources().getString(R.string.employment_stat_current));
                            LocalDB.getDatabase(getApplicationContext()).getStaffDao().insertStaff(staff);
                            // locate UID of inserted staff
                            StaffInfo staffmem = LocalDB.getDatabase(getApplicationContext())
                                    .getStaffDao().findByName(fname, lname);
                            // proceed to availability activity
                            Intent availIntent = new Intent(getBaseContext(), AvailabilityActivity.class);
                            availIntent.putExtra("UID", staffmem.getUid());
                            startActivity(availIntent);
                        }
                    }
                    else {
                        warning.setText("Missing information!");
                        warning.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    warning.setText("First/Last names cannot contain spaces!");
                    warning.setVisibility(View.VISIBLE);
                }
            }
        });

        // handle the cancel button
        Button cancel = (Button) findViewById(R.id.addstaff_cancelbutton);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                returnToMainActivity();
            }
        });

        // handle the conflict display
        // TODO-> CONFLICT DISPLAY LOGIC
        if (issue_exists){
            TextView conflict_disp = findViewById(R.id.addstaff_conflictdisplay);
            conflict_disp.setText("ISSUE DETECTED");
        }else{
            TextView conflict_disp = findViewById(R.id.addstaff_conflictdisplay);
            conflict_disp.setText("");
        }

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