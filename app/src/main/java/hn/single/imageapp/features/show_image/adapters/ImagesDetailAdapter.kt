package hn.single.imageapp.features.show_image.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import hn.single.imageapp.databinding.ItemImageDetailBinding
import hn.single.imageapp.features.show_image.utils.fillImageByGlide

class ImagesDetailAdapter : PagerAdapter() {

    private var listImage = listOf<String>()

    fun loadDataToRecyclerView(data: List<String>) {
        listImage = data
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding =
            ItemImageDetailBinding.inflate(LayoutInflater.from(container.context), container, false)
        binding.imageDetail.apply {
            fillImageByGlide(listImage[position])
        }
        container.addView(binding.root)
        return binding.root
    }

    override fun getCount(): Int = listImage.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}