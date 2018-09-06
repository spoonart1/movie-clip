package com.spoonart.movieclip.feature.main.item

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.spoonart.movieclip.R
import com.spoonart.movieclip.extension.wrapPathUrl
import com.spoonart.movieclip.model.movie.BaseMovieResult
import com.spoonart.movieclip.util.DBUtil

class MovieAdapter(var items: ArrayList<BaseMovieResult>, var favoriteByDefault: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listener: OnMovieClickListener? = null

    fun setFavoriteCheckedByDefault() {
        for (i in items.indices) {
            items[i].isFavorite = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_movie, parent, false)
        return ViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as ViewHolder
        vh.bind(items[position], position)
    }

    fun replaceAll(items: ArrayList<BaseMovieResult>) {
        this.items = items
        if (favoriteByDefault) {
            setFavoriteCheckedByDefault()
        }
        notifyDataSetChanged()
    }

    fun addNewCollection(items: ArrayList<BaseMovieResult>) {
        this.items.addAll(items)
        if (favoriteByDefault) {
            setFavoriteCheckedByDefault()
        }
        notifyDataSetChanged()
    }

    fun setFavorite(index: Int, favorite: Boolean) {
        this.items[index].isFavorite = favorite
        notifyItemChanged(index)
    }

    internal class ViewHolder(itemView: View, val listener: OnMovieClickListener?) : RecyclerView.ViewHolder(itemView) {
        var ivMovie: ImageView
        var btnFav: RadioButton
        var tvTitle: TextView
        var tvDesc: TextView

        init {
            ivMovie = itemView.findViewById(R.id.ivMovie)
            btnFav = itemView.findViewById(R.id.btn_fav)
            tvTitle = itemView.findViewById(R.id.tv_title)
            tvDesc = itemView.findViewById(R.id.tv_desc)
        }

        fun bind(item: BaseMovieResult, position: Int) {

            item.posterPath?.let {
                Glide.with(itemView)
                        .applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.no_image))
                        .load(wrapPathUrl(it))
                        .into(ivMovie)
            }

            tvTitle.setText(item.title)
            tvDesc.setText(DBUtil.getGenreNameById(item.genreIds))
            btnFav.isChecked = item.isFavorite

            itemView.setOnClickListener {
                listener?.onClick(itemView, item, position)
            }

            btnFav.setOnClickListener {
                listener?.onFavBtnClicked(item, position)
            }
        }
    }

    interface OnMovieClickListener {
        fun onClick(view: View, item: BaseMovieResult, position: Int)
        fun onFavBtnClicked(item: BaseMovieResult, position: Int)
    }
}