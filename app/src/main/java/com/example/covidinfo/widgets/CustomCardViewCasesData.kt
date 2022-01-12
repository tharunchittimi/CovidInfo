package com.example.covidinfo.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.example.covidinfo.R

class CustomCardViewCasesData : LinearLayout {

    private var casesHeading: TextView? = null
    private var casesCount: TextView? = null
    private var newCasesHeading: TextView? = null
    private var newCasesCount: TextView? = null
    private var spaceDummy: Space? = null
    private var grpNewCases: Group? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (!isInEditMode)
            initWithAttrs(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (!isInEditMode)
            initWithAttrs(context, attrs, defStyleAttr)
    }

    private fun initWithAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val rootView =
            LayoutInflater.from(context).inflate(
                R.layout.inflate_card_view_cases_item, this
            )
        casesHeading = rootView.findViewById(R.id.tvCasesHeading)
        casesCount = rootView.findViewById(R.id.tvCasesCount)
        newCasesHeading = rootView.findViewById(R.id.tvNewCasesHeading)
        newCasesCount = rootView.findViewById(R.id.tvNewCasesCount)
        spaceDummy = rootView.findViewById(R.id.spaceDummy)
        grpNewCases = rootView.findViewById(R.id.grpNewCases)

        val a =
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CustomCardViewCasesData,
                defStyleAttr,
                0
            )
        val heading = a.getString(R.styleable.CustomCardViewCasesData_setHeading)
        val count = a.getString(R.styleable.CustomCardViewCasesData_setCasesCount)
        val newCasesHeading = a.getString(R.styleable.CustomCardViewCasesData_setNewCasesHeading)
        val newCasesCount = a.getString(R.styleable.CustomCardViewCasesData_setNewCasesCount)
        val casesTextColor = a.getColor(R.styleable.CustomCardViewCasesData_setCasesTextColor, -1)
        setCasesHeading(heading)
        setCasesCount(count)
        setNewCasesHeading(newCasesHeading)
        setNewCasesCount(newCasesCount)
        setCasesTextColor(casesTextColor)
        a.recycle()
    }

    fun setCasesHeading(heading: String?) {
        casesHeading?.text = heading
    }

    fun setCasesCount(count: String?) {
        casesCount?.text = count
    }

    fun setNewCasesHeading(heading: String?) {
        newCasesHeading?.text = heading
    }

    fun setNewCasesCount(count: String?) {
        newCasesCount?.text = count
    }

    fun setCasesTextColor(casesTextColor: Int?) {
        casesTextColor?.let { casesCount?.setTextColor(it) }
    }

    fun visibleExtraSpace(isShow: Boolean) {
        spaceDummy?.visibility = if (isShow) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun visibleNewCases(isShow: Boolean) {
        grpNewCases?.visibility = if (isShow) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}