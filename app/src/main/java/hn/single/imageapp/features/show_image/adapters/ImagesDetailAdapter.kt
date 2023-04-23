package hn.single.imageapp.features.show_image.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import hn.single.imageapp.R
import hn.single.imageapp.databinding.ItemImageDetailBinding

class ImagesDetailAdapter : PagerAdapter() {

    private var listImage = listOf<String>()

    fun loadDataToRecyclerView(data: List<String>) {
        listImage = data
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflate =
            ItemImageDetailBinding.inflate(LayoutInflater.from(container.context), container, false)
        inflate.imageDetail.let {
            Glide.with(it.context).load(listImage[position]).thumbnail(
                Glide.with(it.context).load(
                    AppCompatResources.getDrawable(
                        it.context,
                        R.drawable.ic_launcher
                    )
                )
            ).into(it)
        }
        container.addView(inflate.root)
        return inflate.root
    }

    override fun getCount(): Int = listImage.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}