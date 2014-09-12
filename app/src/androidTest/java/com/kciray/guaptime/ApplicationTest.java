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

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.Calendar;
import java.util.TimeZone;

import static com.kciray.guaptime.GuapTime.*;

public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testCalendar2(){
        Calendar calendar = Calendar.getInstance();
        TimeZone timezone = TimeZone.getTimeZone("GMT+0");
        calendar.setTimeZone(timezone);
        calendar.setTimeInMillis(0);
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        assertEquals(15, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(15 * 60 * 60 * 1000 , calendar.getTimeInMillis());
    }

    public void testCalendar(){
        Calendar calendar = Calendar.getInstance();
        TimeZone timezone = TimeZone.getTimeZone("GMT+0");
        calendar.setTimeZone(timezone);
        calendar.setTimeInMillis(0);
        assertEquals(0, calendar.get(Calendar.HOUR));
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
    }

    public void testTime() {
        TimeSector lesson1 = TimeSector.getLesson(1, 9, 0, 90);
        assertEquals(9 * 60 * 60 * 1000, lesson1.getBeginTime());

        TimeSector dayLesson = TimeSector.getLesson(5, 15, 50, 90); //15:50 - lesson №5
        assertEquals(15 * 60 * 60 * 1000 + 50 * 60 * 1000, dayLesson.getBeginTime());
    }
}