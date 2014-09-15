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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpaceAchievements {
    private static Random randomGenerator = new Random();
    private static List<String> achievments = new ArrayList<>();
    static{
        achievments.add("4 ноября 1957 г . Запущен первый искусственный спутник Земли. Начало космической эры");
        achievments.add("2 января 1959 г . Запуск космической ракеты «Мечта». Выход за пределы действия земного тяготения");
        achievments.add("12 сентября 1959 г . Запуск космического аппарата «Луна-2», достигшего поверхности Луны");
        achievments.add("4 октября 1959 г . Запуск космического аппарата «Луна-3». Он обогнул Луну, пройдя в 6200 км от её поверхности, и сфотографировал примерно 2/3 обратной стороны спутника Земли");
        achievments.add("12 апреля 1961 г . Юрий Гагарин на космическом корабле «Восток» совершил первый в мире полёт в космос");
        achievments.add("18 марта 1965 г . Первый выход в открытый космос осуществил Алексей Леонов («Восход-2»)");
        achievments.add("31 января 1966 г . Запуск космического аппарата «Луна-9» , который впервые в мире осуществил мягкую посадку на Луну и передал на Землю изображение лунной поверхности");
        achievments.add("19 апреля 1971 г . Вывод на орбиту первой орбитальной станции-лаборатории «Салют»");
        achievments.add("20 января 1978 г . Вывод на орбиту первого автоматического грузового транспортного корабля «Прогресс» ");
    }

    public static String getRandom(){
        int index = randomGenerator.nextInt(achievments.size());
        return achievments.get(index);
    }
}
