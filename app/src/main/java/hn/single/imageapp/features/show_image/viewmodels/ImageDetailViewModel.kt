package hn.single.imageapp.features.show_image.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hn.single.imageapp.common.bases.BaseViewModel
import hn.single.imageapp.common.utils.AppConstants
import hn.single.imageapp.common.utils.Logger
import hn.single.imageapp.features.show_image.models.ImageDetail
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class ImageDetailViewModel : BaseViewModel() {

    private val _popular: MutableLiveData<ImageDetail> = MutableLiveData()
    val popular: LiveData<ImageDetail>
        get() = _popular

    override fun initState(): ImagesFragmentState = ImagesFragmentState()

    var isLoading = true

    fun getImagesById(id: String) {
//        Single.just(
            mRepository.getImagesByIs(AppConstants.API_KEY, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
                    isLoading = true
                }.doOnTerminate {
                    isLoading = false
                }.subscribe({
                    Logger.d("Data get: $it")
                    _popular.postValue(it)
                }, {

                }).addToCompositeDisposable()
//        )
    }
}