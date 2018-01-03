package pt.ubi.pdm.vivo.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import pt.ubi.pdm.vivo.Visit.Visit;
import pt.ubi.pdm.vivo.profile.Profile;


public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "vivo.db";

    // DB_VERSION will be used if I need to do any king of modification
    // If I modify the database, then DB_VERSION++ (increment)
    private static final int DB_VERSION = 1;

    // Tables
    private static final String Profile_Table = "profile";
    private static final String Notifications_Table = "notifications";

    // Visits Table is implemented and stores values but is USELESS for now
    // Just wasn't deleted because can still be needed at the FUTURE
    private static final String Visits_Table = "visits";

    // Profile Table Attributes
    private static final String User_id = "user_id";
    private static final String Token = "token";
    private static final String Email = "email";
    private static final String CreatedAt = "date";
    private static final String Username = "username";

    // Visits Table Attributes
    private static final String Visit_id = "visit_id";
    private static final String Visit_user = "user";
    private static final String Visit_date = "date";
    private static final String Visit_duration = "duration";

    // Notifications Table Attributes
    private static final String Notification_id = "notification_id";


    // Profile Table creation query
    private static final String CREATE_TABLE_PROFILE =
            "CREATE TABLE " + Profile_Table + "( Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    User_id + " VARCHAR(32) NOT NULL, " +
                    Username    + " VARCHAR(32), "           +
                    Email      + " VARCHAR(64), "          +
                    CreatedAt  + " DATETIME NOT NULL, "  +
                    Token  + " VARCHAR(64) NOT NULL);";

    // Visits Table creation query
    private static final String CREATE_TABLE_VISITS =
            "CREATE TABLE " + Visits_Table + "( Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Visit_id + " VARCHAR(32) NOT NULL, " +
                    Visit_user + " VARCHAR(32) NOT NULL, " +
                    Visit_date + " DATETIME NOT NULL, " +
                    Visit_duration  + " VARCHAR(12) NOT NULL);";

    // Visits Table creation query
    private static final String CREATE_TABLE_NOTIFICATIONS =
            "CREATE TABLE " + Notifications_Table + "( Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Notification_id + " VARCHAR(32) NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_PROFILE);
        sqLiteDatabase.execSQL(CREATE_TABLE_VISITS);
        sqLiteDatabase.execSQL(CREATE_TABLE_NOTIFICATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Profile_Table);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Visits_Table);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Notifications_Table);
        onCreate(sqLiteDatabase);
    }

    // Inserts Notification ID in Table Notifications, to be able
    // to verify if a received notification was already shown to the user
    public void storeNotificationDB ( String id ) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();

            values.put(Notification_id, id);
            db.insert(Notifications_Table, null, values);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Inserts Profile data in Table Profile
    public void storeProfileDB ( Profile p ) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            values.put(User_id, p.getUserId());
            values.put(Username, p.getUsername());
            values.put(Email, p.getEmail());
            values.put(CreatedAt, dateFormat.format(p.getCreatedAt()));
            values.put(Token, p.getToken());
            db.insert(Profile_Table, null, values);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    // Inserts Visit data in Table Visits
    public void storeVisitsDB (ArrayList<Visit> visits ) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            db.delete(Visits_Table, null, null);

            for (Visit v  : visits) {
                ContentValues values_visit = new ContentValues();

                values_visit.put(Visit_id, v.getId());
                values_visit.put(Visit_user, v.getUser());
                values_visit.put(Visit_date, dateFormat.format(v.getDate()));
                values_visit.put(Visit_duration, v.getDuration());

                db.insert(Visits_Table, null, values_visit);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean NotificationExists (String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        String columns[] = {Notification_id};
        Cursor cursor = db.query(Notifications_Table, columns, Notification_id + " = ?", new String[] { id }, null, null, null);

        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            cursor.close();
            return false;
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        cursor.close();
        return true;
    }

    public Bundle get_Id_Token() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        String columns[] = {User_id, Token};
        Cursor cursor = db.query(Profile_Table, columns, null, null, null, null, null);

        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            cursor.close();
            return null;
        }

        Bundle b = new Bundle();
        b.putString("id", cursor.getString(0));
        b.putString("token", cursor.getString(1));

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        cursor.close();
        return b;
    }

    public void removeDataFromDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            db.delete(Profile_Table, null, null);
            db.delete(Visits_Table, null, null);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}
