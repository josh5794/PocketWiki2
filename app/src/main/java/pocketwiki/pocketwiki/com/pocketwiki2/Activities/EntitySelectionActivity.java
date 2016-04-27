package pocketwiki.pocketwiki.com.pocketwiki2.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pocketwiki.pocketwiki.com.pocketwiki2.Adapters.EntityListAdapter;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.AreaEntities;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.AreaEntitiesDao;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.CategoryEntities;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.CategoryEntitiesDao;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.ContentDao;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.DaoSession;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.Entity;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.EntityDao;
import pocketwiki.pocketwiki.com.pocketwiki2.EntityAndAreaInfo;
import pocketwiki.pocketwiki.com.pocketwiki2.PocketWikiApplication;
import pocketwiki.pocketwiki.com.pocketwiki2.R;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.APICaller;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Config;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.DownLoadFileFromURL;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EntitySelectionActivity extends AppCompatActivity {

    private List<EntityAndAreaInfo> entityAndAreaInfoList;
    ProgressDialog dialog;
    ListView listView;
    public String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().hasExtra(Config.KEY_SUB_ENTITIES_ARRAY)){
            getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_subentity_selection));
        }
        else {
            getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_entity_selection));
        }

        /*ArrayList<Entity> itemList = new ArrayList<>();
        itemList.add(new Entity((long) 35,"","","itemName1",""));
        itemList.add(new Entity((long) 36,"","","itemName1",""));
        itemList.add(new Entity((long) 37,"","","itemName1",""));
        itemList.add(new Entity((long) 38,"","","itemName1",""));
*/

        listView = (ListView) findViewById(R.id.entityselectionactivity_lv);

        if(Config.OPERATION_MODE == Config.MODE_ONLINE) {
            if(getIntent().hasExtra(Config.KEY_SUB_ENTITIES_ARRAY)){
                setupEntityList();
            }
            else {
                fetchEntityAndAreaInfoOnline();
            }
        }
        else {
            setupEntityList();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(Config.OPERATION_MODE == Config.MODE_ONLINE || entityAndAreaInfoList.get(position).isContentExists()) {
                    Intent intent = new Intent(EntitySelectionActivity.this, EntityDetailsActivity.class);
                    intent.putExtra(Config.KEY_AREA_ENTITY_ID, entityAndAreaInfoList.get(position).getAreaEntityId());
                    intent.putExtra(Config.KEY_ENTITY_ID, entityAndAreaInfoList.get(position).getEntityId());
                    if (getIntent().hasExtra(Config.KEY_SUB_ENTITIES_ARRAY)) {
                        try {
                            long subEntityId = new JSONArray(getIntent().getStringExtra(Config.KEY_SUB_ENTITIES_ARRAY)).getLong(position);
                            intent.putExtra(Config.KEY_SUB_ENTITY_ID, subEntityId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    startActivity(intent);
                }
                else {
                    Toast.makeText(EntitySelectionActivity.this,getResources().getString(R.string.toast_go_online),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setContentExists(){


        if(Config.OPERATION_MODE == Config.MODE_ONLINE){
            for (int i=0; i<entityAndAreaInfoList.size(); i++){
                entityAndAreaInfoList.get(i).setContentExists(true);
            }
        }
        else
        {
            DaoSession daoSession = ((PocketWikiApplication) getApplicationContext()).getDaoSession();
            ContentDao contentDao = daoSession.getContentDao();

            //intent arrives from entity details activity
            if (getIntent().hasExtra(Config.KEY_SUB_ENTITIES_ARRAY)) {
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(getIntent().getStringExtra(Config.KEY_SUB_ENTITIES_ARRAY));
                    for (int i = 0; i < entityAndAreaInfoList.size(); i++) {
                        if (contentDao.queryBuilder().
                                where(ContentDao.Properties.SubEntityId.
                                        eq(jsonArray.getLong(i))).list().isEmpty()) {
                            entityAndAreaInfoList.get(i).setContentExists(false);
                        } else {
                            entityAndAreaInfoList.get(i).setContentExists(true);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            //intent arrives from area selection activity
            else {
                for (int i = 0; i < entityAndAreaInfoList.size(); i++) {
                    if (contentDao.queryBuilder().
                            where(ContentDao.Properties.AreaEntitiesId.
                                    eq(entityAndAreaInfoList.get(i).getAreaEntityId())).build().list().isEmpty()) {
                        entityAndAreaInfoList.get(i).setContentExists(false);
                    } else {
                        entityAndAreaInfoList.get(i).setContentExists(true);
                    }
                }
            }
        }
    }

    private void setupEntityList(){
        entityAndAreaInfoList = fetchEntityAndAreaInfo();
        setContentExists();
        EntityListAdapter listAdapter = new EntityListAdapter(entityAndAreaInfoList, this);
        listView.setAdapter(listAdapter);
    }

    private List<EntityAndAreaInfo> fetchEntityAndAreaInfo(){

        DaoSession daoSession = ((PocketWikiApplication) getApplicationContext()).getDaoSession();
        Cursor cursor;

        //Intent arrives from Entity Details Activity
        if(getIntent().hasExtra(Config.KEY_SUB_ENTITIES_ARRAY)){
            String subEntityIdStr = "(";
            JSONArray subEntityJsonArray;
            try {
                subEntityJsonArray = new JSONArray(getIntent().getStringExtra(Config.KEY_SUB_ENTITIES_ARRAY));
                for (int i = 0; i < subEntityJsonArray.length() - 1; i++) {
                    subEntityIdStr = subEntityIdStr + String.valueOf(subEntityJsonArray.getLong(i)) + ", ";
                }
                subEntityIdStr = subEntityIdStr + String.valueOf(subEntityJsonArray.getLong(subEntityJsonArray.length() - 1)) + ")";
            } catch (JSONException e) {
                e.printStackTrace();
                subEntityIdStr = "()";
            }
            cursor = daoSession.getDatabase().rawQuery("SELECT C.NAME , D.NAME, B.AREA_ENTITIES_ID, A.ENTITY_ID, E.NAME, D.IMAGE_URLTHUMB, D.IMAGE_URLTHUMB_ONLINE FROM" +
                    " ENTITY A, AREA_ENTITIES B, AREA C, SUB_ENTITY D, CITY E WHERE" +
                    " A.ENTITY_ID = B.ENTITY_ID AND B.AREA_ID = C.AREA_ID AND" +
                    " A.ENTITY_ID = D.ENTITY_ID AND C.CITY_ID = E.CITY_ID AND" +
                    " D.SUB_ENTITY_ID IN " + subEntityIdStr,null);
        }
        //Intent arrives from Area Selection Activity
        else {
            String areaIdStr = "(", categoryIdStr = "(";
            //Precautionary check, they can't be empty. User can't reach here if they are empty
            if (!Config.AreaIDHolder.isEmpty() && !Config.CategoryIDHolder.isEmpty()) {
                for (int i = 0; i < Config.AreaIDHolder.size() - 1; i++) {
                    areaIdStr = areaIdStr + String.valueOf(Config.AreaIDHolder.get(i)) + ", ";
                }
                areaIdStr = areaIdStr + String.valueOf(Config.AreaIDHolder.get(Config.AreaIDHolder.size() - 1)) + ")";
                for (int i = 0; i < Config.CategoryIDHolder.size() - 1; i++) {
                    categoryIdStr = categoryIdStr + String.valueOf(Config.CategoryIDHolder.get(i)) + ", ";
                }
                categoryIdStr = categoryIdStr + String.valueOf(Config.CategoryIDHolder.get(Config.CategoryIDHolder.size() - 1)) + ")";

                Config.CategoryIDHolder.clear();
                Config.AreaIDHolder.clear();
            }
            else {
                return new ArrayList<EntityAndAreaInfo>();
            }

            cursor = daoSession.getDatabase().rawQuery("SELECT D.NAME, A.NAME ,B.AREA_ENTITIES_ID , A.ENTITY_ID , E.NAME, A.IMAGE_URLTHUMB, A.IMAGE_URLTHUMB_ONLINE " +
                    "FROM ENTITY A, AREA_ENTITIES B, CATEGORY_ENTITIES C, AREA D, CITY E " +
                    "WHERE A.ENTITY_ID = B.ENTITY_ID AND B.ENTITY_ID = C.ENTITY_ID AND D.AREA_ID = B.AREA_ID " +
                    "AND E.CITY_ID = D.CITY_ID AND B.AREA_ID IN " + areaIdStr + " AND C.CATEGORY_ID IN " + categoryIdStr, null);

        }

        List<EntityAndAreaInfo> entityAndAreaInfoList = new ArrayList<>();
        try{
            if (cursor.moveToFirst()) {
                do {
                    entityAndAreaInfoList.add(new EntityAndAreaInfo(
                            cursor.getString(0),cursor.getString(1),cursor.getLong(2),cursor.getLong(3),cursor.getString(4),cursor.getString(5),cursor.getString(6)));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        Log.i(TAG,"ga " + String.valueOf(entityAndAreaInfoList.size()));

        return entityAndAreaInfoList;
    }

    private void fetchEntityAndAreaInfoOnline(){
        DaoSession daoSession = ((PocketWikiApplication) EntitySelectionActivity.this.getApplicationContext()).getDaoSession();
        final EntityDao entityDao = daoSession.getEntityDao();
        final AreaEntitiesDao areaEntitiesDao = daoSession.getAreaEntitiesDao();
        final CategoryEntitiesDao categoryEntitiesDao = daoSession.getCategoryEntitiesDao();

        Callback<String> callback = new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.i(TAG,"response is " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s).getJSONObject(Config.KEY_DATA);
                    JSONArray jsonArray = jsonObject.getJSONArray(Config.KEY_ENTITIES);
                    for(int i=0; i<jsonArray.length();i++){
                        entityDao.insertOrReplace(extractEntity(jsonArray.getJSONObject(i)));
                        areaEntitiesDao.insertOrReplace(extractAreaEntity(jsonArray.getJSONObject(i)));
                        categoryEntitiesDao.insertOrReplace(extractCategoryEntity(jsonArray.getJSONObject(i)));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EntitySelectionActivity.this,getResources().getString(R.string.toast_json_exception),Toast.LENGTH_SHORT).show();
                }
                Utils.dismissDialog(EntitySelectionActivity.this,dialog);
                setupEntityList();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e(TAG,"Something went wrong");
                Log.e(TAG,retrofitError.getMessage() + " hmm");
                Toast.makeText(EntitySelectionActivity.this,getResources().getString(R.string.toast_callback_failure),Toast.LENGTH_SHORT).show();
                Utils.dismissDialog(EntitySelectionActivity.this,dialog);
            }
        };
        APICaller apiCaller = new APICaller();
        dialog = Utils.showDialog(EntitySelectionActivity.this,getResources().getString(R.string.dialog_fetch_data));
        apiCaller.getCall(Config.URL_GET_ENTITIES(Config.AreaIDHolder,Config.CategoryIDHolder),
                callback,EntitySelectionActivity.this);
    }

    private CategoryEntities extractCategoryEntity(JSONObject jsonObject){
        CategoryEntities categoryEntities = new CategoryEntities();
        try {
            JSONObject jObj = jsonObject.getJSONObject(Config.KEY_CATEGORY_ENTITY_OBJECT);
            categoryEntities.setEntityId(jsonObject.getLong(Config.KEY_ID));
            categoryEntities.setCreatedAt(jObj.getString(Config.KEY_CREATED_AT));
            categoryEntities.setUpdatedAt(jObj.getString(Config.KEY_UPDATED_AT));
            categoryEntities.setCategoryId(jObj.getLong(Config.KEY_CATEGORY_ID));
            categoryEntities.setCategoryEntitiesId(jObj.getLong(Config.KEY_ID));
        } catch (JSONException e){
            e.printStackTrace();
        }
        return categoryEntities;
    }

    private AreaEntities extractAreaEntity(JSONObject jsonObject){
        AreaEntities areaEntities = new AreaEntities();
        try {
            JSONObject jObj = jsonObject.getJSONObject(Config.KEY_AREA_ENTITY_OBJECT);
            areaEntities.setEntityId(jsonObject.getLong(Config.KEY_ID));
            areaEntities.setCreatedAt(jObj.getString(Config.KEY_CREATED_AT));
            areaEntities.setUpdatedAt(jObj.getString(Config.KEY_UPDATED_AT));
            areaEntities.setAreaId(jObj.getLong(Config.KEY_AREA_ID));
            areaEntities.setAreaEntitiesId(jObj.getLong(Config.KEY_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return areaEntities;
    }

    private Entity extractEntity(JSONObject jsonObject){
        Entity entity = new Entity();
        try {
            entity.setEntityId(jsonObject.getLong(Config.KEY_ID));
            entity.setName(jsonObject.getString(Config.KEY_ENTITY_NAME));
            entity.setCreatedAt(jsonObject.getString(Config.KEY_CREATED_AT));
            entity.setUpdatedAt(jsonObject.getString(Config.KEY_UPDATED_AT));
            entity.setImageURLThumbOnline(jsonObject.getJSONObject(Config.KEY_IMAGES).getString(Config.KEY_IMAGES_THUMB));
            entity.setImageURLLargeOnline(jsonObject.getJSONObject(Config.KEY_IMAGES).getString(Config.KEY_IMAGES_LARGE));
            entity.setImageURLThumb(Utils.saveImage(jsonObject.getJSONObject(Config.KEY_IMAGES).getString(Config.KEY_IMAGES_THUMB),EntitySelectionActivity.this));
            entity.setImageURLLarge(Utils.saveImage(jsonObject.getJSONObject(Config.KEY_IMAGES).getString(Config.KEY_IMAGES_LARGE),EntitySelectionActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return entity;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}











