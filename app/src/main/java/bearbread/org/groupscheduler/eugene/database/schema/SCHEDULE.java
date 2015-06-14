package bearbread.org.groupscheduler.eugene.database.schema;

/**
 * Created by Eugene J. Jeon on 2015-04-08.
 *
 * this columns are string type and id, owner, name are 32 bit.
 * date are 16 bit, like this. YYYYMMDDHHMMSSDAY, DAY is MON, THE, etc.
 */
public class SCHEDULE {
//    public static int SIZE = 0;
	SCHEDULE() {}

	public static final class SCHEMA {
		private SCHEMA() {}

		public static final String TABLE_NAME = "SCHEDULES";

        public static final String COLUMN_ID = "SID";                   // 일정 고유 아이디, 서버로부터 발급 받을 것!
		public static final String COLUMN_GROUP = "SGID";               // 일정이 속한 그룹 아이디
		public static final String COLUMN_OWNER = "SOWNER";             // 일정의 소유자
		public static final String COLUMN_NAME = "SNAME";               // 일정 이름
		public static final String COLUMN_START_DATE = "SSTARTDATE";    // 일정 시작일
		public static final String COLUMN_END_DATE = "SENDDATE";        // 일정 종료일

		public String[] getColumnNames() {
			String[] columnNames = {COLUMN_GROUP, COLUMN_GROUP, COLUMN_OWNER, COLUMN_NAME, COLUMN_START_DATE, COLUMN_END_DATE};
			return columnNames;
		}
	}
}
