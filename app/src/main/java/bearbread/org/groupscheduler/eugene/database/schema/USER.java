package bearbread.org.groupscheduler.eugene.database.schema;

/**
 * Created by Eugene J. Jeon on 2015-04-08.
 *
 * 테이블명, 컬럼명 정보를 상수로 정의!
 */
public class USER {
	USER() {}

	public static final class SCHEMA {
		private SCHEMA() {}

		public static final String TABLE_NAME = "USERS";

		public static final String COLUMN_ID = "UID";           // 유저 고유 아이디, 유저 폰 번호
		public static final String COLUMN_NAME = "UNAME";       // 유저 이름
        /* update - ver.20150603 */
        public static final String COLUMN_GCM_ID = "UGCMID";     // GMS를 사용하기 위한 등록 아이디!

		public String[] getColumnNames() {
			String[] columnNames = {COLUMN_ID, COLUMN_NAME, COLUMN_GCM_ID};
            return columnNames;
		}
	}
}
