package com.example.a20134833.interc.decorators;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.a20134833.interc.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.time.Month;
import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private final Drawable drawable;
    private int color;
    private HashSet<CalendarDay> dates;

    public EventDecorator(int color, Collection<CalendarDay> dates,Activity context) {
        Log.e("EventDeco", String.valueOf(dates));
        drawable = context.getResources().getDrawable(R.drawable.more);
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
        view.addSpan(new DotSpan(5, Color.RED)); // 날자밑에 점
    }
}
