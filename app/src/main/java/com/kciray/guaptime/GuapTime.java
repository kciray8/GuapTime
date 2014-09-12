/****************************************************************************
 **
 ** GuapTime - Android homescreen widget
 ** Copyright (C) 2014 Yaroslav (aka KciRay).
 ** Contact: Yaroslav (kciray8@gmail.com)
 **
 ** This program is free software: you can redistribute it and/or modify
 ** it under the terms of the GNU General Public License as published by
 ** the Free Software Foundation, either version 3 of the License, or
 ** (at your option) any later version.
 **
 ** This program is distributed in the hope that it will be useful,
 ** but WITHOUT ANY WARRANTY; without even the implied warranty of
 ** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 ** GNU General Public License for more details.
 **
 ** You should have received a copy of the GNU General Public License
 ** along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **
 *****************************************************************************/

package com.kciray.guaptime;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GuapTime extends AppWidgetProvider {
    public static final String ACTION_CLICK_ON_WIDGET = "com.kciray.action.clickOnWidget";
    private static final boolean DEBUG_MODE = false;
    static List<TimeSector> lessons = new ArrayList<>();

    static {
        lessons.add(TimeSector.getLesson(1, 9, 0, 90));
        lessons.add(TimeSector.getLesson(2, 10, 40, 90));
        lessons.add(TimeSector.getLesson(3, 12, 20, 90));
        lessons.add(TimeSector.getLesson(4, 14, 10, 90));
        lessons.add(TimeSector.getLesson(5, 15, 50, 90));
        lessons.add(TimeSector.getLesson(6, 17, 30, 90));

        //Evening lessons:
        lessons.add(TimeSector.getLesson(7, 19, 10, 80));
        lessons.add(TimeSector.getLesson(8, 20, 40, 80));
    }

    private static int testPointer = -1;
    //Pair <Hour, Minute>
    private static List<Pair<Integer, Integer>> tests = new ArrayList<>();

    static {
        if (DEBUG_MODE) {
            tests.add(new Pair<>(21, 55));
            tests.add(new Pair<>(22, 30));
            tests.add(new Pair<>(1, 23));
            tests.add(new Pair<>(3, 34));
            tests.add(new Pair<>(5, 16));
            tests.add(new Pair<>(7, 44));
            tests.add(new Pair<>(8, 31));

            tests.add(new Pair<>(9, 0));
            tests.add(new Pair<>(9, 1));
            tests.add(new Pair<>(9, 55));
            tests.add(new Pair<>(10, 20));
            tests.add(new Pair<>(10, 29));
            tests.add(new Pair<>(10, 30));
            tests.add(new Pair<>(10, 31));
            tests.add(new Pair<>(10, 36));
            tests.add(new Pair<>(10, 40));
            tests.add(new Pair<>(10, 41));
            tests.add(new Pair<>(10, 49));
            tests.add(new Pair<>(12, 15));
            tests.add(new Pair<>(13, 49));
            tests.add(new Pair<>(13, 50));
            tests.add(new Pair<>(13, 55));
            tests.add(new Pair<>(14, 8));
            tests.add(new Pair<>(14, 12));
            tests.add(new Pair<>(20, 35));
            tests.add(new Pair<>(20, 35));
        }
    }

    public static void updateWidgets(Context context) {
        updateWidgetsForClass(context, GuapTime.class);
        updateWidgetsForClass(context, GuapTime_3x1.class);
        updateWidgetsForClass(context, GuapTime_2x1.class);
        updateWidgetsForClass(context, GuapTimeKeyGuard.class);
    }

    private static void updateWidgetsForClass(Context context, Class cls) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        ComponentName componentName = new ComponentName(context, cls);
        int[] ids = appWidgetManager.getAppWidgetIds(componentName);
        updateWidgets(context, appWidgetManager, ids, cls);
    }

    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] ids, Class cls) {
        PendingIntent clickOnWidgetPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        for (int id : ids) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            remoteViews.setViewVisibility(R.id.second_str, View.VISIBLE);
            configureSpecificClass(context, remoteViews, cls);
            TimeSector timeSector = getTimeZoneNow();

            switch (timeSector.getType()) {
                case LESSON:
                    String evening = "";
                    if (timeSector.getNum() >= 7) {
                        evening = " (вечерняя)";
                    }
                    remoteViews.setTextViewText(R.id.first_str, timeSector.getNum() + " пара" + evening);
                    setSeconStrToEnd(context, timeSector, remoteViews);
                    break;
                case PAUSE:
                    remoteViews.setTextViewText(R.id.first_str,
                            "Перерыв между " + timeSector.num + " и " + (timeSector.num + 1) + " парой");

                    setSeconStrToEnd(context, timeSector, remoteViews);
                    break;
                case BEFORE_FIRST:
                    remoteViews.setTextViewText(R.id.first_str, "Утро");
                    int minutesCount = getMinToEnd(timeSector);
                    String minutes = context.getResources().getQuantityString(R.plurals.minutes, minutesCount);
                    remoteViews.setTextViewText(R.id.second_str, "До начала пар " + minutesCount + " " + minutes);
                    break;
                case MORNING:
                    remoteViews.setTextViewText(R.id.first_str, "Утро");
                    remoteViews.setViewVisibility(R.id.second_str, View.GONE);
                    break;
                case NIGHT:
                    remoteViews.setTextViewText(R.id.first_str, "Ночь");
                    remoteViews.setViewVisibility(R.id.second_str, View.GONE);
                    break;
            }

            remoteViews.setOnClickPendingIntent(R.id.main_widget_layout, clickOnWidgetPendingIntent);
            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }

    private static void configureSpecificClass(Context context, RemoteViews remoteViews, Class cls) {
        //DEFAULT = 27 and 12 SP
        if (cls.equals(GuapTime_2x1.class)) {
            if (!isLandscape(context)) {
                setFontSize(remoteViews, 27, 10);
            } else {
                setFontSize(remoteViews, 25, 10);
            }
        }
        if (cls.equals(GuapTimeKeyGuard.class)) {
            setFontSize(remoteViews, 42, 12);
        }
    }

    private static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private static void setFontSize(RemoteViews remoteViews, float logoSP, float textSP) {
        remoteViews.setFloat(R.id.logo, "setTextSize", logoSP);
        remoteViews.setFloat(R.id.first_str, "setTextSize", textSP);
        remoteViews.setFloat(R.id.second_str, "setTextSize", textSP);
    }

    private static void setSeconStrToEnd(Context context, TimeSector timeSector, RemoteViews remoteViews) {
        int minutesCount = getMinToEnd(timeSector);
        String minutes = context.getResources().getQuantityString(R.plurals.minutes, minutesCount);
        remoteViews.setTextViewText(R.id.second_str, "До конца " + minutesCount + " " + minutes);
    }

    public static Calendar getNowCalendarInstance() {
        Calendar calendar = Calendar.getInstance();
        if (DEBUG_MODE) {
            if ((testPointer >= 0) && (testPointer < tests.size())) {
                Pair<Integer, Integer> pair = tests.get(testPointer);
                calendar.set(Calendar.HOUR_OF_DAY, pair.first);
                calendar.set(Calendar.MINUTE, pair.second);
            }
        }
        return calendar;
    }

    public static Calendar getZeroCalendar() {
        Calendar zeroCalendar = Calendar.getInstance();
        zeroCalendar.setTimeInMillis(0);//Zero time
        zeroCalendar.setTimeZone(java.util.TimeZone.getTimeZone("GMT+0"));
        return zeroCalendar;
    }

    public static Calendar getBeginNowDayCalendar() {
        Calendar beginNowDay = Calendar.getInstance();
        beginNowDay.set(Calendar.HOUR_OF_DAY, 0);
        beginNowDay.set(Calendar.MINUTE, 0);
        beginNowDay.set(Calendar.SECOND, 0);
        beginNowDay.set(Calendar.MILLISECOND, 0);
        return beginNowDay;
    }

    private static TimeSector getTimeZoneNow() {
        Calendar nowCalendar = getNowCalendarInstance();
        Calendar beginNowDay = getBeginNowDayCalendar();

        long diffTime = nowCalendar.getTimeInMillis() - beginNowDay.getTimeInMillis();

        for (int i = 0; i < lessons.size(); i++) {
            TimeSector lesson = lessons.get(i);
            if ((diffTime >= lesson.getBeginTime()) && (diffTime <= (lesson.getEndTime()))) {
                return lesson;
            }
            //Have next lesson
            if (i != lessons.size() - 1) {
                TimeSector nextLesson = lessons.get(i + 1);
                TimeSector pause = TimeSector.getPause(lesson.beginTime,
                        nextLesson.beginTime - lesson.beginTime, lesson.num);

                if ((diffTime >= pause.getBeginTime()) && (diffTime <= (pause.getEndTime()))) {
                    return pause;
                }
            }
        }

        long hour = 1000 * 60 * 60;

        if ((diffTime > (hour * 4)) && (diffTime < (hour * 8))) {
            return TimeSector.getMorning();
        }
        if ((diffTime > (hour * 8)) && (diffTime < (hour * 9))) {
            return TimeSector.getTimeBeforeFirstLesson(hour * 8, hour * 1);
        }

        return TimeSector.getNight();
    }

    private static int getMinToEnd(TimeSector lesson) {
        Calendar beginNowDay = getBeginNowDayCalendar();
        Calendar nowCalendar = getNowCalendarInstance();
        long diff = beginNowDay.getTimeInMillis() + lesson.getEndTime() - nowCalendar.getTimeInMillis();

        return (int) ((diff / 1000) / 60) + 1;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d("tag", "onUpdate!");

        updateWidgets(context);
        ensureServiceRun(context);
    }

    private void ensureServiceRun(Context context) {
        if (!isMyServiceRunning(GuapTimeService.class, context)) {
            context.startService(new Intent(context, GuapTimeService.class));
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass, Context context) {

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static class TimeSector {
        public long duration;//ms
        private Type type;
        private int num;
        private long beginTime;//ms from 00:00

        private TimeSector(Type type) {
            this.type = type;
        }

        public static TimeSector getLesson(int num, int hours, int minutes, int durationMin) {
            TimeSector lesson = new TimeSector(Type.LESSON);
            lesson.num = num;

            Calendar zeroCalendar = getZeroCalendar();
            zeroCalendar.set(Calendar.HOUR_OF_DAY, hours);
            zeroCalendar.set(Calendar.MINUTE, minutes);
            lesson.beginTime = zeroCalendar.getTimeInMillis();
            lesson.duration = durationMin * 60 * 1000;
            return lesson;
        }

        public static TimeSector getPause(long beginMs, long durationMs, int afterLessonNum) {
            TimeSector timeSector = new TimeSector(Type.PAUSE);
            timeSector.num = afterLessonNum;
            timeSector.beginTime = beginMs;
            timeSector.duration = durationMs;
            return timeSector;
        }

        public static TimeSector getTimeBeforeFirstLesson(long beginMs, long durationMs) {
            TimeSector timeSector = new TimeSector(Type.BEFORE_FIRST);
            timeSector.beginTime = beginMs;
            timeSector.duration = durationMs;
            return timeSector;
        }

        public static TimeSector getNight() {
            return new TimeSector(Type.NIGHT);
        }

        public static TimeSector getEvening() {
            return new TimeSector(Type.EVENING);
        }

        public static TimeSector getMorning() {
            return new TimeSector(Type.MORNING);
        }

        public Type getType() {
            return type;
        }

        public int getNum() {
            return num;
        }

        public long getBeginTime() {
            return beginTime;
        }

        public long getEndTime() {
            return beginTime + duration;
        }

        public static enum Type {LESSON, PAUSE, BEFORE_FIRST, EVENING, NIGHT, MORNING}
    }
}
