package bearbread.org.groupscheduler.eugene.database.schema;

/**
 * Created by Eugene J. Jeon on 2015-04-08.
 *
 * this columns are string type and 32 bit.
 */
public class GROUP {
//    public static int SIZE = 0;
	GROUP() {}

	public static final class SCHEMA {
		private SCHEMA() {}

		public static final String TABLE_NAME = "GROUPS";

		public static final String COLUMN_ID = "GID";           // 그룹 고유 아이디, 서버로부터 발급 받을 것!
		public static final String COLUMN_ADMIN = "GADMIN";     // 그룹 관리자
		public static final String COLUMN_NAME = "GNAME";       // 그룹 이름

		public String[] getColumnNames() {
			String[] columnNames = {COLUMN_ID, COLUMN_ADMIN, COLUMN_NAME};
			return columnNames;
		}
	}
}
