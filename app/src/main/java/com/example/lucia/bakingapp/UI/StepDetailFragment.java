package com.example.lucia.bakingapp.UI;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lucia.bakingapp.R;
import com.example.lucia.bakingapp.data.Recipe;
import com.example.lucia.bakingapp.data.Step;
import com.example.lucia.bakingapp.utils.Constants;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class StepDetailFragment extends Fragment implements Player.EventListener {

    private static final String TAG = StepDetailFragment.class.getSimpleName();

    // ButterKnife View binding
    @BindView(R.id.text_view_step_description)
    TextView textViewStepDesc;
    @BindView(R.id.relative_layout_player)
    RelativeLayout mRlayoutPlayer;
    @BindView(R.id.playerview_recipe_video)
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.imageview_no_media)
    ImageView imageViewNoMedia;
    // ButterKnife Resource binding
    @BindDrawable(R.drawable.no_image)
    Drawable recipeDefaultImage;


    private Unbinder unbinder;
    private Context mContext;
    private Recipe selectedRecipe;
    private Step selectedStep;
    private String stepDescription;
    private String videoUrl;
    private String imageUrl;
    private int index;
    private int stepCount;
    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private BandwidthMeter bandwidthMeter;
    private TrackSelector trackSelector;
    private long playerPosition;

    public StepDetailFragment() {
    }

    /**
     * Override this method to get the context method as the fragment is used by multiple activities
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ArrayList<Recipe> recipes = getArguments().getParcelableArrayList(Constants.SELECTED_RECIPE);

            if (recipes != null) {
                selectedRecipe = recipes.get(0);
                index = getArguments().getInt(Constants.SELECTED_STEP);
                stepCount = getArguments().getInt(Constants.STEP_COUNT);

                selectedStep = selectedRecipe.getSteps().get(index);
                stepDescription = selectedStep.getStepDescription();
                videoUrl = selectedStep.getStepVideoURL();
                imageUrl = selectedStep.getStepThumbnailURL();
            }
        }

        if (savedInstanceState != null) {
            playerPosition = savedInstanceState.getLong(Constants.STATE_PLAYER_POSITION);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);


        if (savedInstanceState == null) {
            // Create exoplayer to show recipe video
            createMediaPlayer();
        }

        textViewStepDesc.setText(stepDescription);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(Constants.STATE_PLAYER_POSITION, playerPosition);
    }

    /**
     * Method to create ExoPlayer instance, and attach media to it
     */
    public void createMediaPlayer() {

        if (videoUrl != null && !videoUrl.isEmpty()) {
            // hide the overlay default image
            imageViewNoMedia.setVisibility(View.GONE);

            initializeMediaSession();
            initializePlayer(Uri.parse(videoUrl));

        } else {
            // show the recipe image as no video found for the step
            // if no recipe image is found, display a default image
            imageViewNoMedia.setVisibility(View.VISIBLE);

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.with(mContext)
                        .load(imageUrl)
                        .placeholder(recipeDefaultImage)
                        .error(recipeDefaultImage)
                        .into(imageViewNoMedia);
            } else {
                imageViewNoMedia.setImageDrawable(recipeDefaultImage);
            }
        }
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat
        mediaSession = new MediaSessionCompat(mContext, TAG);

        // Enable callbacks from MediaButtons and TransportControls
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Stop MediaButtons from restarting the player when the app is not visible
        mediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());

        // MediaSessionCallback has methods that handle callbacks from a media controller
        mediaSession.setCallback(new MediaSessionCallback());

        // Start the Media Session since the activity is active.
        mediaSession.setActive(true);

    }

    /**
     * Initialize the player
     *
     * @param mediaUri - uri of the media to be played
     */
    private void initializePlayer(Uri mediaUri) {

        if (exoPlayer == null) {

            // Create the player instance
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player
            exoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

            // Attach player to the view
            exoPlayerView.setPlayer(exoPlayer);


            //------------------------
            // Prepare the player
            //------------------------

            //Measures bandwidth during playback. Can be null if not required.
            bandwidthMeter = new DefaultBandwidthMeter();

            // Create datasource instance through which media is loaded
            String userAgent = Util.getUserAgent(mContext, TAG);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext, userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            exoPlayer.prepare(mediaSource);

            exoPlayer.setPlayWhenReady(true);
            exoPlayer.seekTo(playerPosition);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    @Override
    public void onSeekProcessed() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) playerPosition = exoPlayer.getCurrentPosition();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || exoPlayer == null)) {
            createMediaPlayer();
        }
    }

    /**
     * Release the player, and deactivate media session
     */
    private void releasePlayer() {
        if (exoPlayer != null) {
            playerPosition = exoPlayer.getCurrentPosition();
        }

        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

        if (mediaSession != null) {
            mediaSession.setActive(false);
        }

        if (trackSelector != null) {
            trackSelector = null;
        }
    }

    /**
     * Media Session Callbacks which enables all external clients to control the player
     */
    private class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            super.onPlay();
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            exoPlayer.seekTo(0);
        }
    }
}

