import netscape.javascript.JSObject;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DataStorageManager {
	
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String location = "jdbc:mysql://localhost:3306/voting";
	private static final String user = "root";
	private static final String password = "sayan";
	
	private static final String ADMIN_TABLE = "ADMIN";
	private static final String CAMPAIGN_TABLE = "CAMPAIGN";
	private static final String FIELD_TABLE = "FIELDS";
	
	private static final String ALL_COLS = "*";
	private static final String ADMIN_EMAIL = "EMAIL";
	private static final String ADMIN_USERNAME = "USERNAME";
	private static final String ADMIN_PASSWORD = "PASSWORD";
	
	private static final String EQUAL_OPERATOR = "=";
	private static final String NOT_EQUAL_OPERATOR = "<>";
	private static final String GREATER_OPERATOR = ">";
	private static final String LESSER_OPERATOR = "<";
	private static final String GREATER_EQUAL_OPERATOR = ">=";
	private static final String LESSER_EQUAL_OPERATOR = "<=";
	private static final String IN_OPERATOR = "IN";
	private static final String NOT_IN_OPERATOR = "NOT IN";
	
	private static String getInsertCommand(String table, String values){
		return "INSERT INTO " + table + " VALUES (" + values + ");";
	}
	
	private static String getSelectCommand(String table, String attributes){
		return "SELECT " + attributes + " " + table + ";";
	}
	
	private static String getSelectCommand(String table, String attributes, String conditions){
		return "SELECT " + attributes + " FROM " + table + " WHERE " + conditions + ";";
	}
	
	private static String makeAtomicCondition(String attribute, String operator, String value){
		return attribute + " " + operator + " \"" + value + "\"";
	}
	
	private static void executeStatement(String command) throws Exception {
		Class.forName(driver);
		Connection connection = DriverManager.getConnection(location, user, password);
		Statement statement = connection.createStatement();
		statement.execute(command);
	}
	
	private static ResultSet executeQuery(String command) throws Exception{
		Class.forName(driver);
		Connection connection = DriverManager.getConnection(location, user, password);
		Statement statement = connection.createStatement();
		return statement.executeQuery(command);
	}
	
	public static String storeData(AdminData adminData) throws Exception {
		executeStatement(getInsertCommand(ADMIN_TABLE, adminData.toSQLString()));
		return "stored";
	}
	
	public static String getAdminData(String email, String password) throws Exception {
		String condition = makeAtomicCondition(ADMIN_EMAIL, EQUAL_OPERATOR, email);
		ResultSet resultSet = executeQuery(getSelectCommand(ADMIN_TABLE, ALL_COLS, condition));
		if(resultSet.wasNull())
			return "account doesn't exist";
		resultSet.next();
		JSONObject object = new JSONObject();
		object.put(ADMIN_USERNAME, resultSet.getString(ADMIN_USERNAME));
		object.put(ADMIN_EMAIL, resultSet.getString(ADMIN_USERNAME));
		if(password.equals(resultSet.getString(ADMIN_PASSWORD)))
			object.put(ADMIN_PASSWORD, resultSet.getString(ADMIN_PASSWORD));
		else
			return "incorrect password";
		return object.toJSONString();
	}
}
