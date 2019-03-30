package com.londonappbrewery.climapm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class WeatherController extends AppCompatActivity {

    // Request Codes:
    final int REQUEST_CODE = 123; // Request Code for permission request callback
    final int NEW_CITY_CODE = 456; // Request code for starting new activity for result callback

    // Base URL for the OpenWeatherMap API. More secure https is a premium feature =(
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";

    // App ID to use OpenWeather data
    final String APP_ID = "2505fb010162f9ce56b5e15da726a955";

    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;

    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;

    // Don't want to type 'Clima' in all the logs, so putting this in a constant here.
    final String LOGCAT_TAG = "Clima";

    // Set LOCATION_PROVIDER here. Using GPS_Provider for Fine Location (good for emulator):
    // Recommend using LocationManager.NETWORK_PROVIDER on physical devices (reliable & fast!)
    final String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;

    // Member Variables:
    boolean mUseLocation = true;

    //Variables for each layout
    TextView mCityLabel;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;
    TextView mDayNight;

    TextView mCityLabel2;
    ImageView mWeatherImage2;
    TextView mTemperatureLabel2;
    TextView mDayNight2;

    TextView mCityLabel3;
    ImageView mWeatherImage3;
    TextView mTemperatureLabel3;
    TextView mDayNight3;

    TextView mCityLabel4;
    ImageView mWeatherImage4;
    TextView mTemperatureLabel4;
    TextView mDayNight4;

    // Declaring a LocationManager and a LocationListener here:
    LocationManager mLocationManager;
    LocationListener mLocationListener;

    //Layout for listeners
    LinearLayout layout1;
    LinearLayout layout2;
    LinearLayout layout3;
    LinearLayout layout4;

    int w;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);

        //Link all TextViews, Layouts and images to their respective thing in the XML file.
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout4 = findViewById(R.id.layout4);


        mCityLabel = findViewById(R.id.locationTV);
        mWeatherImage = findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = findViewById(R.id.tempTV);
        mDayNight = findViewById(R.id.dayNight);

        mCityLabel2 = findViewById(R.id.locationTV2);
        mWeatherImage2 = findViewById(R.id.weatherSymbolIV2);
        mTemperatureLabel2 = findViewById(R.id.tempTV2);
        mDayNight = findViewById(R.id.dayNight2);

        mCityLabel3 = findViewById(R.id.locationTV3);
        mWeatherImage3 = findViewById(R.id.weatherSymbolIV3);
        mTemperatureLabel3 = findViewById(R.id.tempTV3);
        mDayNight = findViewById(R.id.dayNight3);

        mCityLabel4 = findViewById(R.id.locationTV4);
        mWeatherImage4 = findViewById(R.id.weatherSymbolIV4);
        mTemperatureLabel4 = findViewById(R.id.tempTV4);
        mDayNight = findViewById(R.id.dayNight4);


        //Listener for each layout to change weather.
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(WeatherController.this, ChangeCityController.class);
                //Update which layout was clicked.
                checkClicked(1);

                //Change Screens
                startActivityForResult(myIntent, NEW_CITY_CODE);
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(WeatherController.this, ChangeCityController.class);
                checkClicked(2);
                //Change Screens
                startActivityForResult(myIntent, NEW_CITY_CODE);
            }
        });
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(WeatherController.this, ChangeCityController.class);
                checkClicked(3);
                //Change Screens
                startActivityForResult(myIntent, NEW_CITY_CODE);
            }
        });
        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(WeatherController.this, ChangeCityController.class);
                checkClicked(4);
                //Change Screens
                startActivityForResult(myIntent, NEW_CITY_CODE);
            }
        });
    }

    //This checks which layout was clicked and updates w respectively.
    private void checkClicked(int userClick){
        if (userClick == 1){
            w = 1;
            Log.d("w", "w is: " + w);
        } else if ( userClick == 2){
            w = 2;
            Log.d("w", "w is: " + w);
        } else if ( userClick == 3){
            w = 3;
            Log.d("w", "w is: " + w);
        }
        else if ( userClick == 4){
            w = 4;
            Log.d("w", "w is: " + w);
        }
    }
    // onResume() life cyle callback:
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOGCAT_TAG, "onResume() called");
        if(mUseLocation) getWeatherForCurrentLocation();
    }

    // Callback received when a new city name is entered on the second screen.
    // Checking request code and if result is OK before making the API call.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(LOGCAT_TAG, "onActivityResult() called");

        if (requestCode == NEW_CITY_CODE) {
            if (resultCode == RESULT_OK) {
                String city = data.getStringExtra("City");
                String coords = data.getStringExtra("Coords");
                Log.d(LOGCAT_TAG, "New city is " + city);
                Log.d("coords", "New Coords are: " + coords);

                mUseLocation = false;
                getWeatherForNewCity(city, coords);
            }
        }
    }

    // Configuring the parameters when a new city has been entered:
    private void getWeatherForNewCity(String city, String coords) {
        Log.d(LOGCAT_TAG, "Getting weather for new city");

        //If a new city name was entered pass in city to params
        if (city != null) {
            RequestParams params = new RequestParams();
            params.put("q", city);
            params.put("appid", APP_ID);
            letsDoSomeNetworking(params);
        } else {  // else pass in the coords as one of the two has to be not null
            RequestParams params = new RequestParams();
            String[] parts = coords.split(",");
            String longitude = parts[0];
            String latitude = parts[1];
            double lon = Double.parseDouble(longitude);
            double lat = Double.parseDouble(latitude);
            Log.d("lon", "Lon is " + lon);
            Log.d("lat", "Lat is " + lat);
            params.put("lat", lat);
            params.put("lon", lon);
            params.put("appid", APP_ID);
            letsDoSomeNetworking(params);
        }
    }


    // Location Listener callbacks here, when the location has changed.
    private void getWeatherForCurrentLocation() {

        Log.d(LOGCAT_TAG, "Getting weather for current location");
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d(LOGCAT_TAG, "onLocationChanged() callback received");
                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());

                Log.d(LOGCAT_TAG, "longitude is: " + longitude);
                Log.d(LOGCAT_TAG, "latitude is: " + latitude);

                // Providing 'lat' and 'lon' (spelling: Not 'long') parameter values
                RequestParams params = new RequestParams();
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("appid", APP_ID);
                letsDoSomeNetworking(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // Log statements to help you debug your app.
                Log.d(LOGCAT_TAG, "onStatusChanged() callback received. Status: " + status);
                Log.d(LOGCAT_TAG, "2 means AVAILABLE, 1: TEMPORARILY_UNAVAILABLE, 0: OUT_OF_SERVICE");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d(LOGCAT_TAG, "onProviderEnabled() callback received. Provider: " + provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(LOGCAT_TAG, "onProviderDisabled() callback received. Provider: " + provider);
            }
        };

        // This is the permission check to access (fine) location.

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        // Some additional log statements to help you debug
        Log.d(LOGCAT_TAG, "Location Provider used: "
                + mLocationManager.getProvider(LOCATION_PROVIDER).getName());
        Log.d(LOGCAT_TAG, "Location Provider is enabled: "
                + mLocationManager.isProviderEnabled(LOCATION_PROVIDER));
        Log.d(LOGCAT_TAG, "Last known location (if any): "
                + mLocationManager.getLastKnownLocation(LOCATION_PROVIDER));
        Log.d(LOGCAT_TAG, "Requesting location updates");


        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);

    }

    // This is the callback that's received when the permission is granted (or denied)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Checking against the request code we specified earlier.
        if (requestCode == REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(LOGCAT_TAG, "onRequestPermissionsResult(): Permission granted!");

                // Getting weather only if we were granted permission.
                getWeatherForCurrentLocation();
            } else {
                Log.d(LOGCAT_TAG, "Permission denied =( ");
            }
        }

    }


    // This is the actual networking code. Parameters are already configured.
    private void letsDoSomeNetworking(RequestParams params) {

        // AsyncHttpClient belongs to the loop dependency.
        AsyncHttpClient client = new AsyncHttpClient();

        // Making an HTTP GET request by providing a URL and the parameters.
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(LOGCAT_TAG, "Success! JSON: " + response.toString());
                WeatherDataModel weatherData = WeatherDataModel.fromJson(response);
                updateUI(weatherData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.e(LOGCAT_TAG, "Fail " + e.toString());
                Toast.makeText(WeatherController.this, "Request Failed", Toast.LENGTH_SHORT).show();

                Log.d(LOGCAT_TAG, "Status code " + statusCode);
                Log.d(LOGCAT_TAG, "Here's what we got instead " + response.toString());
            }

        });
    }



    // Updates the information shown on screen.
    private void updateUI(WeatherDataModel weather) {
        Log.d("weather", "Weather daytime: " + weather.getDayNight());
        //The if statements will check which layout was clicked using w and update the repective items in the layout.
        if (w == 1) {
            mTemperatureLabel.setText(weather.getTemperature());
            mCityLabel.setText(weather.getCity());

            // Update the icon based on the resource id of the image in the drawable folder.
            int resourceID = getResources().getIdentifier(weather.getIconName(), "drawable", getPackageName());
            mWeatherImage.setImageResource(resourceID);
            //mDayNight.setText(weather.getDayNight());
        } else if ( w == 2){
            mTemperatureLabel2.setText(weather.getTemperature());
            mCityLabel2.setText(weather.getCity());

            // Update the icon based on the resource id of the image in the drawable folder.
            int resourceID = getResources().getIdentifier(weather.getIconName(), "drawable", getPackageName());
            mWeatherImage2.setImageResource(resourceID);
            //mDayNight2.setText(weather.getDayNight());

        } else if ( w == 3) {
            mTemperatureLabel3.setText(weather.getTemperature());
            mCityLabel3.setText(weather.getCity());

            // Update the icon based on the resource id of the image in the drawable folder.
            int resourceID = getResources().getIdentifier(weather.getIconName(), "drawable", getPackageName());
            mWeatherImage3.setImageResource(resourceID);
            //mDayNight3.setText(weather.getDayNight());
        } else if ( w == 4) {
            mTemperatureLabel4.setText(weather.getTemperature());
            mCityLabel4.setText(weather.getCity());

            // Update the icon based on the resource id of the image in the drawable folder.
            int resourceID = getResources().getIdentifier(weather.getIconName(), "drawable", getPackageName());
            mWeatherImage4.setImageResource(resourceID);
            //mDayNight4.setText(weather.getDayNight());
        }
    }
    // Freeing up resources when the app enters the paused state.
    @Override
    protected void onPause() {
        super.onPause();

        if (mLocationManager != null) mLocationManager.removeUpdates(mLocationListener);
    }

}
