package tools.zhang.com.mytools.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 1.关于事务处理的安全性问题：
 * 通过数据库上的独占性和共享锁来实现独立事务处理，， 多个进程可以在同一时间从同一数据库读取数据，但只有一个可以写入数据。
 *
 * 2.sqlite支持NULL（空值）、INTEGER（整型值）、REAL（浮点值）、TEXT（字符串值）、BLOB（二进制对象）数据类型
 *
 * 3.Cursor要及时关闭
 *
 * 4.使用_id，而不是id
 */
public class SqlUtils {
    private static final String TAG = "SqlUtils";

    public static void testAddData(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues curValues = new ContentValues();
        curValues.put("age", 10);
        curValues.put("name", "zhangf三ss");
//        curValues.put("phone", "158");
        db.insert(DatabaseHelper.TABLE_NAME, null, curValues);

        db.close();
    }
}
