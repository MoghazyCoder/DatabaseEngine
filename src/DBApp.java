import java.io.*;
import java.util.*;
import java.util.Map.Entry;



public class DBApp {

	private String defaultPath = "databases/";
	private String name, dbPath;

	// TODO: Review below attributes.
	private static HashSet<String> IndexedColumns= new HashSet<>(); // add all indexed columns here
	private Hashtable<String,String> htblColNameType ;
	private HashMap<String,Table> tables = new HashMap<>();
	private Writer writer;
	
	
	private File metadata;
	private Properties properties;
	private Integer MaxRowsPerPage;


	public DBApp (String name, Integer MaxRowsPerPage) throws IOException {
		
		this.name = name;
		this.dbPath = defaultPath + this.name + '/';
		this.MaxRowsPerPage = MaxRowsPerPage;
		File dbFolder = new File(dbPath);
		dbFolder.mkdir();
		
		// config file
		properties = new Properties();
		properties.put("MaxRowsPerPage", MaxRowsPerPage.toString());
		new File(dbPath + "/config").mkdirs();
		File config = new File(dbPath + "/config/DBApp.config");
		config.createNewFile();
		FileOutputStream fos = new FileOutputStream(config);
		properties.store(fos, "DB Properties");
		fos.close();

		// metadata file
		File data = new File(dbPath + "data/");
		data.mkdirs();

		this.metadata = new File(dbPath + "data/" + "metadata.csv");
		metadata.createNewFile();

	}


	public void createTable (String strTableName, String strClusteringKeyColumn, 
			Hashtable<String,String> htblColNameType ) throws DBAppException, IOException {

		Table table = new Table(strTableName, dbPath, htblColNameType, strClusteringKeyColumn, MaxRowsPerPage);
		tables.put(strTableName, table);
		
		this.htblColNameType = htblColNameType;
		Set<String> columns = htblColNameType.keySet();

		for(String column: columns){
			boolean key = false;
			boolean indexed = false;
			if(strClusteringKeyColumn.equals(column))
				key = true;
			if(IndexedColumns.contains(column))
				indexed = true;
			//there is no indexed columns for now
			
			//TODO: Fix this ERROR !!
			writer.append(strTableName + "," + column + "," + htblColNameType.get(column) + "," + key + "," + indexed + '\n');
		}
	}
	

	public void insertIntoTable(String strTableName, Hashtable<String,Object> htblColNameValue)
					throws DBAppException, IOException, ClassNotFoundException {
		Table table = getTable(strTableName);
		if(table == null)
			throw new DBAppException("Table: (" + strTableName + ") does not exist");
		if(!table.insert(htblColNameValue))
			throw new DBAppException("Insertion in table: (" + strTableName + ") failed");
	}
	
	private Table getTable(String strTableName) throws FileNotFoundException, IOException, ClassNotFoundException {

		File file = new File(dbPath + strTableName + "/" + strTableName + ".class");
		if(file.exists()){
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Table table = (Table) ois.readObject();
			ois.close();
			return table;
		}
		return null;
	}

}

class DBAppException extends Exception {

	/**
	 * Any errors related to the DB App can be detected using this exceptions
	 */
	private static final long serialVersionUID = 1L;

	public DBAppException(String string) { super(string); }

}
