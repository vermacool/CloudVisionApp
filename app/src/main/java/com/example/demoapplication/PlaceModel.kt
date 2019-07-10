package com.example.demoapplication


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by Manish Verma on 08,Jul,2019
 * Girnarsoft Pvt. Ltd.,
 * Delhi NCR, India
 */

class PlaceModel : Serializable{

    @SerializedName("places")
    @Expose
    var places: List<Place>? = null
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    inner class Place {
        @SerializedName("place_id")
        @Expose
        var placeId: String? = null
        @SerializedName("place_url")
        @Expose
        var placeUrl: String? = null
        @SerializedName("latlong")
        @Expose
        var latlong: String? = null
        @SerializedName("video_url")
        @Expose
        var videoUrl: String? = null

    }

}