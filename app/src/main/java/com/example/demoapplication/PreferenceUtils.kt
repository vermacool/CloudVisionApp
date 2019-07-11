package com.example.demoapplication

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson



/**
 * Created by Manish Verma on 08,Jul,2019
 * Girnarsoft Pvt. Ltd.,
 * Delhi NCR, India
 */
class PreferenceUtils(context: Context) {
    val PREF_NAME = "PLACE_PREFERENCE"
    init {
        preference =  context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    }

   companion object {
       var preference :SharedPreferences? = null
       val PREF_PLACE:String = "place_list_key"
       val LAUNCH_TIME_KEY ="launch_count"

       fun setPlacesPref(places:PlaceModel) {
           var editorData = preference?.edit()
           val gson = Gson()
           val placeObjJSON = gson.toJson(places)
           editorData?.putString(PREF_PLACE, placeObjJSON)
           editorData?.apply()
       }

       fun getPlaceData (): PlaceModel?{
           val gson = Gson()
           val tzJSON = preference?.getString(PREF_PLACE, "")
           return gson.fromJson(tzJSON, PlaceModel::class.java)

       }

       fun setLaunchPref(isFirstTime: Boolean){
            var editorData = preference?.edit()
            editorData?.putBoolean(LAUNCH_TIME_KEY,isFirstTime)
            editorData?.apply()
       }

       fun getLaunchCount():Boolean?{
           return preference?.getBoolean(LAUNCH_TIME_KEY,true)!!
       }

   }



}