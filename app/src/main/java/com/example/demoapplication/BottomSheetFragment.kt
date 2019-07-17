package com.example.demoapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.flipkart.youtubeview.YouTubePlayerView
import com.flipkart.youtubeview.fragment.YouTubeBaseFragment
import com.flipkart.youtubeview.listener.YouTubeEventListener
import com.flipkart.youtubeview.models.ImageLoader
import com.flipkart.youtubeview.models.YouTubePlayerType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_dialog.view.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_dialog.view.tvInstruction

/**
 * Created by Manish Verma on 17,Jul,2019
 * Girnarsoft Pvt. Ltd.,
 * Delhi NCR, India
 */
class BottomSheetFragment : BottomSheetDialogFragment() {

    companion object{
        val KEY_VIDEO = "VIDEO"
        fun getBottomSheetInstance(videoId:String):BottomSheetFragment{
            var bottomsheetInstance = BottomSheetFragment()
            var bundle = Bundle()
            bundle.putString(KEY_VIDEO,videoId)
            bottomsheetInstance.arguments = bundle
            return bottomsheetInstance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
       var view:View = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false)
        view.tvExit.setOnClickListener { dismiss() }
       var videoView = view.youtube_player_view
       var videoId: String? = arguments?.getString(KEY_VIDEO)
       videoView.setOnClickListener { tvInstruction.visibility= View.GONE }
       videoView.initPlayer(PreferenceUtils.YOUTUBE_KEY,videoId!!,"https://cdn.rawgit.com/flipkart-incubator/inline-youtube-view/60bae1a1/youtube-android/youtube_iframe_player.html",
            YouTubePlayerType.WEB_VIEW,object : YouTubeEventListener {
                override fun onSeekTo(p0: Int, p1: Int) {
                }

                override fun onNativeNotSupported() {
                }

                override fun onReady() {
                    Log.d("TAG","ready to play ")

                }


                override fun onPause(p0: Int) {
                }

                override fun onBuffering(p0: Int, p1: Boolean) {
                }

                override fun onCued() {
                    Log.d("TAG","Cued ")

                }

                override fun onInitializationFailure(p0: String?) {
                    Log.d("TAG","Failure: "+p0)

                }

                override fun onPlay(p0: Int) {
                }

                override fun onStop(p0: Int, p1: Int) {
                    Log.d("TAG"," Stopped: "+p0+" : "+p1 )

                }
            },this
        ) { imageView, url, height, width ->
            Log.d("TAG",url +" : "+height+" : "+width)
            Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.ic_thumnail)
                .error(R.drawable.ic_youtube)
                .resize(width, height)
                .centerCrop()
                .into(imageView)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}