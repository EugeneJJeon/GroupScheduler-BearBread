package bearbread.org.groupscheduler.eugene.database;

/**
 * Created by Eugene J. Jeon on 2015-04-08.
 */
public interface DBCreator {
	public static final String DB_NAME = "GroupScheduler.db";
	public static final int DB_VERSION = 1;                     //TODO: 배포전에 꼭! 수정해야할 부분!

	// Table DefinitionStatement
	public String[] getCreateTableStatement();
    public String[] getDropTableStatement();

	// Index DefinitionStatement
	public String[] getCreateIndexStatement();

	// View Definition Statement
	public String[] getCreateViewStatement();

	// Trigger Definition Statement
	public String[] getCreateTriggerStatement();

	// Initial Data Insert Statement
	public String[] getInitDataInsertStatement();
}
