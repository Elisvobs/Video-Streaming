package com.byskopo.byskopoyako;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private PlayerView exoplayerView;
    private SimpleExoPlayer mExoPlayer;
    private View mView;
    private Context mContext;
    private boolean mExoPlayerFullscreen = false;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mContext = mView.getContext();
    }

//    public void setVideo(final Context ctx, String title, final String url) {
    public void setVideo(final Context ctx, String title, final String url) {
        TextView videoTitle, videoYear, videoLength;
        videoTitle = mView.findViewById(R.id.videoTitle);
//        videoYear = mView.findViewById(R.id.videoYear);
//        videoLength = mView.findViewById(R.id.videoLength);
        exoplayerView = mView.findViewById(R.id.videoView);

        videoTitle.setText(title);
//        videoYear.setText(year);
//        videoLength.setText(length);
//        if (mExoPlayer != null) {
            try {
//                LoadControl loadControl = new DefaultLoadControl(
//                        new DefaultAllocator(true, 16),
//                        VideoPlayerConfig.MIN_BUFFER_DURATION,
//                        VideoPlayerConfig.MAX_BUFFER_DURATION,
//                        VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
//                        VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER,
//                        -1, true);
                LoadControl loadControl = new DefaultLoadControl();

                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(ctx).build();
                TrackSelection.Factory videoTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector =
                        new DefaultTrackSelector(videoTrackSelectionFactory);

//                TrackSelector trackSelector =
//                        new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

//                mExoPlayer = ExoPlayerFactory.newSimpleInstance(
//                        new DefaultRenderersFactory(this), trackSelector, loadControl);

                mExoPlayer = ExoPlayerFactory.newSimpleInstance(ctx);

                Uri video = Uri.parse(url);

                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(
                        video, dataSourceFactory, extractorsFactory,
                        null, null
                );

                exoplayerView.setPlayer(mExoPlayer);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(false);
//            mExoPlayer.setPlaybackParameters();
//                exoplayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
//                mExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
//            exoplayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            } catch (Exception e) {
                Log.e("Viewholder", "exoplayer error" + e.toString());
            }
//        }
    }

}