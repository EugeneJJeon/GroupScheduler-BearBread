package bearbread.org.groupscheduler.database.schema;

/**
 * Created by Eugene J. Jeon on 2015-05-04.
 */
public class CACHE {
    public static int SIZE = 0;
    CACHE() {}

    public static final class SCHEMA {
        private SCHEMA() {}

        public static final String TABLE_NAME = "CACHE";

        public static final String COLUMN_ID = "CID";           // integer 8 byte, primary key
        public static final String COLUMN_GROUP = "CGROUP";     // integer 8 byte NOT NULL
        public static final String COLUMN_TYPE = "CTYPE";       // varchar 8 byte NOT NULL
        public static final String COLUMN_QUERY = "CQUERY";     // varchar 32 byte NOT NULL

        public String[] getColumnNames() {
            String[] columnNames = {COLUMN_ID, COLUMN_QUERY};
            return columnNames;
        }
    }
}
