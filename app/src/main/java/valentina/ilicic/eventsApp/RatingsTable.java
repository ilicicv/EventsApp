package valentina.ilicic.eventsApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RatingsTable implements DatabaseTable{
    public static final String TABLE_NAME = "ratings";
    public static final String COL_ID = "id";
    public static final String COL_USER_ID = "userId";
    public static final String COL_EVENT_ID = "eventId";
    public static final String COL_RATING = "rating";

    private Context context;

    public RatingsTable(Context context) {
        this.context = context;
    }

    @Override
    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID + " INTEGER NOT NULL, " +
                COL_EVENT_ID + " INTEGER NOT NULL, " +
                COL_RATING + " INTEGER NOT NULL, " +
                "CHECK (" + COL_RATING + " BETWEEN 1 AND 5), " +
                "UNIQUE(" + COL_USER_ID + ", " + COL_EVENT_ID + "), " +
                "FOREIGN KEY(" + COL_USER_ID + ") REFERENCES users(id), " +
                "FOREIGN KEY(" + COL_EVENT_ID + ") REFERENCES events(id)" +
                ")");
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public boolean insertRating(SQLiteDatabase db, int userId, int eventId, int ratingValue) {
        if (userId <= 0 || eventId <= 0) return false;

        try {
            if (eventRated(db, userId, eventId)) {
                return false;
            }

            ContentValues values = new ContentValues();
            values.put(COL_USER_ID, userId);
            values.put(COL_EVENT_ID, eventId);
            values.put(COL_RATING, ratingValue);

            long insertRate = db.insert(TABLE_NAME, null, values);

            if (insertRate != -1) {
                double[] avgCnt = getAverageAndCount(db, eventId);
                double newAverage = avgCnt[0];
                int newRatingCount = (int) avgCnt[1];

                ContentValues updateValues = new ContentValues();
                updateValues.put("prosecnaOcena", newAverage);
                updateValues.put("brojOcena", newRatingCount);

                int rowsAffected = db.update("events", updateValues, "id = ?", new String[]{String.valueOf(eventId)});
                return rowsAffected > 0;
            }
            return false;
        } catch (Exception e) {
            android.util.Log.e("DB_ERROR", "Greška u insertRating: ", e);
            return false;
        }
    }

    public boolean eventRated(SQLiteDatabase db, int userId, int eventId) {
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME,
                    new String[]{COL_ID},
                    COL_USER_ID + "=? AND " + COL_EVENT_ID + "=?",
                    new String[]{String.valueOf(userId), String.valueOf(eventId)},
                    null, null, null);

            return cursor != null && cursor.getCount() > 0;
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public double[] getAverageAndCount(SQLiteDatabase db, int eventId) {
        Cursor cursor = null;
        try {
            String query = "SELECT AVG(" + COL_RATING + "), COUNT(*) FROM " + TABLE_NAME +
                    " WHERE " + COL_EVENT_ID + " = ?";

            cursor = db.rawQuery(query, new String[]{String.valueOf(eventId)});

            if (cursor != null && cursor.moveToFirst()) {
                double avg = cursor.getDouble(0);
                double count = cursor.getDouble(1);
                return new double[]{avg, count};
            }
            return new double[]{0.0, 0.0};
        } catch (Exception e) {
            return new double[]{0.0, 0.0};
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
