package hn.single.imageapp.features.show_image.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hn.single.imageapp.common.bases.BaseViewModel
import hn.single.imageapp.common.utils.AppConstants
import hn.single.imageapp.common.utils.Logger
import hn.single.imageapp.features.show_image.models.Popular
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class ImagesFragmentViewModel : BaseViewModel() {

    private val _popular: MutableLiveData<Popular> = MutableLiveData()
    val popular: LiveData<Popular>
        get() = _popular

    override fun initState(): ImagesFragmentState = ImagesFragmentState()

    var isLoading = true

    fun getPopularCategories() {
        mRepository.getPopularCategories(AppConstants.API_KEY).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
                isLoading = true
            }.doOnTerminate {
                isLoading = false
            }.subscribe({
                _popular.postValue(it)
                Logger.d("Popular Data get: $it")
            }, {

            }).addToCompositeDisposable()



        mRepository.getImagesByIs(AppConstants.API_KEY, "1rvchkd").subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
                isLoading = true
            }.doOnTerminate {
                isLoading = false
            }.subscribe({
                Logger.d("MediaInfo Data get: $it")
                isLoading = false
            }, {

            }).addToCompositeDisposable()

    }
}