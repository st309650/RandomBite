package com.sutr.practice;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import android.app.Fragment;
import android.widget.Toast;

//import org.apache.http.client.HttpClient;

import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import retrofit2.Call;
import retrofit2.Response;

//When get the release apk
//we need another google map api
//look at the gradle and the androidmanifest
//the key changes if we do release or debug
//get the new RELEASE SHA1 then use it to create the release key
//keytool -list -v -keystore [where you store your .jks file]  -alias [look at it when u press building signed key]
//http://www.truiton.com/2015/04/obtaining-sha1-fingerprint-android-keystore/
public class MainActivity extends AppCompatActivity implements userinputfragment.UserInputListener {


    //Map<String, String> mPrams;

    //map


    public static final int USE_ADDRESS_LOCATION = 2;
    ///*
    //Yelp
    YelpFusionApiFactory apiFactory;

    YelpFusionApi yelpApi;

    //Map
    Map<String, String> params = new HashMap<>();
    ArrayList<Business> businesses;
    String businessName;
    double lon, lat;
    double inputLong, inputLat;
    double price;
    String spinner_choice;
    String bussinessLocation;
    String price_keyword;
    String errorMessage="";
    String phone;
    String url;
    //erros
    String error_cannot_find_res="Cannot find any restaurant.";
    String error_no_response="No response.";
    String search_again="Please search again.";
    String press_i_for_more_info="Press ( i ) for more information";
    ProgressBar progressBar1;
    //toolbar
    Toolbar toolbar;


//*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("INSIDE MAN");
        //action bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //attach toolbar as an action bar
        setSupportActionBar(toolbar);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar);

       // notuse.setVisibility(View.INVISIBLE);

    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    // action when click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
         /*
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;
*/
            case R.id.action_info:
               // Toast.makeText(this, "Ifno", Toast.LENGTH_SHORT).show();
                // User chose the "Info" action, mark the current item
                // as a favorite...
                // go to info child activity
                startActivity(new Intent(this, Child_Info_Activity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    //pass to the second fragment
    //call when click the button
    @Override
    public void passAddress(double lon, double lat, String spinner_input , double price_input) {

        inputLong = lon;
        inputLat = lat;
        spinner_choice = spinner_input;
        price= price_input;


        new SearchYelp().execute();

    }

    class SearchYelp extends AsyncTask<String, Void, Address>
    {
        @Override
        protected void onPreExecute() {
           progressBar1.setVisibility(View.VISIBLE);
        }

        @Override
        protected Address doInBackground(String... Mparams) {

            //execute the search api
            String url = "https://api.yelp.com/v3/autocomplete?text=del&latitude=37.786882&longitude=-122.399972";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("Authorization", "Bearer Token");
            HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            System.out.println(responseEntity);
//
//            SONObject jObject = new JSONObject(response.getBody());
//            JSONArray jArray = jObject.getJSONArray("businesses");
//            HashMap<Integer, HashMap<String, String>> restaurantsInfo = new HashMap<>();
            // coordinates
///*
            apiFactory = new YelpFusionApiFactory();
            // /*
            try {
                yelpApi = apiFactory.createAPI(getString(R.string.ClientID), getString(R.string.Client_Secret));
            } catch (IOException e) {
                e.printStackTrace();
            }
//*/

            //expand catergory
            List<String> asianType = new ArrayList<String>();
            asianType.add("asianfusion");
            asianType.add("burmese");
            asianType.add("chinese");
            asianType.add("japanese");
            asianType.add("korean"); asianType.add("laos"); asianType.add("noodles");
            asianType.add("thai"); asianType.add("vietnamese"); asianType.add("filipino");

            //Euproan pian
            List<String> europeType= new ArrayList<String>();
            europeType.add("belgian"); europeType.add("british"); europeType.add("czech");
            europeType.add("french"); europeType.add("german"); europeType.add("greek");
            europeType.add("italian"); europeType.add("portuguese"); europeType.add("irish");

            //Mediterranean
            List<String> mediterType= new ArrayList<String>();
            mediterType.add("mediterranean"); mediterType.add("turkish");
            mediterType.add("mideastern");

            //African
            List<String> africanType= new ArrayList<String>();
            africanType.add("african");

            //North American
            List<String> naType= new ArrayList<String>();
            naType.add("tradamerican"); naType.add("bbq"); naType.add("breakfast_brunch");
            naType.add("cajun"); naType.add("burgers"); naType.add("hawaiian");
            naType.add("hotdog");

            //South American
            List<String> southAmericaType= new ArrayList<String>();
            southAmericaType.add("argentine"); southAmericaType.add("brazilian");
            southAmericaType.add("chilean"); southAmericaType.add("honduran");
            southAmericaType.add("latin"); southAmericaType.add("mexican");

            //Indian
            List<String> indian= new ArrayList<>();
            indian.add("indpak");
            //fastfood
            List<String> fastFood = new ArrayList<>();
            fastFood.add("hotdogs");

            //ImFeelingLucky
            List<String> lucky = new ArrayList<String>();
            lucky.add("chinese"); lucky.add("vietnamese"); lucky.add("mexican");
            lucky.add("hotdogs"); lucky.add("korean"); lucky.add("japanese");
            lucky.add("chilean"); lucky.add("brazilian"); lucky.add("bbq");
            lucky.add("burgers"); lucky.add("mediterranean"); lucky.add("greek");
            lucky.add("french"); lucky.add("german"); lucky.add("irish");
            lucky.add("noodles");


            //String delet = "Asian";

            List<List<String>> allType = new ArrayList<>();
            allType.add(asianType);
            allType.add(europeType);
            allType.add(mediterType);
            allType.add(africanType);
            allType.add(naType);
            allType.add(southAmericaType);
            allType.add(indian);
            allType.add(fastFood);
            allType.add(lucky);

            //input is the input of the food type enter by the user

            String input = spinner_choice;
            int index;
            switch(input){
                case "Asian":
                    index =0;
                    break;
                case "European":
                    index =1;
                    break;
                case "Mediterranean":
                    index=2;
                    break;
                case "African":
                    index =3;
                    break;
                case "North America":
                    index = 4;
                    break;
                case "South America":
                    index =5;
                    break;
                case "Indian":
                    index =6;
                    break;
                case "Fast Food":
                    index =7;
                    break;
                default:
                    index = 8;
                    break;
            }


            //params.put("term", "asian restaurant");
            params.put("latitude", String.valueOf(inputLat));
            params.put("longitude", String.valueOf(inputLong));
            params.put("radius", "16000");
            params.put("open_now", "true");
            setPrice(price);
            params.put("price", price_keyword);
            // price

            //if there is not a bussize or the thing have been run for 400 times
            int count =0;
            SearchResponse response = null;
            while(count <400 && response ==null) {

                //randomize the food category
                //if the user choose asian, choose differnent kind of asian food
                int foodType = getRandom(allType.get(index));
                params.put("categories", allType.get(index).get(foodType));

                Call<SearchResponse> call = yelpApi.getBusinessSearch(params);
                try {
                    response = call.execute().body();
                    if (response != null) {
                        //int totalNumberOfResult = response.getTotal();  // 3

                        businesses = response.getBusinesses();
                        try {
                            int finalBusinese = getRandom2(businesses);
                            businessName = businesses.get(finalBusinese).getName();
                            lat = businesses.get(finalBusinese).getCoordinates().getLatitude();
                            lon = businesses.get(finalBusinese).getCoordinates().getLongitude();
                            phone= businesses.get(finalBusinese).getPhone();
                            bussinessLocation = businesses.get(finalBusinese).getLocation().getAddress1() + ", " + businesses.get(finalBusinese).getLocation().getCity() +", "
                            + businesses.get(finalBusinese).getLocation().getState();
                            url = businesses.get(finalBusinese).getUrl();
                            Log.v("Business", businessName);
                        }catch (Exception ew){
                            errorMessage = error_cannot_find_res;


                        }

                        //Log.v("Location", businesses.get(finalBusinese).getCoordinates().getLatitude());
                        //show it on map and more information

                    }
                } catch (IOException e) {
                    errorMessage = error_no_response;
                }

                //increase the number of time try to search
                count++;
            }
            if(response ==null || count ==400){
                Log.v("Not good", " Not open");
            }
            //reset count
            count =0;



//*/
            return null;

        }

        protected void onPostExecute(Address address) {
            super.onPostExecute(address);
            progressBar1.setVisibility(View.INVISIBLE);

           // System.out.println(address.getLatitude() + " ADD");
            Map_View Map_view = (Map_View) getSupportFragmentManager().findFragmentById(R.id.fragment2);
            if(Map_view != null) {
                //If there is no error
                if(errorMessage.isEmpty()) {
                    errorMessage ="";
                    Map_view.display(businessName, bussinessLocation, lon, lat, phone, url);
                }else{
                    moreInfo();
                    Map_view.display(errorMessage, search_again, (125.7532), (39.0445), "", "");
                    errorMessage ="";
                }

            }

        }

    }

    public int getRandom(List<String> asian) {
        int rnd = new Random().nextInt(asian.size());
        return rnd;
    }
    public int getRandom2(List<Business> asian) {
        int rnd = new Random().nextInt(asian.size());
        return rnd;
    }


    //1 = $
    //2 = $$...
    public void setPrice(double price){
        if(price <=10){
            //$
            price_keyword = "1";
        }
       else if( price <=30){
            //$$
            price_keyword = "1,2";
        }
        else if(price <= 60) {
            //$$$
            price_keyword= "1,2,3";
        }else{
            //$$$$
            price_keyword= "1,2,3,4";
        }
    }

    public void moreInfo(){
        Toast.makeText(this, press_i_for_more_info, Toast.LENGTH_SHORT).show();
    }

}

//How to use
//Make sure the Location is correctly entered. Ex. Austin Tx or Austin Texas or Austin,TX
//If search does not find a place, press Search again