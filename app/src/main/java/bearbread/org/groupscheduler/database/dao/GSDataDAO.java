package bearbread.org.groupscheduler.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
//import java.util.Iterator;
import java.util.List;

import bearbread.org.groupscheduler.database.Constants;
import bearbread.org.groupscheduler.database.DBHelper;
import bearbread.org.groupscheduler.database.schema.CACHE;
import bearbread.org.groupscheduler.database.schema.GROUP;
import bearbread.org.groupscheduler.database.schema.SCHEDULE;
import bearbread.org.groupscheduler.database.schema.USER;
import bearbread.org.groupscheduler.database.schema.USER_TO_GROUP;

/**
 * Created by Eugene J. Jeon on 2015-04-11.
 */
public class GSDataDAO {
    public static final String TO = "TO";
    public  static enum TYPE {INSERT, UPDATE, DELETE};
    public  static enum TABLE {USER, GROUP, SCHEDULE};

	private static final String CLASSNAME = GSDataDAO.class.getSimpleName();
    private static GSDataDAO dao;
    public static GSDataDAO getInstance(Context context) {
        if (dao == null) {
            dao = new GSDataDAO(context);
        }
        return dao;
    }

	private DBHelper db;

	public  GSDataDAO(Context context) {
        db = DBHelper.getInstance(context);
        CACHE.SIZE = (int)getCacheSize();
        GROUP.SIZE = (int)getGroupSize();
    }

	public  void close() { /*db.close(); dao = null;*/ }

	/*
	* TRANSFER OBJECT
	* USER
	* GROUP
	* SCHEDULE
	* USER_TO_GROUP
	* GROUP_TO_SCHEDULE
	* */
    public static class CacheTO implements Parcelable {
        private final String KEY_COLUMN = CACHE.SCHEMA.COLUMN_ID;
        private long id;
        private long group;
        private String type;
        private String query;

        public CacheTO(){}
        public CacheTO(String type, String query) {
            this.group = -1;
            this.type = type;
            this.query = query;
        }
        public CacheTO(long group, String type, String query) {
            this.group = group;
            this.type = type;
            this.query = query;
        }
        public CacheTO(Context context, TYPE type, UserTO to) {
            this(type.toString(), GSDataDAO.getInstance(context).changeQuery(type, to));
        }
        public CacheTO(Context context, TYPE type, GroupTO to) {
            this(type.toString(), GSDataDAO.getInstance(context).changeQuery(type, to));
        }
        public CacheTO(Context context, TYPE type, ScheduleTO to) {
            this(type.toString(), GSDataDAO.getInstance(context).changeQuery(type, to));
        }
        public CacheTO(Context context, long group, TYPE type, UserTO to) {
            this(group, type.toString(), GSDataDAO.getInstance(context).changeQuery(type, to));
        }
        public CacheTO(Context context, long group, TYPE type, GroupTO to) {
            this(group, type.toString(), GSDataDAO.getInstance(context).changeQuery(type, to));
        }
        public CacheTO(Context context, long group, TYPE type, ScheduleTO to) {
            this(group, type.toString(), GSDataDAO.getInstance(context).changeQuery(type, to));
        }

        /* Parcelable 관련 */
        //조립
        public CacheTO(Parcel src) {
            this.id = src.readLong();
            this.group = src.readLong();
            this.type = src.readString();
            this.query = src.readString();
        }
        public static final Creator CREATOR = new Creator() {
            @Override
            public CacheTO createFromParcel(Parcel parcel) {
                return new CacheTO(parcel);
            }

            @Override
            public CacheTO[] newArray(int i) {
                return new CacheTO[i];
            }
        };
        public int describeContents() {
            return 0;
        }
        //분해
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(id);
            dest.writeLong(group);
            dest.writeString(type);
            dest.writeString(query);
        }

        @Override
        public String toString() {
            return "id : " + String.valueOf(id) + ", group : " + String.valueOf(group) + ", type : " + type + ", query : " + query;
        }

        public long getId() { return id; }
        public void setId(long id) { this.id = id; }
        public long getGroup() { return group; }
        public void setGroup(long group) { this.group = group; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
    }
    public static class UserTO{
        private final TABLE table = TABLE.USER;
		private final String KEY_COLUMN = USER.SCHEMA.COLUMN_ID;
		private String id;
		private String name;
		private String tel;

		public UserTO() {}

		public UserTO(String id, String name, String tel) {
			this.id = id;
			this.name = name;
			this.tel = tel;
		}

		@Override
		public String toString() {
			return "id : " + String.valueOf(id) + ", name : " + name + ", tel : " + tel;
		}

        public TABLE getTo () { return table; }
		public String getId() { return id; }
		public void setId(String id) { this.id = id; }
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		public String getTel() { return tel; }
		public void setTel(String tel) { this.tel = tel; }
	}
	public static class GroupTO {
        private final TABLE table = TABLE.GROUP;
		private final String KEY_COLUMN = GROUP.SCHEMA.COLUMN_ID;
		private long id;
		private String admin;
		private String name;

		public GroupTO() {}
		public GroupTO(String admin, String name) {
			this.admin = admin;
			this.name = name;
		}

		@Override
		public String toString() {
			return "id : " + String.valueOf(id) + ", admin : " + String.valueOf(admin) + ", name : " + name;
		}

        public TABLE getTo() { return table; }
		public long getId() { return id; }
		public void setId(long id) { this.id = id; }
		public String getAdmin() { return admin; }
		public void setAdmin(String admin) { this.admin = admin; }
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
	}
	public static class ScheduleTO {
        private final TABLE table = TABLE.USER;
		private final String KEY_COLUMN[] = {SCHEDULE.SCHEMA.COLUMN_GROUP, SCHEDULE.SCHEMA.COLUMN_OWNER};
		private long group;
		private String owner;
		private String name;
		private String startDate;
		private String endDate;

		public ScheduleTO() {}
		public ScheduleTO(long group, String owner, String name, String startDate, String endDate) {
			this.group = group;
			this.owner = owner;
			this.name = name;
			this.startDate = startDate;
			this.endDate = endDate;
		}

		@Override
		public String toString() {
			return "id : " + String.valueOf(group) + ", owner : " + String.valueOf(owner) + ", name : " + name + ", start date : " + startDate + ", end date : " + endDate;
		}

        public TABLE getTo() { return table; }
		public long getGroup() { return group; }
		public void setGroup(long id) { this.group = id; }
		public String getOwner() { return owner; }
		public void setOwner(String owner) { this.owner = owner; }
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		public String getStartDate() { return startDate; }
		public void setStartDate(String startDate) { this.startDate = startDate; }
		public String getEndDate() { return endDate; }
		public void setEndDate(String endDate) { this.endDate = endDate; }
	}
	public static class UserToGroupTO {
		private final String KEY_COLUMN[] = {USER_TO_GROUP.SCHEMA.COLUMN_USER_ID, USER_TO_GROUP.SCHEMA.COLUMN_GROUP_ID};
		private String uid;
		private long gid;

		public UserToGroupTO() {}
		public UserToGroupTO(String uid, long gid) {
			this.uid = uid;
			this.gid = gid;
		}

		@Override
		public String toString() {
			return "uid : " + String.valueOf(uid) + ", gid : " + String.valueOf(gid);
		}

		public String getUid() { return uid; }
		public void setUid(String uid) { this.uid = uid; }
		public long getGid() { return gid; }
		public void setGid(long gid) { this.gid = gid; }
	}

	/*
	* DDL, insert
	* */
    public long insert(final CacheTO to) {
        ContentValues values = new ContentValues();

        //to.setId(getCacheSize());       // 실제 값과 갯수의 차이를 이용한다. (갯수 : 1개 -> 실제 id : 0번)
        to.setId(++CACHE.SIZE);
        to.setGroup(++GROUP.SIZE);
        Log.i("insert cache, ", String.valueOf(CACHE.SIZE));

        values.put(CACHE.SCHEMA.COLUMN_ID, to.getId());
        values.put(CACHE.SCHEMA.COLUMN_GROUP, to.getGroup());
        values.put(CACHE.SCHEMA.COLUMN_TYPE, to.getType());
        values.put(CACHE.SCHEMA.COLUMN_QUERY, to.getQuery());

        Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " insert, " + to.toString());
        long rowId = db.insert(CACHE.SCHEMA.TABLE_NAME, values);
        if (rowId < 0) {
            throw new SQLException("fail at insert");
        }
        close();

        return to.getGroup();
    }
//    public void insert(final UserTO to) {
//		ContentValues values = new ContentValues();
//
//		values.put(USER.SCHEMA.COLUMN_ID, to.getId());
//		values.put(USER.SCHEMA.COLUMN_NAME, to.getName());
//		values.put(USER.SCHEMA.COLUMN_TEL, to.getTel());
//
//		Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " insert, " + to.toString());
//		long rowId = db.insert(USER.SCHEMA.TABLE_NAME, values);
//		if (rowId < 0) {
//			throw new SQLException("fail at insert");
//		}
//
//        close();
//	}
//	public void insert(final GroupTO to) {
//		ContentValues values = new ContentValues();
//
//		values.put(GROUP.SCHEMA.COLUMN_ADMIN, to.getAdmin());
//		values.put(GROUP.SCHEMA.COLUMN_NAME, to.getName());
//
//		Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " insert, " + to.toString());
//		long rowId = db.insert(GROUP.SCHEMA.TABLE_NAME, values);
//		if (rowId < 0) {
//			throw new SQLException("fail at insert");
//		}
//
//        close();
//	}
//	public void insert(final ScheduleTO to) {
//		ContentValues values = new ContentValues();
//
//		values.put(SCHEDULE.SCHEMA.COLUMN_OWNER, to.getOwner());
//		values.put(SCHEDULE.SCHEMA.COLUMN_NAME, to.getName());
//		values.put(SCHEDULE.SCHEMA.COLUMN_START_DATE, to.getStartDate());
//		values.put(SCHEDULE.SCHEMA.COLUMN_END_DATE, to.getEndDate());
//
//		Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " insert, " + to.toString());
//		long rowId = db.insert(SCHEDULE.SCHEMA.TABLE_NAME, values);
//		if (rowId < 0) {
//			throw new SQLException("fail at insert");
//		}
//
//        close();
//	}
//	public void insert(final UserToGroupTO to) {
//		ContentValues values = new ContentValues();
//
//		values.put(USER_TO_GROUP.SCHEMA.COLUMN_USER_ID, to.getUid());
//		values.put(USER_TO_GROUP.SCHEMA.COLUMN_GROUP_ID, to.getGid());
//
//		Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " insert, " + to.toString());
//		long rowId = db.insert(USER_TO_GROUP.SCHEMA.TABLE_NAME, values);
//		if (rowId < 0) {
//			throw new SQLException("fail at insert");
//		}
//
//        close();
//	}

	/*
	* DDL, update
	* */
//	public void update(final UserTO to) {
//		ContentValues values = new ContentValues();
//
//		values.put(USER.SCHEMA.COLUMN_NAME, to.getName());
//		values.put(USER.SCHEMA.COLUMN_TEL, to.getTel());
//
//		Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " update, " + to.toString());
//		long rowId = db.update(USER.SCHEMA.TABLE_NAME, values, to.KEY_COLUMN, to.getId());
//        if (rowId < 0) {
//            throw new SQLException("fail at update");
//        }
//
//        close();
//	}
//	public void update(final GroupTO to) {
//		ContentValues values = new ContentValues();
//
//		values.put(GROUP.SCHEMA.COLUMN_NAME, to.getName());
//
//		Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " update, " + to.toString());
//		long rowId = db.update(GROUP.SCHEMA.TABLE_NAME, values, to.KEY_COLUMN, to.getId());
//        if (rowId < 0) {
//            throw new SQLException("fail at update");
//        }
//
//        close();
//	}
//	public void update(final ScheduleTO to) {
//		ContentValues values = new ContentValues();
//
//		values.put(SCHEDULE.SCHEMA.COLUMN_NAME, to.getName());
//		values.put(SCHEDULE.SCHEMA.COLUMN_START_DATE, to.getStartDate());
//		values.put(SCHEDULE.SCHEMA.COLUMN_END_DATE, to.getEndDate());
//
//		Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " update, " + to.toString());
//		long rowId = db.update(SCHEDULE.SCHEMA.TABLE_NAME, values, to.KEY_COLUMN[0], to.getGroup(), to.KEY_COLUMN[1], to.getOwner());
//        if (rowId < 0) {
//            throw new SQLException("fail at update");
//        }
//
//        close();
//	}

	/*
	* DDL, delete
	* */
  	public void delete(final CacheTO to) {
        Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " delete, " + to.toString());
        long rowId = db.delete(CACHE.SCHEMA.TABLE_NAME, to.KEY_COLUMN, to.getId());
        if (rowId < 0) {
            throw new SQLException("fail at delete");
        }
        close();
    }
//    public void delete(final UserTO to) {
//		Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " delete, " + to.toString());
//		long rowId = db.delete(USER.SCHEMA.TABLE_NAME, to.KEY_COLUMN, to.getId());
//        if (rowId < 0) {
//            throw new SQLException("fail at delete");
//        }
//
//        close();
//	}
//	public void delete(final GroupTO to) {
//		Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " delete, " + to.toString());
//		long rowId = db.delete(GROUP.SCHEMA.TABLE_NAME, to.KEY_COLUMN, to.getId());
//        if (rowId < 0) {
//            throw new SQLException("fail at delete");
//        }
//
//        close();
//	}
//	public void delete(final ScheduleTO to) {
//		Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " delete, " + to.toString());
//		long rowId = db.delete(SCHEDULE.SCHEMA.TABLE_NAME, to.KEY_COLUMN[0], to.getGroup(), to.KEY_COLUMN[1], to.getOwner());
//        if (rowId < 0) {
//            throw new SQLException("fail at delete");
//        }
//
//        close();
//	}
//	public void delete(final UserToGroupTO to) {
//		Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " delete, " + to.toString());
//		long rowId = db.delete(USER_TO_GROUP.SCHEMA.TABLE_NAME, to.KEY_COLUMN[0], to.getUid(), to.KEY_COLUMN[1], to.getGid());
//        if (rowId < 0) {
//            throw new SQLException("fail at delete");
//        }
//
//        close();
//	}

	/*
	* get, return list of transfer object type
	* DDL, select
	* */
    public List<CacheTO> getCacheTO() {
        Cursor cursor = null;
        ArrayList<CacheTO> ret = null;

        String sql = "SELECT * FROM " + CACHE.SCHEMA.TABLE_NAME + " ORDER BY 1;";     // 1 : first column

        try {
            Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " get - CACHE");
            cursor = db.get(sql);

            // db.logCursorInfo(cursor);

            ret = setBindCursorCacheTO(cursor);
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " getList ", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        close();

        return ret;
    }
    public List<UserTO> getUserTO() {
		Cursor cursor = null;
		ArrayList<UserTO> ret = null;

		String sql = "SELECT * FROM " + USER.SCHEMA.TABLE_NAME + " ORDER BY 1;";     // 1 : first column

		try {
			Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " get - USER");
			cursor = db.get(sql);

			// db.logCursorInfo(cursor);

			ret = setBindCursorUserTO(cursor);
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " getList ", e);
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}

        close();
		return ret;
	}
	public List<GroupTO> getGroupTO() {
        Cursor cursor = null;
        ArrayList<GroupTO> ret = null;

        String sql = "SELECT * FROM " + GROUP.SCHEMA.TABLE_NAME + " ORDER BY 1;";     // 1 : first column

        try {
            Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " get - GROUP");
            cursor = db.get(sql);

            // db.logCursorInfo(cursor);

            ret = setBindCursorGroupTO(cursor);
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " getList ", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        close();
        return ret;
	}
	public List<ScheduleTO> getScheduleTO() {
		Cursor cursor = null;
		ArrayList<ScheduleTO> ret = null;

		String sql = "SELECT * FROM " + SCHEDULE.SCHEMA.TABLE_NAME + " ORDER BY 1;";     // 1 : first column

		try {
			Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " get - SCHEDULE");
			cursor = db.get(sql);

			// db.logCursorInfo(cursor);

			ret = setBindCursorScheduleTO(cursor);
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " getList ", e);
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}

        close();
		return ret;
	}
	public List<UserToGroupTO> getUserToGroupTO() {
		Cursor cursor = null;
		ArrayList<UserToGroupTO> ret = null;

		String sql = "SELECT * FROM " + USER_TO_GROUP.SCHEMA.TABLE_NAME + " ORDER BY 1;";     // 1 : first column

		try {
			Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " get - USERTOGROUP");
			cursor = db.get(sql);

			// db.logCursorInfo(cursor);

			ret = setBindCursorUserToGroupTO(cursor);
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " getList ", e);
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}

        close();
		return ret;
	}

    private ArrayList<CacheTO> setBindCursorCacheTO(final Cursor cursor) {
        ArrayList<CacheTO> ret = new ArrayList<CacheTO>();

        int numRows = cursor.getCount();

        cursor.moveToFirst();

        // SQL문에서 Join 사용시, 테이블명을 사용하면 칼럼명이 틀려지므로 getColumnIndex가 Exception을 낸다.
        // 반드시 alias를 사용해 컬럼명을 동일하게 맞춰야 한다.
        // 값이 null인 경우 getInt()는 0을 반환한다.
        for (int i = 0; i < numRows; i++) {
            CacheTO to = new CacheTO();
            to.setId(cursor.getLong(cursor.getColumnIndex(CACHE.SCHEMA.COLUMN_ID)));
            to.setGroup(cursor.getLong(cursor.getColumnIndex(CACHE.SCHEMA.COLUMN_GROUP)));
            to.setType(cursor.getString(cursor.getColumnIndex(CACHE.SCHEMA.COLUMN_TYPE)));
            to.setQuery(cursor.getString(cursor.getColumnIndex(CACHE.SCHEMA.COLUMN_QUERY)));

            ret.add(to);
            cursor.moveToNext();
        }

        return ret;
    }
	private ArrayList<UserTO> setBindCursorUserTO(final Cursor cursor) {
		ArrayList<UserTO> ret = new ArrayList<UserTO>();

		int numRows = cursor.getCount();

		cursor.moveToFirst();

		// SQL문에서 Join 사용시, 테이블명을 사용하면 칼럼명이 틀려지므로 getColumnIndex가 Exception을 낸다.
		// 반드시 alias를 사용해 컬럼명을 동일하게 맞춰야 한다.
		// 값이 null인 경우 getInt()는 0을 반환한다.
		for (int i = 0; i < numRows; i++) {
			UserTO to = new UserTO();
			to.setId(cursor.getString(cursor.getColumnIndex(USER.SCHEMA.COLUMN_ID)));
			to.setName(cursor.getString(cursor.getColumnIndex(USER.SCHEMA.COLUMN_NAME)));
			to.setTel(cursor.getString(cursor.getColumnIndex(USER.SCHEMA.COLUMN_TEL)));

			ret.add(to);
			cursor.moveToNext();
		}

		return ret;
	}
	private ArrayList<GroupTO> setBindCursorGroupTO(final Cursor cursor) {
		ArrayList<GroupTO> ret = new ArrayList<GroupTO>();

		int numRows = cursor.getCount();

		cursor.moveToFirst();

		// SQL문에서 Join 사용시, 테이블명을 사용하면 칼럼명이 틀려지므로 getColumnIndex가 Exception을 낸다.
		// 반드시 alias를 사용해 컬럼명을 동일하게 맞춰야 한다.
		// 값이 null인 경우 getInt()는 0을 반환한다.
		for (int i = 0; i < numRows; i++) {
			GroupTO to = new GroupTO();
			to.setId(cursor.getLong(cursor.getColumnIndex(GROUP.SCHEMA.COLUMN_ID)));
			to.setAdmin(cursor.getString(cursor.getColumnIndex(GROUP.SCHEMA.COLUMN_ADMIN)));
			to.setName(cursor.getString(cursor.getColumnIndex(GROUP.SCHEMA.COLUMN_NAME)));

			ret.add(to);
			cursor.moveToNext();
		}

		return ret;
	}
	private ArrayList<ScheduleTO> setBindCursorScheduleTO(final Cursor cursor) {
		ArrayList<ScheduleTO> ret = new ArrayList<ScheduleTO>();

		int numRows = cursor.getCount();

		cursor.moveToFirst();

		// SQL문에서 Join 사용시, 테이블명을 사용하면 칼럼명이 틀려지므로 getColumnIndex가 Exception을 낸다.
		// 반드시 alias를 사용해 컬럼명을 동일하게 맞춰야 한다.
		// 값이 null인 경우 getInt()는 0을 반환한다.
		for (int i = 0; i < numRows; i++) {
			ScheduleTO to = new ScheduleTO();
			to.setGroup(cursor.getLong(cursor.getColumnIndex(SCHEDULE.SCHEMA.COLUMN_GROUP)));
			to.setOwner(cursor.getString(cursor.getColumnIndex(SCHEDULE.SCHEMA.COLUMN_OWNER)));
			to.setName(cursor.getString(cursor.getColumnIndex(SCHEDULE.SCHEMA.COLUMN_NAME)));
			to.setStartDate(cursor.getString(cursor.getColumnIndex(SCHEDULE.SCHEMA.COLUMN_START_DATE)));
			to.setEndDate(cursor.getString(cursor.getColumnIndex(SCHEDULE.SCHEMA.COLUMN_END_DATE)));

			ret.add(to);
			cursor.moveToNext();
		}

		return ret;
	}
	private ArrayList<UserToGroupTO> setBindCursorUserToGroupTO(final Cursor cursor) {
		ArrayList<UserToGroupTO> ret = new ArrayList<UserToGroupTO>();

		int numRows = cursor.getCount();

		cursor.moveToFirst();

		// SQL문에서 Join 사용시, 테이블명을 사용하면 칼럼명이 틀려지므로 getColumnIndex가 Exception을 낸다.
		// 반드시 alias를 사용해 컬럼명을 동일하게 맞춰야 한다.
		// 값이 null인 경우 getInt()는 0을 반환한다.
		for (int i = 0; i < numRows; i++) {
			UserToGroupTO to = new UserToGroupTO();
			to.setUid(cursor.getString(cursor.getColumnIndex(USER_TO_GROUP.SCHEMA.COLUMN_USER_ID)));
			to.setGid(cursor.getLong(cursor.getColumnIndex(USER_TO_GROUP.SCHEMA.COLUMN_GROUP_ID)));

			ret.add(to);
			cursor.moveToNext();
		}

		return ret;
	}

    /*
    * change to raw-query
    * */
    public String changeQuery(TYPE type, UserTO to) {
        switch (type) {
            case INSERT : String insertQuery = "INSERT INTO " + USER.SCHEMA.TABLE_NAME + " VALUES (" + to.getId() + ", " + to.getName() + ", " + to.getTel() + ");"; return insertQuery;
            case UPDATE : String updateQuery = "UPDATE " + USER.SCHEMA.TABLE_NAME + " SET " + USER.SCHEMA.COLUMN_NAME + "=" + to.getName() + ", " + USER.SCHEMA.COLUMN_TEL + "=" + to.getTel() +" WHERE " + USER.SCHEMA.COLUMN_ID + "=" + to.getId() +";"; return updateQuery;
            case DELETE : String deleteQuery = "DELETE FROM " + USER.SCHEMA.TABLE_NAME + " WHERE " + USER.SCHEMA.COLUMN_ID + "=" + to.getId()+ ";"; return deleteQuery;
        }
        return null;
    }
    public String changeQuery(TYPE type, GroupTO to) {
        switch (type) {
            case INSERT : String insertQuery = "INSERT INTO " + GROUP.SCHEMA.TABLE_NAME + " VALUES (" + to.getAdmin() + ", " + to.getName() + ");"; return insertQuery;
            case UPDATE : String updateQuery = "UPDATE " + GROUP.SCHEMA.TABLE_NAME + " SET " + GROUP.SCHEMA.COLUMN_NAME + "=" + to.getName() + " WHERE " + GROUP.SCHEMA.COLUMN_ID + "=" + to.getId() + " AND " + GROUP.SCHEMA.COLUMN_ADMIN + "=" + to.getAdmin() + ";"; return updateQuery;
            case DELETE : String deleteQuery = "DELETE FROM " + GROUP.SCHEMA.TABLE_NAME + " WHERE " + GROUP.SCHEMA.COLUMN_ID + "=" + to.getId() + " AND " + GROUP.SCHEMA.COLUMN_ADMIN + "=" + to.getAdmin() + ";"; return deleteQuery;
        }
        return null;
    }
    public String changeQuery(TYPE type, ScheduleTO to) {
        switch (type) {
            case INSERT : String insertQuery = "INSERT INTO " + SCHEDULE.SCHEMA.TABLE_NAME + " VALUES (" + to.getGroup() + ", " + to.getOwner() + ", " + to.getName() + ", " + to.getStartDate() + ", " + to.getEndDate() + ");"; return insertQuery;
            case UPDATE : String updateQuery = "UPDATE " + SCHEDULE.SCHEMA.TABLE_NAME + " SET " + SCHEDULE.SCHEMA.COLUMN_OWNER + "=" + to.getOwner() + ", " + SCHEDULE.SCHEMA.COLUMN_NAME + "=" + to.getName() +", " + SCHEDULE.SCHEMA.COLUMN_START_DATE + "=" + to.getStartDate() + ", " + SCHEDULE.SCHEMA.COLUMN_END_DATE + "=" + to.getEndDate() + " WHERE " + SCHEDULE.SCHEMA.COLUMN_GROUP + "=" + to.getGroup() + " AND " + SCHEDULE.SCHEMA.COLUMN_OWNER + "=" + to.getOwner() + ";"; return updateQuery;
            case DELETE : String deleteQuery = "DELETE FROM " + SCHEDULE.SCHEMA.TABLE_NAME + " WHERE " + SCHEDULE.SCHEMA.COLUMN_GROUP + "=" + to.getGroup() + " AND " + SCHEDULE.SCHEMA.COLUMN_OWNER + "=" + to.getOwner() + ";"; return deleteQuery;
        }
        return null;
    }
    public String changeQuery(TYPE type, UserToGroupTO to) {
        switch (type) {
            case INSERT : String insertQuery = "INSERT INTO " + USER_TO_GROUP.SCHEMA.TABLE_NAME + " VALUES (" + to.getUid() + ", " + to.getGid() + ");"; return insertQuery;
            case DELETE : String deleteQuery = "DELETE FROM " + USER_TO_GROUP.SCHEMA.TABLE_NAME + " WHERE " + USER_TO_GROUP.SCHEMA.COLUMN_USER_ID + "=" + to.getUid() + " AND " + USER_TO_GROUP.SCHEMA.COLUMN_GROUP_ID + "=" + to.getGid() + ";"; return deleteQuery;
        }
        return null;
    }

    /*
    * get GROUP-ID
    * */
    public long getGroupID(UserTO to) {
        return -1;
    }
    public long getGroupID(GroupTO to) {
        return to.getId();
    }
    public long getScheduleID(ScheduleTO to) {
        return to.getGroup();
    }

    /*
    * search last-id in CacheTO
    * */
    private long getCacheSize() {
        Cursor cursor = null;
        long count = 0;

        String sql = "SELECT * FROM " + CACHE.SCHEMA.TABLE_NAME + ";";     // 1 : first column

        try {
            Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " getSize - Cache");
            cursor = db.get(sql);
            count = cursor.getCount();
            // db.logCursorInfo(cursor);

            //ret = setBindCursorUserTO(cursor);
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " getCacheSize ", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        close();
        return count;
    }
    private long getGroupSize() {
        Cursor cursor = null;
        long count = 0;

        String sql = "SELECT * FROM " + GROUP.SCHEMA.TABLE_NAME + ";";     // 1 : first column

        try {
            Log.v(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " getSize - Group");
            cursor = db.get(sql);
            count = cursor.getCount();
            // db.logCursorInfo(cursor);

            //ret = setBindCursorUserTO(cursor);
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, GSDataDAO.CLASSNAME + " getGroupSize ", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        close();
        return count;
    }

	/*
	* logListInfo
	* */
//	public void logListInfoUserTO(List<UserTO> to) {
//		Log.i(Constants.LOG_TAG, " *** List Begin *** " + " Result : " + to.size());
//
//		Iterator<UserTO> iterator = to.iterator();
//		while(iterator.hasNext()) {
//			String msg = ((UserTO)iterator.next()).toString();
//			Log.i(Constants.LOG_TAG, " Datas : " + msg);
//		}
//
//		Log.i(Constants.LOG_TAG, " *** List End ");
//	}
//	public void logListInfoGroupTO(List<GroupTO> to) {
//		Log.i(Constants.LOG_TAG, " *** List Begin *** " + " Result : " + to.size());
//
//		Iterator<GroupTO> iterator = to.iterator();
//		while(iterator.hasNext()) {
//			String msg = ((GroupTO)iterator.next()).toString();
//			Log.i(Constants.LOG_TAG, ", Datas : " + msg);
//		}
//
//		Log.i(Constants.LOG_TAG, " *** List End ");
//	}
//	public void logListInfoScheduleTO(List<ScheduleTO> to) {
//		Log.i(Constants.LOG_TAG, " *** List Begin *** " + " Result : " + to.size());
//
//		Iterator<ScheduleTO> iterator = to.iterator();
//		while(iterator.hasNext()) {
//			String msg = ((ScheduleTO)iterator.next()).toString();
//			Log.i(Constants.LOG_TAG, " Datas : " + msg);
//		}
//
//		Log.i(Constants.LOG_TAG, " *** List End ");
//	}
//	public void logListInfoUserToGroupTO(List<UserToGroupTO> to) {
//		Log.i(Constants.LOG_TAG, " *** List Begin *** " + " Result : " + to.size());
//
//		Iterator<UserToGroupTO> iterator = to.iterator();
//		while(iterator.hasNext()) {
//			String msg = ((UserToGroupTO)iterator.next()).toString();
//			Log.i(Constants.LOG_TAG, " Datas : " + msg);
//		}
//
//		Log.i(Constants.LOG_TAG, " *** List End ");
//	}

//    public TABLE getTO(Parcelable parcelable) {
//        if (parcelable.getClass().isInstance(new UserTO())) {
//            return TABLE.USER;
//        }
//        else if (parcelable.getClass().isInstance(new GroupTO())) {
//            return TABLE.GROUP;
//        }
//        else if (parcelable.getClass().isInstance(new ScheduleTO())) {
//            return TABLE.SCHEDULE;
//        }
//        return null;
//    }
//    public TABLE getTO(UserTO to) {
//        return TABLE.USER;
//    }
//    public TABLE getTO(GroupTO to) {
//        return TABLE.GROUP;
//    }
//    public TABLE getTO(ScheduleTO to) {
//        return TABLE.SCHEDULE;
//    }

    /* raw-query, "execSQL()" */
    public void exec(String sql) {
        db.exec(sql);
        close();
    }
}
