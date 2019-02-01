package com.example.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class WeatherDetail extends AppCompatActivity {

    static List<Place> places = MainActivity.places;
    public static final String ARG_ITEM_ID = "item_id";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
}

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            int position = (getArguments().getInt(ARG_SECTION_NUMBER) - 1);

            TextView city = rootView.findViewById(R.id.cityName);
            city.setText(places.get(position).getName());

            ImageView image = rootView.findViewById(R.id.image);
            String myImageString = places.get(position).getImageUrl();
            if (myImageString.equals("")) image.setVisibility(View.INVISIBLE);
            else
                ImageLoader.getInstance().displayImage(myImageString, image);

            TextView weather = rootView.findViewById(R.id.tempDescription);
            weather.setText(places.get(position).getWeather());
            TextView temp = rootView.findViewById(R.id.temp);
            temp.setText(places.get(position).getTemperature().toString());

            TextView windSpeedText = rootView.findViewById(R.id.wind);
            if (places.get(position).getWindSpeed()!=null)
            windSpeedText.setText(places.get(position).getWindSpeed().toString());

            TextView windDeg = rootView.findViewById(R.id.windDescription);
            if (places.get(position).getWindDeg()!=null)
            windDeg.setText(places.get(position).getWindDeg().toString());

            TextView rainText = rootView.findViewById(R.id.rain);
            if (places.get(position).getRainAmount()!=null)
            rainText.setText(places.get(position).getRainAmount().toString());

            TextView rainClass = rootView.findViewById(R.id.rainDescription);
            rainClass.setText(places.get(position).getRainDesciption());

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            if (mSectionsPagerAdapter != null)
                mSectionsPagerAdapter.notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return WeatherDetail.PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            places = MainActivity.places;
            return places.size();
        }
    }
}

