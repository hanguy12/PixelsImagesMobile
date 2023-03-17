package hn.single.imageapp

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import hn.single.imageapp.common.bases.BaseActivity
import hn.single.imageapp.common.bases.ViewExt.gone
import hn.single.imageapp.common.bases.ViewExt.show
import hn.single.imageapp.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private var mNavController: NavController? = null

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun activityNavController(): NavController? = mNavController

    override fun initView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        mNavController = navHostFragment.navController
    }

    override fun showLoadingProgress(isShow: Boolean) {
        if (isShow) {
            mViewBinding.loadingView.root.show()
        } else {
            mViewBinding.loadingView.root.gone()
        }
    }


}