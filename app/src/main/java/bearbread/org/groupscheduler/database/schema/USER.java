package bearbread.org.groupscheduler.database.schema;

/**
 * Created by Eugene J. Jeon on 2015-04-08.
 *
 * 테이블명, 컬럼명 정보를 상수로 정의!
 */
public class USER {
	USER() {}

	public static final class SCHEMA {
		private SCHEMA() {}

		public static final String TABLE_NAME = "USER";

		public static final String COLUMN_ID = "UID";           // integer 8 byte, primary key
		public static final String COLUMN_NAME = "UNAME";       // varchar 32 byte, NOT NULL
		public static final String COLUMN_TEL = "UTEL";         // varchar 13 byte. NOT NULL, example, 82 010 1234 5678

		public String[] getColumnNames() {
			String[] columnNames = {COLUMN_ID, COLUMN_NAME, COLUMN_TEL};
            return columnNames;
		}
	}
}
