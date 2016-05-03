package pocketwiki.pocketwiki.com.pocketwiki2.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pocketwiki.pocketwiki.com.pocketwiki2.Dao.Category;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.CategoryDao;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.City;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.CityDao;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.DaoSession;
import pocketwiki.pocketwiki.com.pocketwiki2.Fragments.OfflineFragment;
import pocketwiki.pocketwiki.com.pocketwiki2.Fragments.OnlineFragment;
import pocketwiki.pocketwiki.com.pocketwiki2.PocketWikiApplication;
import pocketwiki.pocketwiki.com.pocketwiki2.R;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.APICaller;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Config;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CityCategorySelectionActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>, LocationListener {

    private String TAG = getClass().getSimpleName();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FloatingActionButton btnApply;
    private boolean citiesFetched = false, categoriesFetched = false;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private int REQUEST_CHECK_SETTINGS = 0;
    private String currentArea = "";
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_category_selection);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getResources().getString(R.string.title_activity_city_category_selection));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (getIntent().hasExtra(Config.KEY_ACTIVITY_RECREATED) || Utils.isOnline(this)) {
            Config.OPERATION_MODE = Config.MODE_ONLINE;
            tabLayout.getTabAt(1).select();
            if(!getIntent().hasExtra(Config.KEY_ACTIVITY_RECREATED)){
                fetchCitiesAndCategories();
            }
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(TAG, "pos is " + String.valueOf(tab.getPosition()));
                if (tab.getPosition() == 1 && !Utils.isOnline(CityCategorySelectionActivity.this)) {
                    Toast.makeText(CityCategorySelectionActivity.this,
                            getResources().getString(R.string.toast_no_internet), Toast.LENGTH_SHORT).show();
                    Config.OPERATION_MODE = Config.MODE_OFFLINE;
                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    tabLayout.getTabAt(0).select();
                                }
                            }, 100);
                }
                if (tab.getPosition() == 1 && Utils.isOnline(CityCategorySelectionActivity.this)) {
                    Config.OPERATION_MODE = Config.MODE_ONLINE;
                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    fetchCitiesAndCategories();
                                }
                            }, 100);
                }
                if (tab.getPosition() == 0) {
                    Config.OPERATION_MODE = Config.MODE_OFFLINE;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                Log.e(TAG, "here unselected " + String.valueOf(tab.getPosition()));

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.e(TAG, "here reselected " + String.valueOf(tab.getPosition()));
            }
        });

        btnApply = (FloatingActionButton) findViewById(R.id.citycategoryselectionactivity_btn_apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Config.CityIDHolder.isEmpty() && !Config.CategoryIDHolder.isEmpty()) {
                    Intent intent = new Intent(CityCategorySelectionActivity.this, AreaSelectionActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CityCategorySelectionActivity.this, getResources().getString(R.string.toast_select_city_category), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tabLayout.getSelectedTabPosition() == 0) {
            Config.OPERATION_MODE = Config.MODE_OFFLINE;
        } else {
            Config.OPERATION_MODE = Config.MODE_ONLINE;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OfflineFragment(), getResources().getString(R.string.tab_text_offline));
        adapter.addFragment(new OnlineFragment(), getResources().getString(R.string.tab_text_online));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.e(TAG, "googleapiclient connected");

        createLocationRequest();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        builder.setAlwaysShow(true);
        Log.e(TAG, "heer");

        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(this,getResources().getString(R.string.toast_no_gps),Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult result) {

        final Status status = result.getStatus();
        final LocationSettingsStates locationSettingsStates = result.getLocationSettingsStates();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                try {
                    status.startResolutionForResult(
                            CityCategorySelectionActivity.this,
                            REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                break;
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        if(mGoogleApiClient.isConnected()) {
            mCurrentLocation = location;
            Log.i(TAG, mCurrentLocation.toString());
            Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses != null || addresses.size() > 0) {
                    currentArea = addresses.get(0).getAddressLine(1);
                    Log.i(TAG,currentArea);
                    Intent intent = new Intent(this,EntitySelectionActivity.class);
                    intent.putExtra(Config.KEY_FOR_NEARBY_ENTITIES,"Bajaj Nagar");
                    startActivity(intent);
                } else {
                    Log.e(TAG, "Could not fetch data after googleApiClient connected");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
            mGoogleApiClient.disconnect();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_city_category_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_language:
                if(getResources().getConfiguration().locale.getLanguage().equals("en"))
                    ((PocketWikiApplication) getApplicationContext()).setLocale("hi");
                else
                    ((PocketWikiApplication) getApplicationContext()).setLocale("en");
                recreate();
                return true;
            case R.id.action_fetch_location:
                mGoogleApiClient.connect();
                return true;
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fetchCitiesAndCategories(){
        DaoSession daoSession = ((PocketWikiApplication) getApplicationContext()).getDaoSession();
        final CityDao cityDao = daoSession.getCityDao();
        final CategoryDao categoryDao = daoSession.getCategoryDao();
        final ProgressDialog dialog = Utils.showDialog(this,getResources().getString(R.string.dialog_fetch_data));
        citiesFetched = false;
        categoriesFetched = false;

        Callback<String> callback = new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.i(TAG,"response is " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s).getJSONObject(Config.KEY_DATA);
                    JSONArray jsonArray;
                    if(jsonObject.has(Config.KEY_CITIES)){
                        jsonArray = jsonObject.getJSONArray(Config.KEY_CITIES);
                        for(int i=0;i<jsonArray.length();i++){
                            City city = new City();
                            city.setCityId(jsonArray.getJSONObject(i).getLong(Config.KEY_ID));
                            city.setCreatedAt(jsonArray.getJSONObject(i).getString(Config.KEY_CREATED_AT));
                            city.setUpdatedAt(jsonArray.getJSONObject(i).getString(Config.KEY_UPDATED_AT));
                            city.setName(jsonArray.getJSONObject(i).getString(Config.KEY_CITY_NAME));
                            city.setEntityCount(jsonArray.getJSONObject(i).getInt(Config.KEY_ENTITY_COUNT));
                            cityDao.insertOrReplace(city);
                        }
                        citiesFetched = true;
                    }
                    else if(jsonObject.has(Config.KEY_CATEGORIES)) {
                        jsonArray = jsonObject.getJSONArray(Config.KEY_CATEGORIES);
                        for(int i=0;i<jsonArray.length();i++){
                            Category category = new Category();
                            category.setCategoryId(jsonArray.getJSONObject(i).getLong(Config.KEY_ID));
                            category.setCreatedAt(jsonArray.getJSONObject(i).getString(Config.KEY_CREATED_AT));
                            category.setUpdatedAt(jsonArray.getJSONObject(i).getString(Config.KEY_UPDATED_AT));
                            category.setName(jsonArray.getJSONObject(i).getString(Config.KEY_CATEGORY_NAME));
                            category.setEntityCount(jsonArray.getJSONObject(i).getInt(Config.KEY_ENTITY_COUNT));
                            categoryDao.insertOrReplace(category);
                        }
                        categoriesFetched = true;
                    }

                    if(categoriesFetched && citiesFetched){
                        Utils.dismissDialog(CityCategorySelectionActivity.this, dialog);
                        Intent intent = new Intent(CityCategorySelectionActivity.this,CityCategorySelectionActivity.class);
                        intent.putExtra(Config.KEY_ACTIVITY_RECREATED,true);
                        CityCategorySelectionActivity.this.finish();
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CityCategorySelectionActivity.this,getResources().getString(R.string.toast_json_exception),Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"Something went wrong");
                    Utils.dismissDialog(CityCategorySelectionActivity.this,dialog);
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e(TAG,"Something went wrong");
                Log.e(TAG,retrofitError.getMessage() + " hmm");
                Toast.makeText(CityCategorySelectionActivity.this,getResources().getString(R.string.toast_callback_failure),Toast.LENGTH_SHORT).show();
                Utils.dismissDialog(CityCategorySelectionActivity.this,dialog);
            }
        };
        APICaller apiCaller = new APICaller();
        apiCaller.getCall(Config.URL_GET_CITIES,callback,this);
        apiCaller.getCall(Config.URL_GET_CATEGORIES,callback,this);
    }

    /*private void fetchNearbyEntities(String currentArea){


        Callback<String> callback = new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.i(TAG,"response is " + s);
                try {


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CityCategorySelectionActivity.this,getResources().getString(R.string.toast_json_exception),Toast.LENGTH_SHORT).show();
                }
                Utils.dismissDialog(CityCategorySelectionActivity.this,dialog);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e(TAG,"Something went wrong");
                Log.e(TAG,retrofitError.getMessage() + " hmm");
                Toast.makeText(CityCategorySelectionActivity.this,getResources().getString(R.string.toast_callback_failure),Toast.LENGTH_SHORT).show();
                Utils.dismissDialog(CityCategorySelectionActivity.this,dialog);
            }
        };

        APICaller apiCaller = new APICaller();
        dialog = Utils.showDialog(this,getResources().getString(R.string.dialog_fetch_data));
        apiCaller.getCall(Config.URL_GET_NEARBY_ENTITIES(currentArea),
                callback,this);
    }*/
}
