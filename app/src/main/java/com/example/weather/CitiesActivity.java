package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.entity.CitiesAdapter;
import com.example.weather.entity.CitiesProvider;
import com.example.weather.entity.City;

public class CitiesActivity extends AppCompatActivity {
    private ListView citiesList;
    private TextView nameInput;
    private TextView latitudeInput;
    private TextView longitudeInput;
    private Button addButton;
    private City selectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        citiesList = findViewById(R.id.citiesList);
        nameInput = findViewById(R.id.editName);
        latitudeInput = findViewById(R.id.editLatitude);
        longitudeInput = findViewById(R.id.editLongitude);
        addButton = findViewById(R.id.addButton);

        CitiesAdapter adapter = new CitiesAdapter(getApplicationContext(), R.layout.list_item, CitiesProvider.getCities());
        citiesList.setAdapter(adapter);
        AdapterView.OnItemClickListener itemListener = (parent, v, position, id) -> {
            selectedCity = (City) parent.getItemAtPosition(position);
        };
        citiesList.setOnItemClickListener(itemListener);

        findViewById(R.id.citiesPane).setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
                Toast.makeText(getApplicationContext(), "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                Toast.makeText(getApplicationContext(), "left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });
    }

    public void onDeleteBtnClick(View view) {
        if (selectedCity != null) {
            CitiesProvider.deleteCity(selectedCity);

            CitiesAdapter adapter = new CitiesAdapter(getApplicationContext(), R.layout.list_item, CitiesProvider.getCities());
            citiesList.setAdapter(adapter);
        }
    }

    public void onAddBtnClick(View view) {
        CitiesProvider.addCity(new City(nameInput.getText().toString(),
                Double.parseDouble(longitudeInput.getText().toString()),
                Double.parseDouble(latitudeInput.getText().toString())));


    }
}