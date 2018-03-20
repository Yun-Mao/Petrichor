package yunmao.com.petrichor.api.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by msi on 2018/2/27.
 * description:数据库
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Petrichor.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper mDatabaseHelper = null;

    public static DatabaseHelper getInstance(Context context) {
        if (mDatabaseHelper == null) {
            synchronized (DatabaseHelper.class) {
                if (mDatabaseHelper == null) {
                    mDatabaseHelper = new DatabaseHelper(context);
                }
            }
        }
        return mDatabaseHelper;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists bookshelf("
                + "id integer primary key,"
                + "bookCount integer,"
                + "title varchar not null,"
                + "remark varchar,"
                + "orders integer,"
                + "create_at varchar not null)");
        db.execSQL("create table if not exists notes("
                + "id integer primary key,"
                + "title varchar not null,"
                + "paper varchar not null,"
                + "note varchar not null,"
                + "page integer not null,"
                + "orders integer,"
                + "create_at varchar not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
//                db.execSQL("alter table bookshelf add column 'order' integer");
            default:
                break;
        }
    }
}
