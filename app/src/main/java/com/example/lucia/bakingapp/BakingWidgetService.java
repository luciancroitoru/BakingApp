package com.example.lucia.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.TextView;

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
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                handleActionUpdateWidget();
            }
        }
    }

    private void handleActionUpdateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));

        String ingredients = Constants.WIDGET_INGREDIENTS;
        // Update widgets
        BakingWidgetProvider.updateBakingWidgets(this, appWidgetManager, appWidgetIds, ingredients);
    }
}
