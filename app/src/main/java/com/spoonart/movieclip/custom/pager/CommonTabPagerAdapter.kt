package com.spoonart.movieclip.custom.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class CommonTabPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var modelCommons:List<CommonFragmentViewModel> = listOf()

    companion object {
        fun newInstance(fm:FragmentManager, modelCommons:List<CommonFragmentViewModel>) : CommonTabPagerAdapter {
            val a = CommonTabPagerAdapter(fm)
            a.modelCommons = modelCommons
            return a
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return modelCommons[position].title
    }

    override fun getItem(position: Int): Fragment {
        return modelCommons[position].fragment
    }

    override fun getCount(): Int {
        return modelCommons.size
    }

}