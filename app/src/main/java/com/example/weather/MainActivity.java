package com.example.weather;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
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
    List<Place> places;
    private MainViewModel mMainViewModel;

    static AppDatabase db;

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

        db = AppDatabase.getInstance(this);

        places = new ArrayList<>();

        mRecyclerView = findViewById(R.id.places_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mMainViewModel = new MainViewModel(getApplicationContext());
        mMainViewModel.getPlaces().observe(this, new Observer<List<Place>>() {

            @Override
            public void onChanged(@Nullable List<Place> mPlaces) {
                places = mPlaces;
                updateUI();
            }
        });

        ImageButton search = findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchView placeInput = findViewById(R.id.searchPlace);
                final String place = placeInput.getQuery().toString();
                placeInput.setQuery("", false);
                placeInput.clearFocus();

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
                        mMainViewModel.delete(places.get(position));
                        //db.placeDao().delete(places.get(position));
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
        Place openItem = places.get(i);
        //mMainViewModel.open(updatedItem);
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
                    String weather = weatherResult.getMain();

                    String image = "http://openweathermap.org/img/w/"+weatherResult.getIcon()+".png";

                    Main mainResult = result.getMain();
                    Float floatTemp = mainResult.getTemp();
                    int temp = Math.round(floatTemp);

                    String theCity = city.substring(0,1).toUpperCase() + city.substring(1).toLowerCase();

                    Place newPlace = new Place(theCity, temp, weather, image, "");
                    places.add(newPlace);
                    mMainViewModel.insert(newPlace);
                    //db.placeDao().insertPlace(newPlace);
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
}
