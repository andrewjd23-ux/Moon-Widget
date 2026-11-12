package com.fenny.moon;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import java.text.NumberFormat;

public final class MoonWidgetProvider extends AppWidgetProvider {
    @Override public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        for (int id : ids) updateOne(context, manager, id);
    }

    @Override public void onAppWidgetOptionsChanged(Context context,
            AppWidgetManager manager, int id, Bundle options) {
        super.onAppWidgetOptionsChanged(context, manager, id, options);
        updateOne(context, manager, id);
    }

    private void updateOne(Context context, AppWidgetManager manager, int id) {
        long now = System.currentTimeMillis();
        MoonPhase phase = new MoonPhase(now);
        Bundle options = manager.getAppWidgetOptions(id);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 40);
        int height = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, 40);
        boolean wide = width >= 115 && width >= height * 1.35;
        int layout = wide ? R.layout.moon_widget_wide : R.layout.moon_widget_compact;
        RemoteViews views = new RemoteViews(context.getPackageName(), layout);
        String label = MoonNames.label(phase, now);
        String illumination = NumberFormat.getPercentInstance().format(phase.illumination) + " illuminated";
        views.setImageViewBitmap(R.id.moon, MoonRenderer.render(phase));
        views.setContentDescription(R.id.moon, phase.name() + ", " + label + ", " + illumination);
        if (wide) {
            views.setTextViewText(R.id.phase, label);
            views.setTextViewText(R.id.illumination, illumination);
        }
        Intent refresh = new Intent(context, MoonWidgetProvider.class)
                .setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{id});
        views.setOnClickPendingIntent(R.id.widget_root, PendingIntent.getBroadcast(
                context, id, refresh,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE));
        manager.updateAppWidget(id, views);
    }

    @Override public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (Intent.ACTION_TIME_CHANGED.equals(intent.getAction()) ||
                Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            onUpdate(context, manager,
                    manager.getAppWidgetIds(new ComponentName(context, getClass())));
        }
    }
}
