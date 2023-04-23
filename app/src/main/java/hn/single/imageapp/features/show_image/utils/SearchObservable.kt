package hn.single.imageapp.features.show_image.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import hn.single.imageapp.common.utils.Logger
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

object SearchObservable {

    fun fromViewSearch(editText: EditText?): Observable<String> {
        val publishSubject = PublishSubject.create<String>()

        editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                Logger.d("beforeTextChanged - $s")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Logger.d("onTextChanged - $s")
                publishSubject.onNext(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                Logger.d("afterTextChanged - $s")
            }
        })
        return publishSubject
    }

    fun fromViewSearch(searchView: SearchView?): Observable<String> {
        val publishSubject = PublishSubject.create<String>()

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                publishSubject.onComplete()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { publishSubject.onNext(it) }
                return true
            }
        })
        return publishSubject
    }
}
