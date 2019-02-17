package com.example.lucia.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.example.lucia.bakingapp.utils.Constants;

public class BakingWidgetService extends IntentService {

    private static final String ACTION_UPDATE_WIDGET = "com.example.android.bakingapp.action.update_widget";

    /**
     * Creates an IntentService
     */
    public BakingWidgetService() {
        super("BakingWidgetService");
    }

    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            //final String extra = intent.getStringExtra(Constants.WIDGET_INGREDIENTS);
            SharedPreferences preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
            //SharedPreferences.Editor editor = preferences.edit();
            String extra = preferences.getString("preference_ingredients", "nothing found");
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                handleActionUpdateWidget(extra);
            }
        }
    }

    private void handleActionUpdateWidget(String ingredients) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));

        // Update widgets
        BakingWidgetProvider.updateBakingWidgets(this, appWidgetManager, appWidgetIds, ingredients);
    }
}
