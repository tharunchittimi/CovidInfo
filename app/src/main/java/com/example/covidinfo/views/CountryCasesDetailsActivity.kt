package com.example.covidinfo.views

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.covidinfo.BR
import com.example.covidinfo.R
import com.example.covidinfo.databinding.ActivityCountryCasesDetailsBinding
import com.example.covidinfo.util.Utils
import com.example.covidinfo.viewmodels.CountryCasesDetailsViewModel
import java.text.SimpleDateFormat
import java.util.*

class CountryCasesDetailsActivity : AppCompatActivity() {

    private var mViewDataBinding: ActivityCountryCasesDetailsBinding? = null
    private var mViewModel: CountryCasesDetailsViewModel? = null
    private var isoCode: String? = null
    private val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
    private val myDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        getIntentData()
        callCountryCasesApi()
        addObserver()
        setViewsClickListeners()
    }

    private fun setViewsClickListeners() {
        mViewDataBinding?.imgBack?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initBinding() {
        mViewDataBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_country_cases_details)
        mViewDataBinding?.lifecycleOwner = this
        this.mViewModel = ViewModelProvider(this)[CountryCasesDetailsViewModel::class.java]
        mViewDataBinding?.setVariable(BR.countryCasesDetailsViewModel, mViewModel)
    }

    private fun getIntentData() {
        if (intent?.hasExtra(CountriesListActivity.BUNDLE_ISO_CODE) == true) {
            isoCode = intent?.getStringExtra(CountriesListActivity.BUNDLE_ISO_CODE)
        }
        mViewDataBinding?.imgFlag?.let {
            Glide.with(it.context).load(Utils.getCountryFlags(isoCode))
                .placeholder(R.drawable.no_img).error(R.drawable.no_img).into(it)
        }
    }

    private fun startLoading() {
        mViewDataBinding?.llvLoadingLayout?.visibility = View.VISIBLE
        mViewDataBinding?.imgLoading?.let {
            Glide.with(it.context).asGif()
                .load(R.drawable.loading_gif)
                .into(it)
        }
    }

    private fun finishLoading() {
        mViewDataBinding?.llvLoadingLayout?.visibility = View.GONE
    }

    private fun callCountryCasesApi() {
        if (Utils.isNetworkAvailable(this)) {
            mViewModel?.getCountryCasesData(isoCode)
        } else {
            Utils.showToast(this, "No Internet Connection")
        }
    }

    private fun addObserver() {
        mViewModel?.loadingObservable()?.observe(this, {
            if (it == true) {
                startLoading()
            } else {
                finishLoading()
            }
        })
        mViewModel?.getCountryCases()?.observe(this, {
            if (it.size > 0) {
                val serverTimeZone = TimeZone.getTimeZone("UTC")
                df.timeZone = serverTimeZone
                if (it?.get(0)?.lastUpdate?.length ?:0 > 0) {
                    val date = df.parse(it?.get(0)?.lastUpdate)
                    mViewDataBinding?.tvLatestUpdate?.text = myDateFormat.format(date)
                }

                mViewDataBinding?.tvTotalCases?.text = "Total Cases in ${it?.get(0)?.country}"
                mViewDataBinding?.tvTotalCasesCount?.text = it?.get(0)?.confirmed?.toString()
                mViewDataBinding?.cvDeaths?.setCasesHeading("DEATHS")
                mViewDataBinding?.cvDeaths?.setCasesCount("${it?.get(0)?.deaths?.toString()}")
                mViewDataBinding?.cvDeaths?.visibleExtraSpace(true)
                mViewDataBinding?.cvDeaths?.setCasesTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                )

                mViewDataBinding?.cvRecovered?.setCasesHeading("RECOVERED")
                mViewDataBinding?.cvRecovered?.setCasesCount("${it?.get(0)?.recovered?.toString()}")
                mViewDataBinding?.cvRecovered?.visibleExtraSpace(true)
                mViewDataBinding?.cvRecovered?.setCasesTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.teal_200
                    )
                )

                mViewDataBinding?.cvActiveCases?.setCasesHeading("DEATHS")
                mViewDataBinding?.cvActiveCases?.setCasesCount("${it?.get(0)?.confirmed?.toString()}")
                mViewDataBinding?.cvActiveCases?.setCasesTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                )
                mViewDataBinding?.cvActiveCases?.visibleExtraSpace(false)

                mViewDataBinding?.cvNewCasesCriticalCases?.setCasesHeading("SERIOUS/CRITICAL")
                mViewDataBinding?.cvNewCasesCriticalCases?.setCasesCount("${it?.get(0)?.critical?.toString()}")
                mViewDataBinding?.cvNewCasesCriticalCases?.visibleNewCases(true)
                mViewDataBinding?.cvNewCasesCriticalCases?.setCasesTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                )

                mViewDataBinding?.cvNewCasesCriticalCases?.setNewCasesHeading("NEW CASES")
                mViewDataBinding?.cvNewCasesCriticalCases?.setNewCasesCount("0") // here new cases key im not getting from response so default value is 0
                mViewDataBinding?.cvNewCasesCriticalCases?.visibleExtraSpace(false)
                mViewDataBinding?.grpCases?.visibility = View.VISIBLE
                mViewDataBinding?.tvNoCountriesHeading?.visibility = View.GONE
            } else {
                mViewDataBinding?.grpCases?.visibility = View.GONE
                mViewDataBinding?.tvNoCountriesHeading?.visibility = View.VISIBLE
            }
        })
    }

}