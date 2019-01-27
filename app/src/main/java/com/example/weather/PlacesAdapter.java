package com.example.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder>{

        ArrayList<Place> mPlaces;

    public PlacesAdapter(ArrayList<Place> mPlaces) {
            this.mPlaces=mPlaces;
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
            final Place thisPlace =  mPlaces.get(position);
            holder.placeText.setText(thisPlace.getName());
            holder.weatherText.setText(thisPlace.getTemperature().toString());
            String myImageString = thisPlace.getImageUrl();
            if (myImageString.equals("")) holder.weatherImage.setVisibility(View.INVISIBLE);
            else
                ImageLoader.getInstance().displayImage(myImageString, holder.weatherImage);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            if (mPlaces == null)
                return 0;
            else
                return mPlaces.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            final TextView placeText;
            final TextView weatherText;
            final ImageView weatherImage;
            private final ConstraintLayout main;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                placeText = itemView.findViewById(R.id.text_view_place_name);
                weatherText = itemView.findViewById(R.id.text_view_weather);
                weatherImage = itemView.findViewById(R.id.image_view_place);
                main = itemView.findViewById(R.id.main);        }
        }
    }
