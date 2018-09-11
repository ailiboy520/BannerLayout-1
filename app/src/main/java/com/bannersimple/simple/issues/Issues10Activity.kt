package com.bannersimple.simple.issues

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.bannerlayout.widget.BannerLayout
import com.bannersimple.R
import com.bannersimple.SimpleOnBannerChangeListener
import com.bannersimple.bean.SimpleBannerModel
import com.bannersimple.bean.SimpleData


/**
 * by y on 25/07/2017.
 *
 *
 * Issues sample : https://github.com/7449/BannerLayout/issues/10
 *
 *
 * test:
 * onePlus 3T  1080;
 * virtual machine  480;
 */

/**
 * Just a simple example, so only two resolutions are tested
 */
class Issues10Activity : AppCompatActivity() {

    companion object {
        private val TAG = Issues10Activity::class.java.simpleName
    }

    private lateinit var bannerLayout: BannerLayout
    private lateinit var bannerlnstagram: BannerLayout
    private lateinit var linearLayout: LinearLayout

    private var isShowTips = false
    private var preEnablePosition = 0
    private var sizelnstagram: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issues_10)
        bannerLayout = findViewById(R.id.banner)
        bannerlnstagram = findViewById(R.id.banner_lnstagram)
        linearLayout = findViewById(R.id.ll_view)

        test()
        testlnstagram()
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerLayout.removeHandler()
        bannerlnstagram.removeHandler()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun testlnstagram() {
        val data = SimpleData.lnstagramData()
        bannerlnstagram
                .setPageNumViewMargin(10)
                .initPageNumView()
                .initListResources(data)
                .setDelayTime(1000)
                .switchBanner(false)


        //  dots
        sizelnstagram = data.size
        linearLayout.removeAllViews()
        for (i in 0 until sizelnstagram) {
            val view = View(this)
            view.background = ContextCompat.getDrawable(this, R.drawable.banner)
            view.isEnabled = i == 0
            val params = LinearLayout.LayoutParams(15, 15)
            view.layoutParams = params
            params.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER
            params.leftMargin = 5
            params.rightMargin = 5
            linearLayout.addView(view)
        }

        bannerlnstagram
                .addOnPageChangeListener(
                        object : SimpleOnBannerChangeListener() {
                            override fun onPageSelected(position: Int) {
                                super.onPageSelected(position)
                                linearLayout.getChildAt(position).isEnabled = true
                                linearLayout.getChildAt(preEnablePosition).isEnabled = false
                                preEnablePosition = position
                                val startView = linearLayout.getChildAt(0)
                                val endView = linearLayout.getChildAt(sizelnstagram - 1)
                                if (position == sizelnstagram - 1) {
                                    startView.scaleX = 0.6f
                                    startView.scaleY = 0.6f
                                    endView.scaleX = 1f
                                    endView.scaleY = 1f
                                } else if (position == 0) {
                                    startView.scaleX = 1f
                                    startView.scaleY = 1f
                                    endView.scaleX = 0.6f
                                    endView.scaleY = 0.6f
                                }
                            }
                        })
    }


    private fun test() {
        val display = window.windowManager.defaultDisplay
        val dm = DisplayMetrics()
        display.getMetrics(dm)
        Log.i(TAG, dm.widthPixels.toString() + "   " + dm.heightPixels)
        val dotWidthAndHeight: Int
        val dotMargin: Int
        if (dm.widthPixels >= 1080) {
            dotWidthAndHeight = 15
            dotMargin = 6
        } else {
            dotWidthAndHeight = 6
            dotMargin = 3
        }
        bannerLayout
                .setPageNumViewMargin(10)
                .setDotsSite(BannerLayout.CENTER)
                .setDotsWidthAndHeight(dotWidthAndHeight, dotWidthAndHeight)
                .setDotsMargin(dotMargin)
                .initTips()
                .initPageNumView()
                .initListResources(SimpleData.data())
                .switchBanner(true)
        findViewById<View>(R.id.btn_alter_count)
                .setOnClickListener { alterBannerCount() }
    }

    private fun alterBannerCount() {
        val alterData: MutableList<SimpleBannerModel> = if (!isShowTips) {
            SimpleData.alterData()
        } else {
            SimpleData.data()
        }
        isShowTips = !isShowTips
        val size = alterData.size
        if (size <= 1) {
            bannerLayout
                    .initTips(false, false, false)
                    .initListResources(alterData)
                    .switchBanner(false)
            Toast.makeText(this, "size <=1 , stopBanner , not show tipsLayout", Toast.LENGTH_SHORT).show()
        } else {
            bannerLayout
                    .initTips(true, true, true)
                    .initListResources(alterData)
                    .switchBanner(true)
            Toast.makeText(this, "size >1 , startBanner , show tipsLayout", Toast.LENGTH_SHORT).show()
        }
    }


}
