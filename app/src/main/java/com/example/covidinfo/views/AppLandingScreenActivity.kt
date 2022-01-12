package com.example.covidinfo.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.covidinfo.BR
import com.example.covidinfo.R
import com.example.covidinfo.databinding.ActivityAppLandingScreenBinding
import com.example.covidinfo.viewmodels.AppLandingScreenViewModel

class AppLandingScreenActivity : AppCompatActivity() {

    private var mViewDataBinding: ActivityAppLandingScreenBinding? = null
    private var mViewModel: AppLandingScreenViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_app_landing_screen)
        mViewDataBinding?.lifecycleOwner = this
        this.mViewModel = ViewModelProvider(this)[AppLandingScreenViewModel::class.java]
        mViewDataBinding?.setVariable(BR.appLandingScreenViewModel, mViewModel)
        mViewDataBinding?.executePendingBindings()
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, CountriesListActivity::class.java))
            finish()
        }, 2000)
    }
}