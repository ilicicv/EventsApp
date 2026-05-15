package valentina.ilicic.eventsApp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UsersTable implements DatabaseTable{
    private Context context;

    @Override
    public void createTable(SQLiteDatabase db) {

    }

    @Override
    public String getTableName() {
        return null;
    }

    public UsersTable(Context context) {
        this.context = context;
    }
}
