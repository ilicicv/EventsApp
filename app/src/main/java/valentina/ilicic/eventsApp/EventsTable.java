package valentina.ilicic.eventsApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class EventsTable implements DatabaseTable{

    public static final String TABLE_NAME = "events";

    public static final String COL_ID = "id";
    public static final String COL_NAZIV = "naziv";
    public static final String COL_OPIS = "opis";
    public static final String COL_LOKACIJA = "lokacija";
    public static final String COL_DATUM_VREME = "datumVreme";
    public static final String COL_KATEGORIJA = "kategorija";
    public static final String COL_PROMOTED = "promoted";
    public static final String COL_KAPACITET = "kapacitet";
    public static final String COL_BR_PRISUTNIH = "brojPrisutnih";
    public static final String COL_AVG_RATING = "prosecnaOcena";
    public static final String COL_BR_OCENA = "brojOcena";
    private Context context;

    @Override
    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAZIV + " TEXT NOT NULL, " +
                COL_OPIS + " TEXT, " +
                COL_LOKACIJA + " TEXT NOT NULL, " +
                COL_DATUM_VREME + " TEXT NOT NULL, " +
                COL_KATEGORIJA + " TEXT NOT NULL, " +
                COL_PROMOTED + " INTEGER DEFAULT 0 CHECK(" + COL_PROMOTED + " IN (0, 1)), " +
                COL_KAPACITET + " INTEGER DEFAULT 0 CHECK(" + COL_KAPACITET + " >= 0), " +
                COL_BR_PRISUTNIH + " INTEGER DEFAULT 0 CHECK(" + COL_BR_PRISUTNIH + " >= 0), " +
                COL_AVG_RATING + " REAL DEFAULT 0 CHECK(" + COL_AVG_RATING + " BETWEEN 0 AND 5), " +
                COL_BR_OCENA + " INTEGER DEFAULT 0 CHECK(" + COL_BR_OCENA + " >= 0), " +
                "CHECK (" + COL_PROMOTED + " = 0 OR " + COL_KAPACITET + " > 0), " +
                "CHECK (" + COL_PROMOTED + " = 0 OR " + COL_BR_PRISUTNIH + " <= " + COL_KAPACITET + ")" +
                ");");
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public EventsTable(Context context) {
        this.context = context;
    }

    public boolean insertEvent(SQLiteDatabase db, String naziv, String opis, String lokacija,
                               String datum, String kat, int promoted, int kapacitet) {
        try {
            ContentValues values = new ContentValues();
            values.put(COL_NAZIV, naziv);
            values.put(COL_OPIS, opis);
            values.put(COL_LOKACIJA, lokacija);
            values.put(COL_DATUM_VREME, datum);
            values.put(COL_KATEGORIJA, kat);
            values.put(COL_PROMOTED, promoted);
            values.put(COL_KAPACITET, kapacitet);

            values.put(COL_BR_PRISUTNIH, 0);
            values.put(COL_AVG_RATING, 0.0);
            values.put(COL_BR_OCENA, 0);

            if ( db.insert(TABLE_NAME, null, values) == -1 ) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public Cursor getAllEvents(SQLiteDatabase db) {
        return db.query(TABLE_NAME, null, null, null, null, null, COL_DATUM_VREME + " ASC");
    }

    public Cursor getAllCategories(SQLiteDatabase db) {
        return db.query(true, TABLE_NAME, new String[]{COL_KATEGORIJA},
                null, null, null, null, COL_KATEGORIJA + " ASC", null);
    }
    public Cursor getEventsByCategory(SQLiteDatabase db, String kategorija) {
        return db.query(TABLE_NAME, null, COL_KATEGORIJA + " = ?",
                new String[]{kategorija}, null, null, COL_DATUM_VREME + " ASC");
    }

    public Cursor getEventByName(SQLiteDatabase db, String eventName) {
        return db.query(TABLE_NAME, null, COL_NAZIV + " = ?",
                new String[]{eventName}, null, null, null);
    }

    public boolean registerAttendance(SQLiteDatabase db, int eventId) {
        try {
            Cursor cursor = db.query(TABLE_NAME, new String[]{COL_KAPACITET, COL_BR_PRISUTNIH},
                    COL_ID + " = ?", new String[]{String.valueOf(eventId)}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int kapacitet = cursor.getInt(0);
                int prisutni = cursor.getInt(1);
                cursor.close();

                if (prisutni < kapacitet) {
                    ContentValues values = new ContentValues();
                    values.put(COL_BR_PRISUTNIH, prisutni + 1);

                    int rows = db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(eventId)});
                    return rows > 0;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<Event> getAllEventsList(SQLiteDatabase db) {
        ArrayList<Event> eventList = new ArrayList<>();
        Cursor cursor = getAllEvents(db);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String naziv = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAZIV));
                String opis = cursor.getString(cursor.getColumnIndexOrThrow(COL_OPIS));
                String lokacija = cursor.getString(cursor.getColumnIndexOrThrow(COL_LOKACIJA));
                String datumVreme = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATUM_VREME));
                String kategorija = cursor.getString(cursor.getColumnIndexOrThrow(COL_KATEGORIJA));
                int promoted = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PROMOTED));
                int kapacitet = cursor.getInt(cursor.getColumnIndexOrThrow(COL_KAPACITET));
                int prisutni = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BR_PRISUTNIH));

                Event event = new Event(
                        naziv, opis, lokacija, datumVreme, kategorija,
                        android.R.drawable.ic_menu_gallery, promoted == 1,
                        kapacitet, prisutni, 0.0, 0
                );
                eventList.add(event);
            }
            cursor.close();
        }

        return eventList;
    }
}
