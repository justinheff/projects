package com.londonappbrewery.climapm;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import static com.loopj.android.http.AsyncHttpClient.log;

public class WeatherDataModel {

    // Member variables that hold our relevant weather inforomation.
    private String mTemperature;
    private String mCity;
    private String mIconName;
    private String mDayNight;
    private int mCondition;

    private String day = "Day";
    private String night = "Night";
    // Create a WeatherDataModel from a JSON.
    // We will call this instead of the standard constructor.
    public static WeatherDataModel fromJson(JSONObject jsonObject) {

        // JSON parsing is risky business. Need to surround the parsing code with a try-catch block.
        try {
            //Create new weather model.
            WeatherDataModel weatherData = new WeatherDataModel();
            //Get the data from the JSON
            weatherData.mCity = jsonObject.getString("name");
            weatherData.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherData.mIconName = updateWeatherIcon(weatherData.mCondition);

            //Convert the temp to Celsius
            int time = jsonObject.getInt("dt");
            double tempResult = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundedValue = (int) Math.rint(tempResult);
            Log.d("roundedVal", "Temperature:" + roundedValue);
            int sunrise = jsonObject.getJSONObject("sys").getInt("sunrise");
            int sunset = jsonObject.getJSONObject("sys").getInt("sunset");
            Log.d("dayNight", "sunrise: " + sunrise);
            Log.d("dayNight", "sunset: " + sunset);
            weatherData.mTemperature = Integer.toString(roundedValue);

            //Check if it is daytime or nighttime by seeing if the sun is set against time.
            if(sunrise<time && time<sunset){

                weatherData.mDayNight = new String("Day");
                Log.d("day", "It is day time");
            } else {
                Log.d("day", "It is night time");
                weatherData.mDayNight = new String("Night");
            }

            //Return weather data
            Log.d("weather","weather data: " + weatherData.getDayNight());
            return weatherData;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get the weather image name from OpenWeatherMap's condition (marked by a number code)
    private static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
        }

        return "dunno";
    }

    // Getter methods for temperature, city, and icon name:

    public String getTemperature() {
        return mTemperature + "°";
    }

    public String getCity() {
        return mCity;
    }

    public String getDayNight() {return mDayNight; }

    public String getIconName() {
        return mIconName;
    }
}
