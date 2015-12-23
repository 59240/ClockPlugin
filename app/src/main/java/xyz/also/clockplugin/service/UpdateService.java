package xyz.also.clockplugin.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.provider.AlarmClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import xyz.also.clockplugin.R;
import xyz.also.clockplugin.utils.LunarCalendar;
import xyz.also.clockplugin.widget.ClockWidget;

/**
 * @author zl
 * @date 2015-12-19
 */
public class UpdateService extends Service {

	private SimpleDateFormat df = new SimpleDateFormat("HHmmss");

	public static Context context;
	public static AppWidgetManager appWidgetManager;
	public static RemoteViews remoteViews;

    private TextView clockTime;
    private TextView clockDate;
    private TextView clockWeek;

    private RemoteViews views;
    private ComponentName componentName;

    private static String year;
    private static String month;
    private static String day;
    private static String week;
    private static String hour;
    private static String minute;

    private String lunarMonth;
    private String lunarDate;

    private static final String TAG = "UpdateService";


    public void getData(){
        final Calendar c = Calendar.getInstance(Locale.CHINA);
        year = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        month = dateFormat(c.get(Calendar.MONTH) + 1);// 获取当前月份
        day = dateFormat(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        week = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        hour = dateFormat(c.get(Calendar.HOUR_OF_DAY));
        minute = dateFormat(c.get(Calendar.MINUTE));
        getLunarCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
        if("1".equals(week)){
            week ="周日";
        }else if("2".equals(week)){
            week ="周一";
        }else if("3".equals(week)){
            week ="周二";
        }else if("4".equals(week)){
            week ="周三";
        }else if("5".equals(week)){
            week ="周四";
        }else if("6".equals(week)){
            week ="周五";
        }else if("7".equals(week)){
            week ="周六";
        }
    }


    private String dateFormat(int number) {
        DecimalFormat df = new DecimalFormat("00");
        return df.format(number);
    }

    @Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	// 在服务创建时注册 监听系统时间的BroadcastReceiver
	@Override
	public void onCreate() {
		super.onCreate();

		Log.e("service", "--service created--");
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_TIME_TICK); // 时间的流逝
		intentFilter.addAction(Intent.ACTION_TIME_CHANGED); // 时间被改变，人为设置时间
		registerReceiver(boroadcastReceiver, intentFilter);
        init();
	}
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "桌面时钟服务启动了...");

		updateClock(); // 开始服务前先刷新一次UI
        //这里返回START_REDELIVER_INTENT，那每次停止Servcie后都会再启动一个新的。
        //那这个Servcie就不会停止了，用户会启动多个相同的Service
        return START_STICKY;
    }

	// 在服务停止时解注册BroadcastReceiver
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(boroadcastReceiver);
        //被系统干掉后，服务重启
        Intent intent = new Intent(getApplicationContext(), UpdateService.class);
        getApplication().startService(intent);
    }
	// 用于监听系统时间变化Intent.ACTION_TIME_TICK的BroadcastReceiver，此BroadcastReceiver须为动态注册
	private BroadcastReceiver boroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context acontext, Intent intent) {
			//          Log.e("time received", "--receive--");
			updateClock();
		}
	};

    private void init() {
//        Intent intent =new Intent();
//        intent.setAction("android.intent.action.SHOW_ALARMS");
        Intent intent = new Intent();

        intent.setAction("android.intent.action.SET_ALARM");
//        Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.deskclock");
//        startActivity(intent);

        PendingIntent Pfullintent = PendingIntent.getActivity(this, 0, startClock(), 0);
        views = new RemoteViews(getPackageName(), R.layout.widget_desktop_clock);
        views.setOnClickPendingIntent(R.id.clock, Pfullintent);
        // 将AppWidgetProvider的子类包装成ComponentName对象
        componentName = new ComponentName(this,
                ClockWidget.class);
        appWidgetManager = AppWidgetManager.getInstance(this);
    }

	private void updateClock() {

        getData();

        views.setTextViewText(R.id.clockTime, hour + ":" + minute);
        views.setTextViewText(R.id.clockDate, year + "-" + month + "-" + day);
        views.setTextViewText(R.id.clockWeek, week + "    农历" + lunarMonth + "月" + lunarDate);

        appWidgetManager.updateAppWidget(componentName, views);

	}

	public Intent startClock() {
        Intent intent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        if (isIntentAvailable(this, intent)) {

        } else {
            intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        }
        return intent;
	}

    private Intent intoPerSonalClock() {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);

        return intent;
    }

    private Intent doStartApplicationWithPackageName(String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        Intent intent = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return null;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
        }
        return intent;
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

    private void getLunarCalendar(int year, int month, int date) {
        LunarCalendar lunarCalendar = new LunarCalendar(year, month, date);
        lunarMonth = lunarCalendar.getLunarMonth();
        lunarDate = lunarCalendar.getLunarDate();
    }

}
