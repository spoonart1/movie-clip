package com.spoonart.movieclip.feature.detail.videoview

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener
import com.spoonart.movieclip.R
import com.spoonart.movieclip.custom.SimpleYoutubeFullScreenListener


class VideoItemFragment : Fragment() {

    lateinit var videoKey: String
    lateinit var title: String
    lateinit var ytPlayerView: YouTubePlayerView

    companion object {
        fun newInstance(title: String, videoKey: String): VideoItemFragment {
            val f = VideoItemFragment()
            f.title = title
            f.videoKey = videoKey
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_video_item, null)
        ytPlayerView = contentView.findViewById(R.id.yt_player)
        lifecycle.addObserver(ytPlayerView)
        configPlayer()

        ytPlayerView.initialize(object : YouTubePlayerInitListener {
            override fun onInitSuccess(youTubePlayer: YouTubePlayer) {
                youTubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady() {
                        youTubePlayer.loadVideo(videoKey, 0.0f)
                        youTubePlayer.pause()
                    }
                })
            }

        }, true)

        ytPlayerView.addFullScreenListener(object : SimpleYoutubeFullScreenListener() {
            override fun onYouTubePlayerEnterFullScreen() {
                watchInYoutube()
            }
        })

        return contentView
    }

    private fun configPlayer() {
        val uiController = ytPlayerView.playerUIController
        uiController.setVideoTitle(title)
        uiController.showBufferingProgress(true)
    }


    private fun watchInYoutube() {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoKey"))
        val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=$videoKey"))
        try {
            startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(webIntent)
        }

    }
}