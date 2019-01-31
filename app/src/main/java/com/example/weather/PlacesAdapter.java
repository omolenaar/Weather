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

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder>{

    public interface PlaceClickListener {
        void placeOnClick(int i);
    }

    List<Place> mPlaces;
    final private PlaceClickListener mPlaceClickListener;

    public PlacesAdapter(List<Place> mPlaces, PlaceClickListener mPlaceClickListener) {
            this.mPlaces=mPlaces;
            this.mPlaceClickListener=mPlaceClickListener;
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
            holder.tempText.setText(thisPlace.getTemperature().toString()+ " \u2103");
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
            final TextView tempText;
            final ImageView weatherImage;
            private final ConstraintLayout main;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                placeText = itemView.findViewById(R.id.text_view_place_name);
                tempText = itemView.findViewById(R.id.text_view_temp);
                weatherImage = itemView.findViewById(R.id.image_view_place);
                main = itemView.findViewById(R.id.main);
                main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPlaceClickListener.placeOnClick(getAdapterPosition());
                    }
                });
            }
        }

    public void swapList (List<Place> newList) {
        mPlaces = newList;
        if (newList != null) {
            this.notifyDataSetChanged();
        }
    }
    }

