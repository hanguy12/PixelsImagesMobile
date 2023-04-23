package hn.single.imageapp.common.bases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hn.single.imageapp.common.networking.ImageLoadRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    val mRepository: ImageLoadRepository = ImageLoadRepository()

    private val _errorEvent: MutableLiveData<Throwable> = MutableLiveData()
    val errorEvent: LiveData<Throwable> get() = _errorEvent

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun Disposable.addToCompositeDisposable() = compositeDisposable.add(this)

    protected fun sendError(error: Throwable) {
        _errorEvent.value = error
    }

}