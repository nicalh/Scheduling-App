package com.main;


import android.content.Intent;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/*
    Project:    Scheduling App
    Authors:    Taylor Bennett
                Noman Khan
                Mitch Driedger
                Nick Hogan

    File:       SimpleArrayAdapter.java
    Purpose:    Array adapter extension that allows the employee list view to be populated with
                instances of employee_layout.xml. Adds functionality to info image button.
*/
public class SimpleArrayAdapter extends ArrayAdapter<String> {

    /*
    The current fragment context.
     */
    private final Context context;
    /*
    List of names to populate employee list view.
     */
    private final List<String>  names;
    /*
    List of phone numbers to populate employee list view.
     */
    private final List<String> phones;
    /*
    List of emails to populate employee list view.
     */
    private final List<String> emails;
    /*
    List of employed info to populate employee list view.
     */
    private final List<String> employed;

    private final List<String> trained_open;

    private final List<String> trained_close;

    private final List<ArrayList> upcoming;


    /*
    Function:       SimpleArrayAdapter
    Parameters:     Context context
                    String[] names
                    String[] phones
    Returns:        None
    Purpose:        - to handle the initialization of the array adapter

    Author:         Mitchell Driedger
    Log (2021-10-23) DRIEDGER:   - Created this file
        (2021-11-24) DRIEDGER:   - Added trainedOpen, trainedClose
    */
    public SimpleArrayAdapter(Context context, List<String> names, List<String> phones, List<String> emails, List<String> employed, List<String> trained_open, List<String> trained_close, List<ArrayList> upcoming) {
        super(context,-1, names);
        this.context = context;
        this.names = names;
        this.phones = phones;
        this.emails = emails;
        this.employed = employed;
        this.trained_open = trained_open;
        this.trained_close = trained_close;
        this.upcoming = upcoming;
    }


    /*
    Function:       getView
    Parameters:     int position
                    View convertView
                    ViewGroup parent
    Returns:        View
    Purpose:        - establishes the format of the view in the modified array adapter

    Author:         Mitchell Driedger
    Log (2021-10-23) DRIEDGER:   - Created this file
    Log (2021-10-28) DRIEDGER:   - Added button functionality
    (2021-11-24)     DRIEDGER:   - Added trainedOpen, trainedClose
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.employee_layout, parent, false);
        String pos = getItem(position);

        // Find objects for employee layout fragment
        TextView nameTextView = (TextView) rowView.findViewById(R.id.firstLine);
        TextView phoneTextView = (TextView) rowView.findViewById(R.id.secondLine);
        ImageView faceImageView = (ImageView) rowView.findViewById(R.id.face);
        ImageButton infoButton = (ImageButton) rowView.findViewById(R.id.imageButton);

        // Sets info button to same ID as the index in the list of the name it is associated with
        for (int i=0; i< names.size(); i++) {
            if (names.get(i).equals(pos)) {
                nameTextView.setText(names.get(i));
                phoneTextView.setText(phones.get(i));
                infoButton.setId(i);
            }
        }

        // Info button click listener, associates the proper name with the info button
        infoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                Intent intent_selected = new Intent(getContext(), SelectedEmployeeActivity.class);
                intent_selected.putExtra("name", names.get(infoButton.getId()));
                intent_selected.putExtra("phone", phones.get(infoButton.getId()));
                intent_selected.putExtra("employed", employed.get(infoButton.getId()));
                intent_selected.putExtra("email", emails.get(infoButton.getId()));
                intent_selected.putExtra("trained_open", trained_open.get(infoButton.getId()));
                intent_selected.putExtra("trained_close", trained_close.get(infoButton.getId()));
                if (upcoming.size() > 0) {
                    intent_selected.putExtra("upcoming", upcoming.get(infoButton.getId()));
                }
                context.startActivity(intent_selected);
            }
        });
        return rowView;
    }
}
