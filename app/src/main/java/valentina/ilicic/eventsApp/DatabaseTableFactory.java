package valentina.ilicic.eventsApp;

import android.content.Context;

import android.content.Context;

public class DatabaseTableFactory {

    public static DatabaseTable createTable(String tableName, Context context) {
        switch (tableName.toUpperCase()) {
            case "USERS":
                return new UsersTable(context);
            case "EVENTS":
                return new EventsTable(context);
            case "ATTENDANCE":
                return new AttendanceTable(context);
            case "RATINGS":
                return new RatingsTable(context);
            default:
                throw new IllegalArgumentException("Unknown table name: " + tableName);
        }
    }

    public static UsersTable getUsersTable(Context context) {
        return new UsersTable(context);
    }

    public static EventsTable getEventsTable(Context context) {
        return new EventsTable(context);
    }

    public static AttendanceTable getAttendanceTable(Context context) {
        return new AttendanceTable(context);
    }

    public static RatingsTable getRatingsTable(Context context) {
        return new RatingsTable(context);
    }
}
