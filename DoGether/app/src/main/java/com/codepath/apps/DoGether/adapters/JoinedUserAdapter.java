package com.codepath.apps.DoGether.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.DoGether.R;
import com.codepath.apps.DoGether.helpers.ImageUtility;
import com.codepath.apps.DoGether.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rshah4 on 11/29/15.
 */
public class JoinedUserAdapter extends ArrayAdapter<User> {

    Context context;

    public JoinedUserAdapter(Context context, int resourceId,
                                 List<User> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtUsername;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        User user = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.joined_users, null);
            holder = new ViewHolder();
            holder.txtUsername = (TextView) convertView.findViewById(R.id.tVJoinedUserName);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iVJoinedProfilePic);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtUsername.setText(user.getString("name"));
        Picasso.with(getContext()).
                load(ImageUtility.getModifiedImageUrl(user.getString("profile_image_url"))).
                        fit().
                        placeholder(R.drawable.abc_spinner_mtrl_am_alpha).
                        into(holder.imageView);
        return convertView;
    }
}
