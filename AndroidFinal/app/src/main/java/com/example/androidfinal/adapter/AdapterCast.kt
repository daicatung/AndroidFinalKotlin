package com.example.androidfinal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidfinal.R
import com.example.androidfinal.model.Cast


class AdapterCast(private val mListCast: List<Cast>) : RecyclerView.Adapter<AdapterCast.AdapterCastViewHolder>() {

    private val mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cast, parent, false)
        return AdapterCastViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterCastViewHolder, position: Int) {
        val cast = mListCast[position]
        holder.setData(cast)
    }

    override fun getItemCount(): Int {
        return mListCast.size
    }

    class AdapterCastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mImgCast: ImageView = itemView.findViewById(R.id.item_cast_img)
        private val mTvNameCast: TextView = itemView.findViewById(R.id.item_cast_tv_name)
        fun setData(cast: Cast) {
            Glide.with(mImgCast).load(cast.imgCast).into(mImgCast)
            mTvNameCast.text = cast.nameCast
        }
    }
}