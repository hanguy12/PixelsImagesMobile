package hn.single.imageapp.features.show_image.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hn.single.imageapp.databinding.LayoutImagesListBinding
import hn.single.imageapp.features.show_image.models.Media

class ImageDetailAdapter : RecyclerView.Adapter<ImageDetailAdapter.ImageViewHolder>() {

    private var listImage = listOf<Media>()

    fun loadDataToRecyclerView(data: List<Media>) {
        listImage = data
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(
        private val binding: LayoutImagesListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(position: Int) {
            //binding.progressLoad.visibility = View.VISIBLE
            Glide.with(binding.image.context).load(listImage[position].src?.original)
                /*.addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.image.context.runOnUiThread {
                            binding.progressLoad.visibility = View.GONE
                        }
                        return false
                    }
                })*/.into(binding.image)
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
}