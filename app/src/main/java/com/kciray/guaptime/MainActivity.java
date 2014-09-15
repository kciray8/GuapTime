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

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MainActivity extends ActionBarActivity {
    TextView versionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        StringBuilder aboutText = new StringBuilder()
                .append("Версия ").append(getVersionName()).append("\n")
                .append("Дата сборки ").append(getLastUpdateTime()).append("\n")
                .append("Разработчик - Ярослав aka KciRay").append("\n")
                .append("Лицензия - GNU GPL v3").append("\n")
                .append("4 факультет, группа 4141").append("\n")
                .append("Группа - http://vk.com/guaptime").append("\n")
                .append("Почта - kciray8@gmail.com");

        versionView = (TextView) findViewById(R.id.versionView);
        versionView.setText(aboutText);

        findViewById(R.id.open_table).setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://rasp.guap.ru"));
            startActivity(browserIntent);
        });

        findViewById(R.id.open_vk).setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vk.com/guaptime"));
            startActivity(browserIntent);
        });

        findViewById(R.id.open_google_play).setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.kciray.guaptime"));
            startActivity(browserIntent);
        });

        findViewById(R.id.open_github).setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/kciray8/GuapTime"));
            startActivity(browserIntent);
        });

        findViewById(R.id.open_email).setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"kciray8@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Отзыв по виджету GuapTime");
            try {
                startActivity(Intent.createChooser(i, "Отправить письмо..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "У вас нет почтовых клиентов!", Toast.LENGTH_SHORT).show();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                String achievement = SpaceAchievements.getRandom();
                Toast.makeText(this, achievement, Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }

    private String getVersionName() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getLastUpdateTime() {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(getAppBuildTime()));
    }

    long cachedAppBuildTime = -1;

    private long getAppBuildTime() {
        if (cachedAppBuildTime == -1) {
            try {
                ApplicationInfo ai = getApplicationContext().getPackageManager().getApplicationInfo(getApplicationContext().getPackageName(), 0);
                ZipFile zf = new ZipFile(ai.sourceDir);
                ZipEntry ze = zf.getEntry("classes.dex");
                cachedAppBuildTime = ze.getTime();
            } catch (Throwable t) {
                return 0;
            }
        }
        return cachedAppBuildTime;
    }
}
