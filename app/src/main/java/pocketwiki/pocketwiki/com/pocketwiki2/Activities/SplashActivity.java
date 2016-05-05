package pocketwiki.pocketwiki.com.pocketwiki2.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import pocketwiki.pocketwiki.com.pocketwiki2.Dao.Category;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.CategoryDao;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.City;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.CityDao;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.DaoSession;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.Language;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.LanguageDao;
import pocketwiki.pocketwiki.com.pocketwiki2.PocketWikiApplication;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Config;
import pocketwiki.pocketwiki.com.pocketwiki2.R;

public class SplashActivity extends AppCompatActivity {

    public String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.color_primary_dark));
        }

        DaoSession daoSession = ((PocketWikiApplication) getApplicationContext()).getDaoSession();
        LanguageDao languageDao = daoSession.getLanguageDao();
        languageDao.insertOrReplace(new Language((long)1,String.valueOf(System.currentTimeMillis()),String.valueOf(System.currentTimeMillis()),"en",true));
        languageDao.insertOrReplace(new Language((long)2,String.valueOf(System.currentTimeMillis()),String.valueOf(System.currentTimeMillis()),"hi",true));

        /*DaoSession daoSession = ((PocketWikiApplication) getApplicationContext()).getDaoSession();
        CityDao cityDao = daoSession.getCityDao();
        CategoryDao categoryDao = daoSession.getCategoryDao();
        cityDao.insertOrReplace(new City((long) 0,"","","Sample",0));
        categoryDao.insertOrReplace(new Category((long) 0,"","","Sample",0));*/

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,CityCategorySelectionActivity.class);
                SplashActivity.this.finish();
                startActivity(intent);
            }
        }, Config.SPLASH_ACTIVITY_DELAY);
    }
}
