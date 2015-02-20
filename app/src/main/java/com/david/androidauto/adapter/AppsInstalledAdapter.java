package com.david.androidauto.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.david.androidauto.R;

import java.util.List;

/**
 * Created by davidhodge on 12/4/14.
 */
public class AppsInstalledAdapter extends BaseAdapter {
    private List<ApplicationInfo> items;
    private LayoutInflater inflater;
    private Context mContext;
    PackageManager packageManager;

    public AppsInstalledAdapter(Context context, List<ApplicationInfo> items, PackageManager packageManager) {
        inflater = LayoutInflater.from(context);
        this.items = items;
        mContext = context;
        this.packageManager = packageManager;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_app, parent, false);
            holder.appName = (TextView) convertView.findViewById(R.id.app_name);
            holder.appIcon = (ImageView) convertView.findViewById(R.id.app_icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.appName.setText(items.get(position).loadLabel(packageManager));
        holder.appIcon.setImageDrawable(items.get(position).loadIcon(packageManager));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = packageManager
                            .getLaunchIntentForPackage(items.get(position).packageName);

                    if (null != intent) {
                        mContext.startActivity(intent);
                    }
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(mContext, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(mContext, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView appName;
        ImageView appIcon;
    }
}
