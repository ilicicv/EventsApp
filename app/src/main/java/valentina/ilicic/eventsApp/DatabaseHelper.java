package valentina.ilicic.eventsApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EventsApp.db";
    public static final int DATABASE_VERSION = 1;
    private Context context;
    private UsersTable usersTable;
    private EventsTable eventsTable;
    private AttendanceTable attendanceTable;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        this.usersTable = (UsersTable) DatabaseTableFactory.getUsersTable(context);
        this.eventsTable = (EventsTable) DatabaseTableFactory.getEventsTable(context);
        this.attendanceTable = (AttendanceTable) DatabaseTableFactory.getAttendanceTable(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DatabaseTable usersTableInstance = DatabaseTableFactory.createTable("USERS", context);
        if (usersTableInstance != null) usersTableInstance.createTable(db);

        DatabaseTable eventsTableInstance = DatabaseTableFactory.createTable("EVENTS", context);
        if (eventsTableInstance != null) {
            eventsTableInstance.createTable(db);

            EventsTable table = (EventsTable) eventsTableInstance;

            //Prošli / Regularni događaji
            insertInitialEvent(db, "Jakov Jozinović", "Koncert mlade muzičke senzacije.", "SPENS, Novi Sad", "10.11.2025 20:30", "Concert", 0, 0, 0);
            insertInitialEvent(db, "NEON Party", "Sjajan neon doživljaj uz najbolje DJ izvođače.", "Štark Arena, Beograd", "10.05.2026 22:00", "Party", 0, 0, 0);
            insertInitialEvent(db, "Predstava: Kir Janja", "Vrhunska izvedba večnog klasika Jovana Sterije Popovića na daskama teatra.", "Knjaževsko-srpski teatar, Kragujevac", "05.10.2025 20:00", "Stand-Up & Theater", 0, 0, 0);
            insertInitialEvent(db, "Jesenja izložba", "Godišnja izložba radova eminentnih likovnih umetnika Srbije.", "Galerija ULUS, Beograd", "20.09.2025 18:00", "Exhibition", 0, 0, 0);

            //Promovisani događaji
            insertInitialEvent(db, "EXIT Festival", "Najveći muzički festival u regiji.", "Petrovaradin, Novi Sad", "10.07.2026 18:00", "Festival", 1, 50000, 1);
            insertInitialEvent(db, "BITEF pod zvezdama", "Svečano otvaranje prestižnog beogradskog pozorišnog festivala.", "Stari Bitef Teatar, Beograd", "20.06.2026 21:00", "Stand-Up & Theater", 1, 350, 1);
            insertInitialEvent(db, "Belgrade Beer Fest", "Ogromni letnji festival piva, druženja i muzike na beogradskom Ušću.", "Ušće, Beograd", "12.08.2026 17:00", "Festival", 1, 15000, 0);

            //Regularni događaji
            insertInitialEvent(db, "Yu Grupa na Letnjoj", "Koncert legendarnog benda pod otvorenim nebom u Tvrđavi.", "Letnja pozornica, Niš", "18.05.2026 21:00", "Concert", 0, 0, 0);
            insertInitialEvent(db, "Party Barutana", "Letnja žurka svetski poznatih DJ-a na Kalemegdanu.", "Barutana, Beograd", "22.05.2026 23:00", "Party", 0, 0, 0);
            insertInitialEvent(db, "Beach Party Palić", "Provod na obali jezera uz najbolje letnje hitove.", "Ženski štrand, Palić", "15.07.2026 16:00", "Party", 0, 0, 0);
            insertInitialEvent(db, "Predstava: Gospođa ministarka", "Čuveno Nušićevo delo u izvedbi ansambla kragujevačkog pozorišta.", "Knjaževsko-srpski teatar, Kragujevac", "02.06.2026 20:00", "Stand-Up & Theater", 0, 0, 0);
            insertInitialEvent(db, "Lovefest 2026", "Tradicionalni i mega popularni festival elektronske muzike u srcu banje.", "Vrnjačka Banja", "06.08.2026 18:00", "Festival", 0, 0, 0);
            insertInitialEvent(db, "Noć muzeja u Subotici", "Godišnja manifestacija sa besplatnim ulazom i specijalnim umetničkim vođenjem.", "Savremena Galerija, Subotica", "28.05.2026 19:00", "Exhibition", 0, 0, 0);
            insertInitialEvent(db, "Jazz na Paliću", "Letnje muzičko veče džeza i promocija lokalnih vinarija sa Palića.", "Velika terasa, Palić", "10.06.2026 20:00", "Concert", 0, 0, 0);
            insertInitialEvent(db, "Latino Night u KC Grad", "Tradicionalno plesno latino veče pod otvorenim nebom Beograda.", "Kulturni Centar Grad, Beograd", "25.06.2026 21:00", "Party", 0, 0, 0);
            insertInitialEvent(db, "Stand-up: Srđan Dinčić", "Gostovanje i celovečernji šou jednog od najboljih srpskih komičara.", "Mts Dvorana, Beograd", "11.06.2026 20:00", "Stand-Up & Theater", 0, 0, 0);
            insertInitialEvent(db, "Summer Foam Party", "Veliki popodnevni i večernji pena party na otvorenim bazenima.", "Gradski Bazeni, Kragujevac", "28.07.2026 19:00", "Party", 0, 0, 0);
            insertInitialEvent(db, "Retrospektiva slika MSUB", "Istorijska postavka i izložba slika jugoslovenskog modernizma.", "Muzej Savremene Umetnosti, Beograd", "15.05.2026 12:00", "Exhibition", 0, 0, 0);
        }

        DatabaseTable attendanceTableInstance = DatabaseTableFactory.createTable("ATTENDANCE", context);
        if (attendanceTableInstance != null) {
            attendanceTableInstance.createTable(db);
        }

    }

    private void insertInitialEvent(SQLiteDatabase db, String title, String description, String location,
                                    String dateTime, String category, int promoted, int capacity, int initialAttending) {
        ContentValues values = new ContentValues();
        values.put(EventsTable.COL_NAZIV, title);
        values.put(EventsTable.COL_OPIS, description);
        values.put(EventsTable.COL_LOKACIJA, location);
        values.put(EventsTable.COL_DATUM_VREME, dateTime);
        values.put(EventsTable.COL_KATEGORIJA, category);
        values.put(EventsTable.COL_PROMOTED, promoted);
        values.put(EventsTable.COL_KAPACITET, capacity);
        values.put(EventsTable.COL_BR_PRISUTNIH, initialAttending);
        values.put(EventsTable.COL_AVG_RATING, 0.0);
        values.put(EventsTable.COL_BR_OCENA, 0);

        db.insert(EventsTable.TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        DatabaseTable usersTable = DatabaseTableFactory.createTable("USERS", context);
        if (usersTable != null) db.execSQL("DROP TABLE IF EXISTS " + usersTable.getTableName());

        DatabaseTable eventsTable = DatabaseTableFactory.createTable("EVENTS", context);
        if (eventsTable != null) db.execSQL("DROP TABLE IF EXISTS " + eventsTable.getTableName());

        DatabaseTable attendanceTable = DatabaseTableFactory.createTable("ATTENDANCE", context);
        if (attendanceTable != null) db.execSQL("DROP TABLE IF EXISTS " + attendanceTable.getTableName());

        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // -------------------------------------------------------------------------------------------

    public boolean registerUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (usersTable != null) {
                return usersTable.registerUser(db, username, email, password);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (usersTable != null) {
                return usersTable.usernameExists(db, username);
            }
            return false;
        } finally {
            db.close();
        }
    }
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (usersTable != null) {
                return usersTable.emailExists(db, email);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (usersTable != null) {
                return usersTable.loginUser(db, username, password);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (usersTable != null) {
                return usersTable.updatePassword(db, username, newPassword);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (usersTable != null) {
                return usersTable.getUserIdByUsername(db, username);
            }
            return -1;
        } finally {
            db.close();
        }
    }

    // -------------------------------------------------------------------------------------------

    public boolean insertEvent(String title, String description, String location,
                               String dateTime, String category, int promoted, int capacity) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (eventsTable != null) {
                return eventsTable.insertEvent(db, title, description, location, dateTime, category, promoted, capacity);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (eventsTable != null) {
            return eventsTable.getAllCategories(db);
        }
        return null;
    }

    public Cursor getEventsByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (eventsTable != null) {
            return eventsTable.getEventsByCategory(db, category);
        }
        return null;
    }

    public Cursor getEventByName(String eventName) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (eventsTable != null) {
            return eventsTable.getEventByName(db, eventName);
        }
        return null;
    }

    public int getEventIdByName(String eventName) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (eventsTable != null) {
                return eventsTable.getEventIdByName(db, eventName);
            }
            return -1;
        } finally {
            db.close();
        }
    }

    public ArrayList<Event> getAllEventsList() {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (eventsTable != null) {
                return eventsTable.getAllEventsList(db);
            }
            return new ArrayList<>();
        } finally {
            db.close();
        }
    }

    // -------------------------------------------------------------------------------------------

    public boolean setInterested(int userId, int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (attendanceTable != null) {
                return attendanceTable.addInterested(db, userId, eventId);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public boolean removeInterested(int userId, String eventName) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (attendanceTable != null) {
                return attendanceTable.removeInterested(db, userId, eventName);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public int setAttending(int userId, int eventId, boolean isPromoted, int kapacitet, int trenutnoPrisutnih) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (attendanceTable != null) {
                return attendanceTable.addAttending(db, userId, eventId, isPromoted, kapacitet, trenutnoPrisutnih);
            }
            return -2;
        } finally {
            db.close();
        }
    }

    public boolean removeAttending(int userId, String eventName) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (attendanceTable != null) {
                return attendanceTable.removeAttending(db, userId, eventName);
            }
            return false;
        } finally {
            db.close();
        }
    }

    public String getStatus(int userId, int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (attendanceTable != null) {
                return attendanceTable.getStatus(db, userId, eventId);
            }
            return null;
        } finally {
            db.close();
        }
    }

    public ArrayList<Event> getInterestedEvents(int userId, String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (attendanceTable != null) {
                return attendanceTable.getInterestedEvents(db, userId, status);
            }
            return new ArrayList<>();
        } finally {
            db.close();
        }
    }

    public ArrayList<Event> getAttendingEvents(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (attendanceTable != null) {
                return attendanceTable.getAttendingEvents(db, userId);
            }
            return new ArrayList<>();
        } finally {
            db.close();
        }
    }
}
