package com.byskopo.byskopoyako;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class AltActivity extends AppCompatActivity {
    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private PlayerView mExoPlayerView;
    private MediaSource mVideoSource;
//    private HlsMediaSource mVideoSource;
    private boolean mExoPlayerFullscreen = false;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;

    private int mResumeWindow;
    private long mResumePosition;
    private SimpleExoPlayer mPlayer;
    private PlaybackControlView mControlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alt);

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }

        mExoPlayerView = findViewById(R.id.exoplayer);
        mControlView = mExoPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = mControlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = mControlView.findViewById(R.id.exo_fullscreen_button);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
        super.onSaveInstanceState(outState);
    }

    private void initFullscreenDialog() {
        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {
        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(
                this, R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {
        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(
                ContextCompat.getDrawable(
                        this, R.drawable.ic_fullscreen_expand)
        );
    }

    private void initFullscreenButton() {
        mFullScreenButton.setOnClickListener(v -> {
            if (!mExoPlayerFullscreen)
                openFullscreenDialog();
            else
                closeFullscreenDialog();
        });
    }

    private void initExoPlayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(this).build();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(
                videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();

//        mPlayer = ExoPlayerFactory.newSimpleInstance(this);
        mPlayer = new SimpleExoPlayer.Builder(this).build();
        mExoPlayerView.setPlayer(mPlayer);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            mPlayer.seekTo(mResumeWindow, mResumePosition);
        }

        mExoPlayerView.setPlayer(mPlayer);
        mPlayer.prepare(mVideoSource);
        mPlayer.setPlayWhenReady(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mExoPlayerView == null) {
            initFullscreenDialog();
            initFullscreenButton();

            String streamUrl = "";
            String userAgent = Util.getUserAgent(this,
                    getApplicationContext().getApplicationInfo().packageName);
            DefaultHttpDataSourceFactory httpDataSourceFactory =
                    new DefaultHttpDataSourceFactory(userAgent, null,
                            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                            true);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                    this, null, httpDataSourceFactory);
            Uri daUri = Uri.parse(streamUrl);
//            mVideoSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(daUri);
//            mVideoSource = new ExtractorMediaSource(
//                    daUri, dataSourceFactory, 1,
//                    null, null);
        }

        initExoPlayer();

        if (mExoPlayerFullscreen) {
            ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
            mFullScreenDialog.addContentView(
                    mExoPlayerView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
            );
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(
                    this, R.drawable.ic_fullscreen_skrink));
            mFullScreenDialog.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mExoPlayerView != null && mExoPlayerView.getPlayer() != null) {
            mResumeWindow = mExoPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, mExoPlayerView.getPlayer().getContentPosition());
            mExoPlayerView.getPlayer().release();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }

}