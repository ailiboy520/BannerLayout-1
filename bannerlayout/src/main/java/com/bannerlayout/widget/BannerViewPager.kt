package com.bannerlayout.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import androidx.viewpager.widget.ViewPager

/**
 * by y on 2016/10/25
 */
class BannerViewPager : ViewPager {

    var viewTouchMode: Boolean = false
        set(value) {
            field = value
            if (value && !isFakeDragging) {
                beginFakeDrag()
            } else if (!value && isFakeDragging) {
                endFakeDrag()
            }
        }

    var isVertical: Boolean = false

    private lateinit var scroller: FixedSpeedScroller

    val duration: Int get() = scroller.fixDuration

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        try {
            val mFirstLayout = ViewPager::class.java.getDeclaredField("mFirstLayout")
            mFirstLayout.isAccessible = true
            mFirstLayout.set(this, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDetachedFromWindow() {
        if ((context as Activity).isFinishing) {
            super.onDetachedFromWindow()
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean = if (isVertical) {
        !viewTouchMode && super.onInterceptTouchEvent(swapEvent(event))
    } else {
        !viewTouchMode && super.onInterceptTouchEvent(event)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean = if (isVertical) {
        super.onTouchEvent(swapEvent(ev))
    } else {
        super.onTouchEvent(ev)
    }

    override fun arrowScroll(direction: Int): Boolean = !viewTouchMode && !(direction != View.FOCUS_LEFT && direction != View.FOCUS_RIGHT) && super.arrowScroll(direction)


    fun setDuration(duration: Int) = apply {
        try {
            val mScroller = ViewPager::class.java.getDeclaredField("mScroller")
            mScroller.isAccessible = true
            scroller = FixedSpeedScroller(context)
            mScroller.set(this, scroller)
            scroller.fixDuration = duration
        } catch (e: Exception) {
            Log.i(javaClass.simpleName, e.message)
        }
        return this
    }

    private fun swapEvent(event: MotionEvent): MotionEvent {
        val width = width.toFloat()
        val height = height.toFloat()
        val swappedX = event.y / height * width
        val swappedY = event.x / width * height
        event.setLocation(swappedX, swappedY)
        return event
    }
}

class FixedSpeedScroller(context: Context) : Scroller(context) {

    var fixDuration: Int = 0

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, fixDuration)
    }
}