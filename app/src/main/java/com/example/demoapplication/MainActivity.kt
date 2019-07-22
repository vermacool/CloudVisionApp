package com.example.demoapplication

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import edu.arbelkilani.compass.CompassListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import mumayank.com.airlocationlibrary.AirLocation
import com.crashlytics.android.Crashlytics
import com.flipkart.youtubeview.YouTubePlayerView
import com.flipkart.youtubeview.listener.YouTubeEventListener
import com.flipkart.youtubeview.models.ImageLoader
import com.flipkart.youtubeview.models.YouTubePlayerType
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import io.fabric.sdk.android.Fabric
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity() {
    private var airLocation: AirLocation? = null // ADD THIS LINE ON TOP
    private var TAG:String = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        compass_1.setListener(object : CompassListener{
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//                Log.d(TAG, "onAccuracyChanged : sensor : " + sensor);
//                Log.d(TAG, "onAccuracyChanged : accuracy : " + accuracy);
            }

            override fun onSensorChanged(event: SensorEvent?) {
               // Log.d(TAG, "onSensorChanged : " + event)
            }
        })

       // lifecycle.addObserver(youtube_player_view)

        button.setOnClickListener {
            airLocation = AirLocation(this, true, true, object : AirLocation.Callbacks {
                override fun onSuccess(location: Location) {

                    // location fetched successfully, proceed with it
                    Log.d("TAG", "Coordinates: lat " + location.latitude + " lang " + location.longitude)
                    CompareLocation(this@MainActivity).execute(location)

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

    public class CompareLocation constructor(activity: MainActivity) : AsyncTask<Location, Void, PlaceModel.Place>() {
        var compareResult: PlaceModel.Place ? = null
        private var mParentActivity: WeakReference<MainActivity>? = WeakReference<MainActivity>(activity)

        override fun doInBackground(vararg currentLocation: Location?): PlaceModel.Place? {
            var place: PlaceModel? = PreferenceUtils.getPlaceData()
            var placeList = place?.places
            if (placeList != null && placeList.isNotEmpty()) {
                for (destinationPlace in placeList) {
                    Log.d("TAG", "" + destinationPlace.placeId)

                    var destinationLocation = Location("destination")
                    destinationLocation.latitude = (destinationPlace.latlong?.split(",")!![0]).toDouble()
                    destinationLocation.longitude = (destinationPlace.latlong?.split(",")!![1]).toDouble()

                    var distance = currentLocation[0]?.distanceTo(destinationLocation)
                    Log.d("TAG","Distance: "+distance)
                    if (distance != null && distance.compareTo(5000.0) <= 0) {
                       compareResult = destinationPlace
                        break
                    }

                }
            }
            return compareResult
        }


        override fun onPostExecute(result: PlaceModel.Place?) {
            super.onPostExecute(result)
            if (mParentActivity?.get() != null && result?.videoUrl!=null) {

                // the WeakReference is still valid and hasn't been reclaimed  by the GC
                val parentActivity: MainActivity? = mParentActivity!!.get()

                val videoId:String? = result?.videoUrl.toString()
                var bottomsheetInstance = BottomSheetFragment.getBottomSheetInstance(videoId!!)
                bottomsheetInstance.show(parentActivity?.supportFragmentManager,"")
            }else {
                Toast.makeText(mParentActivity?.get()?.applicationContext,"Couldn't match target.",Toast.LENGTH_LONG).show()

            }

        }
    }


    override fun onStop() {
        super.onStop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
