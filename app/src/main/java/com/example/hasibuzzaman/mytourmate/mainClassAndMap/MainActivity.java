package com.example.hasibuzzaman.mytourmate.mainClassAndMap;


// https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=23.7842676%2C90.369294&radius=5000&types=restaurant&key=AIzaSyB3PpqkyKKcYOiEw1XjQ2BsjF6zB_x8peI
// https://developers.google.com/places/supported_types

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.hasibuzzaman.mytourmate.GeoData.Constants;
import com.example.hasibuzzaman.mytourmate.GeoData.FetchAddressIntentService;
import com.example.hasibuzzaman.mytourmate.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
     TextView lattitudeTv;
    TextView longitudeTv,AddressTV;
    Location latlong;
    AddressResultReceiver resultreceiver;
    String Address;

    LatLng latLng;

    Place Myplace;

    // Views Refernce
        TextView searchTv;

    // Views reference End


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        intilizeViews();  // initilizing all vies

        // Setting our custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Making google Api client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // Making a object to send on The IntentService so that IntentSevice Can send Result to this Class
        resultreceiver = new AddressResultReceiver(new Handler());


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Myplace = place ;
                userSerchedMap();
                Log.e("Error", "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.e("Error", "An error occurred: " + status);
            }
        });



    }

    public void intilizeViews()
    {
        searchTv = (TextView) findViewById(R.id.searchTv);
        lattitudeTv= (TextView) findViewById(R.id.lattitudeTv);
        longitudeTv= (TextView) findViewById(R.id.longitudeTv);
        AddressTV= (TextView) findViewById(R.id.AddressTV);



    }






    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        googleApiClient.disconnect();
        super.onPause();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
         LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    public void startIntentService()
    {
        Intent intent = new Intent(this,FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER,resultreceiver);
        intent.putExtra(Constants.MY_LOCATION,latlong);  // sending the LAt and Long
        Log.e("Start Service ", " Start service");
        startService(intent);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latlong= location;
        startIntentService();

        lattitudeTv.setText(location.getLatitude()+"");
        longitudeTv.setText(location.getLongitude()+"");


    }



    public void map(View view) {
        Intent in = new Intent(this, MapsActivity.class);
        in.putExtra(Constants.LATTITUDE,latlong.getLatitude());
        in.putExtra(Constants.LONGITUDE,latlong.getLongitude());
        startActivity(in);
    }

    public void userSerchedMap()
    {

        latLng=Myplace.getLatLng();

        Intent in = new Intent(this, MapsActivity.class);
        in.putExtra(Constants.LATTITUDE,latLng.latitude);
        in.putExtra(Constants.LONGITUDE,latLng.longitude);
        startActivity(in);
    }

    public void searchTvOnclick(View view) {
        searchTv.setVisibility(View.GONE);

    }


    public class AddressResultReceiver extends ResultReceiver
    {


        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Address = resultData.getString(Constants.RESULT_DATA_KEY);
            AddressTV.setText(Address +"");
            if (resultCode == Constants.SUCCESS_RESULT) {
                Log.e("onReceiveResult","onReceiveResult");
            }


        }
    }




}
