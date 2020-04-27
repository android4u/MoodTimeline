package com.jemiola.moodtimeline.views.calendar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import com.jemiola.moodtimeline.R
import com.jemiola.moodtimeline.base.BaseFragment
import com.jemiola.moodtimeline.customviews.CalendarDayView
import com.jemiola.moodtimeline.customviews.CalendarMoodDayView
import com.jemiola.moodtimeline.databinding.FragmentCalendarBinding
import com.jemiola.moodtimeline.model.data.local.CircleMoodBO
import com.jemiola.moodtimeline.utils.ResUtil
import com.jemiola.moodtimeline.utils.disableFor
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class CalendarFragment : BaseFragment(), CalendarContract.View {

    override val presenter: CalendarPresenter by inject { parametersOf(this) }
    private lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        setupCalendarView()
        super.onStart()
    }

    private fun setupCalendarView() {
        presenter.setupCalendar()
        setupMonthChange()
    }

    private fun setupMonthChange() {
        val disableTime = 1000
        binding.arrowLeftImageView.setOnClickListener {
            it.disableFor(disableTime)
            presenter.openPreviousMonth()
        }
        binding.arrowRightImageView.setOnClickListener {
            it.disableFor(disableTime)
            presenter.openNextMonth()
        }
    }

    override fun setMonthName(monthText: String) {
        binding.monthTextView.text = monthText
    }

    override fun addNotCurrentMonthDay(day: Int) {
        context?.let { notNullContext ->
            val dayView = createDayView(notNullContext)
            binding.calendarDaysGridLayout.addView(dayView)
            dayView.day = day
            dayView.dayTextView.setTextColor(ResUtil.getColor(R.color.colorMoodNone))
            dayView.layoutParams = createCalendarDayLayoutParams()
        }
    }

    override fun addCurrentMonthDefaultDay(day: Int) {
        context?.let { notNullContext ->
            val dayView = createDayView(notNullContext)
            binding.calendarDaysGridLayout.addView(dayView)
            dayView.day = day
            dayView.dayTextView.setTextColor(ResUtil.getColor(R.color.colorTitle))
            dayView.layoutParams = createCalendarDayLayoutParams()
        }
    }

    override fun addCurrentMonthMoodDay(day: Int, mood: CircleMoodBO) {
        context?.let { notNullContext ->
            val moodDayView = createMoodDayView(notNullContext)
            binding.calendarDaysGridLayout.addView(moodDayView)
            moodDayView.day = day
            moodDayView.mood = mood
            moodDayView.layoutParams = createCalendarDayLayoutParams()
        }
    }

    private fun createCalendarDayLayoutParams(): GridLayout.LayoutParams {
        val dayLayoutParams = GridLayout.LayoutParams()
        dayLayoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        return dayLayoutParams
    }

    private fun createDayView(context: Context): CalendarDayView {
        return CalendarDayView(context)
    }

    private fun createMoodDayView(context: Context): CalendarMoodDayView {
        return CalendarMoodDayView(context)
    }

    override fun clearDaysInCalendar() {
        binding.calendarDaysGridLayout.removeAllViews()
    }
}