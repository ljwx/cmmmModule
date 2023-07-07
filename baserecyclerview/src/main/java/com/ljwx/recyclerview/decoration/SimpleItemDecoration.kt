package com.ljwx.recyclerview.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * https://www.jianshu.com/p/41ae13016243
 */

class SimpleItemDecoration(
    firstPaddingLeft: Int? = null, firstPaddingTop: Int? = null,
    lastPaddingRight: Int? = null, lastPaddingBottom: Int? = null,
    norPaddingLeft: Int? = null, norPaddingTop: Int? = null
) : RecyclerView.ItemDecoration() {

    var mPaddingLeft = firstPaddingLeft
    var mNorPaddingLeft = norPaddingLeft
    var mPaddingTop = firstPaddingTop
    var mNorPaddingTop = norPaddingTop
    var mPaddingRight = lastPaddingRight
    var mPaddingBottom = lastPaddingBottom

    //设置ItemView的内嵌偏移长度（inset）
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        //当前item位置
        val position = parent.getChildAdapterPosition(view)
        //第一个的,上左间隔
        if (position == 0) {
            mPaddingTop?.let {
                outRect.top = it
            }
            mPaddingLeft?.let {
                outRect.left = it
            }
        } else {
            mNorPaddingTop?.let {
                outRect.top = it
            }
            mNorPaddingLeft?.let {
                outRect.left = it
            }
        }
        //最后一个的, 右下间隔
        parent.adapter?.let {
            if (position == it.itemCount - 1) {
                mPaddingRight?.let {
                    outRect.right = it
                }
                mPaddingBottom?.let {
                    outRect.bottom = it
                }
            }
        }
        outRect.set(outRect.left, outRect.top, outRect.right, outRect.bottom)
    }

    // 在子视图上设置绘制范围，并绘制内容
    // 绘制图层在ItemView以下，所以如果绘制区域与ItemView区域相重叠，会被遮挡
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    //同样是绘制内容，但与onDraw（）的区别是：绘制在图层的最上层
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }
}
