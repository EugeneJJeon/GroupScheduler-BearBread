package bearbread.org.groupscheduler.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import bearbread.org.groupscheduler.R;

/**
 * Created by Eugene J. Jeon on 2015-04-08.
 */
public class DBHelper extends SQLiteOpenHelper {
	private static final String CLASSNAME = DBHelper.class.getSimpleName();

	private static DBHelper mInstance;
	private static SQLiteDatabase db;
	/*
	* 생성자
	* @param context    : app context
	* @param name       : database name
	* @param factory    : cursor factory
	* @param version    : database version
	* */
	private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
		Log.v(Constants.LOG_TAG, DBHelper.CLASSNAME + " Create or Open database : " + name);
	}

	/*
	* 생성자
	* @param context   : app context
	* */
	private DBHelper(final Context context) {
		super(context, DBCreator.DB_NAME, null, DBCreator.DB_VERSION);
		Log.v(Constants.LOG_TAG, DBHelper.CLASSNAME + " Create or Open database : " + DBCreator.DB_NAME);
	}

	/*
	* Initialize Method
	* @param context    : app context
	* */
	private static void initialize(Context context) {
		if (mInstance == null) {
			Log.i(Constants.LOG_TAG, DBHelper.CLASSNAME);
			mInstance = new DBHelper(context);

			try {
				Log.i(Constants.LOG_TAG, "Creating or Opening the database ( " + DBCreator.DB_NAME + " )");
				db = mInstance.getWritableDatabase();
			} catch (SQLiteException e) {
				Log.e(Constants.LOG_TAG, "Coud not create and(or) open the database ( " + DBCreator.DB_NAME + " ) that will be used for reading and writing.", e);
			}
			Log.i(Constants.LOG_TAG, DBHelper.CLASSNAME + " instance of database ( " + DBCreator.DB_NAME + " ) created !");
		}
	}

	/*
	* Static method for getting singleton instance
	* @param context    : app context
	* @return           : singleton instance
	* */
	public static final DBHelper getInstance(Context context) {
		initialize(context);
		return mInstance;
	}

	/*
	* Method to close database & instance null
	* */
	public void close() {
		if (mInstance != null) {
			Log.i(Constants.LOG_TAG, DBHelper.CLASSNAME + " Closing the database [ " + GSDBCreator.DB_NAME + " ]");
			db.close();
			mInstance = null;
		}
	}

	/*
	* Method for select table
	* @param table      : table name
	* @param columns    : column name array
	* @param id         : record id (pk 컬럼명은 "id" 만 가능하다)
	* @return           : cursor
	* */
	public Cursor get(String table, String[] columns, String keyColumn, long id) {
		Cursor cursor = db.query(true, table, columns, keyColumn + "=" + id, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	/*
	* Method for select statements
	* @param sql        : sql statements
	* @return           : cursor
	* */
	public Cursor get(String sql) { return db.rawQuery(sql, null); }

	/*
	* Method to insert record
	* @param table      : table name
	* @param values     : ContentValues instance
	* @return           : long (rowId)
	* */
	public long insert(String table, ContentValues values) {
		return db.insert(table, null, values);
	}

	/*
	* Method to update record
	* @param table      : table name
	* @param values     : ContentValues instance
	* @param id         : record id
	* @return           : int
	* */
	public int update(String table, ContentValues values, String keyColumn, long id) {
		return db.update(table, values, keyColumn + "=" + id, null);
	}
    public int update(String table, ContentValues values, String keyColumn_1, long id_1, String keyColumn_2, long id_2) {
        return db.update(table, values, keyColumn_1 + "=" + id_1 + " AND " + keyColumn_2 + "=" + id_2, null);
    }

	/*
	* Method to update record
	* @param table          : table name
	* @param values         : ContentValues instance
	* @param whereClause    : Where Clause
	* @return               : int
	* */
	public int update(String table, ContentValues values, String whereClause) {
        return db.update(table, values, whereClause, null);
    }

	/*
	* Method to delete record
	* @param table          : table name
	* @param whereClause    : Where Clause
	* @return               : int
	* */
	public int delete(String table, String whereClause) {
		return db.delete(table, whereClause, null);
	}

	/*
	* Method to delete record
	* @param table          : table name
	* @param id             : record id
	* @return               : int
	* */
	public int delete(String table, String keyColumn, long id) {
		return db.delete(table, keyColumn + "=" + id, null);
	}
    public int delete(String table, String keyColumn_1, long id_1, String keyColumn_2, long id_2) {
        return db.delete(table, keyColumn_1 + "=" + id_1 + " AND " + keyColumn_2 + "=" + id_2, null);
    }

	/*
	* Method to run sql
	* @param sql
	* */
	public void exec(String sql) {
		db.execSQL(sql);
	}

	/*
	* Contents Provider에서 제공하는 기능을 이용하여 개발할 때, 디버깅시에 편리하게 사용할 수 있다.
	* */
	public void logCursorInfo(Cursor cursor) {
		Log.i(Constants.LOG_TAG, " *** Cursor Begin *** " + " REsults : " + cursor.getCount() + " Columns : " + cursor.getColumnCount());

		// Column Name Print
		String rowHeaders = "|| ";
		for (int i = 0; i < cursor.getColumnCount(); i++) {
			rowHeaders = rowHeaders.concat(cursor.getColumnName(i) + " || " );
		}

		Log.i(Constants.LOG_TAG, " COLUMNS " + rowHeaders);
		// Record Print
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			String rowResults = "|| ";
			for (int i= 0; i < cursor.getColumnCount(); i++) {
				rowResults = rowResults.concat(cursor.getString(i) + " || " );
			}

			Log.i(Constants.LOG_TAG, " Row " + cursor.getPosition() + " : " + rowResults);
			cursor.moveToNext();
		}

		Log.i(Constants.LOG_TAG, " *** Cursor End *** ");
	}

	/*
	* Method to create database
	* 데이터베이스 생성, 최초 한번만 실행된다.
	*  @param db        : SQliteDatabase instance\
	* */
	@Override
	public void onCreate(SQLiteDatabase db) {
		DBCreator mCreator = new GSDBCreator();
		String[] tableCreateStatement = mCreator.getCreateTableStatement();
		String[] indexCreateStatement = mCreator.getCreateIndexStatement();
		String[] initDataInsertStatement = mCreator.getInitDataInsertStatement();

		try {
			if (tableCreateStatement != null && tableCreateStatement.length > 0) {
				Log.v(Constants.LOG_TAG, DBHelper.CLASSNAME + " - onCreate() : Table Creation");
				for (int i = 0; i < tableCreateStatement.length; i++) {
					db.execSQL(tableCreateStatement[i]);
				}
			}

			if (indexCreateStatement != null && indexCreateStatement.length > 0) {
				Log.v(Constants.LOG_TAG, DBHelper.CLASSNAME + " - onCreate() : Index Creation");
				for (int i = 0; i < indexCreateStatement.length; i++) {
					db.execSQL(indexCreateStatement[i]);
				}
			}

			if (initDataInsertStatement != null && initDataInsertStatement.length > 0) {
				for (int i = 0; i < initDataInsertStatement.length; i++) {
					Log.v(Constants.LOG_TAG, DBHelper.CLASSNAME + " - onCreate() : Data Load" + initDataInsertStatement[i]);
				}
				Log.v(Constants.LOG_TAG, DBHelper.CLASSNAME + " - onCreate() : Init Data Load");
			}
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, DBHelper.CLASSNAME + " - onCreate() : Table Creation Error", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(Constants.LOG_TAG, DBHelper.CLASSNAME + " - onUpgrade() : Table Upgrade Action");
        DBCreator mCreator = new GSDBCreator();
        String[] tableDropStatement = mCreator.getDropTableStatement();

        for (int i = 0; i < tableDropStatement.length; i++) {
            db.execSQL(tableDropStatement[i]);
        }

        onCreate(db);
	}
}
