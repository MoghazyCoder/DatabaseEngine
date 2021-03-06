import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class DBAppTest {
	static DBApp ourDB;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws DBAppException, IOException, ClassNotFoundException, ParseException {

		try {
			String strTableName = "Student";

			ourDB = new DBApp(strTableName);

			Hashtable htblColNameType = new Hashtable();
			htblColNameType.put("id", "java.lang.Integer");
			htblColNameType.put("name", "java.lang.String");
			htblColNameType.put("gpa", "java.lang.double");
			ourDB.createTable(strTableName, "id", htblColNameType);
			// createBRINIndex( strTableName, "gpa" );
			Hashtable htblColNameValue = new Hashtable();
			htblColNameValue.put("id", new Integer(2343432));
			htblColNameValue.put("name", new String("Ahmed Noor"));
			htblColNameValue.put("gpa", new Double(0.95));
			ourDB.insertIntoTable(strTableName, htblColNameValue);

			ourDB.deleteFromTable(strTableName, htblColNameValue);
			htblColNameValue.clear();
			htblColNameValue.put("id", new Integer(4253455));
			htblColNameValue.put("name", new String("Ahmed Ali"));
			htblColNameValue.put("gpa", new Double(0.95));
			ourDB.insertIntoTable(strTableName, htblColNameValue);
			htblColNameValue.clear();
			htblColNameValue.put("id", new Integer(453455));
			htblColNameValue.put("name", new String("Dalia Noor"));
			htblColNameValue.put("gpa", new Double(1.25));
			ourDB.insertIntoTable(strTableName, htblColNameValue);
			htblColNameValue.clear();
			htblColNameValue.put("id", new Integer(23498));
			htblColNameValue.put("name", new String("John Noor"));
			htblColNameValue.put("gpa", new Double(1.5));
			ourDB.insertIntoTable(strTableName, htblColNameValue);
			htblColNameValue.clear();
			htblColNameValue.put("id", new Integer(78452));
			htblColNameValue.put("name", new String("Zaky Noor"));
			htblColNameValue.put("gpa", new Double(0.88));
			ourDB.insertIntoTable(strTableName, htblColNameValue);
			 htblColNameValue.clear();
			 htblColNameValue.put("id", new Integer(23498));
			 htblColNameValue.put("name", new String("John Noor"));
			 htblColNameValue.put("gpa", new Double(1.5));
			 ourDB.deleteFromTable(strTableName, htblColNameValue);
			 htblColNameValue.clear();
			 htblColNameValue.put("id", new Integer(78452));
			 htblColNameValue.put("name", new String("3ala2 Noor"));
			 htblColNameValue.put("gpa", new Double(1.08));
			 ourDB.updateTable(strTableName,"78452", htblColNameValue);
			 htblColNameValue.clear();

				htblColNameValue.put("id", new Integer(4253455));
				htblColNameValue.put("name", new String("Ahmed Ali"));
				htblColNameValue.put("gpa", new Double(0.95));
				ourDB.deleteFromTable(strTableName, htblColNameValue);

		} catch (DBAppException D) {
			System.out.println(D.getMessage());
		}

		TestSerialization();

	}

	public static void TestSerialization() throws IOException, ClassNotFoundException {
		Set<String> names = ourDB.getTables().keySet();
		for (String name : names) {

			File table1 = new File("databases/" + name + "/" + name + "/" + name + ".class");
			InputStream file1 = new FileInputStream(table1);
			InputStream buffer1 = new BufferedInputStream(file1);
			ObjectInput input1 = new ObjectInputStream(buffer1);

			Table ttt = (Table) input1.readObject();

			for (int i = 0; i <= ttt.getCurPageIndex(); i++) {
				File table = new File("databases/" + name + "/" + name + "/" + name + "_" + i + ".class");
				System.out.println("databases/" + name + "/" + name + "/" + name + "_" + i + ".class");
				InputStream file = new FileInputStream(table);
				InputStream buffer = new BufferedInputStream(file);
				ObjectInput input = new ObjectInputStream(buffer);
				try {

					Page p = (Page) input.readObject();

					ArrayList<Tuple> t = p.getTuples();

					for (Tuple tt : t) {
						if (tt != null)
							System.out.println(tt.toString());
					}

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				input.close();
			}
		}
	}

}
