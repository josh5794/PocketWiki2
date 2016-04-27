package pocketwiki.pocketwiki.com.pocketwiki2.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import pocketwiki.pocketwiki.com.pocketwiki2.Dao.Content;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.ContentDao;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.DaoSession;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.EntityDao;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.SubEntity;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.SubEntityDao;
import pocketwiki.pocketwiki.com.pocketwiki2.PocketWikiApplication;
import pocketwiki.pocketwiki.com.pocketwiki2.R;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.APICaller;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Config;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.DownLoadFileFromURL;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EntityDetailsActivity extends AppCompatActivity {

    private RelativeLayout imageHolder;
    private ImageView imageView;
    private Button btnExploreSubentities;
    private TextView tvContent, tvPlay, tvDownload;
    ProgressDialog dialog;
    private Content content;
    private String audioURL;
    private MediaPlayer mediaPlayer;
    private RelativeLayout btnDownload, btnAudio;
    public String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageHolder = (RelativeLayout) findViewById(R.id.entitydetailsactivity_rlt_img);
        imageView = (ImageView) findViewById(R.id.entitydetailsactivity_iv);
        btnExploreSubentities = (Button) findViewById(R.id.entitydetailsactivity_btn_explore_subentities);
        tvContent = (TextView) findViewById(R.id.entitydetailsactivity_tv);
        btnDownload = (RelativeLayout) findViewById(R.id.entitydetailsactivity_rlt_dwnload);
        btnAudio = (RelativeLayout) findViewById(R.id.entitydetailsactivity_rlt_audio);
        tvPlay = (TextView) findViewById(R.id.entitydetailsactivity_tv_play);
        tvDownload = (TextView) findViewById(R.id.entitydetailsactivity_tv_download);
        if(Config.OPERATION_MODE == Config.MODE_OFFLINE){
            tvDownload.setText(getResources().getString(R.string.btn_text_downloaded));
        }

        loadImage();

        fetchContent();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(metrics.widthPixels,metrics.widthPixels/2);
        imageHolder.setLayoutParams(layoutParams);

        btnExploreSubentities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaoSession daoSession = ((PocketWikiApplication) getApplicationContext()).getDaoSession();
                final SubEntityDao subEntityDao = daoSession.getSubEntityDao();
                final JSONArray subEntityJsonArray = new JSONArray();

                if(Config.OPERATION_MODE == Config.MODE_ONLINE){

                    Callback<String> callback = new Callback<String>() {
                        @Override
                        public void success(String s, Response response) {
                            Log.i(TAG,"response is " + s);
                            try {
                                JSONObject jsonObject = new JSONObject(s).getJSONObject(Config.KEY_DATA);
                                JSONArray jsonArray = jsonObject.getJSONArray(Config.KEY_SUB_ENTITIES);
                                for(int i=0; i<jsonArray.length(); i++){
                                    SubEntity subEntity = new SubEntity();
                                    subEntity.setEntityId(jsonArray.getJSONObject(i).getLong(Config.KEY_ENTITY_ID));
                                    subEntity.setName(jsonArray.getJSONObject(i).getString(Config.KEY_SUB_ENTITY_NAME));
                                    subEntity.setCreatedAt(jsonArray.getJSONObject(i).getString(Config.KEY_CREATED_AT));
                                    subEntity.setUpdatedAt(jsonArray.getJSONObject(i).getString(Config.KEY_UPDATED_AT));
                                    subEntity.setSubEntityId(jsonArray.getJSONObject(i).getLong(Config.KEY_ID));
                                    subEntity.setImageURLThumbOnline(jsonArray.getJSONObject(i).getJSONObject(Config.KEY_IMAGES).getString(Config.KEY_IMAGES_THUMB));
                                    subEntity.setImageURLLargeOnline(jsonArray.getJSONObject(i).getJSONObject(Config.KEY_IMAGES).getString(Config.KEY_IMAGES_LARGE));
                                    subEntity.setImageURLThumb(Utils.saveImage(jsonArray.getJSONObject(i).getJSONObject(Config.KEY_IMAGES).getString(Config.KEY_IMAGES_THUMB),EntityDetailsActivity.this));
                                    subEntity.setImageURLLarge(Utils.saveImage(jsonArray.getJSONObject(i).getJSONObject(Config.KEY_IMAGES).getString(Config.KEY_IMAGES_LARGE),EntityDetailsActivity.this));
                                    subEntityDao.insertOrReplace(subEntity);
                                    subEntityJsonArray.put(subEntity.getSubEntityId());
                                }
                                Utils.dismissDialog(EntityDetailsActivity.this,dialog);
                                if(jsonArray.length() > 0) {
                                    Intent intent = new Intent(EntityDetailsActivity.this, EntitySelectionActivity.class);
                                    intent.putExtra(Config.KEY_ENTITY_ID, getIntent().getLongExtra(Config.KEY_ENTITY_ID, 0));
                                    intent.putExtra(Config.KEY_SUB_ENTITIES_ARRAY, subEntityJsonArray.toString());
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(EntityDetailsActivity.this,getResources().getString(R.string.toast_no_data),Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(EntityDetailsActivity.this,getResources().getString(R.string.toast_json_exception),Toast.LENGTH_SHORT).show();
                            }
                            Utils.dismissDialog(EntityDetailsActivity.this,dialog);
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            Log.e(TAG,"Something went wrong");
                            Toast.makeText(EntityDetailsActivity.this,getResources().getString(R.string.toast_callback_failure),Toast.LENGTH_SHORT).show();
                            Log.e(TAG,retrofitError.getMessage() + " hmm");
                            Utils.dismissDialog(EntityDetailsActivity.this,dialog);
                        }
                    };

                    APICaller apiCaller = new APICaller();
                    dialog = Utils.showDialog(EntityDetailsActivity.this,getResources().getString(R.string.dialog_fetch_data));
                    apiCaller.getCall(Config.URL_GET_SUB_ENTITIES(getIntent().getLongExtra(Config.KEY_ENTITY_ID,0)),
                            callback,EntityDetailsActivity.this);
                }
                else {
                    List<SubEntity> subEntities = subEntityDao.queryBuilder().where(SubEntityDao.Properties.EntityId.
                            eq(getIntent().getLongExtra(Config.KEY_ENTITY_ID,0))).list();
                    for(int i=0; i<subEntities.size(); i++){
                        subEntityJsonArray.put(subEntities.get(i).getSubEntityId());
                    }
                    if(subEntities.size() > 0) {
                        Intent intent = new Intent(EntityDetailsActivity.this, EntitySelectionActivity.class);
                        intent.putExtra(Config.KEY_ENTITY_ID, getIntent().getLongExtra(Config.KEY_ENTITY_ID, 0));
                        intent.putExtra(Config.KEY_SUB_ENTITIES_ARRAY, subEntityJsonArray.toString());
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(EntityDetailsActivity.this,getResources().getString(R.string.toast_no_data),Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer==null){
                    Log.i(TAG,"here2");
                    AudioPlayer audioPlayer = new AudioPlayer();
                    audioPlayer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"","","");
                    tvPlay.setText(getResources().getString(R.string.btn_text_stop));
                }
                else {
                    Log.i(TAG,"here3");
                    stopAudio();
                    tvPlay.setText(getResources().getString(R.string.btn_text_play));
                }
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Config.OPERATION_MODE == Config.MODE_ONLINE)
                    saveInfo();
            }
        });

    }

    private void loadImage(){
        DaoSession daoSession = ((PocketWikiApplication) getApplicationContext()).getDaoSession();
        if(getIntent().hasExtra(Config.KEY_SUB_ENTITY_ID)){
            SubEntityDao dao = daoSession.getSubEntityDao();
            Long subEntityId = getIntent().getLongExtra(Config.KEY_SUB_ENTITY_ID,0);
            getSupportActionBar().setTitle(dao.load(subEntityId).getName());
            if(Config.OPERATION_MODE == Config.MODE_ONLINE){
                Utils.loadImage(this,dao.load(subEntityId).getImageURLLargeOnline(),imageView);
            }
            else {
                Utils.loadImage(this,dao.load(subEntityId).getImageURLLarge(),imageView);
            }
        }
        else {
            EntityDao dao = daoSession.getEntityDao();
            Long entityId = getIntent().getLongExtra(Config.KEY_ENTITY_ID, 0);
            getSupportActionBar().setTitle(dao.load(entityId).getName());
            if (Config.OPERATION_MODE == Config.MODE_ONLINE) {
                Utils.loadImage(this, dao.load(entityId).getImageURLLargeOnline(), imageView);
            } else {
                Utils.loadImage(this, dao.load(entityId).getImageURLLarge(), imageView);
            }
        }

    }

    private void saveInfo(){
        DaoSession daoSession = ((PocketWikiApplication) getApplicationContext()).getDaoSession();
        ContentDao contentDao = daoSession.getContentDao();
        String audioFileName = String.valueOf(System.currentTimeMillis());
        new DownLoadFileFromURL(this,Config.DATA_TYPE_AUDIO).execute(audioURL,
                audioFileName);
        String filepath = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS + "/" + Config.DOWNLOAD_FOLDER_NAME), audioFileName)).toString();
        content.setAudioPath(filepath);
        Log.e(TAG,content.toString());
        contentDao.insertOrReplace(content);

    }

    private class AudioPlayer extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = Utils.showDialog(EntityDetailsActivity.this,getResources().getString(R.string.dialog_loading));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Utils.dismissDialog(EntityDetailsActivity.this,dialog);
        }

        @Override
        protected String doInBackground(String... params) {
            playAudio();
            return "";
        }
    }

    private void playAudio(){
        try {
            String uri = audioURL;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if(Config.OPERATION_MODE == Config.MODE_OFFLINE){
                uri = content.getAudioPath();
            }
            mediaPlayer.setDataSource(uri);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,getResources().getString(R.string.toast_error),Toast.LENGTH_SHORT).show();
        }
    }

    private void stopAudio(){
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void fetchContent(){

        DaoSession daoSession = ((PocketWikiApplication) getApplicationContext()).getDaoSession();
        final ContentDao contentDao = daoSession.getContentDao();
        if(Config.OPERATION_MODE == Config.MODE_ONLINE){
        Log.i(TAG,"here 1");
            Callback <String> callback = new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    Log.i(TAG,"response is " + s);
                    try {
                        JSONObject jsonObject = new JSONObject(s).
                                getJSONObject(Config.KEY_DATA).getJSONObject(Config.KEY_CONTENT);
                        content = new Content();
                        content.setLanguageId((long) 0);
                        content.setContentId(jsonObject.getLong(Config.KEY_ID));
                        content.setAreaEntitiesId(jsonObject.getLong(Config.KEY_AREA_ENTITY_ID));
                        content.setCreatedAt(jsonObject.getString(Config.KEY_CREATED_AT));
                        content.setUpdatedAt(jsonObject.getString(Config.KEY_UPDATED_AT));
                        content.setDescription(jsonObject.getString(Config.KEY_DESCRIPTION));
                        if(!jsonObject.isNull(Config.KEY_SUB_ENTITY_ID))
                            content.setSubEntityId(jsonObject.getLong(Config.KEY_SUB_ENTITY_ID));
                        audioURL = jsonObject.getString(Config.KEY_AUDIO);
                        displayContent(content.getDescription());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(EntityDetailsActivity.this,getResources().getString(R.string.toast_json_exception),Toast.LENGTH_SHORT).show();
                    }
                    Utils.dismissDialog(EntityDetailsActivity.this,dialog);
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Log.i(TAG,"Something went wrong");
                    Toast.makeText(EntityDetailsActivity.this,getResources().getString(R.string.toast_callback_failure),Toast.LENGTH_SHORT).show();
                    Log.e(TAG,retrofitError.getMessage() + " hmm");
                    Utils.dismissDialog(EntityDetailsActivity.this,dialog);
                }
            };

            APICaller apiCaller = new APICaller();
            dialog = Utils.showDialog(this,getResources().getString(R.string.dialog_fetch_data));
            Long entityId = getIntent().getLongExtra(Config.KEY_ENTITY_ID,0);
            if(getIntent().hasExtra(Config.KEY_SUB_ENTITY_ID)){
                Long subEntityId = getIntent().getLongExtra(Config.KEY_SUB_ENTITY_ID,0);
                apiCaller.getCall(Config.URL_GET_CONTENT(Config.DEFAULT_LANGUAGE_ID,entityId,subEntityId),callback,this);
            }
            else {
                apiCaller.getCall(Config.URL_GET_CONTENT(Config.DEFAULT_LANGUAGE_ID, entityId), callback, this);
            }
        }
        else {
            if(getIntent().hasExtra(Config.KEY_SUB_ENTITY_ID)){
                content = contentDao.queryBuilder().
                        where(ContentDao.Properties.SubEntityId.eq(getIntent().getLongExtra(Config.KEY_SUB_ENTITY_ID, 0)),
                                ContentDao.Properties.LanguageId.eq((long) 0)).
                        list().get(0);
            }
            else {
                content = contentDao.queryBuilder().
                        where(ContentDao.Properties.AreaEntitiesId.eq(getIntent().getLongExtra(Config.KEY_AREA_ENTITY_ID, 0)),
                                ContentDao.Properties.LanguageId.eq((long) 0)).
                        list().get(0);
            }
            displayContent(content.getDescription());
        }
    }

    private void displayContent(String desc){
        tvContent.setText(desc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_entity_details, menu);
        if(getIntent().hasExtra(Config.KEY_SUB_ENTITY_ID))
            menu.getItem(0).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_explore_subentities:
                btnExploreSubentities.performClick();
                return true;
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
