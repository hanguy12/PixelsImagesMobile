package hn.single.imageapp.features.show_image.views

import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import hn.single.imageapp.R
import hn.single.imageapp.common.bases.BaseFragment
import hn.single.imageapp.common.utils.AppConstants
import hn.single.imageapp.common.utils.Logger
import hn.single.imageapp.databinding.FragmentImagesBinding
import hn.single.imageapp.features.show_image.adapters.ImagesAdapter
import hn.single.imageapp.features.show_image.models.Media
import hn.single.imageapp.features.show_image.utils.SearchObservable
import hn.single.imageapp.features.show_image.viewmodels.ImagesFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ImagesFragment : BaseFragment<Any, FragmentImagesBinding, ImagesFragmentViewModel>() {

    private var imagesAdapter = ImagesAdapter()
    private var listImages = listOf<Media>()
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = AppConstants.DEFAULT_PAGE

    override fun getViewModelClass(): Class<ImagesFragmentViewModel> {
        return ImagesFragmentViewModel::class.java
    }

    override fun getViewBinding(): FragmentImagesBinding {
        return FragmentImagesBinding.inflate(layoutInflater)
    }

    override fun useSharedViewModel(): Boolean = false

    override fun initData(bundle: Bundle?)= Unit

    override fun initViews() {
        searchData("flo")
        mViewBinding?.popularRecycler?.apply {
            //val grid = GridLayoutManager(requireContext(), 1)
            //val grid = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            /* val linearLayoutManager =
                 LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)*/
            val linearLayoutManager = ImagesAdapter.ScaleLayoutManager(requireContext())
            layoutManager = linearLayoutManager
            adapter = imagesAdapter

            /*val listenerScroll = object : PageScrollListener(linearLayoutManager) {
                override fun isLoading(): Boolean = isLoading

                override fun isLastItem(): Boolean = isLastPage

                override fun loadMoreItems() {
                    isLoading = true
                    currentPage += 1
                    loadNextPage()
                }
            }
            addOnScrollListener(listenerScroll)*/
        }
        SearchObservable.fromViewSearch(mViewBinding?.inputSearch)
            .debounce(1000, TimeUnit.MILLISECONDS)
            .filter { it.trim().isNotEmpty() }
            .distinctUntilChanged()
            .switchMap { text ->
                Observable.just(searchData(text)).delay(3000, TimeUnit.MILLISECONDS)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val data: List<Media>? = mViewModel.imagesSearch.value
                Logger.d("SearchData ----- data: $data")
            }.addToContainer()

    }

    private fun searchData(query: String) {
        mViewModel.searchImagesByText(query)
        /*val data: List<Media>? = mViewModel.imagesSearch.value
        Logger.d("SearchData -- $query --- data: $data")*/

        //return data/*?.filter { it.contains(query, true) }*/

    }

    private fun loadNextPage() {
        Logger.d("LoadMore page")
    }

    override fun initActions() {
        Logger.d("initActions called")
        imagesAdapter.itemClick = {
            Toast.makeText(requireContext(), "Position click = $it", Toast.LENGTH_SHORT).show()
            arguments?.putInt(AppConstants.POSITION_CLICK_HOME, it)
            val listUrl = listImages.map { media ->
                media.src?.original
            }
            fragmentNavController()?.navigate(
                R.id.imageDetailFragment,
                bundleOf(
                    AppConstants.POSITION_CLICK_HOME to it,
                    AppConstants.LIST_URL_IMAGES to listUrl
                )
            )
        }
        /*mViewBinding?.inputSearch?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                Logger.d("onQueryTextChange(newText: String?) -- $newText")
                newText?.let { searchData(it) }
                return true
            }
        })*/
        /* mViewBinding?.inputSearch?.addTextChangedListener(object : TextWatcher {

             override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

             }

             override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

             }

             override fun afterTextChanged(p0: Editable?) {

             }
         })*/

    }

    override fun observeView() {
        mViewModel.getPopularImagesHome(AppConstants.ID_IMAGES)
    }

    override fun observeData() {
        observeLoadingView()
        observeListImages()
    }

    private fun observeLoadingView() {
        mViewModel.isLoading.observe(viewLifecycleOwner) {
            Logger.d("isLoading == $it")
            showLoadingProgress(it)
        }
    }

    private fun observeListImages() {
        mViewModel.images.observe(viewLifecycleOwner) {
            listImages = it.media
            imagesAdapter.loadDataToRecyclerView(listImages)
        }
    }
}