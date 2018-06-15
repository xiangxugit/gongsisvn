package newwater.com.newwater.processpreserve;

import android.content.Context;

/**
 * Created by Administrator on 2018/5/17 0017.
 */

public class AlarmManagerUtil {
    public static void setAlarm(Context context, int flag, int hour, int minute, int id, int
            week, int soundOrVibrator, boolean isdelayClock, String drugname) {/*
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long intervalMillis = 0;
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), hour, minute, 0);
        if (flag == 0) {
            intervalMillis = 0;
        } else if (flag == 1) {
            intervalMillis = 24 * 3600 * 1000;
        } else if (flag == 2) {
            intervalMillis = 24 * 3600 * 1000 * 7;
        }
        Intent intent = new Intent(ALARM_ACTION);
        intent.putExtra("intervalMillis", intervalMillis);
        intent.putExtra("msg",drugname);
        intent.putExtra("id", id);
        intent.putExtra("soundOrVibrator", soundOrVibrator);
        intent.putExtra("isdelayclock",isdelayClock);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent,0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis
                    ()), intervalMillis, sender);
        } else {
            if (flag == 0) {
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            } else {
                am.setRepeating(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis
                        ()), intervalMillis, sender);
            }
        }
    }*/
    }




    public static void closeAlarms(){
       /* List<Alarm_setting> alarm_settings = DaoUtils.getDrugAlarm().loadAll();
        for (Alarm_setting alarm_setting:  alarm_settings) {
            AlarmManagerUtil.cancelAlarm(DemoApplication.applicationContext, alarm_setting.getAlarm_id());
        }*/
    }

    public static void setAlarms() {
//        String idcard = BaseSharedPreferences.getString(DemoApplication.applicationContext, "idcard");
//        //关闭所有闹钟
//        List<Alarm_setting> alarm_settings = DaoUtils.getDrugAlarm().loadAll();
//        for (Alarm_setting alarm_setting:  alarm_settings) {
//            AlarmManagerUtil.cancelAlarm(DemoApplication.applicationContext, alarm_setting.getAlarm_id());
//        }
//        Drug_user drug_user = DaoUtils.getDrugUser().loadByName(idcard);
//        if (drug_user!=null){
//            List<Drug_setting> drug_settings = DaoUtils.getDrugSetting().loadAll();
//            if (drug_settings!=null && drug_settings.size()>0){
//                for (Drug_setting drug_setting : drug_settings){
//                    Boolean isChecked = drug_setting.getDrug_alarm_status();
//                    for (Alarm_setting alarm_setting:drug_setting.getAlarmsettings()) {
//                        if (isChecked) {  //打开所有闹钟
//                            AlarmManagerUtil.setAlarm(DemoApplication.applicationContext, alarm_setting.getAlarm_hour(), alarm_setting.getAlarm_minute(), alarm_setting.getAlarm_id());
//                        } else {
//                            AlarmManagerUtil.cancelAlarm(DemoApplication.applicationContext, alarm_setting.getAlarm_id());
//                        }
//                    }
//
//                }
//            }
//        }
//    }
    }

}
