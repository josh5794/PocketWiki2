package pocketwiki.pocketwiki.com.pocketwiki2.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

public class CityCategorySelectionActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FloatingActionButton btnApply;
    private boolean citiesFetched = false, categoriesFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_category_selection);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if(getIntent().hasExtra(Config.KEY_ACTIVITY_RECREATED)){
            Config.OPERATION_MODE = Config.MODE_ONLINE;
            tabLayout.getTabAt(1).select();
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(TAG,"pos is " + String.valueOf(tab.getPosition()));
                if(tab.getPosition() == 1 && !Utils.isOnline(CityCategorySelectionActivity.this)){
                    Toast.makeText(CityCategorySelectionActivity.this,
                            getResources().getString(R.string.toast_no_internet),Toast.LENGTH_SHORT).show();
                    Config.OPERATION_MODE = Config.MODE_OFFLINE;
                    new Handler().postDelayed(
                            new Runnable(){
                                @Override
                                public void run() {
                                    tabLayout.getTabAt(0).select();
                                }
                            }, 100);
                }
                if(tab.getPosition() == 1 && Utils.isOnline(CityCategorySelectionActivity.this) ){
                    Config.OPERATION_MODE = Config.MODE_ONLINE;
                    new Handler().postDelayed(
                            new Runnable(){
                                @Override
                                public void run() {
                                    fetchCitiesAndCategories();
                                }
                            }, 100);
                }
                if(tab.getPosition() == 0){
                    Config.OPERATION_MODE = Config.MODE_OFFLINE;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                Log.e(TAG,"here unselected " + String.valueOf(tab.getPosition()));

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.e(TAG,"here reselected " + String.valueOf(tab.getPosition()));
            }
        });

        btnApply = (FloatingActionButton) findViewById(R.id.citycategoryselectionactivity_btn_apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Config.CityIDHolder.isEmpty() && !Config.CategoryIDHolder.isEmpty()) {
                    Intent intent = new Intent(CityCategorySelectionActivity.this, AreaSelectionActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(CityCategorySelectionActivity.this, getResources().getString(R.string.toast_select_city_category),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(tabLayout.getSelectedTabPosition() == 0){
            Config.OPERATION_MODE = Config.MODE_OFFLINE;
        }
        else {
            Config.OPERATION_MODE = Config.MODE_ONLINE;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OfflineFragment(), getResources().getString(R.string.tab_text_offline));
        adapter.addFragment(new OnlineFragment(), getResources().getString(R.string.tab_text_online));
        viewPager.setAdapter(adapter);
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
}
