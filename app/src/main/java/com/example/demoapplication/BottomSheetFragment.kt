package com.example.demoapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
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
import kotlinx.android.synthetic.main.performance_parent_listitem.view.*

/**
 * Created by Manish Verma on 17,Jul,2019
 * Girnarsoft Pvt. Ltd.,
 * Delhi NCR, India
 */
class BottomSheetFragment : BottomSheetDialogFragment() {

    companion object{
        val KEY_VIDEO = "VIDEO"
        val KEY_NAME = "NAME"
        fun getBottomSheetInstance(videoId:String,placeName:String):BottomSheetFragment{
            var bottomsheetInstance = BottomSheetFragment()
            var bundle = Bundle()
            bundle.putString(KEY_VIDEO,videoId)
            bundle.putString(KEY_NAME,placeName)
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
       var name: String? = arguments?.getString(KEY_NAME)
       view.tvInstruction.text = "$name : Click below to play video."
       videoView.initPlayer(PreferenceUtils.YOUTUBE_KEY,videoId!!,"https://cdn.rawgit.com/flipkart-incubator/inline-youtube-view/60bae1a1/youtube-android/youtube_iframe_player.html",
            YouTubePlayerType.WEB_VIEW,object : YouTubeEventListener {
                override fun onSeekTo(p0: Int, p1: Int) {
                }

                override fun onNativeNotSupported() {
                }

                override fun onReady() {
                    Log.d("TAG","ready to play ")
                    view.btnPlay.visibility = View.GONE

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
           var imageViewPoster : ImageView = imageView
           imageViewPoster.scaleType = ImageView.ScaleType.CENTER_INSIDE

           Glide
               .with(activity!!)
               .load(url)
               .centerCrop()
               .placeholder(R.drawable.ic_thumnail)
               .into(imageViewPoster)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}