package com.example.demoapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.performance_parent_listitem.view.*
import android.widget.ImageView
import com.bumptech.glide.Glide


/**
 * Created by Manish Verma on 12,Jul,2019
 * Delhi NCR, India
 */
class PerformanceAdapter(var mContext:Context,var itemList:MutableList<PlaceModel.Place>,val itemClick: (PlaceModel.Place) -> Unit) : RecyclerView.Adapter<PerformanceAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindTasks(itemList[position],mContext)
        holder.itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.performance_parent_listitem, parent, false)
        return  ViewHolder(view,itemClick)
    }

    override fun getItemCount(): Int {
        return itemList?.size
    }


    class ViewHolder(itemview: View,private val itemClick: (PlaceModel.Place) -> Unit) : RecyclerView.ViewHolder(itemview) {

        fun bindTasks(mPerformanceModel: PlaceModel.Place,mContext: Context) = with(mPerformanceModel) {
            itemView.tvPlaceName?.text = mPerformanceModel.placeName
            itemView.tvPlaceDes?.text = mPerformanceModel.placeDescription
            var imageView : ImageView = itemView.profile_image

            Glide
                .with(mContext)
                .load(mPerformanceModel.placeUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_thumnail)
                .into(imageView)


            itemView.setOnClickListener { itemClick(this) }
        }
    }
}