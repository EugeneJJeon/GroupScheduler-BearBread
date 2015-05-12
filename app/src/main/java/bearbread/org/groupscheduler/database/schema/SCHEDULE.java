package bearbread.org.groupscheduler.database.schema;

/**
 * Created by Eugene J. Jeon on 2015-04-08.
 *
 * this columns are string type and id, owner, name are 32 bit.
 * date are 16 bit, like this. YYYYMMDDHHMMSSDAY, DAY is MON, THE, etc.
 */
public class SCHEDULE {
    public static int SIZE = 0;
	SCHEDULE() {}

	public static final class SCHEMA {
		private SCHEMA() {}

		public static final String TABLE_NAME = "SCHEDULE";

        public static final String COLUMN_ID = "SID";                   // integer 8 byte primary key
		public static final String COLUMN_GROUP = "SGID";               // integer 8 byte and foreign key(ref group.gid)
		public static final String COLUMN_OWNER = "SOWNER";             // integer 8 byte and foreign key(ref user.uid)
		public static final String COLUMN_NAME = "SNAME";               // varchar 32 byte, NOT NULL
		public static final String COLUMN_START_DATE = "SSTARTDATE";    // varchar 8, NOT NULL
		public static final String COLUMN_END_DATE = "SENDDATE";        // varchar 8, NOT NULL

		public String[] getColumnNames() {
			String[] columnNames = {COLUMN_GROUP, COLUMN_OWNER, COLUMN_NAME, COLUMN_START_DATE, COLUMN_END_DATE};
			return columnNames;
		}
	}
}
