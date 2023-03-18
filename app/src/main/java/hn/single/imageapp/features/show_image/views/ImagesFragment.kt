package hn.single.imageapp.features.show_image.views

import android.os.Handler
import android.os.Looper
import hn.single.imageapp.common.bases.BaseFragment
import hn.single.imageapp.databinding.FragmentImagesBinding
import hn.single.imageapp.features.show_image.viewmodels.ImagesFragmentViewModel

class ImagesFragment : BaseFragment<Any, FragmentImagesBinding, ImagesFragmentViewModel>() {

    override fun getViewModelClass(): Class<ImagesFragmentViewModel> {
        return ImagesFragmentViewModel::class.java
    }

    override fun getViewBinding(): FragmentImagesBinding {
        return FragmentImagesBinding.inflate(layoutInflater)
    }

    override fun useSharedViewModel(): Boolean = false

    override fun initViews() {
        showLoadingProgress(true)
        Handler(Looper.getMainLooper()).postDelayed(
            { showLoadingProgress(false) }, 3000
        )
        mViewBinding.textHome.text = "Home text ${mViewModel.isLoading}"
    }

    override fun observeView() {

    }

    override fun observeData() {

    }
}