package hn.single.imageapp.common.bases

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var mViewBinding: VB

    protected abstract fun getViewBinding(): VB

    abstract fun activityNavController(): NavController?

    abstract fun showLoadingProgress(isShow: Boolean)

    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = getViewBinding()
        setContentView(mViewBinding.root)
        initView()
    }

}