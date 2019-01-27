package com.example.weather;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private boolean mTwoPane;
    EditText inputPlace;
    PlacesAdapter mAdapter;
    RecyclerView mRecyclerView;
    ArrayList<Weather> theWeather;
    ArrayList<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);

        mRecyclerView = findViewById(R.id.places_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final PlacesAdapter mAdapter = new PlacesAdapter(places);
        mRecyclerView.setAdapter(mAdapter);

        places = new ArrayList<>();
        initializeData();
        updateUI();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText placeInput = findViewById(R.id.inputPlace);
                final String place = placeInput.getText().toString();

                getWeather(place);
                updateUI();
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = (viewHolder.getAdapterPosition());
                        //mMainViewModel.delete(mItems.get(position));
                        places.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        updateUI();
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    //@Override
    public void itemOnClick(int i) {
        Place updatedItem = places.get(i);
        //mMainViewModel.update(updatedItem);
    }

    private void getWeather(String place) {
        WeatherApi service = WeatherApi.retrofit.create(WeatherApi.class);
        Result weatherResult = null;

        String key = service.API_KEY;
        final String city = place;
        final String units = "metric";

        Call<Result> call = service.getWeatherInPlace(place, key, units);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    Result result = response.body();
                    ArrayList theWeather = new ArrayList();
                    theWeather.addAll(result.getWeather());
                    Weather weatherResult = (Weather) theWeather.get(0);
                    String image = "http://openweathermap.org/img/w/"+weatherResult.getIcon()+".png";
                    Main mainResult = result.getMain();
                    Float floatTemp = mainResult.getTemp();
                    int temp = Math.round(floatTemp);
                    Place newPlace = new Place(city, temp, null, image, "");
                    places.add(newPlace);
                    updateUI();
                }
                else
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new PlacesAdapter(places);
            mRecyclerView.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initializeData() {
        Weather newWeather1 = new Weather("Broken Clouds", "");
        Weather newWeather2 = new Weather("Rain", "");
        Weather newWeather3 = new Weather("Windy", "");
        Weather newWeather4 = new Weather("Clear", "");

        Place newPlace1 = new Place("Groningen", 4, newWeather1,"http://openweathermap.org/img/w/04d.png", "");
        places.add(newPlace1);
        Place newPlace2 = new Place("Zaandam", 10, newWeather2, "http://openweathermap.org/img/w/10d.png", "");
        places.add(newPlace2);
        Place newPlace3 = new Place("Patras", 14, newWeather4, "", "");
        places.add(newPlace3);
        Place newPlace4 = new Place("Amsterdam", 6, newWeather2, "http://openweathermap.org/img/w/01d.png", "");
        places.add(newPlace4);
    }
}
