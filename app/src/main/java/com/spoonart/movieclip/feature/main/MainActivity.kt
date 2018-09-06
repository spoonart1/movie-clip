package com.spoonart.movieclip.feature.main

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.spoonart.libs.base.BaseActivity
import com.spoonart.movieclip.R
import com.spoonart.movieclip.custom.pager.CommonFragmentViewModel
import com.spoonart.movieclip.custom.pager.CommonTabPagerAdapter

class MainActivity : BaseActivity<MainPresenter>(), MainContract.View {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun attachPresenter(): MainPresenter {
        return MainPresenter(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onViewInited()
        fetchRequest()
    }

    private fun fetchRequest() {
        presenter?.requestGenre()
        presenter?.requestSessionId({ sessionId ->
            presenter?.requestAccountDetail(sessionId)
        })
    }

    override fun onViewInited() {
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tab_layout)
    }

    override fun onFragmentsCreated(items: List<CommonFragmentViewModel>) {
        val tabAdapter = CommonTabPagerAdapter.newInstance(this.supportFragmentManager, items)
        viewPager.adapter = tabAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onAccountDetailFetced() {
        presenter?.makeFragment()
    }

    override fun taskDidBegin() {
        showProgressDialog()
    }

    override fun taskDidFinish() {
        dismissProgressDialog()
    }

    override fun taskDidError(message: String) {
        showToast(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.disposeAllRequest()
    }
}
