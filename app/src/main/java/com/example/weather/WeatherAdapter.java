package com.example.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder>{

        ArrayList<Weather> mItems;

    public WeatherAdapter(ArrayList<Weather> mItems) {
            this.mItems=mItems;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.card, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final Weather theWeather =  mItems.get(position);
            holder.placeText.setText(theWeather.getDescription());
            holder.weatherText.setText(theWeather.getMain());
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            if (mItems == null)
                return 0;
            else
                return mItems.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            final TextView placeText;
            final TextView weatherText;
            private final ConstraintLayout main;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                placeText = itemView.findViewById(R.id.text_view_place_name);
                weatherText = itemView.findViewById(R.id.text_view_weather);
                main = itemView.findViewById(R.id.main);        }
        }

    }
