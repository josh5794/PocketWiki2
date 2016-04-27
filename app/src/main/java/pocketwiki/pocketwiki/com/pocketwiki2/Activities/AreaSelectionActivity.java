package pocketwiki.pocketwiki.com.pocketwiki2.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pocketwiki.pocketwiki.com.pocketwiki2.Adapters.AreaListAdapter;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.Area;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.AreaDao;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.DaoSession;
import pocketwiki.pocketwiki.com.pocketwiki2.PocketWikiApplication;
import pocketwiki.pocketwiki.com.pocketwiki2.R;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.APICaller;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Config;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AreaSelectionActivity extends AppCompatActivity {

    private FloatingActionButton btnApply;
    private List<Area> areas;
    ProgressDialog dialog;
    ListView listView;
    public String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.areaselectionactivity_lv);

        if(Config.OPERATION_MODE == Config.MODE_ONLINE) {
            fetchAreasOnline();
        }
        else {
            setupAreaList();
        }

        btnApply = (FloatingActionButton) findViewById(R.id.areaselectionactivity_btn_apply);
        btnApply.bringToFront();
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AreaSelectionActivity.this,EntitySelectionActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setupAreaList(){
        areas = fetchAreas();
        Log.i(TAG, "Area count is " + String.valueOf(areas.size()));
        AreaListAdapter listAdapter = new AreaListAdapter(areas, AreaSelectionActivity.this, true);
        listView.setAdapter(listAdapter);
    }

    private void fetchAreasOnline(){
        DaoSession daoSession = ((PocketWikiApplication) AreaSelectionActivity.this.getApplicationContext()).getDaoSession();
        final AreaDao areaDao = daoSession.getAreaDao();

        Callback<String> callback = new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.i(TAG,"response is " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s).getJSONObject(Config.KEY_DATA);
                    JSONArray jsonArray = jsonObject.getJSONArray(Config.KEY_AREAS);
                    for(int i=0; i<jsonArray.length();i++){
                        Area area = new Area();
                        area.setAreaId(jsonArray.getJSONObject(i).getLong(Config.KEY_ID));
                        area.setCreatedAt(jsonArray.getJSONObject(i).getString(Config.KEY_CREATED_AT));
                        area.setUpdatedAt(jsonArray.getJSONObject(i).getString(Config.KEY_UPDATED_AT));
                        area.setCityId(jsonArray.getJSONObject(i).getLong(Config.KEY_CITY_ID));
                        area.setName(jsonArray.getJSONObject(i).getString(Config.KEY_AREA_NAME));
                        area.setCityName(jsonArray.getJSONObject(i).getString(Config.KEY_CITY_NAME));
                        //area.setEntityCount(jsonArray.getJSONObject(i).getInt(Config.KEY_ENTITY_COUNT));
                        areaDao.insertOrReplace(area);
                    }
                    setupAreaList();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AreaSelectionActivity.this,getResources().getString(R.string.toast_json_exception),Toast.LENGTH_SHORT).show();
                }
                Utils.dismissDialog(AreaSelectionActivity.this,dialog);

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e(TAG,"Something went wrong");
                Toast.makeText(AreaSelectionActivity.this,getResources().getString(R.string.toast_callback_failure),Toast.LENGTH_SHORT).show();
                Log.e(TAG,retrofitError.getMessage() + " hmm");
                Utils.dismissDialog(AreaSelectionActivity.this,dialog);
            }
        };
        APICaller apiCaller = new APICaller();
        dialog = Utils.showDialog(AreaSelectionActivity.this,getResources().getString(R.string.dialog_fetch_data));
        apiCaller.getCall(Config.URL_GET_AREAS(Config.CategoryIDHolder,Config.CityIDHolder),
                callback,AreaSelectionActivity.this);
    }

    private List<Area> fetchAreas(){
        DaoSession daoSession = ((PocketWikiApplication) getApplicationContext()).getDaoSession();
        AreaDao areaDao = daoSession.getAreaDao();
        List<Area> areas = new ArrayList<>();

        String CityIdStr = "(";
        for (int i = 0; i < Config.CityIDHolder.size() - 1; i++) {
            CityIdStr = CityIdStr + String.valueOf(Config.CityIDHolder.get(i)) + ", ";
        }
        CityIdStr = CityIdStr + String.valueOf(Config.CityIDHolder.get(Config.CityIDHolder.size() - 1)) + ")";

        Cursor cursor = daoSession.getDatabase().rawQuery(
                "SELECT AREA_ID, CREATED_AT, UPDATED_AT, NAME, CITY_NAME, CITY_ID FROM AREA WHERE CITY_ID IN "
                        + CityIdStr,null);

        try{
            if (cursor.moveToFirst()) {
                do {
                    areas.add(new Area(
                            cursor.getLong(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getLong(5)));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return areas;
    }

}
