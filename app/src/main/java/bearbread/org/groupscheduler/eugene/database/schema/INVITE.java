package bearbread.org.groupscheduler.eugene.database.schema;

/**
 * Created by Eugene J. Jeon on 2015-05-26.
 */
public class INVITE {
    INVITE() {}

    public static final class SCHEMA {
        private SCHEMA() {}

        public static final String TABLE_NAME = "INVITE";

        public static final String COLUMN_ID = "ID";            // 초대된 순번
        public static final String COLUMN_GID = "GID";          // 초대된 그룹의 아이디, 수락시 서버로 요청할 때 필요!
        public static final String COLUMN_GNAME = "GNAME";      // 초대된 그룹의 이름

        public String[] getColumnNames() {
            String[] columnNames = {COLUMN_ID, COLUMN_GID, COLUMN_GNAME};
            return columnNames;
        }
    }
}
