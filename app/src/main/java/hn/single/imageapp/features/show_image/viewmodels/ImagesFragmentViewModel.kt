package hn.single.imageapp.features.show_image.viewmodels

import androidx.lifecycle.MutableLiveData
import hn.single.imageapp.common.bases.BaseViewModel
import hn.single.imageapp.common.utils.AppConstants
import hn.single.imageapp.common.utils.Logger
import hn.single.imageapp.features.show_image.models.ImageDetail
import hn.single.imageapp.features.show_image.models.Media
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class ImagesFragmentViewModel : BaseViewModel() {

    val images: MutableLiveData<ImageDetail> = MutableLiveData()
    val imagesSearch: MutableLiveData<List<Media>> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun getPopularImagesHome(id: String) {
        mRepository.getPopularImages(AppConstants.API_KEY, id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
                isLoading.value = true
            }.doOnTerminate {
                isLoading.value = false
            }.subscribe({
                Logger.d("MediaInfo Data get: $it")
                images.value = it
                isLoading.value = false
            }, {
                sendError(it)
                isLoading.value = false
            }).addToCompositeDisposable()
    }

    fun searchImagesByText(value: String) {
        mRepository.searchImagesByText(AppConstants.API_KEY, value).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
                isLoading.postValue(true)
            }.doOnTerminate {
                isLoading.postValue(false)
            }.subscribe({
                Logger.d("MediaInfo Data get: $it")
                imagesSearch.value = it.media
                isLoading.postValue(false)
            }, {
                sendError(it)
                isLoading.postValue(false)
            }).addToCompositeDisposable()
    }
}