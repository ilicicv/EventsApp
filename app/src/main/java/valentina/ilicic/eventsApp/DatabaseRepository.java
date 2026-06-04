package valentina.ilicic.eventsApp;

import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class DatabaseRepository {
    private final DatabaseHelper dbHelper;
    public DatabaseRepository(DatabaseHelper dbHelper) {

        this.dbHelper = dbHelper;
    }

    // ---------------------------------- USERS TABLE  ---------------------------------------
    public boolean registerUser(String username, String email, String password, boolean isAdmin, String mongoId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (dbHelper.getUsersTable() != null) {
                return dbHelper.getUsersTable().registerUser(db, username, email, password, isAdmin, mongoId);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getUsersTable() != null) {
                return dbHelper.getUsersTable().usernameExists(db, username);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getUsersTable() != null) {
                return dbHelper.getUsersTable().emailExists(db, email);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public int loginUser(String username, String password, String mongoIdFromServer) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getUsersTable() != null) {
                return dbHelper.getUsersTable().loginUser(db, username, password, mongoIdFromServer);
            }
            return -1;
        } finally {
            db.close();
        }
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (dbHelper.getUsersTable() != null) {
                return dbHelper.getUsersTable().updatePassword(db, username, newPassword);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public int getLocalUserId(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getUsersTable() != null) {
                return dbHelper.getUsersTable().getLocalUserId(db, username);
            }
            return -1;
        } finally {
            db.close();
        }
    }
    public boolean isUserAdmin(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getUsersTable() != null) {
                return dbHelper.getUsersTable().isUserAdmin(db, username);
            }
            return false;
        } finally {
            db.close();
        }
    }


    public String getMongoUserId(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getUsersTable() != null) {
                return dbHelper.getUsersTable().getMongoUserId(db, username);
            }
            return null;
        } finally {
            db.close();
        }
    }

    // ---------------------------------- EVENTS TABLE  ---------------------------------------

    public boolean insertEvent(String mongoEventId, String title, String description, String location,
                               String dateTime, String category, int promoted, int capacity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (dbHelper.getEventsTable() != null) {
                return dbHelper.getEventsTable().insertEvent(db, mongoEventId, title, description, location, dateTime, category, promoted, capacity);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public ArrayList<Event> getAllEventsList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getEventsTable() != null) {
                return dbHelper.getEventsTable().getAllEventsList(db);
            }
            return new ArrayList<>();
        } finally {
            db.close();
        }
    }

    public ArrayList<Event> getEventsByCategoryList(String category) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getEventsTable() != null) {
                return dbHelper.getEventsTable().getEventsByCategoryList(db, category);
            }
            return new ArrayList<>();
        } finally {
            db.close();
        }
    }

    public Event getEventByName(String eventName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getEventsTable() != null) {
                return dbHelper.getEventsTable().getEventByName(db, eventName);
            }
            return null;
        } finally {
            db.close();
        }
    }

    public int getLocalEventId(String eventName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getEventsTable() != null) {
                return dbHelper.getEventsTable().getLocalEventId(db, eventName);
            }
            return -1;
        } finally {
            db.close();
        }
    }

    public String getMongoEventId(String eventName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getEventsTable() != null) {
                return dbHelper.getEventsTable().getMongoEventId(db, eventName);
            }
            return null;
        } finally {
            db.close();
        }
    }

    public boolean eventExists(String eventName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getEventsTable() != null) {
                return dbHelper.getEventsTable().eventExists(db, eventName);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public void deleteAllEvents() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (dbHelper.getEventsTable() != null) {
                dbHelper.getEventsTable().deleteAllEvents(db);
            }
        } finally {
            db.close();
        }
    }

    // ---------------------------------- ATTENDANCE TABLE  ---------------------------------------
    public int setInterested(int userId, int eventId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (dbHelper.getAttendanceTable() != null) {
                return dbHelper.getAttendanceTable().setInterested(db, userId, eventId);
            }
            return -2;
        } finally {
            db.close();
        }
    }

    public boolean removeInterested(int userId, String eventName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (dbHelper.getAttendanceTable() != null) {
                return dbHelper.getAttendanceTable().removeInterested(db, userId, eventName);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public int setAttending(int userId, int eventId, boolean isPromoted, int kapacitet, int trenutnoPrisutnih) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (dbHelper.getAttendanceTable() != null) {
                return dbHelper.getAttendanceTable().setAttending(db, userId, eventId, isPromoted, kapacitet, trenutnoPrisutnih);
            }
            return -2;
        } finally {
            db.close();
        }
    }

    public boolean removeAttending(int userId, String eventName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (dbHelper.getAttendanceTable() != null) {
                return dbHelper.getAttendanceTable().removeAttending(db, userId, eventName);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public String getAttendanceStatus(int userId, int eventId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getAttendanceTable() != null) {
                return dbHelper.getAttendanceTable().getAttendanceStatus(db, userId, eventId);
            }
            return null;
        } finally {
            db.close();
        }
    }

    public ArrayList<Event> getInterestedEvents(int userId, String status) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getAttendanceTable() != null) {
                return dbHelper.getAttendanceTable().getInterestedEvents(db, userId, status);
            }
            return new ArrayList<>();
        } finally {
            db.close();
        }
    }

    public ArrayList<Event> getAttendingEvents(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getAttendanceTable() != null) {
                return dbHelper.getAttendanceTable().getAttendingEvents(db, userId);
            }
            return new ArrayList<>();
        } finally {
            db.close();
        }
    }

    // ---------------------------------- RATINGS TABLE  ---------------------------------------
    public boolean insertEventRating(int userId, int eventId, int ratingValue) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (dbHelper.getRatingsTable() != null) {
                return dbHelper.getRatingsTable().insertRating(db, userId, eventId, ratingValue);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public boolean eventRated(int userId, int eventId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (dbHelper.getRatingsTable() != null) {
                return dbHelper.getRatingsTable().eventRated(db, userId, eventId);
            }
            return false;
        } finally {
            db.close();
        }
    }
}