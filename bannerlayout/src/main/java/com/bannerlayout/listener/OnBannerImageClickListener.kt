package com.bannerlayout.listener

import android.view.View

interface OnBannerImageClickListener<T> {
    fun onBannerClick(view: View, position: Int, model: T)
}