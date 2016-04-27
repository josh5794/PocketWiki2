package pocketwiki.pocketwiki.com.pocketwiki2.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pocketwiki.pocketwiki.com.pocketwiki2.Fragments.GeneralListFragment;

/**
 * Created by chinmay on 15/1/16.
 */
public class AlphabetsFragmentPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[];
    private int dataTypeCode;

    public AlphabetsFragmentPagerAdapter(FragmentManager fm, String[] tabTitles, int dataTypeCode) {
        super(fm);
        this.tabTitles = tabTitles;
        this.dataTypeCode = dataTypeCode;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        return GeneralListFragment.newInstance(position + 1,dataTypeCode, tabTitles);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }


}
