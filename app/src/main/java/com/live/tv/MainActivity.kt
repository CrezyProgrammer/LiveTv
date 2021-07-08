package com.live.tv

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    // creating a variable for exoplayer
    var exoPlayer: SimpleExoPlayer? = null

    // url of video which we are loading.
    var videoURL =
        ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main)
videoURL= intent.getStringExtra("link").toString()




            // bandwisthmeter is used for
            // getting default bandwidth
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()

            // track selector is used to navigate between
            // video using a default seekbar.
            val trackSelector: TrackSelector =
                DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))

            // we are adding our track selector to exoplayer.
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

            // we are parsing a video url
            // and parsing its video uri.
            val videouri = Uri.parse(videoURL)


            val mediaSource: MediaSource =
               HlsMediaSource.Factory(DefaultHttpDataSourceFactory("userAgent"))
                    .createMediaSource(videouri)
            // inside our exoplayer view
            // we are setting our player
            exoPlayerView!!.player = exoPlayer

            // we are preparing our exoplayer
            // with media source.
            exoPlayer!!.prepare(mediaSource)

            // we are setting our exoplayer
            // when it is ready.
            exoPlayer!!.playWhenReady = true
        exoPlayer!!.addListener(object : Player.EventListener {
            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {
                Log.d("SAMPLE", "TrackGroupArray size=${trackGroups?.length}")
                Log.d("SAMPLE", "TrackSelectionArray size=${trackSelections?.length}")
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                error?.let {
                    Log.e("123321", it.message, it)
                    exoPlayer?.retry()
                }
            }

            override fun onLoadingChanged(isLoading: Boolean) {
                Log.d("SAMPLE", "Loading = $isLoading")
            }

            override fun onPositionDiscontinuity(reason: Int) {
                // noop
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                // noop
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                // noop
            }

            override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {
                // noop
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                // noop
            }

            override fun onSeekProcessed() {
                // noop
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                // noop
            }

        })


    }
    private fun pausePlayer() {
        exoPlayer!!.setPlayWhenReady(false)
        exoPlayer!!.getPlaybackState()
    }

    private fun startPlayer() {
        exoPlayer!!.setPlayWhenReady(true)
        exoPlayer!!.playbackState
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onResume() {
        super.onResume()
        startPlayer()
    }
}