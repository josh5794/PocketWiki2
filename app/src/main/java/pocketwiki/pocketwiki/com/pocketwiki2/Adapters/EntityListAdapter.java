package pocketwiki.pocketwiki.com.pocketwiki2.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import pocketwiki.pocketwiki.com.pocketwiki2.Dao.Entity;
import pocketwiki.pocketwiki.com.pocketwiki2.EntityAndAreaInfo;
import pocketwiki.pocketwiki.com.pocketwiki2.R;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Config;
import pocketwiki.pocketwiki.com.pocketwiki2.Utils.Utils;

/**
 * Created by chinmay on 24/1/16.
 */
public class EntityListAdapter extends BaseAdapter {

    private List<EntityAndAreaInfo> mDataSet;
    private Context mContext;
    private LayoutInflater mInflater;

    public EntityListAdapter() {
    }

    public EntityListAdapter(List<EntityAndAreaInfo> dataSet, Context context) {
        this.mDataSet = dataSet;
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(position==0){

        }
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_entity, parent, false);
            holder = new ViewHolder();
            holder.tvEntityName = (TextView) convertView.findViewById(R.id.listitem_entity_tv_name);
            holder.tvAreaName = (TextView) convertView.findViewById(R.id.list_item_entity_tv_area);
            holder.ivThumb = (ImageView) convertView.findViewById(R.id.listitem_entity_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(!mDataSet.get(position).isContentExists()){
            convertView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.red));
        }
        else {
            convertView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
        }
        Log.i("etladapter",mDataSet.get(position).getThumbURL() + " hkl");
        holder.tvEntityName.setText(mDataSet.get(position).getEntityName());
        holder.tvAreaName.setText(mDataSet.get(position).getAreaName() + ", " + mDataSet.get(position).getCityName());
        if(Config.OPERATION_MODE == Config.MODE_OFFLINE){
            Utils.loadImage(mContext,mDataSet.get(position).getThumbURL(),holder.ivThumb);
        }
        else {
            Utils.loadImage(mContext,mDataSet.get(position).getThumbURLOnline(),holder.ivThumb);
        }

        return convertView;
    }

    private static class ViewHolder {
        public TextView tvEntityName;
        public TextView tvAreaName;
        public ImageView ivThumb;
    }
}
