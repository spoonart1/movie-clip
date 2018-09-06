package com.spoonart.movieclip.feature.main

import com.spoonart.libs.base.IBaseView
import com.spoonart.movieclip.custom.pager.CommonFragmentViewModel

class MainContract {

    interface View : IBaseView {
        fun onFragmentsCreated(items: List<CommonFragmentViewModel>)
        fun onAccountDetailFetced()
    }

    interface Presenter {
        fun requestAccountDetail(sessionId:String)
        fun requestGenre()
        fun makeFragment()
    }
}