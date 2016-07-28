package vn.jupiter.android.listingview

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import vn.jupiter.android.logger.EmptyLogger

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 7/25/16.
 */
const val STATE_RELOAD_DATA: Int = 0x1
const val STATE_LOADING_MORE = 0x2
const val STATE_LOAD_MORE_ERROR = 0x3
const val STATE_LOAD_ERROR = 0x4

interface OnStateUpdateListener<in D> {
    fun onStateUpdate(state: D)
}

interface OnListingDataUpdateListener<D> : OnStateUpdateListener<ListingPresentationModel<D>> {

}

class CompositeListeners<D> : OnListingDataUpdateListener<D> {
    private var dataUpdateListeners = listOf<OnListingDataUpdateListener<D>>()

    override fun onStateUpdate(state: ListingPresentationModel<D>) {
        dataUpdateListeners.map { listener -> listener.onStateUpdate(state) }
    }

    fun addListener(listener : OnListingDataUpdateListener<D>) {
        dataUpdateListeners += listener
    }
}


class ListingView<D> : SwipeRefreshLayout, OnListingDataUpdateListener<D> {
    var logger = EmptyLogger()
    val dataUpdateListener = CompositeListeners<D>()


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    override fun addView(child: View?, index: Int, params: LayoutParams?) {
        super.addView(child, index, params)
        if (child is OnListingDataUpdateListener<*>) {
            dataUpdateListener.addListener(child as OnListingDataUpdateListener<D>)
        }
    }

    override fun onStateUpdate(state: ListingPresentationModel<D>) {
        dataUpdateListener.onStateUpdate(state)
    }
}

class SimpleErrorView<D> : TextView, OnListingDataUpdateListener<D> {
    var exceptionToTextFunction: ((ex: Exception?) -> String)? = null
    val defaultErrorText = ""

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs)
    }

    private fun initView(context: Context?, attrs: AttributeSet?) {
    }


    override fun onStateUpdate(state: ListingPresentationModel<D>) {
        when (state.state) {
            STATE_LOAD_ERROR -> {
                text = exceptionToTextFunction?.invoke(state.exception) ?: defaultErrorText
                visibility = View.VISIBLE
            }
            else -> visibility = View.GONE
        }
    }
}


class SimpleRecylerView<D> : OnListingDataUpdateListener<D> {
    lateinit var adapter: ItemSpecRecyclerViewAdapter<D>

    override fun onStateUpdate(state: ListingPresentationModel<D>) {
        adapter.setItems(state.data)
    }

}


data class ListingPresentationModel<out C>(val state: Int, val data: C, val exception: Exception?)
