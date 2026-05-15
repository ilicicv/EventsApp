package valentina.ilicic.eventsApp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class EventsTable implements DatabaseTable{
    private Context context;

    @Override
    public void createTable(SQLiteDatabase db) {

    }

    @Override
    public String getTableName() {
        return null;
    }

    public EventsTable(Context context) {
        this.context = context;
    }
}
