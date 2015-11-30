package com.codepath.apps.DoGether.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.DoGether.R;
import com.codepath.apps.DoGether.helpers.DateFormatter;
import com.codepath.apps.DoGether.helpers.ImageUtility;
import com.codepath.apps.DoGether.models.LandingActivityView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by msamant on 11/7/15.
 */
public class LandingActivityViewAdapter extends ArrayAdapter<LandingActivityView> {

    private static final String TAG = "LandingActivityViewAdapter";
    private List<LandingActivityView> cardList = new ArrayList<LandingActivityView>();
        public LandingActivityViewAdapter(Context context, List <LandingActivityView> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
        }

        static class CardViewHolder {
            TextView username;
            TextView eventName;
            ImageView profilePicture;
            TextView createdAt;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            LandingActivityView landingActivityView = getItem(position);
            View row = convertView;
            CardViewHolder viewHolder;
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                //convertView = LayoutInflater.from(getContext()).inflate(R.layout.community_users, parent, false);

                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.community_users, parent, false);
                viewHolder = new CardViewHolder();
                viewHolder.username = (TextView) row.findViewById(R.id.tvComUserUsername);
                viewHolder.eventName = (TextView) row.findViewById(R.id.tvComUserEvent);
                viewHolder.profilePicture = (ImageView)row.findViewById(R.id.ivComUserPhoto);
                viewHolder.createdAt = (TextView)row.findViewById(R.id.tvCreateTime);
                row.setTag(viewHolder);
            }
            else {
                viewHolder = (CardViewHolder)row.getTag();
            }
            // Lookup view for data population
//            ImageView ivComUserPhoto = (ImageView)convertView.findViewById(R.id.ivComUserPhoto);
//            TextView tvComUserUsername = (TextView) convertView.findViewById(R.id.tvComUserUsername);
//            TextView tvComUserEvent = (TextView)convertView.findViewById(R.id.tvComUserEvent);
//
//            Picasso.with(getContext()).load(landingActivityView.getComUserUserPhoto()).fit().placeholder(R.drawable.abc_spinner_mtrl_am_alpha).into(ivComUserPhoto);
//            tvComUserUsername.setText(String.valueOf(landingActivityView.getComUserUserName()));
//            tvComUserEvent.setText(String.valueOf(landingActivityView.getComUserEventText()));

            LandingActivityView card = getItem(position);
            viewHolder.username.setText(card.getComUserUserName());
            viewHolder.eventName.setText(card.getComUserEventText());

            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
            System.out.println("formatted date inside " + DateFormatter.getTimeAgo(card.getComCreateTime().getTime(), getContext()));
            viewHolder.createdAt.setText(DateFormatter.getTimeAgo(card.getComCreateTime().getTime(), getContext()));

            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.BLACK)
                    .borderWidthDp(1)
                    .cornerRadiusDp(15)
                    .oval(false)
                    .build();
            Picasso.with(getContext()).load(ImageUtility.getModifiedImageUrl(landingActivityView.getComUserUserPhoto())).fit().placeholder(R.drawable.abc_spinner_mtrl_am_alpha).transform(transformation).into(viewHolder.profilePicture);

            return row;

            // Return the completed view to render on screen
            //return convertView;
        }
}
