package com.demo.online.helpers.recyclerViewAdapters

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper


/**
 * Created by Midas on 06.03.2018.
 */
class SimpleItemTouchHelperCallback(private val mAdapter: SimpleAdapter) : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return source.itemViewType != target.itemViewType
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        mAdapter.onItemDismiss(viewHolder.adapterPosition)
    }
}