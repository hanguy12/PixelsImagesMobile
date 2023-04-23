package hn.single.imageapp.features.show_image.viewmodels

import androidx.lifecycle.MutableLiveData
import hn.single.imageapp.common.bases.BaseViewModel
import hn.single.imageapp.common.utils.AppConstants
import hn.single.imageapp.common.utils.Logger
import hn.single.imageapp.features.show_image.models.ImageDetail
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class ImageDetailViewModel : BaseViewModel() {

    val popular: MutableLiveData<ImageDetail> = MutableLiveData()
    var isLoading = true

    fun getImagesById(id: String) {
        mRepository.getImagesByIs(AppConstants.API_KEY, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
                isLoading = true
            }.doOnTerminate {
                isLoading = false
            }.subscribe({
                Logger.d("Data get: $it")
                popular.postValue(it)
            }, {
                sendError(it)
            }).addToCompositeDisposable()
    }
}