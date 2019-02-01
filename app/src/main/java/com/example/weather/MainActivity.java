package com.example.weather;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements PlacesAdapter.PlaceClickListener {

    PlacesAdapter mAdapter;
    RecyclerView mRecyclerView;
    static List<Place> places;
    private MainViewModel mMainViewModel;
    private FusedLocationProviderClient mFusedLocationClient;
    static public Place currentLocation;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
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
        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mMainViewModel = new MainViewModel(getApplicationContext());
        mMainViewModel.getPlaces().observe(this, new Observer<List<Place>>() {

            @Override
            public void onChanged(@Nullable List<Place> mPlaces) {
                places = mPlaces;
                updateUI();
            }
        });

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != getPackageManager().PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Task<Location> lastLocation = mFusedLocationClient.getLastLocation();
            lastLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location == null) {
                        location = new Location("");
                        location.setLatitude(52d);
                        location.setLongitude(4d);
                    }
                    getWeatherCoord(location);
                }
            });
        }

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

        ConstraintLayout current = findViewById(R.id.main);
        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CurrentLocationDetail.class);
                intent.putExtra(CurrentLocationDetail.ARG_ITEM_ID, 0);
                startActivityForResult(intent, 1);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] foundPermissions) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (foundPermissions.length > 0 && foundPermissions[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void placeOnClick(int i) {
        Place openItem = places.get(i);
        //getWeather(openItem.getName());
        Context context = this;
        Intent intent = new Intent(context, WeatherDetail.class);
        intent.putExtra(WeatherDetail.ARG_ITEM_ID, i);
        context.startActivity(intent);
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

                    String theCity = result.getName();

                    ArrayList theWeather = new ArrayList();
                    theWeather.addAll(result.getWeather());
                    Weather weatherResult = (Weather) theWeather.get(0);
                    String weather = weatherResult.getMain();

                    String image = "http://openweathermap.org/img/w/"+weatherResult.getIcon()+".png";

                    Main mainResult = result.getMain();

                    Float floatTemp = mainResult.getTemp();
                    int temp = Math.round(floatTemp);

                    Float rain;
                    if (result.getRain() != null)
                        rain = result.getRain().get3h();
                    else rain = 0F;
                    Rain rainObject = new Rain(rain);
                    String rainDescription = rainObject.getRainClass();


                    Float windSpeed, windDir;
                    if (result.getWind() != null)
                    windSpeed = result.getWind().getSpeed();
                    else windSpeed = 0F;
                    windDir = result.getWind().getDeg();

                    Place newPlace = new Place(theCity, weather, temp, image, windSpeed, windDir, rain, rainDescription);
                    places.add(newPlace);
                    mMainViewModel.insert(newPlace);
                    //db.placeDao().insertPlace(newPlace);
                    updateUI();
                }
                else
                    Toast.makeText(MainActivity.this, "City not found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No internet connection?", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWeatherCoord(Location location) {
        WeatherApi service = WeatherApi.retrofit.create(WeatherApi.class);
        Result weatherResult = null;

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        String key = service.API_KEY;
        final String units = "metric";

        Call<Result> call = service.getWeatherByCoord(lat, lon, key, units);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    Result result = response.body();

                    String theCity = result.getName();

                    ArrayList theWeather = new ArrayList();
                    theWeather.addAll(result.getWeather());
                    Weather weatherResult = (Weather) theWeather.get(0);
                    String weather = weatherResult.getMain();
                    String image = "http://openweathermap.org/img/w/"+weatherResult.getIcon()+".png";

                    Main mainResult = result.getMain();

                    Float floatTemp = mainResult.getTemp();
                    int temp = Math.round(floatTemp);

                    Float rain;
                    if (result.getRain() != null)
                        rain = result.getRain().get3h();
                    else rain = 0F;
                    Rain rainObject = new Rain(rain);
                    String rainDescription = rainObject.getRainClass();


                    Float windSpeed, windDir;
                    if (result.getWind() != null)
                        windSpeed = result.getWind().getSpeed();
                    else windSpeed = 0F;
                    windDir = result.getWind().getDeg();

                    Place newPlace = new Place(theCity, weather, temp, image, windSpeed, windDir, rain, rainDescription);
                    currentLocation = newPlace;
                    inflateCard(newPlace);
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
            mAdapter = new PlacesAdapter(places, this);
            mRecyclerView.setAdapter(mAdapter);
        }
        mAdapter.swapList(places);
        //mAdapter.swapList(mPlaces);
    }

    private void inflateCard(Place newPlace) {
        final TextView placeText = findViewById(R.id.text_view_place_name);
        final TextView tempText = findViewById(R.id.text_view_temp);
        final ImageView weatherImage = findViewById(R.id.image_view_place);
        placeText.setText(newPlace.getName());
        tempText.setText(newPlace.getTemperature().toString()+ " \u2103");
        String myImageString = newPlace.getImageUrl();
        if (myImageString.equals("")) weatherImage.setVisibility(View.INVISIBLE);
        else
            ImageLoader.getInstance().displayImage(myImageString, weatherImage);
    }
}
