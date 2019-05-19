package com.example.recorder;


import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity  {

    private int RECORD_AUDIO_REQUEST_CODE = 1;
//
//    private MediaRecorder mRecorder;
//    private MediaPlayer mPlayer;
//    private String fileName = null;
//    private int lastProgress = 0;
//    private Handler mHandler = new Handler();
//    private boolean isPlaying = false;
//
//    private Toolbar toolbar;
//    private Chronometer chronometer;
//    private ImageView imageViewRecord, imageViewPlay, imageViewStop;
//    private SeekBar seekBar;
//    private LinearLayout linearLayoutRecorder, linearLayoutPlay;
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            getPermissionToRecordAudio();


        }
//
//        initViews();


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToRecordAudio() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RECORD_AUDIO_REQUEST_CODE);

        }
    }
    // Callback with the request from calling requestPermissions(...)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED){

                //Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "You must give permissions to use this app. App is exiting.", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        }

    }
//
//    private void initViews() {
//
//        /** setting up the toolbar  **/
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Voice Recorder");
//
//        linearLayoutRecorder = (LinearLayout) findViewById(R.id.linearLayoutRecorder);
//                imageViewRecord = (ImageView) findViewById(R.id.imageViewRecord);
//        imageViewStop = (ImageView) findViewById(R.id.imageViewStop);
//        imageViewPlay = (ImageView) findViewById(R.id.imageViewPlay);
//        linearLayoutPlay = (LinearLayout) findViewById(R.id.linearLayoutPlay);
//        seekBar = (SeekBar) findViewById(R.id.seekBar);
//
//        imageViewRecord.setOnClickListener(this);
//        imageViewStop.setOnClickListener(this);
//        imageViewPlay.setOnClickListener(this);
//
//
//
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        if (v == imageViewRecord) {
//            prepareforRecording();
//            startRecording();
//        }
//            else if(v == imageViewStop ){
//                prepareforStop();
//                stopRecording();
//
//        }
//    }
//
//    private void prepareforRecording() {
//        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
//        imageViewRecord.setVisibility(View.GONE);
//        imageViewStop.setVisibility(View.VISIBLE);
//        linearLayoutPlay.setVisibility(View.GONE);
//    }
//
//
//    private void startRecording() {
//        //we use the MediaRecorder class to record
//        mRecorder = new MediaRecorder();
//        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        /**In the lines below, we create a directory VoiceRecorderSimplifiedCoding/Audios in the phone storage
//         * and the audios are being stored in the Audios folder **/
//        File root = android.os.Environment.getExternalStorageDirectory();
//        File file = new File(root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios");
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//
//        fileName =  root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios/" +
//                String.valueOf(System.currentTimeMillis() + ".mp3");
//        Log.d("filename",fileName);
//        mRecorder.setOutputFile(fileName);
//        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//
//        try {
//            mRecorder.prepare();
//            mRecorder.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        lastProgress = 0;
//        seekBar.setProgress(0);
//        stopPlaying();
//        //starting the chronometer
//
//    }
//
//
//    private void stopPlaying() {
//        try{
//            mPlayer.release();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        mPlayer = null;
//        //showing the play button
//
//    }
//    private void prepareforStop() {
//        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
//        imageViewRecord.setVisibility(View.VISIBLE);
//        imageViewStop.setVisibility(View.GONE);
//        linearLayoutPlay.setVisibility(View.VISIBLE);
//    }
//
//    private void stopRecording() {
//
//        try{
//            mRecorder.stop();
//            mRecorder.release();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        mRecorder = null;
//        //showing the play button
//        Toast.makeText(this, "Recording saved successfully.", Toast.LENGTH_SHORT).show();
//    }
//
//    private void startPlaying() {
//        mPlayer = new MediaPlayer();
//        try {
////fileName is global string. it contains the Uri to the recently recorded audio.
//            mPlayer.setDataSource(fileName);
//            mPlayer.prepare();
//            mPlayer.start();
//        } catch (IOException e) {
//            Log.e("LOG_TAG", "prepare() failed");
//        }
//        //making the imageview pause button
//        imageViewPlay.setImageResource(R.drawable.ic_stop_black_24dp);
//
//        seekBar.setProgress(lastProgress);
//        mPlayer.seekTo(lastProgress);
//        seekBar.setMax(mPlayer.getDuration());
//        seekUpdation();
//        chronometer.start();
//
//        /** once the audio is complete, timer is stopped here**/
//        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                imageViewPlay.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
//                isPlaying = false;
//
//            }
//        });
//
//        /** moving the track as per the seekBar's position**/
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if( mPlayer!=null && fromUser ){
//                    //here the track's progress is being changed as per the progress bar
//                    mPlayer.seekTo(progress);
//                    //timer is being updated as per the progress of the seekbar
//                    chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
//                    lastProgress = progress;
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//    }
//
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            seekUpdation();
//        }
//    };
//
//    private void seekUpdation() {
//        if(mPlayer != null){
//            int mCurrentPosition = mPlayer.getCurrentPosition() ;
//            seekBar.setProgress(mCurrentPosition);
//            lastProgress = mCurrentPosition;
//        }
//        mHandler.postDelayed(runnable, 100);
//


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}