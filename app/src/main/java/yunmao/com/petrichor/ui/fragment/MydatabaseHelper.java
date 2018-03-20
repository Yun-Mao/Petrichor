package yunmao.com.petrichor.ui.fragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by sweets on 17/4/14.
 */

public class MydatabaseHelper extends SQLiteOpenHelper{

    private static final String CREATE_BOOK = "CREATE TABLE IF NOT EXISTS localbook (bookname VARCHAR(255),bookurl VARCHAR(255))";
    private static final String CREATE_COLLECT = "CREATE TABLE IF NOT EXISTS collect (bookname VARCHAR(255),bookurl VARCHAR(255))";
    private static final String CREATE_USER = "CREATE TABLE IF NOT EXISTS user (account VARCHAR(255),password VARCHAR(255))";
    private static final String CREATE_LOGINUSER = "CREATE TABLE IF NOT EXISTS loginuser (account VARCHAR(255),password VARCHAR(255))";
    private Context mContext;

    public MydatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_COLLECT);
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_LOGINUSER);
        Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();
        //Log.d("TAG","Create succeeded");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
