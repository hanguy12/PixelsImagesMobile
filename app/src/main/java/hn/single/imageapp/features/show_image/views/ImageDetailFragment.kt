package hn.single.imageapp.features.show_image.views

import androidx.recyclerview.widget.GridLayoutManager
import hn.single.imageapp.common.bases.BaseFragment
import hn.single.imageapp.databinding.FragmentImageDetailBinding
import hn.single.imageapp.features.show_image.adapters.ImageDetailAdapter
import hn.single.imageapp.features.show_image.viewmodels.ImageDetailViewModel


class ImageDetailFragment(val id: String) :
    BaseFragment<Any, FragmentImageDetailBinding, ImageDetailViewModel>() {

    private var imageDetailAdapter = ImageDetailAdapter()

    override fun getViewModelClass(): Class<ImageDetailViewModel> {
        return ImageDetailViewModel::class.java
    }

    override fun getViewBinding(): FragmentImageDetailBinding =
        FragmentImageDetailBinding.inflate(layoutInflater)

    override fun useSharedViewModel(): Boolean = false

    override fun initViews() {
        val grid = GridLayoutManager(requireContext(), 1)
//        val grid = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        mViewBinding?.collectionRecycler?.layoutManager = grid
        mViewBinding?.collectionRecycler?.adapter = imageDetailAdapter
    }

    override fun initActions() {

    }

    override fun observeView() {
        mViewModel.getImagesById(id)
    }

    override fun observeData() {
        mViewModel.popular.observe(viewLifecycleOwner) {
            imageDetailAdapter.loadDataToRecyclerView(it.media)
        }
    }

}