package xyz.also.clockplugin.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import xyz.also.clockplugin.service.UpdateService;

/**
 * Created by win on 2015/12/19.
 */
public class ClockWidget extends AppWidgetProvider {
    private Intent intent;

    public static final String CLICK_ACTION = "xyz.also.clockplugin.click";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        intent = new Intent(context, UpdateService.class);
        context.startService(intent);
        // 获取AppWidget对应的视图
       /* RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_desktop_clock);
        Intent alarmas = new Intent(AlarmClock.ACTION_SET_ALARM);
//        startActivity(alarmas);
        // 设置响应 “按钮(bt_refresh)” 的intent
        Intent btIntent = new Intent().setAction(CLICK_ACTION);
        PendingIntent btPendingIntent = PendingIntent.getBroadcast(context, 0, btIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.bt_refresh, btPendingIntent);*/
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        intent = new Intent(context, UpdateService.class);
        context.stopService(intent);
        super.onDeleted(context, appWidgetIds);
    }
    //第一次往桌面添加Widgets时会被调用，之后添加同类型Widgets不会被调用
    public void onEnabled(Context context) {
        context.startService(new Intent(context, UpdateService.class));
//        context.startService(new Intent(context, TimerService.class));
    }
    //从桌面上删除最后一个Widgets时会被调用
    public void onDisabled(Context context) {
        context.stopService(new Intent(context, UpdateService.class));
    }
}

