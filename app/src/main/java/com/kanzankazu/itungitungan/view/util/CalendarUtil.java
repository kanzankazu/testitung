package com.kanzankazu.itungitungan.view.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CalendarUtil {
    private static final int MY_CAL_REQ = 12345;
    private static int MY_CAL_WRITE_REQ = 1234;
    /**/

    public static void addCalendar(Activity context) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Calendars.ACCOUNT_NAME, "cal@zoftino.com");
        contentValues.put(CalendarContract.Calendars.ACCOUNT_TYPE, "cal.zoftino.com");
        contentValues.put(CalendarContract.Calendars.NAME, "zoftino calendar");
        contentValues.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Zoftino.com Calendar");
        contentValues.put(CalendarContract.Calendars.CALENDAR_COLOR, "232323");
        contentValues.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        contentValues.put(CalendarContract.Calendars.OWNER_ACCOUNT, "cal@zoftino.com");
        contentValues.put(CalendarContract.Calendars.ALLOWED_REMINDERS, "METHOD_ALERT, METHOD_EMAIL, METHOD_ALARM");
        contentValues.put(CalendarContract.Calendars.ALLOWED_ATTENDEE_TYPES, "TYPE_OPTIONAL, TYPE_REQUIRED, TYPE_RESOURCE");
        contentValues.put(CalendarContract.Calendars.ALLOWED_AVAILABILITY, "AVAILABILITY_BUSY, AVAILABILITY_FREE, AVAILABILITY_TENTATIVE");

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        uri = uri.buildUpon().appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true").appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "cal@zoftino.com").appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "cal.zoftino.com").build();
        context.getContentResolver().insert(uri, contentValues);
    }

    public static void updateCalendar(View view, Activity context) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Zoftino.com GIGA Calendar");
        contentValues.put(CalendarContract.Calendars.NAME, "zoftino giga calendar");

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        String mSelectionClause = CalendarContract.Calendars.ACCOUNT_NAME + " = ? AND ";
        mSelectionClause = mSelectionClause + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?";
        String[] mSelectionArgs = {"cal@zoftino.com", "cal.zoftino.com"};

        int updCount = context.getContentResolver().update(uri, contentValues, mSelectionClause, mSelectionArgs);

    }

    public static void getDataFromCalendarTable(View v, Activity context) {
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();

        String[] mProjection =
                {
                        CalendarContract.Calendars.ALLOWED_ATTENDEE_TYPES,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                        CalendarContract.Calendars.CALENDAR_LOCATION,
                        CalendarContract.Calendars.CALENDAR_TIME_ZONE
                };

        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND (" + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND (" + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{"cal@zoftino.com", "cal.zoftino.com", "cal@zoftino.com"};

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }
        cur = cr.query(uri, mProjection, selection, selectionArgs, null);

        while (cur.moveToNext()) {
            String displayName = cur.getString(cur.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME));
            String accountName = cur.getString(cur.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME));

            TextView tv1 = new TextView(context);
            tv1.setText(displayName);
            //cont.addView(tv1);
        }

    }

    private long getCalendarId(Activity context, String MY_ACCOUNT_NAME) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            String[] projection = new String[]{CalendarContract.Calendars._ID};
            String selection = CalendarContract.Calendars.ACCOUNT_NAME + " = ? AND " + CalendarContract.Calendars.ACCOUNT_TYPE + " = ? ";
            // use the same values as above:
            String[] selArgs = new String[]{MY_ACCOUNT_NAME, CalendarContract.ACCOUNT_TYPE_LOCAL};
            Cursor cursor = context.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, projection, selection, selArgs, null);
            if (cursor.moveToFirst()) {
                return cursor.getLong(0);
            }
            return -1;
        } else {
            return 0;
        }

    }

    private void getAllCalendar(Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            String[] projection = new String[]{CalendarContract.Calendars._ID, CalendarContract.Calendars.NAME, CalendarContract.Calendars.ACCOUNT_NAME, CalendarContract.Calendars.ACCOUNT_TYPE};
            Cursor calCursor = context.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, projection, CalendarContract.Calendars.VISIBLE + " = 1", null, CalendarContract.Calendars._ID + " ASC");
            if (calCursor.moveToFirst()) {
                do {
                    long id = calCursor.getLong(0);
                    String displayName = calCursor.getString(1);

                } while (calCursor.moveToNext());
            }
        }
    }

    private void saveCalendar1(Activity context, String MY_ACCOUNT_NAME) {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Calendars.ACCOUNT_NAME, MY_ACCOUNT_NAME);
        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(CalendarContract.Calendars.NAME, "GrokkingAndroid Calendar");
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "GrokkingAndroid Calendar");
        values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0xffff0000);
        values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        values.put(CalendarContract.Calendars.OWNER_ACCOUNT, "some.account@googlemail.com");
        values.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, "Europe/Berlin");
        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        Uri.Builder builder = CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "com.grokkingandroid");
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true");
        Uri uri = context.getContentResolver().insert(builder.build(), values);
    }

    private void saveCalendarAllDay(Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

        }

        long calId = getCalendarId(context, "oke");
        if (calId == -1) {
            // no calendar account; react meaningfully
            return;
        }
        Calendar cal = new GregorianCalendar(2012, 11, 14);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long start = cal.getTimeInMillis();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, start);
        values.put(CalendarContract.Events.DTEND, start);
        values.put(CalendarContract.Events.RRULE, "FREQ=DAILY;COUNT=20;BYDAY=MO,TU,WE,TH,FR;WKST=MO");
        values.put(CalendarContract.Events.TITLE, "Some title");
        values.put(CalendarContract.Events.EVENT_LOCATION, "Münster");
        values.put(CalendarContract.Events.CALENDAR_ID, calId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Berlin");
        values.put(CalendarContract.Events.DESCRIPTION, "The agenda or some description of the event");
        // reasonable defaults exist:
        values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        values.put(CalendarContract.Events.SELF_ATTENDEE_STATUS, CalendarContract.Events.STATUS_CONFIRMED);
        values.put(CalendarContract.Events.ALL_DAY, 1);
        values.put(CalendarContract.Events.ORGANIZER, "some.mail@some.address.com");
        values.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, 1);
        values.put(CalendarContract.Events.GUESTS_CAN_MODIFY, 1);
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        Uri uri = context.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
        long eventId = new Long(uri.getLastPathSegment());
    }

    private void readCalendar(Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

        }

        long selectedEventId = 0;
        String[] proj = new String[]{CalendarContract.Events._ID, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, CalendarContract.Events.RRULE, CalendarContract.Events.TITLE};
        Cursor cursor = context.getContentResolver().query(CalendarContract.Events.CONTENT_URI, proj, CalendarContract.Events._ID + " = ? ", new String[]{Long.toString(selectedEventId)}, null);
        if (cursor.moveToFirst()) {
            // read event data
        }
    }

    private void deleteCalendar(Activity context, long selectedEventId) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

        }

        String[] selArgs = new String[]{Long.toString(selectedEventId)};
        int deleted = context.getContentResolver().delete(CalendarContract.Events.CONTENT_URI, CalendarContract.Events._ID + " =? ", selArgs);
    }

    private void updateCalendar(Activity context, long selectedEventId) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

        }

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, "Some new title");
        values.put(CalendarContract.Events.EVENT_LOCATION, "A new location");
        String[] selArgs = new String[]{Long.toString(selectedEventId)};
        int updated = context.getContentResolver().update(CalendarContract.Events.CONTENT_URI, values, CalendarContract.Events._ID + " =? ", selArgs);
    }
    /**/

    public void addEvent(View view, Activity context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        ContentResolver cr = context.getContentResolver();
        ContentValues contentValues = new ContentValues();

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2017, 2, 4, 9, 30);

        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 4, 4, 7, 35);

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, "Tech Stores");
        values.put(CalendarContract.Events.DESCRIPTION, "Successful Startups");
        values.put(CalendarContract.Events.CALENDAR_ID, 2);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/London");
        values.put(CalendarContract.Events.EVENT_LOCATION, "London");
        values.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, "1");
        values.put(CalendarContract.Events.GUESTS_CAN_SEE_GUESTS, "1");

        cr.insert(CalendarContract.Events.CONTENT_URI, values);
    }

    public void updateEvent(View view, Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events.TITLE, "Zoftino.com Tech Event");
        contentValues.put(CalendarContract.Events.EVENT_LOCATION, "NEW YORK");

        Uri uri = CalendarContract.Events.CONTENT_URI;

        String mSelectionClause = CalendarContract.Events.TITLE + " = ?";
        String[] mSelectionArgs = {"Tech Stores"};

        int updCount = context.getContentResolver().update(uri, contentValues, mSelectionClause, mSelectionArgs);

    }

    public void deleteEvent(View view, Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        Uri uri = CalendarContract.Events.CONTENT_URI;

        String mSelectionClause = CalendarContract.Events.TITLE + " = ?";
        String[] mSelectionArgs = {"Zoftino.com Tech Event"};

        int updCount = context.getContentResolver().delete(uri, mSelectionClause, mSelectionArgs);
    }

    public void getDataFromEventTable(View v, Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();

        String[] mProjection =
                {
                        "_id",
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.EVENT_LOCATION,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND,
                };

        Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = CalendarContract.Events.EVENT_LOCATION + " = ? ";
        String[] selectionArgs = new String[]{"London"};

        cur = cr.query(uri, mProjection, selection, selectionArgs, null);

        while (cur.moveToNext()) {
            String title = cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE));

            TextView tv1 = new TextView(context);
            tv1.setText(title);
            //cont.addView(tv1);
        }
    }
    /**/

    public void addInstance(View view, Activity context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        ContentResolver cr = context.getContentResolver();
        ContentValues contentValues = new ContentValues();

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2017, 02, 8, 11, 15);

        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 5, 8, 9, 45);

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Instances.EVENT_ID, "2");
        values.put(CalendarContract.Instances.BEGIN, beginTime.getTimeInMillis());
        values.put(CalendarContract.Instances.END, endTime.getTimeInMillis());

        cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
    }

    public void updateInstance(View view, Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }
        ContentValues contentValues = new ContentValues();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 8, 9, 9, 45);
        contentValues.put(CalendarContract.Instances.END, endTime.getTimeInMillis());

        Uri uri = CalendarContract.Instances.CONTENT_URI;

        String mSelectionClause = CalendarContract.Instances.EVENT_ID + " = ? ";
        String[] mSelectionArgs = {"2"};

        int updCount = context.getContentResolver().update(uri, contentValues, mSelectionClause, mSelectionArgs);

    }

    public void deleteIntance(View view, Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        Uri uri = CalendarContract.Instances.CONTENT_URI;

        String mSelectionClause = CalendarContract.Instances.EVENT_ID + " = ?";
        String[] mSelectionArgs = {"2"};

        int updCount = context.getContentResolver().delete(uri, mSelectionClause, mSelectionArgs);

    }

    public void getDataFromInstancesTable(View v, Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();

        String[] mProjection =
                {
                        CalendarContract.Instances.BEGIN,
                        CalendarContract.Instances.END,
                };

        Uri uri = CalendarContract.Instances.CONTENT_URI;
        String selection = CalendarContract.Instances.EVENT_ID + " = ? ";
        String[] selectionArgs = new String[]{"2"};

        cur = cr.query(uri, mProjection, selection, selectionArgs, null);

        while (cur.moveToNext()) {
            String begin = cur.getString(cur.getColumnIndex(CalendarContract.Instances.BEGIN));

            TextView tv1 = new TextView(context);
            tv1.setText(begin);
            //cont.addView(tv1);
        }

    }
    /**/

    public void addAttendee(View view, Activity context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        ContentResolver cr = context.getContentResolver();
        ContentValues contentValues = new ContentValues();

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Attendees.ATTENDEE_NAME, "KTR");
        values.put(CalendarContract.Attendees.ATTENDEE_TYPE, CalendarContract.Attendees.TYPE_REQUIRED);
        values.put(CalendarContract.Attendees.ATTENDEE_EMAIL, "ktr@example.com");
        values.put(CalendarContract.Attendees.ATTENDEE_STATUS, CalendarContract.Attendees.ATTENDEE_STATUS_INVITED);
        values.put(CalendarContract.Attendees.ATTENDEE_RELATIONSHIP, CalendarContract.Attendees.RELATIONSHIP_SPEAKER);
        values.put(CalendarContract.Attendees.EVENT_ID, "1");

        cr.insert(CalendarContract.Attendees.CONTENT_URI, values);
    }

    public void updateAttendee(View view, Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Attendees.ATTENDEE_TYPE, CalendarContract.Attendees.TYPE_OPTIONAL);

        Uri uri = CalendarContract.Attendees.CONTENT_URI;

        String mSelectionClause = CalendarContract.Attendees.ATTENDEE_STATUS + " = ? AND ";
        mSelectionClause = mSelectionClause + CalendarContract.Attendees.EVENT_ID + " = ?";
        String[] mSelectionArgs = {"" + CalendarContract.Attendees.ATTENDEE_STATUS_INVITED, "1"};

        int updCount = context.getContentResolver().update(uri, contentValues, mSelectionClause, mSelectionArgs);

    }

    public void deleteAttendee(View view, Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        Uri uri = CalendarContract.Attendees.CONTENT_URI;

        String mSelectionClause = CalendarContract.Attendees.ATTENDEE_STATUS + " = ?";
        String[] mSelectionArgs = {"" + CalendarContract.Attendees.ATTENDEE_STATUS_DECLINED};

        int updCount = context.getContentResolver().delete(uri, mSelectionClause, mSelectionArgs);

    }

    public void getDataFromAttendeesTable(View view, Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();

        String[] mProjection =
                {
                        CalendarContract.Attendees.ATTENDEE_NAME,
                        CalendarContract.Attendees.ATTENDEE_RELATIONSHIP,
                        CalendarContract.Attendees.ATTENDEE_EMAIL,
                        CalendarContract.Attendees.ATTENDEE_TYPE,
                };

        Uri uri = CalendarContract.Attendees.CONTENT_URI;
        String selection = CalendarContract.Attendees.ATTENDEE_TYPE + " = ? ";
        String[] selectionArgs = new String[]{"" + CalendarContract.Attendees.TYPE_REQUIRED};

        cur = cr.query(uri, mProjection, selection, selectionArgs, null);

        while (cur.moveToNext()) {
            String name = cur.getString(cur.getColumnIndex(CalendarContract.Attendees.ATTENDEE_NAME));

            TextView tv1 = new TextView(context);
            tv1.setText(name);
            //cont.addView(tv1);
        }

    }
    /**/

    public void addReminder(View view, Activity context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        ContentResolver cr = context.getContentResolver();
        ContentValues contentValues = new ContentValues();

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, "2");
        values.put(CalendarContract.Reminders.MINUTES, "25");
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALARM);

        cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
    }

    public void updateReminder(View view, Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_EMAIL);

        Uri uri = CalendarContract.Reminders.CONTENT_URI;

        String mSelectionClause = CalendarContract.Reminders.EVENT_ID + " = ? AND ";
        mSelectionClause = mSelectionClause + CalendarContract.Reminders.METHOD + " = ?";
        String[] mSelectionArgs = {"2", "" + CalendarContract.Reminders.METHOD_ALARM};

        int updCount = context.getContentResolver().update(uri, contentValues, mSelectionClause, mSelectionArgs);

    }

    public void deleteReminder(View view, Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        Uri uri = CalendarContract.Reminders.CONTENT_URI;

        String mSelectionClause = CalendarContract.Reminders.EVENT_ID + " = ?";
        String[] mSelectionArgs = {"2"};

        int updCount = context.getContentResolver().delete(uri, mSelectionClause, mSelectionArgs);

    }

    public void getDataFromRemindersTable(View view, Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();

        String[] mProjection =
                {
                        CalendarContract.Reminders.METHOD,
                        CalendarContract.Reminders.MINUTES,
                };

        Uri uri = CalendarContract.Reminders.CONTENT_URI;
        String selection = CalendarContract.Reminders.EVENT_ID + " = ? ";
        String[] selectionArgs = new String[]{"2"};

        cur = cr.query(uri, mProjection, selection, selectionArgs, null);

        while (cur.moveToNext()) {
            String minut = cur.getString(cur.getColumnIndex(CalendarContract.Reminders.MINUTES));

            TextView tv1 = new TextView(context);
            tv1.setText(minut);
            //cont.addView(tv1);
        }

    }
    /**/
}
