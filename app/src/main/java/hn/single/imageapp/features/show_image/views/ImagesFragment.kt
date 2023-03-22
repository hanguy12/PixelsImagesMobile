package hn.single.imageapp.features.show_image.views

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import hn.single.imageapp.common.bases.BaseFragment
import hn.single.imageapp.common.utils.Logger
import hn.single.imageapp.databinding.FragmentImagesBinding
import hn.single.imageapp.features.show_image.adapters.PagerAdapterPopular
import hn.single.imageapp.features.show_image.viewmodels.ImagesFragmentViewModel

class ImagesFragment : BaseFragment<Any, FragmentImagesBinding, ImagesFragmentViewModel>() {

    override fun getViewModelClass(): Class<ImagesFragmentViewModel> {
        return ImagesFragmentViewModel::class.java
    }

    override fun getViewBinding(): FragmentImagesBinding {
        return FragmentImagesBinding.inflate(layoutInflater)
    }

    override fun useSharedViewModel(): Boolean = false

    override fun initViews() {
        showLoadingProgress(true)
        Handler(Looper.getMainLooper()).postDelayed(
            { showLoadingProgress(false) }, 3000
        )


    }

    override fun initActions() {
        mViewBinding?.inputSearch?.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun afterTextChanged(p0: Editable?) {
                //TODO("Not yet implemented")
            }
        })
    }

    override fun observeView() {
        mViewModel.getPopularCategories()
    }

    override fun observeData() {
        mViewModel.popular.observe(viewLifecycleOwner) {
            val collections = it.collections
//            for (i in collections.indices) {
            val pagerAdapter = PagerAdapterPopular(childFragmentManager, lifecycle)
            for (i in collections.indices) {
                mViewBinding?.tabLayout?.run {
                    addTab(newTab().setText(collections[i].title))
                    pagerAdapter.addFragment(ImageDetailFragment("1rvchkd"))
                    //pagerAdapter.addFragment(ImageDetailFragment(collections[i].id))
                }
            }
            mViewBinding?.viewPager2?.adapter = pagerAdapter
            mViewBinding?.viewPager2?.offscreenPageLimit = collections.size
            mViewBinding?.tabLayout?.tabGravity = TabLayout.GRAVITY_FILL

            mViewBinding?.viewPager2?.addOnAttachStateChangeListener(object :
                View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(p0: View) {}

                override fun onViewDetachedFromWindow(p0: View) {}
            })

            (TabLayout.TabLayoutOnPageChangeListener(mViewBinding?.tabLayout))
//            mViewBinding?.viewPager2?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mViewBinding?.tabLayout))

            mViewBinding?.tabLayout?.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    /****/
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    /****/
                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    mViewBinding?.viewPager2?.currentItem = tab.position
                }
            })
            /* if (collections.isNotEmpty()){
                 mViewBinding?.tabLayout?.visibility = View.GONE
             }else {
                 mViewBinding?.tabLayout?.visibility = View.VISIBLE
             }*/
            val tabLayoutMediator = mViewBinding?.viewPager2?.let { it1 ->
                mViewBinding?.tabLayout?.let { it2 ->
                    TabLayoutMediator(it2, it1) { tab, position ->
                        tab.text = collections[position].title
                    }
                }
            }
            tabLayoutMediator?.attach()

            Logger.d("Data get: $it")
        }
    }
}