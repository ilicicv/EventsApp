package valentina.ilicic.eventsApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class AttendanceTable implements DatabaseTable{
    public static final String TABLE_NAME = "attendance";

    public static final String COL_ID = "id";
    public static final String COL_USER_ID = "userId";
    public static final String COL_EVENT_ID = "eventId";
    public static final String COL_PRISUSTVO = "prisustvo";

    private Context context;

    public AttendanceTable(Context context) {
        this.context = context;
    }

    @Override
    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID + " INTEGER NOT NULL, " +
                COL_EVENT_ID + " INTEGER NOT NULL, " +
                COL_PRISUSTVO + " TEXT NOT NULL, " +
                "CHECK (" + COL_PRISUSTVO + " IN ('ZAINTERESOVAN', 'PRISUSTVUJE')), " +
                "UNIQUE(" + COL_USER_ID + ", " + COL_EVENT_ID + "), " +
                "FOREIGN KEY(" + COL_USER_ID + ") REFERENCES users(id), " +
                "FOREIGN KEY(" + COL_EVENT_ID + ") REFERENCES events(id)" +
                ")");
    }
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String getStatus(SQLiteDatabase db, int userId, int eventId) {
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME,
                    new String[]{COL_PRISUSTVO},
                    COL_USER_ID + "=? AND " + COL_EVENT_ID + "=?",
                    new String[]{String.valueOf(userId), String.valueOf(eventId)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                 return cursor.getString(0);
            }
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public boolean addInterested(SQLiteDatabase db, int userId, int eventId) {
        try {
            String trenutniStatus = getStatus(db, userId, eventId);

            if (trenutniStatus == null) {
                ContentValues values = new ContentValues();
                values.put(COL_USER_ID, userId);
                values.put(COL_EVENT_ID, eventId);
                values.put(COL_PRISUSTVO, "ZAINTERESOVAN");

                return db.insert(TABLE_NAME, null, values) != -1;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean removeInterested(SQLiteDatabase db, int userId, String eventName) {
        try {
            int rows = db.delete(TABLE_NAME,
                    COL_USER_ID + "=? AND " + COL_PRISUSTVO + "='ZAINTERESOVAN' AND " +
                            COL_EVENT_ID + " = (SELECT id FROM events WHERE naziv = ?)",
                    new String[]{String.valueOf(userId), eventName});
            return rows > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public int addAttending(SQLiteDatabase db, int userId, int eventId, boolean isPromoted, int kapacitet, int trenutnoPrisutnih) {
        try {
            String trenutniStatus = getStatus(db, userId, eventId);

            if ("PRISUSTVUJE".equals(trenutniStatus)) {
                return 0; // Već je prijavljen
            }

            if (isPromoted && (kapacitet - trenutnoPrisutnih) <= 0) {
                return -1; // nema slobodnih mesta
            }

            if ("ZAINTERESOVAN".equals(trenutniStatus)) {
                ContentValues values = new ContentValues();
                values.put(COL_PRISUSTVO, "PRISUSTVUJE");

                int rowsUpdated = db.update(TABLE_NAME, values,
                        COL_USER_ID + "=? AND " + COL_EVENT_ID + "=?",
                        new String[]{String.valueOf(userId), String.valueOf(eventId)});

                if (rowsUpdated > 0) {
                    db.execSQL("UPDATE events SET brojPrisutnih = brojPrisutnih + 1 WHERE id = ?", new Object[]{eventId});
                    return 1; // zainteresovan na prisustvuje
                }
            } else {
                ContentValues values = new ContentValues();
                values.put(COL_USER_ID, userId);
                values.put(COL_EVENT_ID, eventId);
                values.put(COL_PRISUSTVO, "PRISUSTVUJE");

                long id = db.insert(TABLE_NAME, null, values);
                if (id != -1) {
                    db.execSQL("UPDATE events SET brojPrisutnih = brojPrisutnih + 1 WHERE id = ?", new Object[]{eventId});
                    return 1; // prva prijava na događaj
                }
            }

            return -2; // update ili insert greska
        } catch (Exception e) {
            return -2; // greska sa bazom
        }
    }

    public boolean removeAttending(SQLiteDatabase db, int userId, String eventName) {
        try {
            int rows = db.delete(TABLE_NAME,
                    COL_USER_ID + "=? AND " + COL_PRISUSTVO + "='PRISUSTVUJE' AND " +
                            COL_EVENT_ID + " = (SELECT id FROM events WHERE naziv = ?)",
                    new String[]{String.valueOf(userId), eventName});

            if (rows > 0) {
                db.execSQL("UPDATE events SET brojPrisutnih = MAX(0, brojPrisutnih - 1) WHERE naziv = ?", new Object[]{eventName});
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }


    public ArrayList<Event> getInterestedEvents(SQLiteDatabase db, int userId, String status) {
        ArrayList<Event> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT e.* FROM events e " +
                    "INNER JOIN " + TABLE_NAME + " a ON e.id = a." + COL_EVENT_ID + " " +
                    "WHERE a." + COL_USER_ID + " = ? AND a." + COL_PRISUSTVO + " = ?";

            cursor = db.rawQuery(query, new String[]{String.valueOf(userId), status});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String naziv = cursor.getString(cursor.getColumnIndexOrThrow("naziv"));
                    String opis = cursor.getString(cursor.getColumnIndexOrThrow("opis"));
                    String lokacija = cursor.getString(cursor.getColumnIndexOrThrow("lokacija"));
                    String datumVreme = cursor.getString(cursor.getColumnIndexOrThrow("datumVreme"));
                    String kategorija = cursor.getString(cursor.getColumnIndexOrThrow("kategorija"));
                    int promoted = cursor.getInt(cursor.getColumnIndexOrThrow("promoted"));
                    int kapacitet = cursor.getInt(cursor.getColumnIndexOrThrow("kapacitet"));
                    int prisutni = cursor.getInt(cursor.getColumnIndexOrThrow("brojPrisutnih"));
                    double prosecnaOcena = cursor.getDouble(cursor.getColumnIndexOrThrow("prosecnaOcena"));
                    int brojOcena = cursor.getInt(cursor.getColumnIndexOrThrow("brojOcena"));

                    Event event = new Event(
                            naziv, opis, lokacija, datumVreme, kategorija,
                            android.R.drawable.ic_menu_gallery, promoted == 1,
                            kapacitet, prisutni, prosecnaOcena, brojOcena
                    );

                    list.add(event);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    public ArrayList<Event> getAttendingEvents(SQLiteDatabase db, int userId) {
        ArrayList<Event> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT e.* FROM events e " +
                    "INNER JOIN " + TABLE_NAME + " a ON e.id = a." + COL_EVENT_ID + " " +
                    "WHERE a." + COL_USER_ID + " = ? AND a." + COL_PRISUSTVO + " = 'PRISUSTVUJE'";

            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String naziv = cursor.getString(cursor.getColumnIndexOrThrow("naziv"));
                    String opis = cursor.getString(cursor.getColumnIndexOrThrow("opis"));
                    String lokacija = cursor.getString(cursor.getColumnIndexOrThrow("lokacija"));
                    String datumVreme = cursor.getString(cursor.getColumnIndexOrThrow("datumVreme"));
                    String kategorija = cursor.getString(cursor.getColumnIndexOrThrow("kategorija"));
                    int promoted = cursor.getInt(cursor.getColumnIndexOrThrow("promoted"));
                    int kapacitet = cursor.getInt(cursor.getColumnIndexOrThrow("kapacitet"));
                    int prisutni = cursor.getInt(cursor.getColumnIndexOrThrow("brojPrisutnih"));
                    double prosecnaOcena = cursor.getDouble(cursor.getColumnIndexOrThrow("prosecnaOcena"));
                    int brojOcena = cursor.getInt(cursor.getColumnIndexOrThrow("brojOcena"));

                    Event event = new Event(
                            naziv, opis, lokacija, datumVreme, kategorija,
                            android.R.drawable.ic_menu_gallery, promoted == 1,
                            kapacitet, prisutni, prosecnaOcena, brojOcena
                    );

                    list.add(event);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

}
