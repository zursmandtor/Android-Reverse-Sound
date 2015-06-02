package com.rudenko.android.reversesound;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rudenko.android.reversesound.threads.PlayerThread;
import com.rudenko.android.reversesound.threads.RecorderThread;



public class MainActivity extends ActionBarActivity {


    final String LOG_TAG = "myLogs";

    /**
     * Message for handler about main activities statement
     */
    public static final int STATUS_NONE = 0;

    /**
     * Message for handler about activities statement, when recording is in progress
     */
    public static final int STATUS_RECORDING = 1;

    /**
     * Message for handler about activities statement, when playing is in progress
     */
    public static final int STATUS_PLAYING = 2;

    /**
     * Message for handler about activities statement, when recording is finished
     */
    public static final int STATUS_RECORDING_END = 3;

    /**
     * Message for handler about activities statement, when playing is finished
     */
    public static final int STATUS_PLAYING_END = 4;

    /**
     * Message for handler about activities statement,
     * when application has not file to playing
     */
    public static final int STATUS_NONE_PLAY = 5;


    /**
     * Show operations name and status
     */
    TextView tvStatus;

    /**
     * Show operation progress
     */
    ProgressBar pb;

    /**
     * Button to start record
     */
    Button btnRecord;

    /**
     * Button to start play
     */
    Button btnPlay;

    /**
     * RecorderThread object
     */
    RecorderThread recorderThread;

    /**
     *  PlayerThread object
     */
    PlayerThread playerThread;


    /**
     * Show the progress of operations and statement of activities element
     */
    Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case STATUS_NONE:
                    btnRecord.setEnabled(true);
                    btnPlay.setEnabled(true);
                    tvStatus.setText("Select button, please)");
                    pb.setVisibility(View.GONE);
                    break;
                case STATUS_RECORDING:
                    btnRecord.setEnabled(false);
                    btnPlay.setEnabled(false);
                    tvStatus.setText("Recording...");
                    pb.setMax(msg.arg1);
                    pb.setProgress(msg.arg2);
                    pb.setVisibility(View.VISIBLE);

                    break;
                case STATUS_PLAYING:
                    btnRecord.setEnabled(false);
                    btnPlay.setEnabled(false);
                    tvStatus.setText("Playing...");
                    pb.setMax(msg.arg1);
                    pb.setProgress(msg.arg2);
                    pb.setVisibility(View.VISIBLE);
                    break;
                case STATUS_RECORDING_END:
                    tvStatus.setText("Recording complete!");
                    break;
                case STATUS_PLAYING_END:
                    tvStatus.setText("Playing complete!");
                    break;
                case STATUS_NONE_PLAY:
                    Toast.makeText(getBaseContext(), "File doesn't exist", Toast.LENGTH_SHORT).show();
                    break;
            }
        };
    };

    /**
     * Create activity
     * @param savedInstanceState - saveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = (TextView) findViewById(R.id.tvStatus);
        pb = (ProgressBar) findViewById(R.id.pb);
        btnRecord = (Button) findViewById(R.id.btnRecord);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        myHandler.sendEmptyMessage(STATUS_NONE);

    }

    /**
     * Starting thread to record sound
     * @param v - building block for interface components
     * @throws InterruptedException
     */
    public void onClickRecord(View v) throws InterruptedException {
        if(recorderThread != null && recorderThread.isAlive()) {
            recorderThread.interrupt();
        } else {
            recorderThread = new RecorderThread(myHandler);
            recorderThread.start();
        }

    }

    /**
     * Starting thread to play recorded sound
     * @param v - building block for interface components
     * @throws InterruptedException
     */
    public void onClickPlay(View v) throws InterruptedException {
        if(playerThread != null && playerThread.isAlive()) {
            playerThread.interrupt();
        } else {
            playerThread = new PlayerThread(myHandler);
            playerThread.start();
        }

    }
}