package valentina.ilicic.eventsApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class EventsTable implements DatabaseTable {

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

            return db.insert(TABLE_NAME, null, values) != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Event> getAllEventsList(SQLiteDatabase db) {
        ArrayList<Event> eventList = new ArrayList<>();
        String orderBy = COL_PROMOTED + " DESC, " + COL_DATUM_VREME + " ASC";
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, orderBy);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    eventList.add(mapCursorToEvent(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        sortEventsByPromotedAndDate(eventList);
        return eventList;
    }

    public ArrayList<Event> getEventsByCategoryList(SQLiteDatabase db, String category) {
        ArrayList<Event> eventList = new ArrayList<>();
        String selection = COL_KATEGORIJA + " = ?";
        String[] selectionArgs = new String[]{category};
        String orderBy = COL_PROMOTED + " DESC, " + COL_DATUM_VREME + " ASC";
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, orderBy);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    eventList.add(mapCursorToEvent(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        sortEventsByPromotedAndDate(eventList);
        return eventList;
    }

    public Event getEventByName(SQLiteDatabase db, String eventName) {
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, null, COL_NAZIV + " = ?", new String[]{eventName}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return mapCursorToEvent(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    public int getEventId(SQLiteDatabase db, String eventName) {
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, new String[]{COL_ID}, COL_NAZIV + " = ?", new String[]{eventName}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return -1;
    }

    private Event mapCursorToEvent(Cursor cursor) {
        String naziv = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAZIV));
        String opis = cursor.getString(cursor.getColumnIndexOrThrow(COL_OPIS));
        String lokacija = cursor.getString(cursor.getColumnIndexOrThrow(COL_LOKACIJA));
        String datumVreme = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATUM_VREME));
        String kategorija = cursor.getString(cursor.getColumnIndexOrThrow(COL_KATEGORIJA));
        int promoted = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PROMOTED));
        int kapacitet = cursor.getInt(cursor.getColumnIndexOrThrow(COL_KAPACITET));
        int prisutni = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BR_PRISUTNIH));
        double prosecnaOcena = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AVG_RATING));
        int brojOcena = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BR_OCENA));

        if (promoted == 1) {
            return EventFactory.createPromotedFromDb(
                    naziv, opis, lokacija, datumVreme, kategorija,
                    kapacitet, prisutni, prosecnaOcena, brojOcena
            );
        } else {
            return EventFactory.createRegularFromDb(
                    naziv, opis, lokacija, datumVreme, kategorija,
                    prisutni, prosecnaOcena, brojOcena
            );
        }
    }

    public static void sortEventsByPromotedAndDate(ArrayList<Event> list) {
        if (list == null || list.size() <= 1) return;

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

        Collections.sort(list, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                boolean past1 = e1.isPast();
                boolean past2 = e2.isPast();

                if (past1 != past2) {
                    return past1 ? 1 : -1;
                }

                int p1 = e1.isPromoted() ? 1 : 0;
                int p2 = e2.isPromoted() ? 1 : 0;

                if (p1 != p2) {
                    return Integer.compare(p2, p1);
                }

                try {
                    Date d1 = sdf.parse(e1.getDateTime());
                    Date d2 = sdf.parse(e2.getDateTime());
                    if (d1 != null && d2 != null) {
                        return d1.compareTo(d2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }
}