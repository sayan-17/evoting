import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataStorageManager {
	
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String location = "jdbc:mysql://localhost:3306/voting";
	private static final String user = "root";
	private static final String passkey = "sayan";
	
	private static final String ADMIN_TABLE = "ADMIN";
	private static final String CAMPAIGN_TABLE = "CAMPAIGN";
	private static final String FIELD_TABLE = "FIELDS";
	
	private static final String ALL_COLS = "*";
	private static final String ADMIN_ID = "ADMIN_ID";
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
	
	private static String getUpdateCommand(String tableName, String attribute, String value){
		return "UPDATE " + tableName + " SET " + attribute + "=\"" + value + "\"";
	}
	
	private static String getUpdateCommand(String tableName, String attribute, String value, String conditions){
		return "UPDATE " + tableName + " SET " + attribute + "=\"" + value + "\"" + " WHERE " + conditions;
	}
	
	private static void executeStatement(String command) throws Exception {
		Class.forName(driver);
		Connection connection = DriverManager.getConnection(location, user, passkey);
		Statement statement = connection.createStatement();
		statement.execute(command);
	}
	
	private static ResultSet executeQuery(String command) throws Exception{
		Class.forName(driver);
		Connection connection = DriverManager.getConnection(location, user, passkey);
		Statement statement = connection.createStatement();
		return statement.executeQuery(command);
	}
	
	public static String storeData(AdminData adminData) throws Exception {
		try{
			executeStatement(getInsertCommand(ADMIN_TABLE, adminData.toSQLString()));
		}catch (MySQLIntegrityConstraintViolationException e){
			if(e.toString().contains("Duplicate entry"))
				return "already registered email";
			else
				throw new Exception("some error occurred");
		}
		return "stored";
	}
	
	public static String getAdminData(String email, String password) throws Exception {
		String condition = makeAtomicCondition(ADMIN_EMAIL, EQUAL_OPERATOR, email);
		ResultSet resultSet = executeQuery(getSelectCommand(ADMIN_TABLE, ALL_COLS, condition));
		if(resultSet.wasNull())
			return "account doesn't exist";
		if(resultSet.next()) {
			JSONObject object = new JSONObject();
			object.put(ADMIN_USERNAME, resultSet.getString(ADMIN_USERNAME));
			object.put(ADMIN_EMAIL, resultSet.getString(ADMIN_EMAIL));
			if (password.equals(resultSet.getString(ADMIN_PASSWORD)))
				return object.toJSONString();
			else
				return "incorrect password";
		}else {
			return "account doesn't exist";
		}
	}
	
	public static String getUsernameForEmail(String email) throws Exception {
		String condition = makeAtomicCondition(ADMIN_EMAIL, EQUAL_OPERATOR, email);
		ResultSet resultSet = executeQuery(getSelectCommand(ADMIN_TABLE, ALL_COLS, condition));
		if(resultSet.wasNull())
			return "account doesn't exist";
		if(resultSet.next())
			return resultSet.getString(ADMIN_USERNAME);
		else
			return "account doesn't exist";
	}
	
	public static void changePassword(String email, String newPassword) throws Exception {
		String condition = makeAtomicCondition(ADMIN_EMAIL, EQUAL_OPERATOR, email);
		executeStatement(getUpdateCommand(ADMIN_TABLE, ADMIN_PASSWORD, newPassword, condition));
	}
}
