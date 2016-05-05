package pocketwiki.pocketwiki.com.pocketwiki2.Utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.okhttp.OkHttpClient;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * Created by chinmay on 3/12/15.
 */
public class APICaller {

    int statusCode;
    String result;
    RestAdapter restAdapter;
    APIList apiList;

    public APICaller() {

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);

        restAdapter = new RestAdapter.Builder().
                setServer(Config.URL_ROOT).
                setConverter(new CustomJsonConverter()).
                setClient(new OkClient(okHttpClient)).
                build();
        apiList = restAdapter.create(APIList.class);
    }

    public void getCall(String URL, Callback callback, Context context){
        apiList.getAPIcall("",URL,callback);
    }

    public void postCall(String URL, String body,Callback callback,Context context){
        TypedInput in = null;
        try {
            in = new TypedByteArray("application/json", body.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        apiList.postAPIcall("",in,URL,callback);
    }

}
