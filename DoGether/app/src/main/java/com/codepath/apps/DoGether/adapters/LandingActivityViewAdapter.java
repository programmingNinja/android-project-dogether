package com.codepath.apps.DoGether.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.DoGether.R;
import com.codepath.apps.DoGether.models.LandingActivityView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by msamant on 11/7/15.
 */
public class LandingActivityViewAdapter extends ArrayAdapter<LandingActivityView> {


        public LandingActivityViewAdapter(Context context, List <LandingActivityView> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            LandingActivityView landingActivityView = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.community_users, parent, false);
            }
            // Lookup view for data population
            ImageView ivComUserPhoto = (ImageView)convertView.findViewById(R.id.ivComUserPhoto);
            TextView tvComUserUsername = (TextView) convertView.findViewById(R.id.tvComUserUsername);
            TextView tvComUserEvent = (TextView)convertView.findViewById(R.id.tvComUserEvent);

            Picasso.with(getContext()).load(landingActivityView.getComUserUserPhoto()).fit().placeholder(R.drawable.abc_spinner_mtrl_am_alpha).into(ivComUserPhoto);
            tvComUserUsername.setText(String.valueOf(landingActivityView.getComUserUserName()));
            tvComUserEvent.setText(String.valueOf(landingActivityView.getComUserEventText()));

            // Return the completed view to render on screen
            return convertView;
        }
}
