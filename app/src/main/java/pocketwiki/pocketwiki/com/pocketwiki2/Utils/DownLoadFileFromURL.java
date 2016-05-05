package pocketwiki.pocketwiki.com.pocketwiki2.Utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import pocketwiki.pocketwiki.com.pocketwiki2.R;

/**
 * Created by chinmay on 16/10/15.
 */
public class DownLoadFileFromURL extends AsyncTask<String, String, String> {

    Context context;
    int dataType;

    public DownLoadFileFromURL(Context context, int dataType) {
        this.dataType = dataType;
        this.context = context;
    }

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     * */



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(dataType == Config.DATA_TYPE_AUDIO)
        Toast.makeText(context,context.getResources().getString(R.string.toast_downloading),Toast.LENGTH_LONG).show();
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            File dir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            dir.mkdirs();
            Uri downloadLocation;
            if(dataType == Config.DATA_TYPE_IMAGE) {
                downloadLocation = Uri.fromFile(new File(dir + "/" + Config.DOWNLOAD_FOLDER_NAME, f_url[1] + ".png"));
            }
            else {
                downloadLocation = Uri.fromFile(new File(dir + "/" + Config.DOWNLOAD_FOLDER_NAME, f_url[1]));
            }

            Log.i("dwn",downloadLocation.toString());

            DownloadManager mdDownloadManager = (DownloadManager)context
                    .getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(f_url[0]));
            if(dataType == Config.DATA_TYPE_IMAGE){
                Log.i("dwn","imgurl " + downloadLocation.toString());
                request.setVisibleInDownloadsUi(false);
            }
            else {
                request.setVisibleInDownloadsUi(true);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            }
            request.setDestinationUri(downloadLocation);
            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setDescription("Downloading via PocketWiki..");
            request.setTitle(f_url[1]);
            if(!new File(dir + "/" + Config.DOWNLOAD_FOLDER_NAME, f_url[1] + ".png").exists()){
                mdDownloadManager.enqueue(request);
            }

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        super.onPostExecute(file_url);
    }

}
