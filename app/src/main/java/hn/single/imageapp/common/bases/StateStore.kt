package hn.single.imageapp.common.bases

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class StateStore<T : Any>(initialState: T) {

    private val stateData = MutableLiveData<T>().apply {
        value = initialState
    }

    val state: T
        get() = stateData.value!!

    fun <S> observe(
        owner: LifecycleOwner,
        selector: (T) -> S,
        observer: Observer<S>
    ) {
        //stateData.map(selector).distinctUntilChanged().observe(owner, observer)
    }

    fun <S> observeAnyway(
        owner: LifecycleOwner,
        selector: (T) -> S,
        observer: Observer<S>
    ) {
        //stateData.map(selector).observe(owner, observer)
    }

    fun sendState(state: T) {
        stateData.value = state
    }

    fun postState(state: T) {
        stateData.postValue(state)
    }
}