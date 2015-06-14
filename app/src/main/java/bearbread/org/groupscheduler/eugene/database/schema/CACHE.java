package bearbread.org.groupscheduler.eugene.database.schema;

/**
 * Created by Eugene J. Jeon on 2015-05-04.
 */
public class CACHE {
    public static int SIZE = 0;
    CACHE() {}

    public static final class SCHEMA {
        private SCHEMA() {}

        public static final String TABLE_NAME = "CACHE";

        public static final String COLUMN_ID = "CID";           // 캐시에 저장된 순번
        public static final String COLUMN_USER = "CUID";        // 유저 ID, 서버에서 누가보낸 건지 판단하기 위함
        public static final String COLUMN_TYPE = "CTYPE";       // 쿼리 타입을 알기 위해서 사용
        public static final String COLUMN_QUERY = "CQUERY";     // 캐시에 저장된 쿼리

        public String[] getColumnNames() {
            String[] columnNames = {COLUMN_ID, COLUMN_USER, COLUMN_TYPE, COLUMN_QUERY};
            return columnNames;
        }
    }
}
