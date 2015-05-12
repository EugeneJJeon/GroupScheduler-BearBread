package bearbread.org.groupscheduler.database.schema;

/**
 * Created by Eugene J. Jeon on 2015-04-08.
 */
public class USER_TO_GROUP {
	USER_TO_GROUP() {}

	public static class SCHEMA {
		private SCHEMA() {}

		public static final String TABLE_NAME = "USERTOGROUP";

		public static final String COLUMN_USER_ID = "UTGUID";       // integer 8 byte and foreign key, NOT NULL, references uid of user table
		public static final String COLUMN_GROUP_ID = "UTGGID";      // integer 8 byte and foreign key, NOT NULL, references gid of group table

		public String[] getColumnNames() {
			String[] columnNames = {COLUMN_USER_ID, COLUMN_GROUP_ID};
			return columnNames;
		}
	}
}
