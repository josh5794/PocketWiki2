package pocketwiki.pocketwiki.com.pocketwiki2.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.Category;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.CategoryDao;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.City;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.CityDao;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.DaoSession;
import pocketwiki.pocketwiki.com.pocketwiki2.PocketWikiApplication;
import pocketwiki.pocketwiki.com.pocketwiki2.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by chinmay on 5/4/16.
 */
public class Utils {

    public static String TAG = "UTILS";
    private static boolean citiesFetched = false, categoriesFetched = false;

    public static boolean isOnline(Context ctx) {
        if (ctx == null)
            return false;

        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static ProgressDialog showDialog(Context context, String message){
        ProgressDialog dialog=new ProgressDialog(context);
        if(dialog!=null){
            dialog = null;
        }
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        dialog.setMessage(message);
        dialog.show();
        return dialog;
    }

    public static void dismissDialog(Context context, ProgressDialog dialog){
        if(dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
    }

    public static void fetchCitiesAndCategories(final Context context){
        DaoSession daoSession = ((PocketWikiApplication) context.getApplicationContext()).getDaoSession();
        final CityDao cityDao = daoSession.getCityDao();
        final CategoryDao categoryDao = daoSession.getCategoryDao();
        final ProgressDialog dialog = Utils.showDialog(context,context.getResources().getString(R.string.dialog_fetch_data));
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
                            categoryDao.insertOrReplace(category);
                        }
                        categoriesFetched = true;
                    }

                    if(categoriesFetched && citiesFetched){
                        Utils.dismissDialog(context, dialog);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,context.getResources().getString(R.string.toast_json_exception),Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"Something went wrong");
                    Utils.dismissDialog(context,dialog);
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e(TAG,"Something went wrong");
                Log.e(TAG,retrofitError.getMessage() + " hmm");
                Toast.makeText(context,context.getResources().getString(R.string.toast_callback_failure),Toast.LENGTH_SHORT).show();
                Utils.dismissDialog(context,dialog);
            }
        };
        APICaller apiCaller = new APICaller();
        apiCaller.getCall(Config.URL_GET_CITIES,callback,context);
        apiCaller.getCall(Config.URL_GET_CATEGORIES,callback,context);
    }

    public static String saveImage(String imageURL, Context context, String imageFileName){
        new DownLoadFileFromURL(context,Config.DATA_TYPE_IMAGE).execute(imageURL,
                String.valueOf(imageFileName.hashCode()));
        String filepath = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS + "/" + Config.DOWNLOAD_FOLDER_NAME), String.valueOf(imageFileName.hashCode()))).toString();

        Log.i(TAG,imageFileName + " :: " + filepath);
        return filepath;
    }

    public static String[] trimAlphabetArray(String[] alphabets, Context context, int code){
        DaoSession daoSession = ((PocketWikiApplication) context.getApplicationContext()).getDaoSession();
        CityDao cityDao = daoSession.getCityDao();
        CategoryDao categoryDao = daoSession.getCategoryDao();
        List<String> list = new LinkedList<>(Arrays.asList(alphabets));
        if(code == Config.DATA_TYPE_CITY) {
            for (int i = 0; i < alphabets.length; i++) {
                if (cityDao.queryBuilder().where(CityDao.Properties.Name.like(alphabets[i].trim() + "%")).build().list().isEmpty()) {
                    list.remove(list.indexOf(alphabets[i]));
                }
            }
        }
        else if(code == Config.DATA_TYPE_CATEGORY){
            for (int i = 0; i < alphabets.length; i++) {
                if (categoryDao.queryBuilder().where(CategoryDao.Properties.Name.like(alphabets[i].trim() + "%")).build().list().size()==0) {
                    list.remove(list.indexOf(alphabets[i]));
                }
            }
        }
        //Log.i(TAG,String.valueOf(list.size()));
        return list.toArray(new String[list.size()]);
    }

    public static void downloadAudio(Context context){

        DownloadFileAsync downloadFileAsync = new DownloadFileAsync(context);
        downloadFileAsync.execute();
    }

    private static class DownloadFileAsync extends AsyncTask<String, String, String> {

        ProgressDialog dialog;
        Context context;

        public DownloadFileAsync(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = Utils.showDialog(context,"");
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;
            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream("/sdcard/some_photo_from_gdansk_poland.mp3");
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {}
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC",progress[0]);
            dialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(context,dialog);
        }
    }

    public static void loadImage(Context mContext, String URL, ImageView imageView){
        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration
                configuration = ImageLoaderConfiguration
                .createDefault(mContext);
        imageLoader.init(configuration);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_image)
                .showImageForEmptyUri(R.drawable.default_image)
                .showImageOnFail(R.drawable.default_image)
                .cacheInMemory().cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        if(Config.OPERATION_MODE == Config.MODE_OFFLINE) {
            String fileName= URL;
            fileName = fileName.replace(':', '/');
            fileName = fileName.replace('/', '_');
            String loadURL="file://"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+Config.DOWNLOAD_FOLDER_NAME+"/"+fileName + ".png";
            Log.i(TAG,"jh " + URL);
            URL = URL + ".png";
            imageView.setImageBitmap(BitmapFactory.decodeFile(URL.substring(5)));
        }
        else {
            imageLoader.displayImage(URL,imageView,options);
        }


    }
}
