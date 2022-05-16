package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView textView;
    private TextView country;
    private TextView humidity;
    private TextView pressure;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        country = findViewById(R.id.textView2);
        humidity = findViewById(R.id.textView3);
        pressure = findViewById(R.id.textView4);
        imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.a01d);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = "Moscow";
                String url = "https://api.openweathermap.org/data/2.5/weather?lat=54&lon=55&appid=508dabe2e8078280e8b3e4198e0c0068&lang=ru&units=metric";
                new GetUrlData().execute(url);


            }
        });
    }


    private class GetUrlData extends AsyncTask<String, String, String>{
        protected void onPreExecute(){
            super.onPreExecute();
            textView.setText("ЖДИ");
            country.setText("ЖДИ");
            pressure.setText("ЖДИ");
            humidity.setText("ЖДИ");
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
                String line = "";

                while ((line = reader.readLine()) != null){
                    buffer.append(line).append("\n");
                }
                System.out.println(buffer);
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null)
                    connection.disconnect();
                try {
                    if (reader != null) {
                        reader.close();
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONObject obj = new JSONObject(result);
                textView.setText("ТЕМПЕРАТУРА " + obj.getJSONObject("main").getDouble("temp"));
                country.setText(obj.getString("name"));
                humidity.setText("Влажность воздуха " + obj.getJSONObject("main").getDouble("humidity"));
                pressure.setText("Давление атм: " + obj.getJSONObject("main").getDouble("pressure"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}