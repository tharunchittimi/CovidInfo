package com.example.covidinfo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.covidinfo.R
import com.example.covidinfo.util.StickHeaderItemDecoration
import com.example.covidinfo.databinding.InflateHeaderTitleBinding
import com.example.covidinfo.databinding.InflateRvCountriesItemBinding
import com.example.covidinfo.models.BaseCountriesHeaderModel
import com.example.covidinfo.models.CountriesHeaderTitle
import com.example.covidinfo.models.CountriesListResponse
import com.example.covidinfo.util.Utils

class CountriesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    StickHeaderItemDecoration.StickyHeaderInterface {

    private var countryList: ArrayList<BaseCountriesHeaderModel> = arrayListOf()
    private var mainList: ArrayList<CountriesListResponse.CountriesListResponseItem> = arrayListOf()
    private var communicator: Communicator? = null

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var itemPosition = itemPosition
        var headerPosition = 0
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition
                break
            }
            itemPosition -= 1
        } while (itemPosition >= 0)
        return headerPosition
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return R.layout.sticky_header_transparent
    }

    override fun bindHeaderData(header: View?, headerPosition: Int) {
        val tvHeader: TextView = header?.findViewById(R.id.tvCountryAlphabet)!!
        val headerModel: CountriesHeaderTitle = countryList[headerPosition] as CountriesHeaderTitle
        Log.e("Header", "$headerPosition ${headerModel.countryName[0]}")
        tvHeader.text = headerModel.countryName[0].toString()
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return countryList[itemPosition] is CountriesHeaderTitle
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            countryList[position] is CountriesListResponse.CountriesListResponseItem -> 0
            countryList[position] is CountriesHeaderTitle -> 1
            else -> -1
        }
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CountriesViewHolder) {
            (holder).bind(position)
            val nextPosition = position + 1
            if (nextPosition < countryList.size) {
                holder.inflateRvCountriesItemBinding.viewUnderLine.visibility =
                    if (countryList[nextPosition] is CountriesHeaderTitle) View.INVISIBLE else View.VISIBLE
            }
        } else if (holder is CountriesHeaderViewHolder) {
            (holder).bind(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                val inflateRvCountriesItemBinding: InflateRvCountriesItemBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.inflate_rv_countries_item, parent, false
                    )
                return CountriesViewHolder(inflateRvCountriesItemBinding)
            }
            1 -> {
                val inflateHeaderTitleBinding: InflateHeaderTitleBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.inflate_header_title, parent, false
                )
                return CountriesHeaderViewHolder(inflateHeaderTitleBinding)
            }
            else -> {
                val inflateRvCountriesItemBinding: InflateRvCountriesItemBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.inflate_rv_countries_item, parent, false
                    )
                return CountriesViewHolder(inflateRvCountriesItemBinding)
            }
        }
    }

    inner class CountriesViewHolder(var inflateRvCountriesItemBinding: InflateRvCountriesItemBinding) :
        RecyclerView.ViewHolder(inflateRvCountriesItemBinding.root) {
        fun bind(position: Int) {
            val country = countryList[position] as CountriesListResponse.CountriesListResponseItem
            inflateRvCountriesItemBinding.tvCountryName.text = country.name
            inflateRvCountriesItemBinding.imgFlag.let {
                Glide.with(it.context).load(Utils.getCountryFlags(country.alpha2code))
                    .placeholder(R.drawable.no_img).error(R.drawable.no_img).into(it)
            }
            Log.e("url", "${Utils.getCountryFlags(country.alpha2code)}")
            inflateRvCountriesItemBinding.rootLayout.setOnClickListener {
                communicator?.onItemClick(country.alpha2code)
            }
        }
    }

    inner class CountriesHeaderViewHolder(var inflateHeaderTitleBinding: InflateHeaderTitleBinding) :
        RecyclerView.ViewHolder(inflateHeaderTitleBinding.root) {
        fun bind(position: Int) {
            val countriesList = countryList[position] as CountriesHeaderTitle
            inflateHeaderTitleBinding.tvCountryAlphabet.text =
                countriesList.countryName[0].toString()
        }
    }

    fun addCountries(list: ArrayList<CountriesListResponse.CountriesListResponseItem>) {
        addHeaders(list)
        mainList.addAll(list)
    }

    private fun addHeaders(list: ArrayList<CountriesListResponse.CountriesListResponseItem>) {
        countryList.clear()
        var currentHeaderAdded = ""
        for (myItem in list) {
            val initialChar = (myItem.name?.get(0)).toString()
            if (currentHeaderAdded.equals(initialChar, true)) {
                countryList.add(myItem)
            } else {
                currentHeaderAdded = initialChar
                countryList.add(CountriesHeaderTitle(myItem.name ?: ""))
                countryList.add(myItem)
            }
        }
        notifyDataSetChanged()
    }

    fun showSearchedList(newText: String?) {
        val searchedList: ArrayList<CountriesListResponse.CountriesListResponseItem> = ArrayList()
        if (newText?.isEmpty() == true) {
            searchedList.addAll(mainList)
            communicator?.showNoDataMessage(mainList.size <= 0)
        } else {
            val searchedName = newText.toString().trim()
            searchedList.clear()
            for (item in mainList) {
                (item as? CountriesListResponse.CountriesListResponseItem)?.let {
                    if (it.name?.startsWith(searchedName, true) == true) {
                        searchedList.add(item)
                    }
                }
            }
            communicator?.showNoDataMessage(searchedList.size <= 0)
        }
        addHeaders(searchedList)
    }

    fun setOnItemCommunicatorListener(communicator: Communicator) {
        this.communicator = communicator
    }

    interface Communicator {
        fun showNoDataMessage(isDataNotAvailable: Boolean)
        fun onItemClick(isoCode: String?)
    }

}

