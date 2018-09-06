package com.spoonart.movieclip.feature.main.item

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spoonart.libs.base.BaseFragment
import com.spoonart.movieclip.R
import com.spoonart.movieclip.custom.OnVerticalScrollListener
import com.spoonart.movieclip.extension.setRefresh
import com.spoonart.movieclip.feature.detail.DetailActivity
import com.spoonart.movieclip.model.movie.BaseMovieResult
import com.spoonart.movieclip.model.movie.MovieItem

class MovieFragment() : BaseFragment<MoviePresenter>(), MovieContract.MovieView, MovieAdapter.OnMovieClickListener {

    private var contentView: View? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var type: MovieType
    private lateinit var adapter: MovieAdapter

    private var items: ArrayList<BaseMovieResult> = arrayListOf()
    private var currentPage = 1

    companion object {
        fun make(type: MovieType): MovieFragment {
            val f = MovieFragment()
            f.type = type
            return f
        }
    }

    override fun attachPresenter(): MoviePresenter {
        return MoviePresenter(this.context!!, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = inflater.inflate(R.layout.fragment_movie, null)
        onViewInited()
        return contentView!!
    }

    override fun onViewInited() {
        swipeRefreshLayout = contentView!!.findViewById(R.id.swipeLayout)
        recyclerView = contentView!!.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this.context, 2)

        adapter = MovieAdapter(items, type == MovieType.Favorite)
        adapter.listener = this
        recyclerView.adapter = adapter

        presenter?.loadMovie(currentPage, type)
        setOnScrollEnded()
        setOnSwipeRefreshListener()
    }

    private fun setOnScrollEnded() {

        recyclerView.addOnScrollListener(object : OnVerticalScrollListener() {
            override fun onScrolledToBottom() {
                currentPage++
                presenter?.loadMovieMore(currentPage, type)
            }

            override fun onScrolledToTop() {

            }
        })
    }

    private fun setOnSwipeRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener {
            onReload()
        }
    }

    override fun onResume() {
        super.onResume()
        onReload()
    }

    override fun taskDidBegin() {
        swipeRefreshLayout.setRefresh(true)
    }

    override fun taskDidFinish() {
        swipeRefreshLayout.setRefresh(false)
    }

    override fun onMovieLoaded(movies: MovieItem) {
        recyclerView.recycledViewPool.clear()
        adapter.replaceAll(movies.results)
    }

    override fun onMovieFavorited(message: String) {
        showToast(message)

        if (type == MovieType.Favorite) {
            onReload()
        }
    }

    override fun onMovieLoadedMore(currentPage: Int, totalPage: Int, movies: MovieItem) {
        if (this.currentPage >= totalPage) {
            this.currentPage = totalPage
        }
        adapter.addNewCollection(movies.results)
    }

    override fun onReload() {
        presenter?.loadMovie(currentPage, type)
    }

    override fun onClick(item: BaseMovieResult, position: Int) {
        Intent(this.activity, DetailActivity::class.java).apply {
            putExtra("movie_id", item.id)
            startActivity(this)
        }
    }

    override fun onFavBtnClicked(item: BaseMovieResult, position: Int) {
        val mark = !item.isFavorite
        adapter.setFavorite(position, mark)
        presenter?.setFavoriteMovie(item.id, mark)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.disposeAllRequest()
    }

}

enum class MovieType {
    Popular, TopRated, Favorite
}