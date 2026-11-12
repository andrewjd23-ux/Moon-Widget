package com.fenny.moon;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class MainActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(32, 32, 32, 32);
        layout.setBackgroundColor(Color.rgb(17, 24, 40));
        MoonPhase phase = new MoonPhase(System.currentTimeMillis());
        ImageView moon = new ImageView(this);
        moon.setImageBitmap(MoonRenderer.render(phase));
        moon.setContentDescription(phase.name());
        layout.addView(moon, new LinearLayout.LayoutParams(240, 240));
        TextView title = new TextView(this);
        title.setText("Moon Phase\n" + phase.name());
        title.setTextColor(Color.WHITE);
        title.setTextSize(26);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);
        TextView help = new TextView(this);
        help.setText("Your moon, at a glance.\n\nAdd the widget here, or long-press your home screen, choose Widgets, and find Moon Phase.\n\nUpdates about every 30 minutes. Tap the widget to refresh.\n\nUses an approximate lunar cycle, with north-up orientation.");
        help.setTextColor(Color.rgb(170, 187, 214));
        help.setTextSize(16);
        help.setGravity(Gravity.CENTER);
        help.setPadding(0, 32, 0, 32);
        layout.addView(help);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        if (manager.isRequestPinAppWidgetSupported()) {
            Button add = new Button(this);
            add.setText("Add widget");
            add.setOnClickListener(v -> manager.requestPinAppWidget(
                    new ComponentName(this, MoonWidgetProvider.class), null, null));
            layout.addView(add);
        }
        setContentView(layout);
    }
}
