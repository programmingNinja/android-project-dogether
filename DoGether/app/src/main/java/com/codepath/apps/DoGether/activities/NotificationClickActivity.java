package com.codepath.apps.DoGether.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.DoGether.LocalModels.LocalUser;
import com.codepath.apps.DoGether.R;
import com.codepath.apps.DoGether.helpers.CircleTransformation;
import com.codepath.apps.DoGether.helpers.ImageUtility;
import com.codepath.apps.DoGether.models.Joining;
import com.codepath.apps.DoGether.models.User;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class NotificationClickActivity extends ActionBarActivity {

    ImageView ivJoinMePhoto;
    TextView tvUname;
    TextView tvJoinMeText;
    String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_click);
        setUpView();
        Intent i = getIntent();
        String userId = i.getStringExtra("userId");
        String message = i.getStringExtra("userMsg");
        eventId = i.getStringExtra("eventId");
        User userInfo = User.getUser(userId.toString());
        tvUname.setText(userInfo.get("name").toString());
        tvJoinMeText.setText(message.toString());
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(15)
                .oval(false)
                .build();
        Picasso.with(this).load(ImageUtility.getModifiedImageUrl(userInfo.get("profile_image_url").toString())).fit().placeholder(R.drawable.abc_spinner_mtrl_am_alpha).transform(transformation).into(ivJoinMePhoto);

    }

    public void joinEvent(View v) {
        Joining.setJoiningUser(eventId, LocalUser.getUser());
        Toast.makeText(this, "You joined this event", Toast.LENGTH_LONG);
        startActivity(new Intent(NotificationClickActivity.this, LandingActivity.class));
    }

    public void setUpView(){
        ivJoinMePhoto = (ImageView)findViewById(R.id.ivJoinMePhoto);
        tvUname = (TextView)findViewById(R.id.tvJoinMeUname);
        tvJoinMeText = (TextView)findViewById(R.id.tvJoinMeText);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification_click, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
