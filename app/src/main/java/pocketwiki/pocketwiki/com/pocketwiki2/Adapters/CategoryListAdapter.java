package pocketwiki.pocketwiki.com.pocketwiki2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pocketwiki.pocketwiki.com.pocketwiki2.Dao.Category;
import pocketwiki.pocketwiki.com.pocketwiki2.R;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Config;

/**
 * Created by chinmay on 4/4/16.
 */
public class CategoryListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    List<Category> mDataSet;
    boolean mCheckBoxDesired;

    public CategoryListAdapter() {
    }

    public CategoryListAdapter(List<Category> dataSet, Context context, boolean checkBoxDesired) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCheckBoxDesired = checkBoxDesired;
        mDataSet = dataSet;
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_general, parent, false);
            holder = new ViewHolder();
            holder.tvItemName = (TextView) convertView.findViewById(R.id.listitem_general_tv_heading);
            holder.tvEntityCount = (TextView) convertView.findViewById(R.id.listitem_general_tv_subheading);
            if(mCheckBoxDesired){
                convertView.findViewById(R.id.listitem_general_rlt_cb).setVisibility(View.VISIBLE);
                holder.cbTick = (CheckBox) convertView.findViewById(R.id.listitem_general_cb);
            }
            else {
                convertView.findViewById(R.id.listitem_general_rlt_cb).setVisibility(View.GONE);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvItemName.setText(mDataSet.get(position).getName());
        if(mDataSet.get(position).getEntityCount() == 1){
            holder.tvEntityCount.setText(String.valueOf(mDataSet.get(position).getEntityCount()) +
                    " " +
                    mContext.getResources().getString(R.string.suffix_single_entity));
        }
        else {
            holder.tvEntityCount.setText(String.valueOf(mDataSet.get(position).getEntityCount()) +
                    " " +
                    mContext.getResources().getString(R.string.suffix_entities));
        }
        if(mCheckBoxDesired){

        }

        holder.cbTick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Config.CategoryIDHolder.add(mDataSet.get(position).getCategoryId());
                    HashSet hs = new HashSet();
                    hs.addAll(Config.CategoryIDHolder);
                    Config.CategoryIDHolder.clear();
                    Config.CategoryIDHolder.addAll(hs);
                }
                else {
                    Config.CategoryIDHolder.remove(mDataSet.get(position).getCategoryId());
                }
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        public TextView tvItemName;
        public TextView tvEntityCount;
        public CheckBox cbTick;
    }


}
