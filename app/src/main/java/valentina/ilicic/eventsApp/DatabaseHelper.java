package valentina.ilicic.eventsApp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EventsApp.db";
    public static final int DATABASE_VERSION = 1;
    private Context context;
    private UsersTable usersTable;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        this.usersTable = (UsersTable) DatabaseTableFactory.getUsersTable(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DatabaseTable usersTable = DatabaseTableFactory.createTable("USERS", context);

        if (usersTable != null) {
            usersTable.createTable(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        DatabaseTable usersTable = DatabaseTableFactory.createTable("USERS", context);

        if (usersTable != null) {
            db.execSQL("DROP TABLE IF EXISTS " + usersTable.getTableName());
        }

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
}
