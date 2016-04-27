package pocketwiki.pocketwiki.com.pocketwiki2.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import pocketwiki.pocketwiki.com.pocketwiki2.Adapters.CategoryListAdapter;
import pocketwiki.pocketwiki.com.pocketwiki2.Adapters.CityListAdapter;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.Category;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.CategoryDao;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.City;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.CityDao;
import pocketwiki.pocketwiki.com.pocketwiki2.Dao.DaoSession;
import pocketwiki.pocketwiki.com.pocketwiki2.PocketWikiApplication;
import pocketwiki.pocketwiki.com.pocketwiki2.R;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.APICaller;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Config;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by chinmay on 4/4/16.
 */
public class GeneralListFragment extends android.support.v4.app.Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_DATA_TYPE_CODE = "ARG_DATA_TYPE_CODE";
    public static final String ARG_TAB_TITLES = "ARG_TAB_TITLES";
    public static String TAG = "GeneralListFragment";
    private CityListAdapter cityListAdapter;
    private CategoryListAdapter categoryListAdapter;
    private static int mPage;
    private DaoSession daoSession;
    private CityDao cityDao;
    private CategoryDao categoryDao;
    private String[] alphabets;
    private List<pocketwiki.pocketwiki.com.pocketwiki2.Dao.City> cities;
    private List<Category> categories;


    public static GeneralListFragment newInstance(int page, int dataTypeCode, String[] tabTitles) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putInt(ARG_DATA_TYPE_CODE,dataTypeCode);
        args.putStringArray(ARG_TAB_TITLES,tabTitles);
        GeneralListFragment fragment = new GeneralListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        daoSession = ((PocketWikiApplication) getActivity().getApplicationContext()).getDaoSession();
        cityDao = daoSession.getCityDao();
        categoryDao = daoSession.getCategoryDao();
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.generallistfragment_lv);

        int page = 0;
        int dataTypeCode = -1;
        if(getArguments() !=  null){
            page = getArguments().getInt(ARG_PAGE);
            dataTypeCode = getArguments().getInt(ARG_DATA_TYPE_CODE);
        }
        String[] tabTitles = getArguments().getStringArray(ARG_TAB_TITLES);

        cities = cityDao.queryBuilder().where(CityDao.Properties.Name.like(tabTitles[page - 1].trim() + "%")).build().list();
        categories = categoryDao.queryBuilder().where(CategoryDao.Properties.Name.like(tabTitles[page - 1].trim() + "%")).list();


        if(dataTypeCode == Config.DATA_TYPE_CITY) {
            cityListAdapter = new CityListAdapter(cities, getActivity(), true);
            listView.setAdapter(cityListAdapter);
        }
        else if(dataTypeCode == Config.DATA_TYPE_CATEGORY){
            categoryListAdapter = new CategoryListAdapter(categories, getActivity(), true);
            listView.setAdapter(categoryListAdapter);
        }
        return view;
    }

}
