package com.michalenko.olga.simpleinsta.home;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.michalenko.olga.simpleinsta.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;
    private FirebaseAuth mAuth;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private StorageReference mStorageRef;

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();

    private View mControlsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mControlsView = findViewById(R.id.fullscreen_content_controls);

        ImageView image = (ImageView)findViewById(R.id.fullPhoto);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference().child(mAuth.getCurrentUser().getUid().toString());

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(mStorageRef.child(getIntent().getStringExtra("path")))
                .into(image);

    }


    public void close(View view) {
        this.finish();
    }
}
