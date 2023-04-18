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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.main.Caller.LocalDB;
import com.main.Caller.ShiftInfo;
import com.main.Caller.StaffInfo;
import com.main.databinding.ActivityEditStaffBinding;

import java.io.Console;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/*
    Project:    Scheduling App
    Authors:    Taylor Bennett
                Noman Khan
                Mitch Driedger
                Nick Hogan

    File:       EditStaffActivity.java
    Purpose:    Activity to edit a selected staff.
*/
public class EditStaffActivity extends AppCompatActivity {
    /*
        Used for displaying this fragment.
        */
    private @NonNull ActivityEditStaffBinding binding;
    /*
        Boolean to represent if an issue exists with the input of the user.
         */
    private boolean issue_exists = false;
    /*
        String of the first name before editing.
        */
    private String og_fname;
    /*
        String of the last name before editing.
        */
    private String og_lname;

    private boolean yesDelete = true;


    /*
        Function:       onCreate
        Parameters:     Bundle savedInstanceState
        Returns:        void
        Purpose:        - to handle the initialization of the edit staff activity
                        - handles the edit staff logic

        Author:         Taylor Bennett
        Log (2021-10-27) BENNETT:   - Created the activity class
                                    - added the toolbar edits
                                    - added listeners for the cancel button
                                    - return to mainActivity function
        Log (2021-10-29) BENNETT:   added the submit button handling area
         */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff);

        binding = ActivityEditStaffBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Edit the toolbar text and color
        binding.editstaffToolbar.setTitle(R.string.title_edit_staff);
        binding.editstaffToolbar.setTitleTextColor(getResources().getColor(R.color.design_default_color_on_primary));

        TextView warning = (TextView)findViewById(R.id.editstaff_conflictdisplay);
        warning.setVisibility(View.INVISIBLE);

        // handle the submit button
        Button submit = (Button) findViewById(R.id.editstaff_nextbutton);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                EditText FName = findViewById(R.id.editTextTextPersonName3);
                EditText LName = findViewById(R.id.editTextTextPersonName4);
                EditText PhoneNum = findViewById(R.id.editTextPhone2);
                EditText Email = findViewById(R.id.editTextTextEmailAddress2);
                Spinner exp_open = findViewById(R.id.editstaff_trained_to_open_spinner);
                Spinner exp_close = findViewById(R.id.editstaff_trained_to_close_spinner);

                // remove previous iteration of user object
                StaffInfo staffmem = LocalDB.getDatabase(getApplicationContext())
                        .getStaffDao().findByName(og_fname, og_lname);

                // get all changed info
                String fname = FName.getText().toString();
                String lname = LName.getText().toString();
                String phoneNum = PhoneNum.getText().toString();
                String email = Email.getText().toString();
                String trained_open = exp_open.getSelectedItem().toString();
                String trained_close = exp_close.getSelectedItem().toString();
//                String employed = Empl.getSelectedItem().toString();
                //StaffInfo staff = new StaffInfo(fname,lname,phoneNum, email, trained);
                staffmem.setFName(fname);
                staffmem.setLName(lname);
                staffmem.setPhoneNum(phoneNum);
                staffmem.setEmail(email);
                staffmem.setTrainedOpen(trained_open);
                staffmem.setTrained_close(trained_close);

                LocalDB.getDatabase(getApplicationContext()).getStaffDao().updateUser(staffmem);

                // populate item
//                staffmem.firstname = fname;
//                staffmem.lastname = lname;
//                staffmem.phoneNum = phoneNum;
//                staffmem.email = email;
//                staffmem.setTrained(trained);
//                staffmem.setEmployed(employed);

                //LocalDB.getDatabase(getApplicationContext()).getStaffDao().insertStaff(staffmem);

                // proceed to availability activity
                Intent availIntent = new Intent (getBaseContext(), AvailabilityActivity.class);
                availIntent.putExtra("UID", staffmem.getUid());
                startActivity(availIntent);
            }
        });

        // handle the cancel button
        Button cancel = (Button) findViewById(R.id.editstaff_cancelbutton);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                returnToMainActivity();
            }
        });

        // handle the remove button
        Button remove = (Button) findViewById(R.id.editstaff_deletebutton);
        remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                EditText FName = findViewById(R.id.editTextTextPersonName3);
                EditText LName = findViewById(R.id.editTextTextPersonName4);
                EditText PhoneNum = findViewById(R.id.editTextPhone2);
                EditText Email = findViewById(R.id.editTextTextEmailAddress2);
                Spinner exp_open = findViewById(R.id.editstaff_trained_to_open_spinner);
                Spinner exp_close = findViewById(R.id.editstaff_trained_to_close_spinner);

                String fname = FName.getText().toString();
                String lname = LName.getText().toString();
                String phoneNum = PhoneNum.getText().toString();
                String email = Email.getText().toString();
                String employed = getResources().getString(R.string.employment_stat_past);
                String trained_open = exp_open.getSelectedItem().toString();
                String trained_close = exp_close.getSelectedItem().toString();

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                // find and remove all upcoming shifts for staff to be deleted
                List<ShiftInfo> shifts = LocalDB.getDatabase(getApplicationContext()).getShiftDao().findShiftByName(fname + " " + lname);
                Date today = new Date(System.currentTimeMillis());
                for (int j = 0; j < shifts.size(); j++) {
                    if (today.before(shifts.get(j).date)) {
                        ShiftInfo shift = shifts.get(j);
                        String staff1 = shift.getStaffName1();
                        String staff2 = shift.getStaffName2();
                        String staff3 = shift.getStaffName3();
                        if (staff1.equals(fname + " " + lname)) {
                            staff1 = "No selection";
                        }
                        if (staff2.equals(fname + " " + lname)) {
                            staff2 = "No selection";
                        }
                        if (staff3.equals(fname + " " + lname)) {
                            staff3 = "No selection";
                        }
                        shift.setStaff1(staff1);
                        shift.setStaff2(staff2);
                        shift.setStaff3(staff3);
                        LocalDB.getDatabase(getApplicationContext()).getShiftDao().updateShift(shift);
                    }
                }

                // set employed to former staff
                StaffInfo staff_mem = LocalDB.getDatabase(getApplicationContext())
                        .getStaffDao().findByName(fname, lname);
                staff_mem.setFName(fname);
                staff_mem.setLName(lname);
                staff_mem.setEmployed(employed);
                staff_mem.setPhoneNum(phoneNum);
                staff_mem.setEmail(email);
                staff_mem.setTrainedOpen(trained_open);
                staff_mem.setTrained_close(trained_close);
                LocalDB.getDatabase(getApplicationContext()).getStaffDao().updateUser(staff_mem);
                returnToMainActivity();
            }
        });

        // handle the spinners
//        spinUpSpinner(R.array.employment_type, R.id.editstaff_employmentspinner);
        spinUpSpinner(R.array.experience_type, R.id.editstaff_trained_to_open_spinner);
        spinUpSpinner(R.array.experience_type, R.id.editstaff_trained_to_close_spinner);

        // fill textboxes with information if arrived via 'edit' button on selected employee
        Intent intent = getIntent();
        if (intent.hasExtra("fname")) {
            
            // get intents
            String fname = intent.getExtras().getString("fname");
            String lname = intent.getExtras().getString("lname");
            og_fname = fname;
            og_lname = lname;
            String employed = intent.getExtras().getString("employed");
            String trained_open = intent.getExtras().getString("trained_open");
            String trained_close = intent.getExtras().getString("trained_close");

            //System.out.println("["+employed + "]  [" + trained+"]");
            String phone = intent.getExtras().getString("phone");
            String email = intent.getExtras().getString("email");

            // set intents
            EditText fnameBox = findViewById(R.id.editTextTextPersonName3);
            fnameBox.setText(fname);
            EditText lnameBox = findViewById(R.id.editTextTextPersonName4);
            lnameBox.setText(lname);
            EditText phoneBox = findViewById(R.id.editTextPhone2);
            phoneBox.setText(phone);
            EditText emailBox = findViewById(R.id.editTextTextEmailAddress2);
            emailBox.setText(email);
//            Spinner employmentSpinner = findViewById(R.id.editstaff_employmentspinner);

            String curr_employed_str = getResources().getString(R.string.employment_stat_current);
            String trained_str = getResources().getString(R.string.trained_stat_trained);


            // set employed spinner to display what the current status of the staff mem is
//            if (employed.compareTo(curr_employed_str) == 0){
//                employmentSpinner.setSelection(0);
//            } else{
//                employmentSpinner.setSelection(1);
//            }

            // set experience spinner to display what the current status of the staff mem is
            Spinner openSpinner = findViewById(R.id.editstaff_trained_to_open_spinner);
            if (trained_open.compareTo(trained_str) == 0){
                openSpinner.setSelection(1);
            } else {
                openSpinner.setSelection(0);
            }

            Spinner closeSpinner = findViewById(R.id.editstaff_trained_to_close_spinner);
            if (trained_close.compareTo(trained_str) == 0){
                closeSpinner.setSelection(1);
            } else {
                closeSpinner.setSelection(0);
            }

        }
        //TODO - fill staff selection spinner with employees

        // update the conflict display
        //TODO - Create Conflict Display Logic
        if (issue_exists){
            TextView conflict_display = findViewById(R.id.editstaff_conflictdisplay);
            conflict_display.setText("ISSUE DETECTED");
        } else {
            TextView conflict_display = findViewById(R.id.editstaff_conflictdisplay);
            conflict_display.setText("");
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
     Returns:        None
     Purpose:        - navigates back to the mainActivity (actually parent activity)

     Author:         Taylor Bennett
     Log (2021-10-27) BENNETT:   Created this function
     */
    public void returnToMainActivity(){
        NavUtils.navigateUpFromSameTask(this);
    }
}