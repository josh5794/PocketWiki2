package pocketwiki.pocketwiki.com.pocketwiki2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import pocketwiki.pocketwiki.com.pocketwiki2.Dao.Area;
import pocketwiki.pocketwiki.com.pocketwiki2.R;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Config;

/**
 * Created by chinmay on 4/4/16.
 */
public class AreaListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    List<Area> mDataSet;
    boolean mCheckBoxDesired;

    public AreaListAdapter() {
    }

    public AreaListAdapter(List<Area> dataSet, Context context, boolean checkBoxDesired) {
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
            /*if(mCheckBoxDesired){
                convertView.findViewById(R.id.listitem_general_rlt_cb).setVisibility(View.VISIBLE);
                holder.cbTick = (CheckBox) convertView.findViewById(R.id.listitem_general_cb);
            }
            else {
                convertView.findViewById(R.id.listitem_general_rlt_cb).setVisibility(View.GONE);
            }*/
            convertView.findViewById(R.id.listitem_general_rlt_cb).setVisibility(View.VISIBLE);
            holder.cbTick = (CheckBox) convertView.findViewById(R.id.listitem_general_cb);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvItemName.setText(mDataSet.get(position).getName());
        holder.tvEntityCount.setText(mDataSet.get(position).getCityName());

        if(mCheckBoxDesired){
            holder.cbTick.setChecked(true);
            Config.AreaIDHolder.add(mDataSet.get(position).getAreaId());
        }

        holder.cbTick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Config.AreaIDHolder.add(mDataSet.get(position).getAreaId());
                }
                else {
                    Config.AreaIDHolder.remove(mDataSet.get(position).getAreaId());
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
