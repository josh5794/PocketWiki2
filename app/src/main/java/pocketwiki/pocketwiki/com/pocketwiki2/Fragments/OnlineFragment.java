package pocketwiki.pocketwiki.com.pocketwiki2.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pocketwiki.pocketwiki.com.pocketwiki2.Adapters.AlphabetsFragmentPagerAdapter;
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

public class OnlineFragment extends Fragment {

    public String TAG = getClass().getSimpleName();
    View header1, header2;
    TextView tvHeader1, tvHeader2;
    RelativeLayout cityListLayout, categoryListLayout, rltHeaderArrow1, rltHeaderArrow2, parentView;
    ImageView headerArrow1, headerArrow2;
    FloatingActionButton btnApply;
    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_online, container, false);

        parentView = (RelativeLayout) view.findViewById(R.id.fragmentonline_parentView);

        String[] alphabetArray = getResources().getStringArray(R.array.alphabets_english);
        ViewPager viewPager1 = (ViewPager) view.findViewById(R.id.fragmentonline_viewpager1);
        ViewPager viewPager2 = (ViewPager) view.findViewById(R.id.fragmentonline_viewpager2);
        final String[] cityAlphabetArray = Utils.trimAlphabetArray(alphabetArray,getActivity(),Config.DATA_TYPE_CITY);
        final String[] categoryAlphabetArray = Utils.trimAlphabetArray(alphabetArray,getActivity(),Config.DATA_TYPE_CATEGORY);
        viewPager1.setAdapter(new AlphabetsFragmentPagerAdapter(getActivity().getSupportFragmentManager(), cityAlphabetArray, Config.DATA_TYPE_CITY));
        viewPager2.setAdapter(new AlphabetsFragmentPagerAdapter(getActivity().getSupportFragmentManager(), categoryAlphabetArray, Config.DATA_TYPE_CATEGORY));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip1 = (PagerSlidingTabStrip) view.findViewById(R.id.fragmentonline_tabs1);
        PagerSlidingTabStrip tabsStrip2 = (PagerSlidingTabStrip) view.findViewById(R.id.fragmentonline_tabs2);
        // Attach the view pager to the tab strip
        tabsStrip1.setViewPager(viewPager1);
        tabsStrip2.setViewPager(viewPager2);

        cityListLayout = (RelativeLayout) view.findViewById(R.id.fragmentonline_rlt_city_list);
        categoryListLayout = (RelativeLayout) view.findViewById(R.id.fragmentonline_rlt_category_list);
        cityListLayout.setVisibility(View.GONE);
        categoryListLayout.setVisibility(View.GONE);

        header1 = view.findViewById(R.id.fragmentonline_header1);
        header2 = view.findViewById(R.id.fragmentonline_header2);
        tvHeader1 = (TextView) header1.findViewById(R.id.listheader_tv);
        tvHeader2 = (TextView) header2.findViewById(R.id.listheader_tv);
        rltHeaderArrow1 = (RelativeLayout) header1.findViewById(R.id.listheader_rlt_arrow);
        rltHeaderArrow2 = (RelativeLayout) header2.findViewById(R.id.listheader_rlt_arrow);
        rltHeaderArrow1.setVisibility(View.VISIBLE);
        rltHeaderArrow2.setVisibility(View.VISIBLE);
        headerArrow1 = (ImageView) header1.findViewById(R.id.listheader_iv_arrow);
        headerArrow2 = (ImageView) header2.findViewById(R.id.listheader_iv_arrow);
        tvHeader1.setText(getResources().getString(R.string.list_header_select_city));
        tvHeader2.setText(getResources().getString(R.string.list_header_select_category));
        header1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cityAlphabetArray.length > 0) {
                    if (cityListLayout.getVisibility() == View.VISIBLE) {
                        cityListLayout.setVisibility(View.GONE);
                        headerArrow1.setImageResource(R.drawable.right_arrow_gray);
                    } else if (cityListLayout.getVisibility() == View.GONE) {
                        cityListLayout.setVisibility(View.VISIBLE);
                        headerArrow1.setImageResource(R.drawable.down_arrow_gray);
                        if (categoryListLayout.getVisibility() == View.VISIBLE) {
                            categoryListLayout.setVisibility(View.GONE);
                            headerArrow2.setImageResource(R.drawable.right_arrow_gray);
                        }
                    }
                }
                else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.toast_no_data),Toast.LENGTH_SHORT).show();
                }
            }
        });
        header2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categoryAlphabetArray.length > 0) {
                    if (categoryListLayout.getVisibility() == View.VISIBLE) {
                        categoryListLayout.setVisibility(View.GONE);
                        headerArrow2.setImageResource(R.drawable.right_arrow_gray);

                    } else if (categoryListLayout.getVisibility() == View.GONE) {
                        categoryListLayout.setVisibility(View.VISIBLE);
                        headerArrow2.setImageResource(R.drawable.down_arrow_gray);
                        if (cityListLayout.getVisibility() == View.VISIBLE) {
                            cityListLayout.setVisibility(View.GONE);
                            headerArrow1.setImageResource(R.drawable.right_arrow_gray);
                        }
                    }
                }
                else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.toast_no_data),Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
        
    }


}
