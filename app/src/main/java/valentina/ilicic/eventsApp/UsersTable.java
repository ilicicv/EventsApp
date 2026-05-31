package valentina.ilicic.eventsApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UsersTable implements DatabaseTable{
    public static final String TABLE_NAME = "users";
    public static final String COL_ID = "id";
    public static final String COL_MONGO_ID = "mongo_id";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_LOZINKA = "lozinka";
    public static final String COL_IS_ADMIN = "is_admin";
    private Context context;

    @Override
    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MONGO_ID + " TEXT, " +
                COL_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COL_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COL_LOZINKA + " TEXT NOT NULL, " +
                COL_IS_ADMIN + " INTEGER NOT NULL DEFAULT 0)");
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
    public UsersTable(Context context) {
        this.context = context;
    }

    public boolean registerUser(SQLiteDatabase db, String username, String email, String password, boolean isAdmin, String mongoId) {
        try {
            ContentValues values = new ContentValues();
            values.put(COL_USERNAME, username);
            values.put(COL_EMAIL, email);
            values.put(COL_MONGO_ID, mongoId);
            values.put(COL_LOZINKA, PasswordHasher.hashPassword(password));
            values.put(COL_IS_ADMIN, isAdmin ? 1 : 0);

            long insertUser = db.insert(TABLE_NAME, null, values);
            return insertUser != -1;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean usernameExists(SQLiteDatabase db, String username) {
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, null, COL_USERNAME + " = ?", new String[]{username}, null, null, null);
            return cursor != null && cursor.getCount() > 0;
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public boolean emailExists(SQLiteDatabase db, String email) {
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, null, COL_EMAIL + " = ?", new String[]{email}, null, null, null);
            return cursor != null && cursor.getCount() > 0;
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public int loginUser(SQLiteDatabase db, String username, String enteredPassword, String mongoIdFromServer) {
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, new String[]{COL_LOZINKA, COL_IS_ADMIN, COL_MONGO_ID},
                    COL_USERNAME + " = ?", new String[]{username},null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String storedHashedPassword = cursor.getString(0);
                int isAdmin = cursor.getInt(1);

                if (PasswordHasher.verifyPassword(enteredPassword, storedHashedPassword)) {
                    // Ako ID iz servera nije isti kao lokalni azurira ga
                    String storedMongoId = cursor.getString(2);
                    if (!mongoIdFromServer.equals(storedMongoId)) {
                        ContentValues values = new ContentValues();
                        values.put(COL_MONGO_ID, mongoIdFromServer);
                        db.update(TABLE_NAME, values, COL_USERNAME + " = ?", new String[]{username});
                    }
                    return isAdmin;
                }
            }
            return -1;
        } catch (Exception e) {
            return -1;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public boolean updatePassword(SQLiteDatabase db, String username, String newPlainPassword) {
        try {
            ContentValues values = new ContentValues();

            String newHashedPassword = PasswordHasher.hashPassword(newPlainPassword);
            values.put(COL_LOZINKA, newHashedPassword);

            if (db.update(TABLE_NAME, values, COL_USERNAME + " = ?", new String[]{username}) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public int getLocalUserId(SQLiteDatabase db, String username) {
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, new String[]{COL_ID},  COL_USERNAME + " = ?", new String[]{username},
                    null, null, null
            );

            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            }
            return -1;
        } catch (Exception e) {
            return -1;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public String getMongoUserId(SQLiteDatabase db, String username) {
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, new String[]{COL_MONGO_ID}, COL_USERNAME + " = ?",
                    new String[]{username}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(COL_MONGO_ID));
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }
}
