package tools.zhang.com.mytools.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zhangdecheng on 2016/11/7.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "videolist.db";

    public static final String TABLE_NAME = "video";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 如果数据库文件不存在，只有onCreate()被调用（该方法在创建数据库时被调用一次）
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "onCreate: ");
        String CREATEFILEDBSQL = "CREATE TABLE IF NOT EXISTS %1$s(" +
                "%2$s TEXT PRIMARY KEY, " +
                "%3$s INTEGER," +
                "%4$s TEXT," +
                "%5$s TEXT," +
                "%6$s TEXT" +
                ");";

        String sql = String.format(CREATEFILEDBSQL,
                TABLE_NAME,
                "_id",
                "age",
                "name",
                "phone",
                "test"
        );

        try {
            db.execSQL(sql);
        } catch (Exception e) {
            Log.e(TAG, "onCreate: error", e);
        }
    }

    /**
     * 当系统在构造SQLiteOpenHelper类的对象时，如果发现版本号不一样，就会自动调用onUpgrade函数，让你在这里对数据库进行升级。
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "onUpgrade: oldVersion:" + oldVersion + " , newVersion:" + newVersion);

        /**
         * 如此会导致以前的数据丢失
         */
        String tempTableName = null;
        try {
            if (DATABASE_VERSION == newVersion) {
                String tableName = TABLE_NAME;
                db.beginTransaction();

                // 1, Rename table.
                tempTableName = tableName + "_temps";
                String sql = "ALTER TABLE " + tableName + " RENAME TO " + tempTableName;
                db.execSQL(sql);

                // 2, Create table.
                onCreate(db);
            }

            // 3, Drop the temporary table.
            db.execSQL("DROP TABLE IF EXISTS " + tempTableName);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "onUpgrade: error", e);
        } finally {
            if ( db != null ) {
                db.endTransaction();
            }
        }
    }
}
