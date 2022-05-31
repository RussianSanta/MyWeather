package com.example.weather;

import android.annotation.SuppressLint;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weather.entity.CitiesProvider;
import com.example.weather.entity.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager;

    private TextView temperature;
    private TextView country;
    private TextView humidity;
    private TextView pressure;
    private TextView midTemperature;
    private TextView midHumidity;
    private TextView midPressure;
    private ImageView imageView;

    private int cityId;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        country = findViewById(R.id.textView);
        midTemperature = findViewById(R.id.textView2);
        midHumidity = findViewById(R.id.textView3);
        midPressure = findViewById(R.id.textView4);
        temperature = findViewById(R.id.textView5);
        humidity = findViewById(R.id.textView6);
        pressure = findViewById(R.id.textView7);


        temperature.setText(" ");
        humidity.setText(" ");
        pressure.setText(" ");
//        imageView = findViewById(R.id.imageView);
//        imageView.setImageResource(R.drawable.a02d);

        loadLocation();

//        findViewById(R.id.mainPane).setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
//            public void onSwipeTop() {
//                Intent intent = new Intent();
//                intent.setClass(getApplicationContext(), CitiesActivity.class);
//                startActivity(intent);
//            }
//
//            public void onSwipeRight() {
//                cityId--;
//                if (cityId < 0) cityId = CitiesProvider.getCities().size() - 1;
//                City city = CitiesProvider.getCities().get(cityId);
//
//                String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + city.getLatitude() + "&lon=" + city.getLongitude() + "&appid=bb6eed77276b5bbdefc4747a596f8bd3&lang=ru&units=metric";
//                new GetUrlData().execute(url);
//            }
//
//            public void onSwipeLeft() {
//                cityId++;
//                if (cityId > (CitiesProvider.getCities().size() - 1)) cityId = 0;
//                City city = CitiesProvider.getCities().get(cityId);
//
//                String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + city.getLatitude() + "&lon=" + city.getLongitude() + "&appid=bb6eed77276b5bbdefc4747a596f8bd3&lang=ru&units=metric";
//                new GetUrlData().execute(url);
//            }
//
//            public void onSwipeBottom() {
//                String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude() + "&lon=" + locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude() + "&appid=bb6eed77276b5bbdefc4747a596f8bd3&lang=ru&units=metric";
//                new GetUrlData().execute(url);
//            }
//        });
    }

    @SuppressLint("MissingPermission")
    private void loadLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, location -> {
        });

        double latitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        double longitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();

//        String urlOne = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=bb6eed77276b5bbdefc4747a596f8bd3&lang=ru&units=metric";
        String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&appid=bb6eed77276b5bbdefc4747a596f8bd3";

        System.out.println(url);
        City city = new City("On location", longitude, latitude);
        CitiesProvider.addBaseCity(city);
        cityId = 3;
        new GetUrlData().execute(url);
    }

    private class GetUrlData extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            country.setText("...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                System.out.println(buffer);
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            double temp = 0;
            double hum = 0;
            double press = 0;
            super.onPostExecute(result);
            try {
                System.out.println(result);
                JSONObject obj = new JSONObject(result);
//                String iconName = "a" + obj.getJSONArray("weather").getJSONObject(0).getString("icon");
//                imageView.setImageResource(getApplicationContext().getResources().getIdentifier(iconName,"drawable",getApplicationContext().getPackageName()));
                String name = obj.getJSONObject("city").getString("name");
                if (!name.equals("")) country.setText(obj.getJSONObject("city").getString("name"));
                else country.setText(CitiesProvider.getCities().get(cityId).getName());

                JSONArray array = obj.getJSONArray("list");
                for (int i = 0; i < 5; i++) {
                    double t = array.getJSONObject(i).getJSONObject("main").getDouble("temp");
                    double h = array.getJSONObject(i).getJSONObject("main").getDouble("humidity");
                    double p = array.getJSONObject(i).getJSONObject("main").getDouble("pressure");

                    temperature.setText(temperature.getText() + " " + t);
                    humidity.setText(humidity.getText() + " " + h);
                    pressure.setText(pressure.getText() + " " + p);

                    temp = temp + t;
                    hum = hum + h;
                    press = press + p;
                    System.out.println(temp);
                }

                String tempS = String.format("%.2f", temp/5);
                midTemperature.setText("Ср. температура - " + tempS);
                midHumidity.setText("Ср. влажность - " + hum / 5);
                midPressure.setText("Ср. давление - " + press / 5);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}