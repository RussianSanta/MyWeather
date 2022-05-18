package com.example.weather.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.weather.R;

import java.util.List;

public class CitiesAdapter extends ArrayAdapter<City> {
    private final LayoutInflater inflater;
    private final int layout;
    private final List<City> cities;

    public CitiesAdapter(Context context, int resource, List<City> cities) {
        super(context, resource, cities);
        this.cities = cities;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView nameView = view.findViewById(R.id.name);

        City state = cities.get(position);

        nameView.setText(state.getName());

        return view;
    }
}
