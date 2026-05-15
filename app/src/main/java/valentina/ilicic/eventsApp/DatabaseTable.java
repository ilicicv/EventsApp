package valentina.ilicic.eventsApp;

import android.database.sqlite.SQLiteDatabase;

public interface DatabaseTable {
    void createTable(SQLiteDatabase db);
    String getTableName();
}
