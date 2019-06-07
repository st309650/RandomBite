package com.sutr.practice;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.List;
import java.util.Locale;


public class userinputfragment extends Fragment{

   private  EditText addressEdit;
   private ProgressBar progressBar;
   private TextView infoText;
   private Button checkBox;
   private EditText price;
    private String spinner_input= "Input from Spinner";
    private EditText foodType; //Not Use
    private Spinner spinner;
    private double price_input=9;
    private ArrayAdapter<CharSequence> adapter;

    private TextInputLayout inputLayoutAddress, inputLayoutPrice;

    //Context context;
   Activity context;
    GeocodeAsyncTask task;
    Address address1;
    String errorMessage = "";
    String name;
    private static final String TAG = "MAIN_ACTIVITY_ASYNC";

    String location_empty="Location is empty.";
    String error_no_service=    "Service not available";
    String error_location_not_found="Location not found";

    UserInputListener activityCommander;
    //this interface would be implimented by the main activity
    //ensure that the method below would be implemented
    public  interface UserInputListener{
        public void passAddress(double lon, double lat, String name, double price);

    }

    // get called when we attach the fragment into the main activity

    @Override
    public void onAttach(Activity context) {
        this.context = context;
        try {
            activityCommander = (UserInputListener)context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }
        super.onAttach(context);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //this is what layout we're using for this fragment
        View view = inflater.inflate(R.layout.userinput,container,false);

        addressEdit = (EditText) view.findViewById(R.id.addressEdit);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        inputLayoutAddress = (TextInputLayout) view.findViewById(R.id.input_layout_addressEdit);
        inputLayoutPrice = (TextInputLayout) view.findViewById(R.id.input_layout_maxPrice);
        infoText = (TextView) view.findViewById(R.id.infoText);
        checkBox = (Button) view.findViewById(R.id.actionButton);
        spinner = (Spinner) view.findViewById(R.id.food_choice_spinner1);
        price =(EditText) view.findViewById(R.id.maxPrice);
        foodType = (EditText) view.findViewById(R.id.FoodType);
        foodType.setClickable(false);
        foodType.setCursorVisible(false);
        //show the dropdown menu
        adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.type_of_food_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //when user select
        //get the selected value
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //when select an item on the drop down menu
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                spinner_input= parent.getItemAtPosition(pos).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        //run the location task to get the longtitude and the latitude of the user

        checkBox.setOnClickListener(
                new View.OnClickListener(){
                    //what happen when the buuton click
                    public void onClick(View v){
                        task = new GeocodeAsyncTask();
                        System.out.println("Just created new geo");
                        buttonClicked(v);
                    }
                }
        );

        return  view;
    }


    //when the button click
    public void buttonClicked(View v){

        //get the price
        //if the price field is empty, pick again

        /*
        addressEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    addressEdit.setHint(getString(R.string.address_hint));
                }else{
                    addressEdit.setHint("");
                }
            }
        });

        price.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    addressEdit.setHint(getString(R.string.price_hint));
                }else{
                    addressEdit.setHint("");
                }
            }
        });
*/
        String temp = price.getText().toString();
        if (temp.trim().equals("")){
            //Toast.makeText(getActivity(),"Max Price is empty. Default is $", Toast.LENGTH_SHORT).show();
            //buttonClicked(v);

        }

        if(!temp.isEmpty())
            try{
                price_input = Double.parseDouble(temp);
            } catch(Exception e1){
                e1.printStackTrace();
            }

        //Toast.makeText(getActivity(),"Spinner input: " + spinner_input + " Price: " + price_input , Toast.LENGTH_LONG).show();

        String name = addressEdit.getText().toString();
        if(name.isEmpty()){
            inputLayoutAddress.setError(location_empty);
            /*
            Toast.makeText(getActivity(),location_empty
                    ,
                    Toast.LENGTH_SHORT).show();
                    */
        }else {
            inputLayoutAddress.setErrorEnabled(false);
            task.execute(name);
            //checkBox.setClickable(false);
        }

    }


    class GeocodeAsyncTask extends AsyncTask<String, Void, Address> {

        @Override
        protected void onPreExecute() {
           // infoText.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Address doInBackground(String... name1) {

            name = name1[0];
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            System.out.println("After dong geo");
            List<Address> addresses = null;


            try {
                addresses = geocoder.getFromLocationName(name, 1);
            } catch (IOException e) {
                errorMessage = error_no_service;
                Log.e(TAG, errorMessage, e);
            }

            if(addresses != null && addresses.size() > 0) {
                address1= addresses.get(0);
                return addresses.get(0);
            }else{
                errorMessage=error_location_not_found;


            }
            return null;
        }

        protected void onPostExecute(Address address) {
            super.onPostExecute(address);
            progressBar.setVisibility(View.INVISIBLE);
            infoText.setVisibility(View.VISIBLE);

            if(address == null) {

                infoText.setText(errorMessage);

            }
            else {
                String addressName = "";
                for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressName += " --- " + address.getAddressLine(i);
                }
                address1 = address;

                process(address);


            }
        }
    }

    public void process(Address address){

        //calling this method in the MainActvity

        //price, spinner input, location
        //if there is no error

        activityCommander.passAddress(address1.getLongitude(), address1.getLatitude(), spinner_input, price_input);

/*
        infoText.setText("Latitude: " + address1.getLatitude() + "\n" +
                "Longitude: " + address1.getLongitude() + "\n" +
                "Address: " + address1.getCountryCode())
        ;
*/
    }

}
