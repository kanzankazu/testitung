package com.kanzankazu.itungitungan.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class DateTimeAlarmUtil extends DateTimeUtil {
    public static PendingIntent setPendingIntentMakeAlarm(Context context, Intent intent, int REQ_CODE) {
        return PendingIntent.getBroadcast(context, REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent setPendingIntentMake(Context context, Intent intent, int REQ_CODE) {
        return PendingIntent.getBroadcast(context, REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent setPendingIntentRemove(Context context, Intent intent, int REQ_CODE) {
        return PendingIntent.getBroadcast(context, REQ_CODE, intent, PendingIntent.FLAG_NO_CREATE);
    }

    public static boolean isAlarmActive(Context context, Intent intent, int REQ_CODE) {
        return PendingIntent.getBroadcast(context, REQ_CODE, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    public static void setAlarm(Activity activity, PendingIntent pendingIntent, int hourOfDay, int minute) {
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calSet.set(Calendar.MINUTE, minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if (calSet.compareTo(calNow) <= 0) {
            //jika ternyata waktu lewat maka alarm akan di atur untuk besok
            calSet.add(Calendar.DATE, 1);
            Toast.makeText(activity, "Alarm Has set At " + hourOfDay + ":" + minute + " Tomorrow", Toast.LENGTH_SHORT).show();
        }

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.v("AlarmManager", "Starting AlarmManager for >= KITKAT version");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
        } else {
            Log.v("AlarmManager", "Starting AlarmManager for < KITKAT version");
            alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
        }*/

        Toast.makeText(activity, "Alarm Has set At " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
    }

    public static void setAlarm(Context context, PendingIntent pendingIntent, int hourOfDay, int minute) {
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calSet.set(Calendar.MINUTE, minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if (calSet.compareTo(calNow) <= 0) {
            //jika ternyata waktu lewat maka alarm akan di atur untuk besok
            calSet.add(Calendar.DATE, 1);
            Toast.makeText(context, "Alarm Has set At " + hourOfDay + ":" + minute + " Tomorrow", Toast.LENGTH_SHORT).show();
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.v("AlarmManager", "Starting AlarmManager for >= KITKAT version");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
        } else {
            Log.v("AlarmManager", "Starting AlarmManager for < KITKAT version");
            alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
        }*/

        Toast.makeText(context, "Alarm Has set At " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
    }

    public static void setAlarm(Activity activity, Intent intent, int hourOfDay, int minute, int reqCode) {
        PendingIntent pendingIntent = DateTimeAlarmUtil.setPendingIntentMakeAlarm(activity, intent, reqCode);

        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calSet.set(Calendar.MINUTE, minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if (calSet.compareTo(calNow) <= 0) {
            //jika ternyata waktu lewat maka alarm akan di atur untuk besok
            calSet.add(Calendar.DATE, 1);
            Toast.makeText(activity, "Alarm Has set At " + hourOfDay + ":" + minute + " Tomorrow", Toast.LENGTH_SHORT).show();
        }

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.v("AlarmManager", "Starting AlarmManager for >= KITKAT version");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
        } else {
            Log.v("AlarmManager", "Starting AlarmManager for < KITKAT version");
            alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
        }*/

        Toast.makeText(activity, "Alarm Has set At " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
    }

    public static void setAlarm(Context context, Intent intent, int hourOfDay, int minute, int reqCode) {
        PendingIntent pendingIntent = DateTimeAlarmUtil.setPendingIntentMakeAlarm(context, intent, reqCode);

        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calSet.set(Calendar.MINUTE, minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if (calSet.compareTo(calNow) <= 0) {
            //jika ternyata waktu lewat maka alarm akan di atur untuk besok
            calSet.add(Calendar.DATE, 1);
            Toast.makeText(context, "Alarm Has set At " + hourOfDay + ":" + minute + " Tomorrow", Toast.LENGTH_SHORT).show();
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.v("AlarmManager", "Starting AlarmManager for >= KITKAT version");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
        } else {
            Log.v("AlarmManager", "Starting AlarmManager for < KITKAT version");
            alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
        }*/

        Toast.makeText(context, "Alarm Has set At " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param activity
     * @param pendingIntent
     * @param hourOfDay
     * @param minute
     * @param dayOfWeek     sun 1,mon 2, tue 3, wed 4, thu 5, fri 6, sat 7
     */
    public static void setAlarmRepeatDay(Activity activity, PendingIntent pendingIntent, int hourOfDay, int minute, int dayOfWeek) {
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calSet.set(Calendar.MINUTE, minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if (calSet.compareTo(calNow) <= 0) {
            //jika ternyata waktu lewat maka alarm akan di atur untuk besok
            calSet.add(Calendar.DATE, 1);
        }

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), WEEK_MILLIS, pendingIntent);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.v("AlarmManager", "Starting AlarmManager for >= KITKAT version");
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), WEEK_MILLIS, pendingIntent);
        } else {
            Log.v("AlarmManager", "Starting AlarmManager for < KITKAT version");
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), WEEK_MILLIS, pendingIntent);
        }*/

    }

    public static void setAlarmRepeatDay(Context context, PendingIntent pendingIntent, int hourOfDay, int minute, int dayOfWeek) {
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calSet.set(Calendar.MINUTE, minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if (calSet.compareTo(calNow) <= 0) {
            //jika ternyata waktu lewat maka alarm akan di atur untuk besok
            calSet.add(Calendar.DATE, 1);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), WEEK_MILLIS, pendingIntent);


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.v("AlarmManager", "Starting AlarmManager for >= KITKAT version");
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), WEEK_MILLIS, pendingIntent);
        } else {
            Log.v("AlarmManager", "Starting AlarmManager for < KITKAT version");
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), WEEK_MILLIS, pendingIntent);
        }*/

    }

    public static void setAlarmRepeatDay(Activity activity, Intent intent, int hourOfDay, int minute, int dayOfWeek, int reqCode) {
        PendingIntent pendingIntent = DateTimeAlarmUtil.setPendingIntentMakeAlarm(activity, intent, reqCode);

        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calSet.set(Calendar.MINUTE, minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if (calSet.compareTo(calNow) <= 0) {
            //jika ternyata waktu lewat maka alarm akan di atur untuk besok
            calSet.add(Calendar.DATE, 1);
        }

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), WEEK_MILLIS, pendingIntent);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.v("AlarmManager", "Starting AlarmManager for >= KITKAT version");
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), WEEK_MILLIS, pendingIntent);
        } else {
            Log.v("AlarmManager", "Starting AlarmManager for < KITKAT version");
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), WEEK_MILLIS, pendingIntent);
        }*/

    }

    public static void setAlarmRepeatDay(Context context, Intent intent, int hourOfDay, int minute, int dayOfWeek, int reqCode) {
        PendingIntent pendingIntent = DateTimeAlarmUtil.setPendingIntentMakeAlarm(context, intent, reqCode);

        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calSet.set(Calendar.MINUTE, minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if (calSet.compareTo(calNow) <= 0) {
            //jika ternyata waktu lewat maka alarm akan di atur untuk besok
            calSet.add(Calendar.DATE, 1);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), WEEK_MILLIS, pendingIntent);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.v("AlarmManager", "Starting AlarmManager for >= KITKAT version");
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), WEEK_MILLIS, pendingIntent);
        } else {
            Log.v("AlarmManager", "Starting AlarmManager for < KITKAT version");
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), WEEK_MILLIS, pendingIntent);
        }*/
    }

    public static void cancelAlarm(Activity activity, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static void cancelAlarm(Context context, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static void cancelAlarm(Activity activity, Intent intent, int reqCode) {
        PendingIntent pendingIntent = DateTimeAlarmUtil.setPendingIntentRemove(activity, intent, reqCode);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static void cancelAlarm(Context context, Intent intent, int reqCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = DateTimeAlarmUtil.setPendingIntentRemove(context, intent, reqCode);

        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static void cancelAllAlarm(Activity activity, Class<?> targetClass) {
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

        Intent updateServiceIntent = new Intent(activity, targetClass);
        PendingIntent pendingUpdateIntent = PendingIntent.getService(activity, 0, updateServiceIntent, 0);

        // Cancel alarms
        try {
            alarmManager.cancel(pendingUpdateIntent);
        } catch (Exception e) {
            Log.e("Lihat", "cancelAllAlarm DateTimeAlarmUtil : " + e.toString());
        }
    }

    public static void cancelAllAlarm(Context activity, Class<?> targetClass) {
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

        Intent updateServiceIntent = new Intent(activity, targetClass);
        PendingIntent pendingUpdateIntent = PendingIntent.getService(activity, 0, updateServiceIntent, 0);

        // Cancel alarms
        try {
            alarmManager.cancel(pendingUpdateIntent);
        } catch (Exception e) {
            Log.e("Lihat", "cancelAllAlarm DateTimeAlarmUtil : " + e.toString());
        }
    }

    /*public static void setAlarmSwitch(Activity mActivity, AlarmModel model, int isActive) {
        SQLiteHelper db = new SQLiteHelper(mActivity);

        String s;
        if (TextUtils.isEmpty(model.getAlarm_title())) {
            s = "";
        } else {
            s = "dan sudah waktunya " + model.getAlarm_title() + " . ";
        }

        Intent intent = new Intent(mActivity, AlarmReceiver.class);
        intent.putExtra("id", model.getAlarm_id());
        intent.putExtra("title", "Notifikasi " + model.getAlarm_title());
        intent.putExtra("msg", "Hay Kamu.. sekarang sudah jam " + model.getAlarm_hour() + ":" + model.getAlarm_minute() + " . " + s);
        intent.putExtra("record", model.getAlarm_voice_uri());
        intent.putExtra("ringtone", model.getAlarm_ringtone_uri());

        String alarm_day = model.getAlarm_day();
        if (!TextUtils.isEmpty(alarm_day)) {
            if (alarm_day.contains(",")) {
                String[] alarm_sub_ids = model.getAlarm_sub_id().split(",");
                String[] alarm_days = model.getAlarm_day().split(",");
                for (int i = 0; i < alarm_sub_ids.length; i++) {
                    String alarmSubId = alarm_sub_ids[i];
                    String alarmDay = alarm_days[i];

                    Intent intent1 = new Intent(mActivity, AlarmReceiver.class);
                    boolean alarmActive = DateTimeAlarmUtil.isAlarmActive(mActivity, intent1, Integer.parseInt(alarmSubId));

                    intent.putExtra("subid", SystemUtil.getLastString(alarmSubId, 4));

                    if (alarmActive && isActive == 0) {
                        DateTimeAlarmUtil.cancelAlarm(mActivity, intent1, Integer.parseInt(alarmSubId));
                    } else if (!alarmActive && isActive == 1) {
                        DateTimeAlarmUtil.setAlarmRepeatDay(mActivity, intent, Integer.parseInt(model.getAlarm_hour()), Integer.parseInt(model.getAlarm_minute()), Integer.parseInt(alarmDay), Integer.parseInt(alarmSubId));
                    }
                }
            } else {
                String alarmSubId = model.getAlarm_sub_id();

                Intent intent1 = new Intent(mActivity, AlarmReceiver.class);
                boolean alarmActive = DateTimeAlarmUtil.isAlarmActive(mActivity, intent1, Integer.parseInt(alarmSubId));

                intent.putExtra("subid", SystemUtil.getLastString(alarmSubId, 4));

                if (alarmActive && isActive == 0) {
                    DateTimeAlarmUtil.cancelAlarm(mActivity, intent1, Integer.parseInt(alarmSubId));
                } else if (!alarmActive && isActive == 1) {
                    DateTimeAlarmUtil.setAlarmRepeatDay(mActivity, intent, Integer.parseInt(model.getAlarm_hour()), Integer.parseInt(model.getAlarm_minute()), Integer.parseInt(alarm_day), Integer.parseInt(alarmSubId));
                }
            }
        } else {
            String alarmSubId = model.getAlarm_sub_id();
            Intent intent1 = new Intent(mActivity, AlarmReceiver.class);
            boolean alarmActive = DateTimeAlarmUtil.isAlarmActive(mActivity, intent1, Integer.parseInt(alarmSubId));

            intent.putExtra("subid", SystemUtil.getLastString(alarmSubId, 4));

            if (alarmActive && isActive == 0) {
                DateTimeAlarmUtil.cancelAlarm(mActivity, intent1, Integer.parseInt(alarmSubId));
            } else if (!alarmActive && isActive == 1) {
                DateTimeAlarmUtil.setAlarm(mActivity, intent, Integer.parseInt(model.getAlarm_hour()), Integer.parseInt(model.getAlarm_minute()), Integer.parseInt(alarmSubId));
            }
        }

        db.alarmSetActive(model.getAlarm_id(), isActive);
    }

    public static void setAlarmSwitch(Context context, AlarmModel model, int isActive) {
        SQLiteHelper db = new SQLiteHelper(context);

        String s;
        if (TextUtils.isEmpty(model.getAlarm_title())) {
            s = "";
        } else {
            s = "dan sudah waktunya " + model.getAlarm_title() + " . ";
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("id", model.getAlarm_id());
        intent.putExtra("title", "Notifikasi " + model.getAlarm_title());
        intent.putExtra("msg", "Hay Kamu.. sekarang sudah jam " + model.getAlarm_hour() + ":" + model.getAlarm_minute() + " . " + s);
        intent.putExtra("record", model.getAlarm_voice_uri());
        intent.putExtra("ringtone", model.getAlarm_ringtone_uri());

        String alarm_day = model.getAlarm_day();
        if (!TextUtils.isEmpty(alarm_day)) {
            if (alarm_day.contains(",")) {
                String[] alarm_sub_ids = model.getAlarm_sub_id().split(",");
                String[] alarm_days = model.getAlarm_day().split(",");
                for (int i = 0; i < alarm_sub_ids.length; i++) {
                    String alarmSubId = alarm_sub_ids[i];
                    String alarmDay = alarm_days[i];
                    Intent intent1 = new Intent(context, AlarmReceiver.class);
                    boolean alarmActive = DateTimeAlarmUtil.isAlarmActive(context, intent1, Integer.parseInt(alarmSubId));

                    Log.d("Lihat", "setAlarmSwitch DateTimeAlarmUtil : " + alarm_day.contains(","));
                    Log.d("Lihat", "setAlarmSwitch DateTimeAlarmUtil : " + alarmActive);
                    Log.d("Lihat", "setAlarmSwitch DateTimeAlarmUtil : " + isActive);

                    intent.putExtra("subid", SystemUtil.getLastString(alarmSubId, 4));

                    if (alarmActive && isActive == 0) {
                        DateTimeAlarmUtil.cancelAlarm(context, intent1, Integer.parseInt(alarmSubId));
                    } else if (!alarmActive && isActive == 1) {
                        DateTimeAlarmUtil.setAlarmRepeatDay(context, intent, Integer.parseInt(model.getAlarm_hour()), Integer.parseInt(model.getAlarm_minute()), Integer.parseInt(alarmDay), Integer.parseInt(alarmSubId));
                    }
                }
            } else {
                String alarmSubId = model.getAlarm_sub_id();
                Intent intent1 = new Intent(context, AlarmReceiver.class);
                boolean alarmActive = DateTimeAlarmUtil.isAlarmActive(context, intent1, Integer.parseInt(alarmSubId));

                Log.d("Lihat", "setAlarmSwitch DateTimeAlarmUtil : " + alarm_day.contains(","));
                Log.d("Lihat", "setAlarmSwitch DateTimeAlarmUtil : " + alarmActive);
                Log.d("Lihat", "setAlarmSwitch DateTimeAlarmUtil : " + isActive);

                intent.putExtra("subid", SystemUtil.getLastString(alarmSubId, 4));

                if (alarmActive && isActive == 0) {
                    DateTimeAlarmUtil.cancelAlarm(context, intent1, Integer.parseInt(alarmSubId));
                } else if (!alarmActive && isActive == 1) {
                    DateTimeAlarmUtil.setAlarmRepeatDay(context, intent, Integer.parseInt(model.getAlarm_hour()), Integer.parseInt(model.getAlarm_minute()), Integer.parseInt(alarm_day), Integer.parseInt(alarmSubId));
                }
            }
        } else {
            String alarmSubId = model.getAlarm_sub_id();
            Intent intent1 = new Intent(context, AlarmReceiver.class);
            boolean alarmActive = DateTimeAlarmUtil.isAlarmActive(context, intent1, Integer.parseInt(alarmSubId));

            Log.d("Lihat", "setAlarmSwitch DateTimeAlarmUtil : " + alarmActive);
            Log.d("Lihat", "setAlarmSwitch DateTimeAlarmUtil : " + isActive);

            intent.putExtra("subid", SystemUtil.getLastString(alarmSubId, 4));

            if (alarmActive && isActive == 0) {
                DateTimeAlarmUtil.cancelAlarm(context, intent1, Integer.parseInt(alarmSubId));
            } else if (!alarmActive && isActive == 1) {
                DateTimeAlarmUtil.setAlarm(context, intent, Integer.parseInt(model.getAlarm_hour()), Integer.parseInt(model.getAlarm_minute()), Integer.parseInt(alarmSubId));
            }
        }

        db.alarmSetActive(model.getAlarm_id(), isActive);
    }*/

    public long addAppointmentsToCalender(Activity curActivity, String title, String desc, String place, int status, long startDate, boolean needReminder, boolean needMailService) {
        /***************** Event: add event *******************/
        long eventID = -1;
        try {
            String eventUriString = "content://com.android.calendar/events";
            ContentValues eventValues = new ContentValues();
            eventValues.put("calendar_id", 1); // id, We need to choose from
            // our mobile for primary its 1
            eventValues.put("title", title);
            eventValues.put("description", desc);
            eventValues.put("eventLocation", place);

            long endDate = startDate + 1000 * 10 * 10; // For next 10min
            eventValues.put("dtstart", startDate);
            eventValues.put("dtend", endDate);

            // values.put("allDay", 1); //If it is bithday alarm or such kind (which should remind me for whole day) 0 for false, 1 for true

            eventValues.put("eventStatus", status); // This information is
            // sufficient for most entries tentative (0),confirmed (1) or canceled(2):

            eventValues.put("eventTimezone", "UTC/GMT +7:00");
            /*
             * Comment below visibility and transparency column to avoid
             * java.lang.IllegalArgumentException column visibility is invalid
             * error
             */

            // eventValues.put("visibility", 0); // visibility to default (0),confidential (1), private (2), or public (3):
            // eventValues.put("transparency", 0); // You can control whether an event consumes time opaque (0) or transparent (1).

            eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

            Uri eventUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(eventUriString), eventValues);
            eventID = Long.parseLong(eventUri.getLastPathSegment());

            /***************** Event: Reminder(with alert) Adding reminder to event ***********        ********/
            if (needReminder) {

                String reminderUriString = "content://com.android.calendar/reminders";
                ContentValues reminderValues = new ContentValues();
                reminderValues.put("event_id", eventID);
                reminderValues.put("minutes", 5); // Default value of the system. Minutes is a integer
                reminderValues.put("method", 1); // Alert Methods: Default(0),Alert(1), Email(2),SMS(3)

                Uri reminderUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
            }

            /***************** Event: Meeting(without alert) Adding Attendies to the meeting *******************/
            if (needMailService) {
                String attendeuesesUriString = "content://com.android.calendar/attendees";
                /********
                 * To add multiple attendees need to insert ContentValues
                 * multiple times
                 ***********/
                ContentValues attendeesValues = new ContentValues();
                attendeesValues.put("event_id", eventID);
                attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
                attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee Email
                attendeesValues.put("attendeeRelationship", 0);
                // Relationship_Attendee(1),Relationship_None(0),Organizer(2),Performer(3),Speaker(4)
                attendeesValues.put("attendeeType", 0);
                // None(0), Optional(1),Required(2),Resource(3)
                attendeesValues.put("attendeeStatus", 0);
                // NOne(0),Accepted(1),Decline(2),Invited(3),Tentative(4)

                Uri eventsUri = Uri.parse("content://calendar/events");
                Uri url = curActivity.getApplicationContext().getContentResolver().insert(eventsUri, attendeesValues);

                // Uri attendeuesesUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(attendeuesesUriString), attendeesValues);
            }
        } catch (Exception ex) {
            Log.e("Lihat", "addAppointmentsToCalender DateTimeAlarmUtil : " + ex.getMessage());
        }

        return eventID;

    }
}


