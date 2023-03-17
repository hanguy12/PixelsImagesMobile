package hn.single.imageapp.features.show_image.viewmodels

import hn.single.imageapp.common.bases.BaseViewModel

class ImagesFragmentViewModel : BaseViewModel() {

    override fun initState(): ImagesFragmentState = ImagesFragmentState()

    val isLoading = true
}