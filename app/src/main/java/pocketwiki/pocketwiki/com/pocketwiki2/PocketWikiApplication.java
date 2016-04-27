package pocketwiki.pocketwiki.com.pocketwiki2;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

import pocketwiki.pocketwiki.com.pocketwiki2.Dao.DaoMaster;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.DaoSession;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Config;

/**
 * Created by chinmay on 5/4/16.
 */
public class PocketWikiApplication extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Config.LOCAL_DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        //setLocale("hi");
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }



    public DaoSession getDaoSession(){
        return daoSession;
    }
}
