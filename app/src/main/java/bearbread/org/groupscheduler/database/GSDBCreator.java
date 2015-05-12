package bearbread.org.groupscheduler.database;

import bearbread.org.groupscheduler.database.schema.CACHE;
import bearbread.org.groupscheduler.database.schema.GROUP;
import bearbread.org.groupscheduler.database.schema.SCHEDULE;
import bearbread.org.groupscheduler.database.schema.USER;
import bearbread.org.groupscheduler.database.schema.USER_TO_GROUP;

/**
 * Created by Eugene J. Jeon on 2015-04-08.
 */
public class GSDBCreator implements DBCreator {
	// Table Creation USER DDL
	private final String TABLE_CREATE_USER = "CREATE TABLE '"
			+ USER.SCHEMA.TABLE_NAME + "' ("
			+ USER.SCHEMA.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
			+ USER.SCHEMA.COLUMN_NAME + " TEXT NOT NULL, "
			+ USER.SCHEMA.COLUMN_TEL + " TEXT NOT NULL"
			+ ");";
    private final String TABLE_DROP_USER = "DROP TABLE IF EXISTS '" + USER.SCHEMA.TABLE_NAME + "';";

	// Index Creation USER DDL
	private final String INDEX_CREATE_USER = "CREATE UNIQUE INDEX "
			+ USER.SCHEMA.TABLE_NAME + "_pk ON "
			+ USER.SCHEMA.TABLE_NAME + " (" + USER.SCHEMA.COLUMN_ID + ");";

	// Table Creation GROUP DDL
	private final String TABLE_CREATE_GROUP = "CREATE TABLE '"
			+ GROUP.SCHEMA.TABLE_NAME + "' ("
			+ GROUP.SCHEMA.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
			+ GROUP.SCHEMA.COLUMN_ADMIN + " INTEGER NOT NULL, "
//            + "FOREIGN KEY(" + GROUP.SCHEMA.COLUMN_ADMIN + ") REFERENCES " + USER.SCHEMA.TABLE_NAME + "(" + USER.SCHEMA.COLUMN_ID + "), "
			+ GROUP.SCHEMA.COLUMN_NAME + " TEXT NOT NULL"
            + ");";
    private final String TABLE_DROP_GROUP = "DROP TABLE IF EXISTS '" + GROUP.SCHEMA.TABLE_NAME + "';";

	// Index Creation GROUP DDL
//	private final String INDEX_CREATE_GROUP = "CREATE UNIQUE INDEX "
//			+ GROUP.SCHEMA.TABLE_NAME + "_pk ON "
//			+ GROUP.SCHEMA.TABLE_NAME + " (" + GROUP.SCHEMA.COLUMN_ID + ");";
	// TODO: COLUMN_ADMIN IS FOREIGN KEY!!

	// Table Creation SCHEDULE DDL
	private final String TABLE_CREATE_SCHEDULE = "CREATE TABLE '"
			+ SCHEDULE.SCHEMA.TABLE_NAME + "' ("
            + SCHEDULE.SCHEMA.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
			+ SCHEDULE.SCHEMA.COLUMN_GROUP + " INTEGER NOT NULL, "
//            + "FOREIGN KEY(" + SCHEDULE.SCHEMA.COLUMN_GROUP + ") REFERENCES " + GROUP.SCHEMA.TABLE_NAME + "(" + GROUP.SCHEMA.COLUMN_ID + "), "
			+ SCHEDULE.SCHEMA.COLUMN_OWNER + " INTEGER NOT NULL, "
//            + "FOREIGN KEY(" + SCHEDULE.SCHEMA.COLUMN_OWNER + ") REFERENCES " + USER.SCHEMA.TABLE_NAME + "(" + USER.SCHEMA.COLUMN_ID + "), "
			+ SCHEDULE.SCHEMA.COLUMN_NAME + " TEXT NOT NULL, "
			+ SCHEDULE.SCHEMA.COLUMN_START_DATE + " TEXT NOT NULL, "
			+ SCHEDULE.SCHEMA.COLUMN_END_DATE + " TEXT NOT NULL"
            + ");";
    private final String TABLE_DROP_SCHEDULE = "DROP TABLE IF EXISTS '" + SCHEDULE.SCHEMA.TABLE_NAME + "';";

	// Index Creation SCHEDULE DDL
//	private final String INDEX_CREATE_SCHEDULE = "CREATE UNIQUE INDEX "
//            + SCHEDULE.SCHEMA.TABLE_NAME + "_pk ON "
//            + SCHEDULE.SCHEMA.TABLE_NAME + " (" + SCHEDULE.SCHEMA.COLUMN_ID + ");";
	// TODO: COLUMN_OWNER IS FOREIGN KEY!

	// Table Creation USER_TO_GROUP DDL
	private final String TABLE_CREATE_USER_TO_GROUP = "CREATE TABLE '"
            + USER_TO_GROUP.SCHEMA.TABLE_NAME + "' ("
            + USER_TO_GROUP.SCHEMA.COLUMN_USER_ID + " INTEGER NOT NULL, "
//            + "FOREIGN KEY(" + USER_TO_GROUP.SCHEMA.COLUMN_USER_ID + ") REFERENCES " + USER.SCHEMA.TABLE_NAME + "(" + USER.SCHEMA.COLUMN_ID + "), "
            + USER_TO_GROUP.SCHEMA.COLUMN_GROUP_ID + " INTEGER NOT NULL"
//            + "FOREIGN KEY(" + USER_TO_GROUP.SCHEMA.COLUMN_GROUP_ID + ") REFERENCES " + GROUP.SCHEMA.TABLE_NAME + "(" + GROUP.SCHEMA.COLUMN_ID + "), "
            + ");";
    private final String TABLE_DROP_USER_TO_GROUP = "DROP TABLE IF EXISTS " + USER_TO_GROUP.SCHEMA.TABLE_NAME + ";";

    // Index Creation USER_TO_GROUP DDL
//	private final String INDEX_CREATE_USER_TO_GROUP = "CREATE UNIQUE INDEX "
//            + USER_TO_GROUP.SCHEMA.TABLE_NAME + "_pk ON "
	// TODO: COLUMN_USER_ID IS FOREIGN KEY!
	// TODO: COLUMN_GROUP_ID IS FOREIGN KEY!

    // TABLE Creation CACHE
    private final String TABLE_CREATE_CACHE = "CREATE TABLE '"
            + CACHE.SCHEMA.TABLE_NAME + "' ("
            + CACHE.SCHEMA.COLUMN_ID + " INTEGER, "
            + CACHE.SCHEMA.COLUMN_TYPE + " TEXT, "
//            + CACHE.SCHEMA.COLUMN_TABLE + " TEXT, "
            + CACHE.SCHEMA.COLUMN_QUERY + " TEXT"
            + ");";
    private final String TABLE_DROP_CACHE = "DROP TABLE IF EXISTS '" + CACHE.SCHEMA.TABLE_NAME + "';";

    @Override
	public String[] getCreateTableStatement() {
		String[] tableStatement = {TABLE_CREATE_USER, TABLE_CREATE_GROUP, TABLE_CREATE_SCHEDULE, TABLE_CREATE_USER_TO_GROUP, TABLE_CREATE_CACHE};
		return tableStatement;
	}

    @Override
    public String[] getDropTableStatement() {
        String[] tableStatement = {TABLE_DROP_USER, TABLE_DROP_GROUP, TABLE_DROP_SCHEDULE, TABLE_DROP_USER_TO_GROUP, TABLE_DROP_CACHE};
        return tableStatement;
    }

	@Override
	public String[] getCreateIndexStatement() {
		//String[] indexStatement = {INDEX_CREATE_USER, INDEX_CREATE_GROUP, INDEX_CREATE_SCHEDULE, INDEX_CREATE_USER_TO_GROUP, INDEX_CREATE_GROUP_TO_SCHEDULE};
		//return indexStatement;
        return null;
	}

	@Override
	public String[] getCreateViewStatement() { return null; }

	@Override
	public String[] getCreateTriggerStatement() { return null; }

	@Override
	public String[] getInitDataInsertStatement() { return null;	}
}
