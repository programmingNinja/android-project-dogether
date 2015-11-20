package com.codepath.apps.DoGether.adapters;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.DoGether.LocalModels.LocalEvent;
import com.codepath.apps.DoGether.LocalModels.LocalUser;
import com.codepath.apps.DoGether.R;
import com.codepath.apps.DoGether.activities.MyProfileActivity;
import com.codepath.apps.DoGether.models.Event;
import com.codepath.apps.DoGether.models.User;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rshah4 on 11/17/15.
 */
public class MyProfileAdapter extends RecyclerView.Adapter<MyProfileAdapter.EventViewHolder> implements ItemTouchHelperAdapter {

    List<Event> events;
    private Activity mContext;
    public MyProfileAdapter(){}

    public MyProfileAdapter(Activity context){
        events = new ArrayList<>();
        this.mContext = context;
    }

    public void setEvents(List<Event> events) {
        this.events.addAll(events);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.profile_item, parent, false);
        EventViewHolder pvh = new EventViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {

        User u = User.getUser(LocalUser.getUser());
        holder.username.setText(u.getString("name"));
        holder.eventName.setText(events.get(position).getString("text"));
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(15)
                .oval(false)
                .build();
        Picasso.with(mContext).
                load(u.getString("profile_image_url")).
                fit().
                placeholder(R.drawable.abc_spinner_mtrl_am_alpha).
                transform(transformation).
                into(holder.profilePicture);

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(int position) {

        Event toBeRemoved = events.get(position);
        String objId = toBeRemoved.getObjectId();
        // delete from parse
        Event.removeEvent(objId);

        // delete from local
        LocalEvent.deleteLocalEvent(objId);

        // delete from the list
        events.remove(position);

        // update adapter
        notifyItemRemoved(position);

    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView username;
        TextView eventName;
        ImageView profilePicture;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            username = (TextView)itemView.findViewById(R.id.tvUsername);
            eventName = (TextView)itemView.findViewById(R.id.tvEventName);
            profilePicture = (ImageView)itemView.findViewById(R.id.ivProfilPic);
        }
    }
}
