package com.spoonart.movieclip.feature.detail

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.spoonart.libs.base.BaseActivity
import com.spoonart.movieclip.R
import com.spoonart.movieclip.custom.pager.CommonFragmentViewModel
import com.spoonart.movieclip.custom.pager.CommonTabPagerAdapter
import com.spoonart.movieclip.extension.wrapPathUrl
import com.spoonart.movieclip.feature.detail.videoview.VideoItemFragment
import com.spoonart.movieclip.model.movie.MovieDetail
import com.spoonart.movieclip.model.movie.MovieTrailer
import com.spoonart.movieclip.model.movie.SpokenLanguage
import com.spoonart.movieclip.model.movie.TrailerResult
import com.spoonart.movieclip.util.DateUtil
import me.relex.circleindicator.CircleIndicator
import kotlin.math.abs

class DetailActivity : BaseActivity<DetailPresenter>(), DetailContract.View {

    private lateinit var rootLayout: View
    private lateinit var ivBackdrop: ImageView
    private lateinit var ivPoster: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvRate: TextView
    private lateinit var tvVotes: TextView
    private lateinit var tvCalendar: TextView
    private lateinit var tvLang: TextView
    private lateinit var tvOverview: TextView
    private lateinit var viewPager: ViewPager
    private lateinit var indicator: CircleIndicator
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout

    private var movieDetail: MovieDetail? = null

    private var responsePattern = "yyyy-MM-dd"
    private var desiredPattern = "yyyy MMMM dd"

    override fun attachPresenter(): DetailPresenter {
        return DetailPresenter(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        onViewInited()
        loadResource()
    }

    override fun onViewInited() {
        setupToolbar(R.id.toolbar, R.color.colorPrimary)
        setDisplayHome(true)

        rootLayout = findViewById(R.id.root_layout)
        ivBackdrop = findViewById(R.id.backdrop)
        ivPoster = findViewById(R.id.iv_poster)
        tvTitle = findViewById(R.id.tv_title)
        tvRate = findViewById(R.id.tv_rate)
        tvVotes = findViewById(R.id.tv_votes)
        tvCalendar = findViewById(R.id.tv_date)
        tvLang = findViewById(R.id.tv_lang)
        tvOverview = findViewById(R.id.tv_overview)
        viewPager = findViewById(R.id.view_pager)
        indicator = findViewById(R.id.indicator)
        appBarLayout = findViewById(R.id.app_bar_layout)
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar)

        collapsingToolbarLayout.title = " "
        setAppBarLayoutListener()
    }

    private fun loadResource() {
        intent.getIntExtra("movie_id", -1).let { movieId ->
            println("check movieId int extra")
            if (movieId == -1) {
                throw RuntimeException("`movie_id` from intent has not been set, please provide it before starting this activity")
            }

            presenter?.requestDetailMovie(movieId)
            presenter?.requestTrailer(movieId)
        }
    }

    private fun setAppBarLayoutListener() {
        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val currentOffset = abs(verticalOffset).toFloat()
            val opacity = 1 - (currentOffset / appBarLayout.totalScrollRange)
            ivPoster.alpha = opacity
            setToolbarTitle(opacity)
        }
    }

    private fun setToolbarTitle(opacity: Float) {
        if (opacity <= 0.0) {
            movieDetail?.let {
                collapsingToolbarLayout.title = it.title
            }
        } else {
            collapsingToolbarLayout.title = " "
        }
    }

    override fun onDetailLoaded(detail: MovieDetail) {
        this.movieDetail = detail

        setImage(detail.backdropPath, ivBackdrop)
        setImage(detail.posterPath, ivPoster)

        tvTitle.text = detail.title
        tvOverview.text = detail.overview
        tvLang.text = getLanguages(detail.spokenLanguages)
        tvCalendar.text = DateUtil.transformToNewDateStr(detail.releaseDate, responsePattern, desiredPattern)
        tvVotes.text = detail.voteCount.toString()
        tvRate.text = "${detail.voteAverage}/10"
    }

    private fun setImage(path: String?, target: ImageView) {
        path?.let {
            val url = wrapPathUrl(path)
            Glide.with(this)
                    .load(url)
                    .apply(RequestOptions().placeholder(R.drawable.no_image))
                    .into(target)
        }
    }

    private fun getLanguages(sources: List<SpokenLanguage>): String {
        var langs = ""
        sources.forEach {
            langs += "${it.iso639_1},".toUpperCase()
        }
        return langs.removeSuffix(",")
    }

    override fun onTrailerLoaded(trailer: MovieTrailer) {
        val adapter = CommonTabPagerAdapter.newInstance(supportFragmentManager, makeVideos(trailer.results))
        viewPager.adapter = adapter
        indicator.setViewPager(viewPager)
        adapter.registerDataSetObserver(indicator.dataSetObserver)
    }

    private fun makeVideos(resources: List<TrailerResult>): List<CommonFragmentViewModel> {
        val list = arrayListOf<CommonFragmentViewModel>()
        resources.forEach { trailer ->
            val fragment = VideoItemFragment.newInstance(trailer.name, trailer.key)
            val cfm = CommonFragmentViewModel(trailer.name, fragment)
            list.add(cfm)
        }
        return list
    }

    override fun taskDidBegin() {
        showProgressDialog()
    }

    override fun taskDidFinish() {
        dismissProgressDialog()
    }

    override fun taskDidError(message: String) {
        showAlert(getString(R.string.text_title_error), message, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                finish()
            }
        })
    }

    override fun onDestroy() {
        presenter?.disposeAllRequest()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mn_share -> {
                shareBody()
                return true
            }
            R.id.mn_page -> {
                openHomepage()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareBody() {
        movieDetail?.let {
            val body = "${it.title} has been released!!\nsee here: ${it.homePage}"
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.setType("text/plain")
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.text_share_title))
            sharingIntent.putExtra(Intent.EXTRA_TEXT, body)
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.text_share_via_title)))
        }
    }

    private fun openHomepage() {
        movieDetail?.let {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.homePage))
            startActivity(browserIntent)
        }
    }

}