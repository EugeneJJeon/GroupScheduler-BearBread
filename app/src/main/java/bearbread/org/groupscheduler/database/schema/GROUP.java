package bearbread.org.groupscheduler.database.schema;

/**
 * Created by Eugene J. Jeon on 2015-04-08.
 *
 * this columns are string type and 32 bit.
 */
public class GROUP {
    public static int SIZE = 0;
	GROUP() {}

	public static final class SCHEMA {
		private SCHEMA() {}

		public static final String TABLE_NAME = "GROUP";

		public static final String COLUMN_ID = "GID";           // integer 8 byte, primary key, after sync online db
		public static final String COLUMN_ADMIN = "GADMIN";     // integer 8 byte and foreign key, NOT NULL, references uid of user table
		public static final String COLUMN_NAME = "GNAME";       // varchar 32 byte, NOT NULL

		public String[] getColumnNames() {
			String[] columnNames = {COLUMN_ID, COLUMN_ADMIN, COLUMN_NAME};
			return columnNames;
		}
	}
}
