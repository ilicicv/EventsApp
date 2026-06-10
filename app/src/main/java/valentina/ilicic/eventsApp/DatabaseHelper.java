package valentina.ilicic.eventsApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EventsApp.db";
    public static final int DATABASE_VERSION = 2;
    private Context context;
    private UsersTable usersTable;
    private EventsTable eventsTable;
    private AttendanceTable attendanceTable;
    private RatingsTable ratingsTable;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        this.usersTable = (UsersTable) DatabaseTableFactory.getUsersTable(context);
        this.eventsTable = (EventsTable) DatabaseTableFactory.getEventsTable(context);
        this.attendanceTable = (AttendanceTable) DatabaseTableFactory.getAttendanceTable(context);
        this.ratingsTable = (RatingsTable) DatabaseTableFactory.getRatingsTable(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DatabaseTable usersTableInstance = DatabaseTableFactory.createTable("USERS", context);
        if (usersTableInstance != null) {
            usersTableInstance.createTable(db);
        }

        DatabaseTable eventsTableInstance = DatabaseTableFactory.createTable("EVENTS", context);
        if (eventsTableInstance != null) {
            eventsTableInstance.createTable(db);
        }

        DatabaseTable attendanceTableInstance = DatabaseTableFactory.createTable("ATTENDANCE", context);
        if (attendanceTableInstance != null) {
            attendanceTableInstance.createTable(db);
        }

        DatabaseTable ratingsTableInstance = DatabaseTableFactory.createTable("RATINGS", context);
        if (ratingsTableInstance != null) {
            ratingsTableInstance.createTable(db);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        if (oldVer < 2) {
            try {
                db.execSQL("ALTER TABLE " + UsersTable.TABLE_NAME +
                        " ADD COLUMN " + UsersTable.COL_IS_ADMIN + " INTEGER NOT NULL DEFAULT 0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public UsersTable getUsersTable() {
        return usersTable;
    }

    public EventsTable getEventsTable() {
        return eventsTable;
    }

    public AttendanceTable getAttendanceTable() {
        return attendanceTable;
    }

    public RatingsTable getRatingsTable() {
        return ratingsTable;
    }
}