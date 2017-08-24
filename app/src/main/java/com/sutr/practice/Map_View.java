package com.sutr.practice;



import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.Map;


public class Map_View extends Fragment implements OnMapReadyCallback{




    GoogleMap mGoogleMap;
    MapView mapView;

    String value;
    String url;

    TextView addressTextView;
    TextView nameTextView;
    TextView phoneTextView;
    View view;

    String map_is_not_working="Map is not working. Please restart the application";
    //location
    double  longt= -73.96828, latt= 40.78509;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.map_view,container,false);

        //get mapview from xml and creates it
        addressTextView= (TextView) view.findViewById(R.id.maptextview);
        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        phoneTextView = (TextView) view.findViewById(R.id.phone_num);
        return  view;
    }

    public void display( String name,String address, double lon, double lat, String phone, String url){

        longt = lon;
        latt = lat;
        //Toast.makeText(getActivity(), "Inview", Toast.LENGTH_LONG).show();
        this.url = url;
       // nameTextView.setText(name);
        nameTextView.setText(Html.fromHtml("<a href=\""+url + "\">"+ name + "</a"));
        nameTextView.setMovementMethod(LinkMovementMethod.getInstance());


        phoneTextView.setText(phone);
        addressTextView.setText(address);
        //make it so people can clic on it
        Linkify.addLinks(phoneTextView  , Linkify.PHONE_NUMBERS);
        Linkify.addLinks(addressTextView, Linkify.MAP_ADDRESSES);
        //System.out.println("long " + lon + " " + lat);

       CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latt,longt), 15);
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(longt,latt),15));
        mGoogleMap.animateCamera(cameraUpdate);
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latt,longt)).title(name));
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) view.findViewById(R.id.mapView2);

        if(mapView!= null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);

        }else{
            //Toast.makeText(getContext(), "Hello").show();
            Toast.makeText(getActivity(), map_is_not_working, Toast.LENGTH_LONG).show();

        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap){

        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;


        mGoogleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

       // CameraPosition business = CameraPosition.builder().target(new LatLng(32.912624,-96.6388833)).build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt,longt),14));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latt,longt)));



    }


    @Override
    public void onResume() {
        mapView.onResume();

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }



}
