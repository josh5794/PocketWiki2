package pocketwiki.pocketwiki.com.pocketwiki2.Utils;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.EncodedPath;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.mime.TypedInput;

/**
 * Created by chinmay on 3/12/15.
 */
public interface APIList {

    @POST("/{path}")
    void postAPIcall(@Header("token") String mAuthtoken, @Body TypedInput body, @EncodedPath("path") String path, Callback<String> callback);

    @GET("/{path}")
    void getAPIcall(@Header("token") String mAuthtoken, @EncodedPath("path") String path, Callback<String> callback);
}

