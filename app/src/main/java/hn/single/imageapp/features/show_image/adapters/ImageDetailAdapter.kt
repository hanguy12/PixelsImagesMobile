package hn.single.imageapp.features.show_image.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hn.single.imageapp.R
import hn.single.imageapp.databinding.LayoutImagesListBinding
import hn.single.imageapp.features.show_image.models.Media

class ImageDetailAdapter : RecyclerView.Adapter<ImageDetailAdapter.ImageViewHolder>() {

    private var listImage = listOf<Media>()
    var itemClick: ((Int) -> Unit)? = null

    fun loadDataToRecyclerView(data: List<Media>) {
        listImage = data
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(
        private val binding: LayoutImagesListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(position: Int) {
            binding.rootItem.setOnClickListener {
                itemClick?.invoke(position)
            }
            binding.image.let {
                it.setOnClickListener {
                    itemClick?.invoke(position)
                }
                val recyclerHeight = it.context.resources.getDimensionPixelSize(R.dimen.dimen_200dp)
                val percent: Float = listImage[position].height?.toFloat()
                    ?.let { it1 -> listImage[position].width?.toFloat()?.div(it1) } ?: 1f
                it.layoutParams =
                    ConstraintLayout.LayoutParams(
                        (recyclerHeight * percent).toInt(),
                        recyclerHeight
                    )
                Glide.with(it.context).load(listImage[position].src?.original).into(it)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutImagesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return listImage.size
    }

    internal class ScaleLayoutManager(
        context: Context,
        private val minScaleDistance: Float = 1.5f,
        private val scaleDown: Float = 0.3f
    ) : LinearLayoutManager(context, HORIZONTAL, false) {

        override fun onLayoutCompleted(state: RecyclerView.State?) =
            super.onLayoutCompleted(state).also { scaleChildren() }

        override fun scrollHorizontallyBy(
            dx: Int,
            recycler: RecyclerView.Recycler,
            state: RecyclerView.State
        ) = super.scrollHorizontallyBy(dx, recycler, state).also {
            if (orientation == HORIZONTAL) scaleChildren()
        }

        private fun scaleChildren() {
            val containerCenter = width / 2f
            val scaleDistanceThreshold = minScaleDistance * containerCenter
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                val childCenter = ((child?.left ?: 0) + (child?.right ?: 0)) / 2f
                val distanceToCenter = kotlin.math.abs(childCenter - containerCenter)
                val scaleDownAmount = (distanceToCenter / scaleDistanceThreshold).coerceAtMost(1f)
                val scale = 1f - scaleDown * scaleDownAmount
                child?.scaleX = scale
                child?.scaleY = scale
            }
        }
    }

    class PhotoDiffUtils() : DiffUtil.ItemCallback<Media>() {
        override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem.id == newItem.id
        }

    }
}