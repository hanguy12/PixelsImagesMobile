package hn.single.imageapp.features.show_image.views

import android.os.Bundle
import hn.single.imageapp.common.bases.BaseFragment
import hn.single.imageapp.common.bases.ViewExt.gone
import hn.single.imageapp.common.bases.ViewExt.show
import hn.single.imageapp.common.utils.AppConstants
import hn.single.imageapp.common.utils.Logger
import hn.single.imageapp.databinding.FragmentImageDetailBinding
import hn.single.imageapp.features.show_image.adapters.ImagesDetailAdapter
import hn.single.imageapp.features.show_image.viewmodels.ImageDetailViewModel

class ImageDetailFragment :
    BaseFragment<Any, FragmentImageDetailBinding, ImageDetailViewModel>() {

    private var imageAdapter = ImagesDetailAdapter()
    private var listUrl: List<String> = listOf()
    private var positionClick: Int? = null

    override fun getViewModelClass(): Class<ImageDetailViewModel> {
        return ImageDetailViewModel::class.java
    }

    override fun getViewBinding(): FragmentImageDetailBinding =
        FragmentImageDetailBinding.inflate(layoutInflater)

    override fun useSharedViewModel(): Boolean = false

    override fun initData(bundle: Bundle?) {
        positionClick = bundle?.getInt(AppConstants.POSITION_CLICK_HOME)
        listUrl = bundle?.getStringArrayList(AppConstants.LIST_URL_IMAGES) ?: listOf()
        Logger.d("position clicked = $positionClick")
        Logger.d("ListUrl = $listUrl")
    }

    override fun initViews() {
        if (listUrl.isEmpty()) {
            mViewBinding?.collectionViewPager?.gone()
            mViewBinding?.textNoImages?.show()
        } else {
            mViewBinding?.collectionViewPager?.adapter = imageAdapter
            listUrl.let {
                imageAdapter.loadDataToRecyclerView(it)
                if (positionClick != null) {
                    mViewBinding?.collectionViewPager?.currentItem = positionClick as Int
                }
            }
        }
    }

    override fun initActions() = Unit

    override fun observeView() = Unit

    override fun observeData() = Unit

}