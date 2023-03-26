package hn.single.imageapp.features.show_image.adapters

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PageScrollListener(
    private val layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {

    abstract fun isLoading(): Boolean

    abstract fun isLastItem(): Boolean

    abstract fun loadMoreItems()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val itemVisible = layoutManager.childCount
        val totalItems = layoutManager.itemCount
        val firstItemVisible = layoutManager.findFirstVisibleItemPosition()
        if (isLoading() || isLastItem()) {
            return
        }
        if (firstItemVisible >= 0 && (itemVisible + firstItemVisible >= totalItems)) {
            loadMoreItems()
        }

    }


}