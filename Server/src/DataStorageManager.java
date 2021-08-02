import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.json.simple.JSONObject;

import java.beans.PropertyEditorSupport;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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
	private static final String ADMIN_USERNAME = "NAME";
	private static final String ADMIN_PASSWORD = "PASSWORD";
	private static final String CAMPAIGN_ID = "CAMPAIGN_ID";
	private static final String CAMPAIGN_STATUS = "STATUS";
	private static final String CAMPAIGN_META = "META";
	private static final String CAMPAIGN_NAME = "NAME";
	
	private static final String EQUAL_OPERATOR = "=";
	private static final String NOT_EQUAL_OPERATOR = "<>";
	private static final String GREATER_OPERATOR = ">";
	private static final String LESSER_OPERATOR = "<";
	private static final String GREATER_EQUAL_OPERATOR = ">=";
	private static final String LESSER_EQUAL_OPERATOR = "<=";
	private static final String IN_OPERATOR = "IN";
	private static final String NOT_IN_OPERATOR = "NOT IN";
	private static final String AND_CLAUSE = "AND";
	
	private static String getInsertCommand(String table, String values){
		return "INSERT INTO " + table + " VALUES (" + values + ");";
	}
	
	private static String getSelectCommand(String table, String attributes){
		return "SELECT " + attributes + " " + table + ";";
	}
	
	private static String getSelectCommand(String table, String attributes, String conditions){
		return "SELECT " + attributes + " FROM " + table + " WHERE " + conditions + ";";
	}
	
	private static String getSelectCommand(String table, String[] attributes){
		StringBuilder sb = new StringBuilder("");
		for(String s : attributes){
			sb.append(s);
			sb.append((attributes[attributes.length-1].equals(s))?",":"");
		}
		return "SELECT " + sb + " " + table + ";";
	}
	
	private static String getSelectCommand(String table, String[] attributes, String conditions){
		StringBuilder sb = new StringBuilder("");
		for(String s : attributes){
			sb.append(s);
			sb.append((attributes[attributes.length-1].equals(s))?"":",");
		}
		return "SELECT " + sb + " FROM " + table + " WHERE " + conditions + ";";
	}
	
	private static String makeAtomicCondition(String attribute, String operator, String value){
		return attribute + " " + operator + " \"" + value + "\"";
	}
	
	private static String combineTwoConditions(String condition1, String clause, String condition2){
		return condition1 + " " + clause + " " +  condition2;
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
		System.out.println(command);
		return statement.executeQuery(command);
	}
	
	public static void storeData(String tableName, String sqlDataString) throws Exception {
		executeStatement(getInsertCommand(tableName, sqlDataString));
	}
	
	public static String storeAdminData(AdminData adminData) throws Exception {
		try{
			storeData(ADMIN_TABLE, adminData.toSQLString());
		}catch (MySQLIntegrityConstraintViolationException e){
			if(e.toString().contains("Duplicate entry"))
				return "already registered email";
			else
				throw new Exception("some error occurred");
		}
		return "stored";
	}
	
	public static String getAdminEmail(String email, String password) throws Exception {
		
		String condition = makeAtomicCondition(ADMIN_EMAIL, EQUAL_OPERATOR, email);
		String[] attr = {ADMIN_EMAIL, ADMIN_PASSWORD};
		
		ResultSet resultSet = executeQuery(getSelectCommand(ADMIN_TABLE, attr, condition));
		
		if(resultSet.wasNull())
			return "account doesn't exist";
		
		if(resultSet.next()) {
			if (password.equals(resultSet.getString(ADMIN_PASSWORD)))
				return resultSet.getString(ADMIN_EMAIL);
			else
				return "incorrect password";
		}else {
			return "account doesn't exist";
		}
	}
	
	public static String getAdminCampaigns(String email) throws Exception {
		
		String condition = makeAtomicCondition(ADMIN_EMAIL, EQUAL_OPERATOR, email);
		String[] attr = {ADMIN_USERNAME, ADMIN_ID};
		
		ResultSet resultSet = executeQuery(getSelectCommand(ADMIN_TABLE, attr, condition));
		
		if(!resultSet.wasNull() && resultSet.next()) {
			
			int totalCampaigns = 0, successfulCampaigns = 0, activeCampaigns = 0, upcomingCampaigns = 0;
			
			String username = resultSet.getString(ADMIN_USERNAME);
			condition = makeAtomicCondition(ADMIN_ID, EQUAL_OPERATOR, resultSet.getString(ADMIN_ID));
			String[] attr2 = {CAMPAIGN_ID, ADMIN_ID, CAMPAIGN_STATUS, CAMPAIGN_NAME};
			
			ResultSet resultSet2 = executeQuery(getSelectCommand(CAMPAIGN_TABLE, attr2, condition));
			JSONObject fullData = new JSONObject();
			int len = resultSet2.getFetchSize();
			fullData.put("userName", username.substring(0,username.indexOf(" ")));
			ArrayList<String> campaignList = new ArrayList<>();
			while (resultSet2.next()){
				String status = resultSet2.getString(CAMPAIGN_STATUS);
				String campaignName =resultSet2.getString(CAMPAIGN_NAME);
				JSONObject entry = new JSONObject();
				switch (status) {
					case "UPCOMING" : upcomingCampaigns++; break;
					case "ACTIVE" : activeCampaigns++; break;
					case "SUCCESSFUL" : successfulCampaigns++; break;
				}
				entry.put(CAMPAIGN_STATUS, status);
				entry.put(CAMPAIGN_NAME, campaignName);
				campaignList.add(entry.toJSONString());
			}
			fullData.put("campaigns", toJSONArrayString(campaignList.toArray()));
			fullData.put("total", upcomingCampaigns+activeCampaigns+successfulCampaigns);
			fullData.put("active", activeCampaigns);
			fullData.put("successful", successfulCampaigns);
			fullData.put("upcoming", upcomingCampaigns);
			
			return fullData.toJSONString();
		}else {
			return "account doesn't exist";
		}
	}
	
	public static String getAdminCampaignsForStatus(String email, String status) throws Exception {
		
		String condition1 = makeAtomicCondition(ADMIN_EMAIL, EQUAL_OPERATOR, email);
		String[] attr = {ADMIN_USERNAME, ADMIN_ID};
		
		ResultSet resultSet = executeQuery(getSelectCommand(ADMIN_TABLE, attr, condition1));
		
		if(!resultSet.wasNull() && resultSet.next()) {
			
			String username = resultSet.getString(ADMIN_USERNAME);
			condition1 = makeAtomicCondition(ADMIN_ID, EQUAL_OPERATOR, resultSet.getString(ADMIN_ID));
			String condition2 = makeAtomicCondition(CAMPAIGN_STATUS, EQUAL_OPERATOR, status);
			System.out.println(condition2);
			String condition = status.equals("all") ? condition1 : combineTwoConditions(condition1, AND_CLAUSE, condition2);
			String[] attr2 = {CAMPAIGN_ID, ADMIN_ID, CAMPAIGN_STATUS, CAMPAIGN_NAME};
			
			ResultSet resultSet2 = executeQuery(getSelectCommand(CAMPAIGN_TABLE, attr2, condition));
			JSONObject fullData = new JSONObject();
			int len = resultSet2.getFetchSize();
			fullData.put("userName", username.substring(0,username.indexOf(" ")));
			ArrayList<String> campaignList = new ArrayList<>();
			while (resultSet2.next()){
				String _status = resultSet2.getString(CAMPAIGN_STATUS);
				String campaignName =resultSet2.getString(CAMPAIGN_NAME);
				JSONObject entry = new JSONObject();
				entry.put(CAMPAIGN_STATUS, _status);
				entry.put(CAMPAIGN_NAME, campaignName);
				campaignList.add(entry.toJSONString());
			}
			fullData.put("campaigns", toJSONArrayString(campaignList.toArray()));
			
			return fullData.toJSONString();
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
	
	public static String getAdminId(String email) throws Exception {
		String condition = makeAtomicCondition(ADMIN_EMAIL, EQUAL_OPERATOR, email);
		ResultSet set = executeQuery(getSelectCommand(ADMIN_TABLE, ADMIN_ID, condition));
		if(set.next()) {
			return set.getString(ADMIN_ID);
		} else
			return "not present";
	}
	
	public static String toJSONArrayString(Object[] array){
		StringBuilder sb = new StringBuilder("");
		for(Object str : array){
			sb.append(str);
			sb.append((str.equals(array[array.length-1]))?"":",");
		}
		return sb.toString();
	}
}
