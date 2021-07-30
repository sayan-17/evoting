import javafx.util.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

public class AdminData {
	private final String username, password, email, admin_id;
	Date whenCreated;
	
	private static HashMap<String,Pair<AdminData,String>> queue = new HashMap<>();
	
	private static final String[] USER_DATA_FIELDS = {"username", "password", "email", "code"};
	private static final int INDEX_USERNAME = 0;
	private static final int INDEX_PASSWORD = 1;
	private static final int INDEX_EMAIL = 2;
	private static final int INDEX_CODE = 3;
	
	private AdminData(String username, String email){
		this.username = username;
		this.email = email;
		this.password = "";
		Calendar calendar = Calendar.getInstance();
		calendar.getTime();
		calendar.add(Calendar.MINUTE, 2);
		this.whenCreated = calendar.getTime();
		this.admin_id = IDGenerator.generateID();
	}
	
	public AdminData(String username, String password, String email){
		this.username = username;
		this.password = password;
		this.email = email;
		Calendar calendar = Calendar.getInstance();
		calendar.getTime();
		calendar.add(Calendar.MINUTE, 2);
		this.whenCreated = calendar.getTime();
		this.admin_id = IDGenerator.generateID();
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getEmail(){
		return email;
	}
	
	private static HashMap<String,String> parseFromJSONString(String jsonString) throws ParseException {
		HashMap<String,String> json = new HashMap<>();
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
		for(String index : USER_DATA_FIELDS){
			String val = (String) jsonObject.get(index);
			if(val != null)
				json.put(index, val);
		}
		return json;
	}
	
	public static String addAdminDataToQueue(String jsonData) throws Exception {
		try {
			HashMap<String,String> admin = parseFromJSONString(jsonData);
			String username = admin.get(USER_DATA_FIELDS[INDEX_USERNAME]);
			String password = admin.get(USER_DATA_FIELDS[INDEX_PASSWORD]);
			String email = admin.get(USER_DATA_FIELDS[INDEX_EMAIL]);
			if(DataStorageManager.getUsernameForEmail(email).equals("account doesn't exist")) {
				System.out.println("1");
				AdminData newAdmin = new AdminData(username, password, email);
				System.out.println(username + " : " + password + " : " + email);
				sendEmailWithVerificationCode(VerificationCodeGenerator.EMAIL_TYPE_AT_CREATE, newAdmin);
			}else
				return "account already exists";
		}catch (Exception e){
			return "failed";
		}
		return "sent mail";
	}
	
	public static void updateEmailQueue(){
		for (Map.Entry<String,Pair<AdminData,String>> entry : queue.entrySet()){
			if(!VerificationCodeGenerator.checkCodeValidity(entry.getValue().getKey().whenCreated))
				queue.remove(entry.getKey());
		}
	}
	
	public static boolean checkIfCodeInQueue(String code, String email) throws Exception {
		if(queue.containsKey(email)) {
			AdminData data = queue.get(email).getKey();
			String key = queue.get(email).getValue();
			if (key.equals(code) && VerificationCodeGenerator.checkCodeValidity(data.whenCreated)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkIfCodeInQueue(String jsonData) throws Exception {
		HashMap<String,String> details = AdminData.parseFromJSONString(jsonData);
		String email = details.get(USER_DATA_FIELDS[INDEX_EMAIL]);
		String code = details.get(USER_DATA_FIELDS[INDEX_CODE]);
		if(queue.containsKey(email)) {
			AdminData data = queue.get(email).getKey();
			String key = queue.get(email).getValue();
			if (key.equals(code) && VerificationCodeGenerator.checkCodeValidity(data.whenCreated)){
				return true;
			}
		}
		return false;
	}
	
	public static String storeAdminData(String jsonData) throws Exception {
		HashMap<String,String> verifier = parseFromJSONString(jsonData);
		String email = verifier.get(USER_DATA_FIELDS[INDEX_EMAIL]);
		String code = verifier.get(USER_DATA_FIELDS[INDEX_CODE]), result;
		if (checkIfCodeInQueue(code, email)){
			AdminData data = queue.get(email).getKey();
			result = DataStorageManager.storeData(data);
			queue.remove(email);
			return (result.equals("stored"))?data.email : result;
		}else
			return "invalid code";
	}
	
	private static void sendEmailWithVerificationCode(int emailType, AdminData newAdmin) throws Exception {
		String verificationCode = VerificationCodeGenerator.sendEmailWithVerificationCode(emailType,
				newAdmin.username, newAdmin.email);
		if(verificationCode.equals("email could not be sent"))
			throw new Exception("email could not be sent");
		queue.put(newAdmin.email, new Pair<>(newAdmin, verificationCode));
	}
	
	public static void sendEmailWithVerificationCode(int emailType, String username, String email) throws Exception {
		String verificationCode = VerificationCodeGenerator.sendEmailWithVerificationCode(emailType, username, email);
		if(verificationCode.equals("email could not be sent"))
			throw new Exception("email could not be sent");
		queue.put(email, new Pair<>(new AdminData(username, email), verificationCode));
	}
	
	public static void showQueue(){
		for (Map.Entry<String,Pair<AdminData,String>> entry : queue.entrySet()){
			System.out.println(entry.getValue().getKey().toString());
		}
	}
	
	public static void updateRecord(String jsonData) throws Exception {
		HashMap<String,String> data = AdminData.parseFromJSONString(jsonData);
		String email = data.get(USER_DATA_FIELDS[INDEX_EMAIL]);
		String password = data.get(USER_DATA_FIELDS[INDEX_PASSWORD]);
		DataStorageManager.changePassword(email, password);
	}
	
	@Override
	public String toString(){
		return username + " : " + " : " + password + " : " + email;
	}
	
	public String toSQLString(){
		return "\"" + admin_id + "\",\"" + username + "\",\"" + email + "\",\"" + password + "\"";
	}
}
