package bearbread.org.groupscheduler.eugene.database.dao;

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

import bearbread.org.groupscheduler.eugene.database.Constants;
import bearbread.org.groupscheduler.eugene.database.DBHelper;
import bearbread.org.groupscheduler.eugene.database.schema.CACHE;
import bearbread.org.groupscheduler.eugene.database.schema.GROUP;
import bearbread.org.groupscheduler.eugene.database.schema.INVITE;
import bearbread.org.groupscheduler.eugene.database.schema.SCHEDULE;
import bearbread.org.groupscheduler.eugene.database.schema.USER;

/**
 * Created by Eugene J. Jeon on 2015-04-11.
 */
public class DATA {
    public static final String TO = "TO";
    public  static enum QUERY_TYPE {INSERT, UPDATE, DELETE};
    public  static enum TABLE {USER, GROUP, SCHEDULE, INVITED};

	private static final String CLASSNAME = DATA.class.getSimpleName();
    private static DATA dao;
    public static DATA getInstance(Context context) {
        if (dao == null) {
            dao = new DATA(context);
        }
        CACHE.SIZE = (int)dao.getCacheSize();
        return dao;
    }

	private DBHelper db;

	public  DATA(Context context) {
        db = DBHelper.getInstance(context);
    }

	public  void close() { db.close(); dao = null; }

	/*
	* TRANSFER OBJECT
	* CACHE | USER | GROUP | SCHEDULE | INVITED
	* */
    public static class Cache implements Parcelable {
        private final String KEY_COLUMN = CACHE.SCHEMA.COLUMN_ID;
        private int id;
        private int user;
        private String type;
        private String query;

        public Cache(){}
        public Cache(int user, String type, String query) {
            this.id = 0;
            this.user = user;
            this.type = type;
            this.query = query;
        }
        public Cache(Context context, QUERY_TYPE type, User to) {
            this(DATA.getInstance(context).getUser().get(0).getId(), type.toString(), DATA.getInstance(context).changeQuery(type, to));
        }
        public Cache(Context context, QUERY_TYPE type, Group to) {
            this(DATA.getInstance(context).getUser().get(0).getId(), type.toString(), DATA.getInstance(context).changeQuery(type, to));
        }
        public Cache(Context context, QUERY_TYPE type, Schedule to) {
            this(DATA.getInstance(context).getUser().get(0).getId(), type.toString(), DATA.getInstance(context).changeQuery(type, to));
        }

        /* Parcelable 관련 */
        //조립
        public Cache(Parcel src) {
            this.id = src.readInt();
            this.user = src.readInt();
            this.type = src.readString();
            this.query = src.readString();
        }
        public static final Creator CREATOR = new Creator() {
            @Override
            public Cache createFromParcel(Parcel parcel) {
                return new Cache(parcel);
            }

            @Override
            public Cache[] newArray(int i) {
                return new Cache[i];
            }
        };
        public int describeContents() {
            return 0;
        }
        //분해
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeInt(user);
            dest.writeString(type);
            dest.writeString(query);
        }

        @Override
        public String toString() {
            return "id : " + String.valueOf(id) + ", user : " + String.valueOf(user) + ", type : " + type + ", query : " + query;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getUser() { return user; }
        public void setUser(int user) { this.user = user; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
    }
    public static class User{
        private final TABLE table = TABLE.USER;
		private final String KEY_COLUMN = USER.SCHEMA.COLUMN_ID;
		private int id;
		private String name;
        private String gcmId;       /* update - ver.20150603 */

		public User() {}
		public User(String name) {
			this.name = name;
		}

        @Override                   /* update - ver.20150603 */
		public String toString() { return "id : " + String.valueOf(id) + ", name : " + name + ", gms id : " + gcmId; }

        public TABLE getTo () { return table; }
		public int getId() { return id; }
		public void setId(int id) { this.id = id; }
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
        /* update - ver.20150603 */
        public String getGCMId() { return gcmId; }
        public void setGCMId(String gmsId) { this.gcmId = gmsId; }
	}
	public static class Group {
        private final TABLE table = TABLE.GROUP;
		private final String KEY_COLUMN = GROUP.SCHEMA.COLUMN_ID;
		private int id;
		private int admin;
		private String name;

		public Group() {}
        public Group(int admin, String name) {
            this.admin = admin;
            this.name = name;
        }

		@Override
		public String toString() {
			return "id : " + String.valueOf(id) + ", admin : " + String.valueOf(admin) + ", name : " + name;
		}

        public TABLE getTo() { return table; }
		public int getId() { return id; }
		public void setId(int id) { this.id = id; }
		public int getAdmin() { return admin; }
		public void setAdmin(int admin) { this.admin = admin; }
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
	}
	public static class Schedule {
        private final TABLE table = TABLE.SCHEDULE;
		private final String KEY_COLUMN[] = {SCHEDULE.SCHEMA.COLUMN_GROUP, SCHEDULE.SCHEMA.COLUMN_OWNER};
        private int id;
        private int group;
		private int owner;
		private String name;
		private String startDate;
		private String endDate;

		public Schedule() {}
		public Schedule(int group, int owner, String name, String startDate, String endDate) {
			this.group = group;
			this.owner = owner;
			this.name = name;
			this.startDate = startDate;
			this.endDate = endDate;
		}

		@Override
		public String toString() {
			return "id : " + String.valueOf(id) + ", group : " + String.valueOf(group) + ", owner : " + String.valueOf(owner) + ", name : " + name + ", start date : " + startDate + ", end date : " + endDate;
		}

        public TABLE getTo() { return table; }
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
		public int getGroup() { return group; }
		public void setGroup(int id) { this.group = id; }
		public int getOwner() { return owner; }
		public void setOwner(int owner) { this.owner = owner; }
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		public String getStartDate() { return startDate; }
		public void setStartDate(String startDate) { this.startDate = startDate; }
		public String getEndDate() { return endDate; }
		public void setEndDate(String endDate) { this.endDate = endDate; }
	}
    public static class Invite {
        private final TABLE table = TABLE.INVITED;
        private final String KEY_COLUMN = INVITE.SCHEMA.COLUMN_ID;
        private int id;
        private int groupId;
        private String groupName;

        public Invite() {}

        public Invite(int id, int gid, String gname) {
            this.id = id;
            this.groupId = gid;
            this.groupName = gname;
        }

        @Override
        public String toString() {
            return "id : " + String.valueOf(id) + ", name : " + String.valueOf(groupId) + ", tel : " + groupName;
        }

        public TABLE getTo () { return table; }
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getGroupID() { return groupId; }
        public void setGroupID(int gid) { this.groupId = gid; }
        public String getGroupName() { return groupName; }
        public void setGroupName(String gname) { this.groupName = gname; }
    }

	/*
	* DDL, insert|delete cache
	* */
    public void insert(final Cache to) {
        ContentValues values = new ContentValues();

        //to.setId(getCacheSize());       // 실제 값과 갯수의 차이를 이용한다. (갯수 : 1개 -> 실제 id : 0번)
        to.setId(++CACHE.SIZE);
        Log.i("insert cache, ", String.valueOf(CACHE.SIZE));

        values.put(CACHE.SCHEMA.COLUMN_ID, to.getId());
        values.put(CACHE.SCHEMA.COLUMN_USER, to.getUser());
        values.put(CACHE.SCHEMA.COLUMN_TYPE, to.getType());
        values.put(CACHE.SCHEMA.COLUMN_QUERY, to.getQuery());

        Log.v(Constants.LOG_TAG, DATA.CLASSNAME + " insert, " + to.toString());
        long rowId = db.insert(CACHE.SCHEMA.TABLE_NAME, values);
        if (rowId < 0) {
            throw new SQLException("fail at insert");
        }
    }
  	public void delete(final Cache to) {
        Log.v(Constants.LOG_TAG, DATA.CLASSNAME + " delete, " + to.toString());
        long rowId = db.delete(CACHE.SCHEMA.TABLE_NAME, to.KEY_COLUMN, to.getId());
        if (rowId < 0) {
            throw new SQLException("fail at delete");
        }
    }
    /*
	* DDL, insert user
	* */
    public void insert(final User to) {
        ContentValues values = new ContentValues();

        values.put(USER.SCHEMA.COLUMN_ID, to.getId());
        values.put(USER.SCHEMA.COLUMN_NAME, to.getName());
        /* update - ver.20150603 */
        values.put(USER.SCHEMA.COLUMN_GCM_ID, to.getGCMId());

        Log.v(Constants.LOG_TAG, DATA.CLASSNAME + " insert, " + to.toString());
        long rowId = db.insert(USER.SCHEMA.TABLE_NAME, values);
        if (rowId < 0) {
            throw new SQLException("fail at insert");
        }
    }

	/*
	* get, return list of transfer object type
	* DDL, select cache | user | group | schedule | invite
	* */
    public List<Cache> getCache() {
        Cursor cursor = null;
        ArrayList<Cache> ret = null;

        String sql = "SELECT * FROM " + CACHE.SCHEMA.TABLE_NAME + " ORDER BY 1;";     // 1 : first column

        try {
            Log.v(Constants.LOG_TAG, DATA.CLASSNAME + " get - CACHE");
            cursor = db.get(sql);

            // db.logCursorInfo(cursor);

            ret = setBindCursorCache(cursor);
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, DATA.CLASSNAME + " getList ", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return ret;
    }
    public List<User> getUser() {
		Cursor cursor = null;
		ArrayList<User> ret = null;

		String sql = "SELECT * FROM " + USER.SCHEMA.TABLE_NAME + " ORDER BY 1;";     // 1 : first column

		try {
			Log.v(Constants.LOG_TAG, DATA.CLASSNAME + " get - USER");
			cursor = db.get(sql);

			// db.logCursorInfo(cursor);

			ret = setBindCursorUser(cursor);
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, DATA.CLASSNAME + " getList ", e);
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return ret;
	}
	public List<Group> getGroup() {
        Cursor cursor = null;
        ArrayList<Group> ret = null;

        String sql = "SELECT * FROM " + GROUP.SCHEMA.TABLE_NAME + " ORDER BY 1;";     // 1 : first column

        try {
            Log.v(Constants.LOG_TAG, DATA.CLASSNAME + " get - GROUP");
            cursor = db.get(sql);

            // db.logCursorInfo(cursor);

            ret = setBindCursorGroup(cursor);
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, DATA.CLASSNAME + " getList ", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return ret;
	}
	public List<Schedule> getSchedule() {
		Cursor cursor = null;
		ArrayList<Schedule> ret = null;

		String sql = "SELECT * FROM " + SCHEDULE.SCHEMA.TABLE_NAME + " ORDER BY 1;";     // 1 : first column

		try {
			Log.v(Constants.LOG_TAG, DATA.CLASSNAME + " get - SCHEDULE");
			cursor = db.get(sql);

			// db.logCursorInfo(cursor);

			ret = setBindCursorSchedule(cursor);
		} catch (SQLException e) {
			Log.e(Constants.LOG_TAG, DATA.CLASSNAME + " getList ", e);
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return ret;
	}
    public List<Invite> getInvite() {
        Cursor cursor = null;
        ArrayList<Invite> ret = null;

        String sql = "SELECT * FROM " + INVITE.SCHEMA.TABLE_NAME + " ORDER BY 1;";     // 1 : first column

        try {
            Log.v(Constants.LOG_TAG, DATA.CLASSNAME + " get - INVITED");
            cursor = db.get(sql);

            // db.logCursorInfo(cursor);

            ret = setBindCursorInvite(cursor);
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, DATA.CLASSNAME + " getList ", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return ret;
    }

    private ArrayList<Cache> setBindCursorCache(final Cursor cursor) {
        ArrayList<Cache> ret = new ArrayList<Cache>();

        int numRows = cursor.getCount();

        cursor.moveToFirst();

        // SQL문에서 Join 사용시, 테이블명을 사용하면 칼럼명이 틀려지므로 getColumnIndex가 Exception을 낸다.
        // 반드시 alias를 사용해 컬럼명을 동일하게 맞춰야 한다.
        // 값이 null인 경우 getInt()는 0을 반환한다.
        for (int i = 0; i < numRows; i++) {
            Cache to = new Cache();
            to.setId(cursor.getInt(cursor.getColumnIndex(CACHE.SCHEMA.COLUMN_ID)));
            to.setUser(cursor.getInt(cursor.getColumnIndex(CACHE.SCHEMA.COLUMN_USER)));
            to.setType(cursor.getString(cursor.getColumnIndex(CACHE.SCHEMA.COLUMN_TYPE)));
            to.setQuery(cursor.getString(cursor.getColumnIndex(CACHE.SCHEMA.COLUMN_QUERY)));

            ret.add(to);
            cursor.moveToNext();
        }

        return ret;
    }
	private ArrayList<User> setBindCursorUser(final Cursor cursor) {
		ArrayList<User> ret = new ArrayList<User>();

		int numRows = cursor.getCount();

		cursor.moveToFirst();

		// SQL문에서 Join 사용시, 테이블명을 사용하면 칼럼명이 틀려지므로 getColumnIndex가 Exception을 낸다.
		// 반드시 alias를 사용해 컬럼명을 동일하게 맞춰야 한다.
		// 값이 null인 경우 getInt()는 0을 반환한다.
		for (int i = 0; i < numRows; i++) {
			User to = new User();
			to.setId(cursor.getInt(cursor.getColumnIndex(USER.SCHEMA.COLUMN_ID)));
			to.setName(cursor.getString(cursor.getColumnIndex(USER.SCHEMA.COLUMN_NAME)));
            /* update - ver.20150603 */
            to.setGCMId(cursor.getString(cursor.getColumnIndex(USER.SCHEMA.COLUMN_GCM_ID)));

			ret.add(to);
			cursor.moveToNext();
		}

		return ret;
	}
	private ArrayList<Group> setBindCursorGroup(final Cursor cursor) {
		ArrayList<Group> ret = new ArrayList<Group>();

		int numRows = cursor.getCount();

		cursor.moveToFirst();

		// SQL문에서 Join 사용시, 테이블명을 사용하면 칼럼명이 틀려지므로 getColumnIndex가 Exception을 낸다.
		// 반드시 alias를 사용해 컬럼명을 동일하게 맞춰야 한다.
		// 값이 null인 경우 getInt()는 0을 반환한다.
		for (int i = 0; i < numRows; i++) {
			Group to = new Group();
			to.setId(cursor.getInt(cursor.getColumnIndex(GROUP.SCHEMA.COLUMN_ID)));
			to.setAdmin(cursor.getInt(cursor.getColumnIndex(GROUP.SCHEMA.COLUMN_ADMIN)));
			to.setName(cursor.getString(cursor.getColumnIndex(GROUP.SCHEMA.COLUMN_NAME)));

			ret.add(to);
			cursor.moveToNext();
		}

		return ret;
	}
	private ArrayList<Schedule> setBindCursorSchedule(final Cursor cursor) {
		ArrayList<Schedule> ret = new ArrayList<Schedule>();

		int numRows = cursor.getCount();

		cursor.moveToFirst();

		// SQL문에서 Join 사용시, 테이블명을 사용하면 칼럼명이 틀려지므로 getColumnIndex가 Exception을 낸다.
		// 반드시 alias를 사용해 컬럼명을 동일하게 맞춰야 한다.
		// 값이 null인 경우 getInt()는 0을 반환한다.
		for (int i = 0; i < numRows; i++) {
			Schedule to = new Schedule();
			to.setGroup(cursor.getInt(cursor.getColumnIndex(SCHEDULE.SCHEMA.COLUMN_GROUP)));
			to.setOwner(cursor.getInt(cursor.getColumnIndex(SCHEDULE.SCHEMA.COLUMN_OWNER)));
			to.setName(cursor.getString(cursor.getColumnIndex(SCHEDULE.SCHEMA.COLUMN_NAME)));
			to.setStartDate(cursor.getString(cursor.getColumnIndex(SCHEDULE.SCHEMA.COLUMN_START_DATE)));
			to.setEndDate(cursor.getString(cursor.getColumnIndex(SCHEDULE.SCHEMA.COLUMN_END_DATE)));

			ret.add(to);
			cursor.moveToNext();
		}

		return ret;
	}
    private ArrayList<Invite> setBindCursorInvite(final Cursor cursor) {
        ArrayList<Invite> ret = new ArrayList<Invite>();

        int numRows = cursor.getCount();

        cursor.moveToFirst();

        // SQL문에서 Join 사용시, 테이블명을 사용하면 칼럼명이 틀려지므로 getColumnIndex가 Exception을 낸다.
        // 반드시 alias를 사용해 컬럼명을 동일하게 맞춰야 한다.
        // 값이 null인 경우 getInt()는 0을 반환한다.
        for (int i = 0; i < numRows; i++) {
            Invite to = new Invite();
            to.setId(cursor.getInt(cursor.getColumnIndex(INVITE.SCHEMA.COLUMN_ID)));
            to.setGroupID(cursor.getInt(cursor.getColumnIndex(INVITE.SCHEMA.COLUMN_GID)));
            to.setGroupName(cursor.getString(cursor.getColumnIndex(INVITE.SCHEMA.COLUMN_GNAME)));

            ret.add(to);
            cursor.moveToNext();
        }

        return ret;
    }

    /*
    * change to raw-query
    * */
    public String changeQuery(QUERY_TYPE type, User to) {
        switch (type) {
            /* update - ver.20150603 */
            case INSERT : String insertQuery = "INSERT INTO " + USER.SCHEMA.TABLE_NAME + " VALUES (" + to.getId() + ", '" + to.getName() + "', '" + to.getGCMId() +"');"; return insertQuery;
            case UPDATE : String updateQuery = "UPDATE " + USER.SCHEMA.TABLE_NAME + " SET " + USER.SCHEMA.COLUMN_NAME + "='" + to.getName() + "', WHERE " + USER.SCHEMA.COLUMN_ID + "=" + to.getId() +";"; return updateQuery;
            case DELETE : String deleteQuery = "DELETE FROM " + USER.SCHEMA.TABLE_NAME + " WHERE " + USER.SCHEMA.COLUMN_ID + "=" + to.getId()+ ";"; return deleteQuery;
        }
        return null;
    }
    public String changeQuery(QUERY_TYPE type, Group to) {
        switch (type) {
            case INSERT : String insertQuery = "INSERT INTO " + GROUP.SCHEMA.TABLE_NAME + " (" + GROUP.SCHEMA.COLUMN_ADMIN + ", " + GROUP.SCHEMA.COLUMN_NAME + ") VALUES ('" + to.getAdmin() + "', '" + to.getName() + "');"; return insertQuery;
            case UPDATE : String updateQuery = "UPDATE " + GROUP.SCHEMA.TABLE_NAME + " SET " + GROUP.SCHEMA.COLUMN_NAME + "='" + to.getName() + "' WHERE " + GROUP.SCHEMA.COLUMN_ID + "=" + to.getId() + ";"; return updateQuery;
            case DELETE : String deleteQuery = "DELETE FROM " + GROUP.SCHEMA.TABLE_NAME + " WHERE " + GROUP.SCHEMA.COLUMN_ID + "=" + to.getId() + ";"; return deleteQuery;
        }
        return null;
    }
    public String changeQuery(QUERY_TYPE type, Schedule to) {
        switch (type) {
            case INSERT : String insertQuery = "INSERT INTO " + SCHEDULE.SCHEMA.TABLE_NAME + " (" + SCHEDULE.SCHEMA.COLUMN_GROUP + ", " + SCHEDULE.SCHEMA.COLUMN_OWNER + ", " + SCHEDULE.SCHEMA.COLUMN_NAME + ", " + SCHEDULE.SCHEMA.COLUMN_START_DATE + ", " + SCHEDULE.SCHEMA.COLUMN_END_DATE + ") VALUES (" + to.getGroup() + ", " + to.getOwner() + ", '" + to.getName() + "', '" + to.getStartDate() + "', '" + to.getEndDate() + "');"; return insertQuery;
            case UPDATE : String updateQuery = "UPDATE " + SCHEDULE.SCHEMA.TABLE_NAME + " SET " + SCHEDULE.SCHEMA.COLUMN_OWNER + "=" + to.getOwner() + ", " + SCHEDULE.SCHEMA.COLUMN_NAME + "='" + to.getName() +"', " + SCHEDULE.SCHEMA.COLUMN_START_DATE + "='" + to.getStartDate() + "', " + SCHEDULE.SCHEMA.COLUMN_END_DATE + "='" + to.getEndDate() + "' WHERE " + SCHEDULE.SCHEMA.COLUMN_ID + "=" + to.getId() + ";"; return updateQuery;
            case DELETE : String deleteQuery = "DELETE FROM " + SCHEDULE.SCHEMA.TABLE_NAME + " WHERE " + SCHEDULE.SCHEMA.COLUMN_ID + "=" + to.getId() + ";"; return deleteQuery;
        }
        return null;
    }

    /*
    * search last-id in Cache
    * */
    public long getCacheSize() {
        Cursor cursor = null;
        long count = 0;

        String sql = "SELECT * FROM '" + CACHE.SCHEMA.TABLE_NAME + "';";     // 1 : first column

        try {
            Log.v(Constants.LOG_TAG, DATA.CLASSNAME + " getSize - Cache");
            cursor = db.get(sql);
            count = cursor.getCount();
            // db.logCursorInfo(cursor);

            //ret = setBindCursorUser(cursor);
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, DATA.CLASSNAME + " getCacheSize ", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        Log.i("getSize - Cache : ", String.valueOf(count));
        return count;
    }

    /* raw-query, "execSQL()" */
    public void exec(String sql) {
        db.exec(sql);
    }
}
