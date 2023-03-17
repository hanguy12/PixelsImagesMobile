package hn.single.imageapp.common.bases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseViewModel<S : Any> : ViewModel() {

    private val _errorLiveEvent: MutableLiveData<Throwable> = MutableLiveData()
    val errorLiveEvent: LiveData<Throwable> get() = _errorLiveEvent

    private val compositeDisposable = CompositeDisposable()

    abstract fun initState(): S

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun Disposable.addToCompositeDisposable() = compositeDisposable.add(this)

    protected fun dispatchError(error: Throwable) {
        _errorLiveEvent.value = error
    }

}