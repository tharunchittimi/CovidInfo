package com.example.covidinfo.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.covidinfo.BR
import com.example.covidinfo.R
import com.example.covidinfo.util.StickHeaderItemDecoration
import com.example.covidinfo.adapter.CountriesAdapter
import com.example.covidinfo.databinding.ActivityCountriesBinding
import com.example.covidinfo.util.Utils
import com.example.covidinfo.viewmodels.CountriesListViewModel

class CountriesListActivity : AppCompatActivity() {

    private var mViewDataBinding: ActivityCountriesBinding? = null
    private var mViewModel: CountriesListViewModel? = null
    private var countriesAdapter: CountriesAdapter? = null

    companion object {
        const val BUNDLE_ISO_CODE = "BUNDLE_ISO_CODE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setUpRecyclerView()
        setViewsClickListeners()
        initSearchView()
        callCountriesListApi()
        addObserver()
    }

    private fun setViewsClickListeners() {
        mViewDataBinding?.searchView?.tvSearchCloseIcon?.setOnClickListener {
            mViewDataBinding?.searchView?.editTextSearchView?.setText("")
        }
    }

    private fun initBinding() {
        mViewDataBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_countries)
        mViewDataBinding?.lifecycleOwner = this
        this.mViewModel = ViewModelProvider(this)[CountriesListViewModel::class.java]
        mViewDataBinding?.setVariable(BR.countriesListViewModel, mViewModel)
    }

    private fun callCountriesListApi() {
        if (Utils.isNetworkAvailable(this@CountriesListActivity)) {
            mViewModel?.getCountriesListData()
        } else {
            Utils.showToast(this@CountriesListActivity, "No Internet Connection")
        }
    }

    private fun addObserver() {
        mViewModel?.getCountriesList()?.observe(this, {
            if (it.size > 0) {
                mViewDataBinding?.rvCountriesList?.visibility = View.VISIBLE
                mViewDataBinding?.tvNoCountriesHeading?.visibility = View.GONE
                countriesAdapter?.addCountries(it)
            } else {
                mViewDataBinding?.rvCountriesList?.visibility = View.GONE
                mViewDataBinding?.tvNoCountriesHeading?.visibility = View.VISIBLE
            }
        })
        mViewModel?.loadingObservable()?.observe(this, {
            if (it == true) {
                startLoading()
            } else {
                finishLoading()
            }
        })
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

    private fun setUpRecyclerView() {
        countriesAdapter = CountriesAdapter()
        mViewDataBinding?.rvCountriesList?.apply {
            layoutManager = LinearLayoutManager(this@CountriesListActivity)
            countriesAdapter?.let {
                addItemDecoration(StickHeaderItemDecoration(it))
            }
            adapter = countriesAdapter
        }
        countriesAdapter?.setOnItemCommunicatorListener(object : CountriesAdapter.Communicator {
            override fun showNoDataMessage(isDataNotAvailable: Boolean) {
                if (isDataNotAvailable) {
                    mViewDataBinding?.tvNoCountriesHeading?.visibility = View.VISIBLE
                    mViewDataBinding?.rvCountriesList?.visibility = View.GONE
                } else {
                    mViewDataBinding?.rvCountriesList?.visibility = View.VISIBLE
                    mViewDataBinding?.tvNoCountriesHeading?.visibility = View.GONE
                }
            }

            override fun onItemClick(isoCode: String?) {
                startActivity(
                    Intent(
                        this@CountriesListActivity,
                        CountryCasesDetailsActivity::class.java
                    ).putExtra(BUNDLE_ISO_CODE, isoCode)
                )
            }
        })
    }

    private fun initSearchView() {
        mViewDataBinding?.searchView?.editTextSearchView?.addTextChangedListener(object :
            TextWatcher {
            private val handler = Handler(Looper.getMainLooper())
            private var runnable: Runnable? = null
            override fun afterTextChanged(newText: Editable) {
                runnable?.let {
                    handler.removeCallbacks(it)
                }
                runnable = Runnable {
                    if (newText.trim().length <= 0) {
                        countriesAdapter?.showSearchedList("")
                    } else {
                        countriesAdapter?.showSearchedList(newText.toString())
                    }
                }
                handler.postDelayed(runnable!!, 400L)
            }

            override fun beforeTextChanged(
                newText: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(newText: CharSequence, start: Int, before: Int, count: Int) {
                if (newText.length <= 0) {
                    mViewDataBinding?.searchView?.tvSearchCloseIcon?.visibility = View.GONE
                } else {
                    mViewDataBinding?.searchView?.tvSearchCloseIcon?.visibility = View.VISIBLE
                }
            }
        })
    }
}