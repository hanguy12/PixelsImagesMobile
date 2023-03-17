package hn.single.imageapp.common.bases

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseContentFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    open var useSharedViewModel: Boolean = false
    private val disposableContainer = CompositeDisposable()
    protected lateinit var viewModel: VM
    protected lateinit var binding: VB

    protected abstract fun getViewModelClass(): Class<VM>

    protected abstract fun getViewBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setUpViews()
        observeView()
    }

    abstract fun useSharedViewModel(): Boolean

    abstract fun setUpViews()

    abstract fun observeView()

    abstract fun observeData()

    fun Disposable.addToContainer() = disposableContainer.add(this)

    private fun init() {
        binding = getViewBinding()
        viewModel = if (useSharedViewModel) {
            ViewModelProvider(requireActivity())[getViewModelClass()]
        } else {
            ViewModelProvider(this)[getViewModelClass()]
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposableContainer.clear()
    }


}