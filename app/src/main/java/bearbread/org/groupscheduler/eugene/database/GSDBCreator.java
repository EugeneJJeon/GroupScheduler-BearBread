package bearbread.org.groupscheduler.eugene.database;

import bearbread.org.groupscheduler.eugene.database.schema.CACHE;
import bearbread.org.groupscheduler.eugene.database.schema.GROUP;
import bearbread.org.groupscheduler.eugene.database.schema.INVITE;
import bearbread.org.groupscheduler.eugene.database.schema.SCHEDULE;
import bearbread.org.groupscheduler.eugene.database.schema.USER;

/**
 * Created by Eugene J. Jeon on 2015-04-08.
 */
public class GSDBCreator implements DBCreator {
	// Table Creation USER DDL
	private final String TABLE_CREATE_USER = "CREATE TABLE '"
			+ USER.SCHEMA.TABLE_NAME + "' ("
			+ USER.SCHEMA.COLUMN_ID + " INTEGER, "
			+ USER.SCHEMA.COLUMN_NAME + " TEXT NOT NULL, "
            + USER.SCHEMA.COLUMN_GCM_ID + " TEXT NOT NULL"              /* update - ver.20150603 */
			+ ");";
    private final String TABLE_DROP_USER = "DROP TABLE IF EXISTS '" + USER.SCHEMA.TABLE_NAME + "';";

	//Index Creation USER DDL
//	private final String INDEX_CREATE_USER = "CREATE UNIQUE INDEX "
//			+ USER.SCHEMA.TABLE_NAME + "_pk ON "
//			+ USER.SCHEMA.TABLE_NAME + " (" + USER.SCHEMA.COLUMN_ID + ");";

	// Table Creation GROUP DDL
	private final String TABLE_CREATE_GROUP = "CREATE TABLE '"
			+ GROUP.SCHEMA.TABLE_NAME + "' ("
			+ GROUP.SCHEMA.COLUMN_ID + " INTEGER, "
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
            + SCHEDULE.SCHEMA.COLUMN_ID + " INTEGER, "
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

    // TABLE Creation INVITED
    private final String TABLE_INVITED = "CREATE TABLE '"
            + INVITE.SCHEMA.TABLE_NAME + "' ("
            + INVITE.SCHEMA.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
            + INVITE.SCHEMA.COLUMN_GID + " INTEGER NOT NULL, "
            + INVITE.SCHEMA.COLUMN_GNAME + " TEXT NOT NULL"
            + ");";
    private final String TABLE_DROP_INVITED = "DROP TABLE IF EXISTS '" + INVITE.SCHEMA.TABLE_NAME + "';";

    // TABLE Creation CACHE
    private final String TABLE_CREATE_CACHE = "CREATE TABLE '"
            + CACHE.SCHEMA.TABLE_NAME + "' ("
            + CACHE.SCHEMA.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
            + CACHE.SCHEMA.COLUMN_USER + " INTEGER NOT NULL, "
            + CACHE.SCHEMA.COLUMN_TYPE + " TEXT NOT NULL, "
            + CACHE.SCHEMA.COLUMN_QUERY + " TEXT NOT NULL"
            + ");";
    private final String TABLE_DROP_CACHE = "DROP TABLE IF EXISTS '" + CACHE.SCHEMA.TABLE_NAME + "';";

    @Override
	public String[] getCreateTableStatement() {
		String[] tableStatement = {TABLE_CREATE_USER, TABLE_CREATE_GROUP, TABLE_CREATE_SCHEDULE, TABLE_INVITED, TABLE_CREATE_CACHE};
		return tableStatement;
	}

    @Override
    public String[] getDropTableStatement() {
        String[] tableStatement = {TABLE_DROP_USER, TABLE_DROP_GROUP, TABLE_DROP_SCHEDULE, TABLE_DROP_INVITED, TABLE_DROP_CACHE};
        return tableStatement;
    }

	@Override
	public String[] getCreateIndexStatement() {
		//String[] indexStatement = {INDEX_CREATE_USER, INDEX_CREATE_GROUP, INDEX_CREATE_SCHEDULE};
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
