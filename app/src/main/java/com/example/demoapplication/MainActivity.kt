package com.example.demoapplication

import android.content.Intent
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import mumayank.com.airlocationlibrary.AirLocation

class MainActivity : AppCompatActivity() {
    private var airLocation: AirLocation? = null // ADD THIS LINE ON TOP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        var preferenceUtils = PreferenceUtils(this@MainActivity)

        lifecycle.addObserver(youtube_player_view)
        youtube_player_view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {

                val videoId = "bCXVsfDEsNQ"
               youTubePlayer.loadVideo(videoId, 0f)
            }
        })
        fab.setOnClickListener { view ->
            airLocation = AirLocation(this, true, true, object : AirLocation.Callbacks {
                override fun onSuccess(location: Location) {

                    // location fetched successfully, proceed with it
                    Log.d("TAG", "Coordinates: lat " + location.latitude + " lang " + location.longitude)
                    CompareLocation().execute(location)

                }

                override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                    // couldn't fetch location due to reason available in locationFailedEnum
                    // you may optionally do something to inform the user, even though the reason may be obvious
                    Toast.makeText(applicationContext, "" + locationFailedEnum.name, Toast.LENGTH_LONG).show()

                }

            })
        }


    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        airLocation?.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        ) // ADD THIS LINE INSIDE onRequestPermissionResult
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        airLocation?.onActivityResult(requestCode, resultCode, data) // ADD THIS LINE INSIDE onActivityResult
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_sync -> {
                PlaceUpdateService.startActionFoo(this@MainActivity, "", "")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Compare distance if it is under defined radius play video.
     */

    public class CompareLocation : AsyncTask<Location, Void, Int>() {
        var compareResult: Int = 0
        override fun doInBackground(vararg currentLocation: Location?): Int {
            var place: PlaceModel? = PreferenceUtils.getPlaceData()
            var placeList = place?.places
            if (placeList != null) {
                for (destinationPlace in placeList) {
                    Log.d("TAG", "" + destinationPlace.placeId)

                    var destinationLocation = Location("destination")
                    destinationLocation.latitude = (destinationPlace.latlong?.split(",")!![0]).toDouble()
                    destinationLocation.longitude = (destinationPlace.latlong?.split(",")!![1]).toDouble()

                    var distance = currentLocation[0]?.distanceTo(destinationLocation)
                    Log.d("TAG","Distance: "+distance)
                    if (distance != null && distance.compareTo(30.0) <= 0) {
                        compareResult = 1
                    }
                }
            }
            return compareResult
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            if (result!=null && result == 1){
                Log.d("TAG","Location under radius")


            }
        }
    }
}
