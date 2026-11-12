package com.fenny.moon;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import java.text.NumberFormat;

public final class MoonWidgetProvider extends AppWidgetProvider {
    @Override public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        MoonPhase phase = new MoonPhase(System.currentTimeMillis());
        for (int id : ids) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.moon_widget);
            String illumination = NumberFormat.getPercentInstance().format(phase.illumination) + " illuminated";
            views.setTextViewText(R.id.phase, phase.name());
            views.setTextViewText(R.id.illumination, illumination);
            views.setImageViewBitmap(R.id.moon, MoonRenderer.render(phase));
            views.setContentDescription(R.id.moon, phase.name() + ", " + illumination);
            Intent refresh = new Intent(context, MoonWidgetProvider.class)
                    .setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                    .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{id});
            views.setOnClickPendingIntent(R.id.widget_root, PendingIntent.getBroadcast(context, id,
                    refresh, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE));
            manager.updateAppWidget(id, views);
        }
    }

    @Override public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (Intent.ACTION_TIME_CHANGED.equals(intent.getAction()) ||
                Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            onUpdate(context, manager, manager.getAppWidgetIds(new ComponentName(context, getClass())));
        }
    }
}
