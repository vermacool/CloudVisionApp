package com.example.demoapplication

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.util.Log
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import java.util.ArrayList


private const val ACTION_FOO = "com.example.demoapplication.action.FOO"
private const val ACTION_BAZ = "com.example.demoapplication.action.BAZ"

private const val EXTRA_PARAM1 = "com.example.demoapplication.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.example.demoapplication.extra.PARAM2"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * helper methods.
 */
class PlaceUpdateService : IntentService("PlaceUpdateService") {

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_FOO -> {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionFoo()
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFoo() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://vermacool.github.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiServices = retrofit.create(ApiServices::class.java)
        var syncPlace:Call<PlaceModel> = apiServices.listRepos()
        syncPlace.enqueue(object : Callback<PlaceModel>{
            override fun onFailure(call: Call<PlaceModel>, t: Throwable) {
                Log.d("TAG","Response:  "+ t.localizedMessage)
                Toast.makeText(applicationContext,"Failed to update places", Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<PlaceModel>, response: Response<PlaceModel>) {
                if(response.isSuccessful){

                var placeModel = response.body()
                 Log.d("TAG","Response:  "+ placeModel?.status)

                 if(placeModel !=null)
                        PreferenceUtils.clearPref()
                        PreferenceUtils.setPlacesPref(placeModel)
                        Toast.makeText(applicationContext,"Places updated", Toast.LENGTH_LONG).show()

                }else{
                    Toast.makeText(applicationContext,"Couldn't update places", Toast.LENGTH_LONG).show()

                    Log.d("TAG","Response:  "+ response.message())

                }

            }
        })
    }



    companion object {
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionFoo(context: Context, param1: String, param2: String) {
            val intent = Intent(context, PlaceUpdateService::class.java).apply {
                action = ACTION_FOO
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
        }
    }
}
