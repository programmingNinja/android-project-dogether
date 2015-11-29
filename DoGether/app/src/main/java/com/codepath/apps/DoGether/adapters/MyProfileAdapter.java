package com.codepath.apps.DoGether.adapters;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.DoGether.LocalModels.LocalEvent;
import com.codepath.apps.DoGether.LocalModels.LocalUser;
import com.codepath.apps.DoGether.R;
import com.codepath.apps.DoGether.activities.MyProfileActivity;
import com.codepath.apps.DoGether.models.Community;
import com.codepath.apps.DoGether.models.Event;
import com.codepath.apps.DoGether.models.Joining;
import com.codepath.apps.DoGether.models.User;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;
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
    public void onBindViewHolder(final EventViewHolder holder, int position) {

        User u = User.getUser(LocalUser.getUser());
        holder.username.setText(u.getString("name"));
        holder.eventName.setText(events.get(position).getString("text"));

        // testing
        List<User> joinedUsers = new ArrayList<>();
        joinedUsers.add(User.getUser(LocalUser.getUser()));
        joinedUsers.add(User.getUser(LocalUser.getUser()));
        joinedUsers.add(User.getUser(LocalUser.getUser()));
        joinedUsers.add(User.getUser(LocalUser.getUser()));

        holder.joinedUserList.addAll(joinedUsers);

//        String eventId = events.get(position).getObjectId();
//        Joining j = Joining.getJoining(eventId);
//        if(j != null) {
//            ParseRelation<User> joiningRelation = j.getRelation("users");
//            joiningRelation.getQuery().findInBackground(new FindCallback<User>() {
//                public void done(List<User> results, ParseException e) {
//                    if (e == null) {
//                        // access your user list here
//                        holder.joinedUserList.addAll(results);
//                        holder.joinedUserAdapter.notifyDataSetChanged();
//                    } else {
//                        // results have all the developer objects in the Icon
//                    }
//                }
//            });
//        }



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
        CardView mainLayout;
        LinearLayout expandingLayout;
        ListView expandEvents;

        JoinedUserAdapter joinedUserAdapter;
        List<User> joinedUserList;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            username = (TextView)itemView.findViewById(R.id.tvUsername);
            eventName = (TextView)itemView.findViewById(R.id.tvEventName);
            profilePicture = (ImageView)itemView.findViewById(R.id.ivProfilPic);
            expandingLayout = (LinearLayout)itemView.findViewById(R.id.expandable);
            expandingLayout.setVisibility(View.GONE);
            mainLayout = (CardView)itemView.findViewById(R.id.cv);
            expandEvents = (ListView) itemView.findViewById(R.id.lvExpandableText);
            joinedUserList = new ArrayList<>();
            joinedUserAdapter = new JoinedUserAdapter(itemView.getContext(),
                    R.layout.joined_users, joinedUserList);
            expandEvents.setAdapter(joinedUserAdapter);
            mainLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (expandingLayout.getVisibility() == View.GONE) {
                        System.out.println("clicked...expanding");
                        expand(expandEvents);
                    } else {
                        System.out.println("clicked...collapsing");
                        collapse(expandEvents);
                    }
                }
            });
        }

        private void expand(ListView listView) {
            //set Visible
            expandingLayout.setVisibility(View.VISIBLE);

            final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            expandingLayout.measure(widthSpec, heightSpec);
            ValueAnimator mAnimator = slideAnimator(0, expandingLayout.getMeasuredHeight(), listView);
            mAnimator.start();
        }

        private ValueAnimator slideAnimator(int start, int end, final ListView listView) {

            ValueAnimator animator = ValueAnimator.ofInt(start, end);

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    //Update Height
                    //int value = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = expandingLayout.getLayoutParams();
                    layoutParams.height = listView.getAdapter().getCount()*20;
                    expandingLayout.setLayoutParams(layoutParams);
                }
            });
            return animator;
        }

        private void collapse(ListView listView) {
            int finalHeight = expandingLayout.getHeight();

            ValueAnimator mAnimator = slideAnimator(finalHeight, 0, listView);

            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    //Height=0, but it set visibility to GONE
                    expandingLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mAnimator.start();
        }


    }
}
