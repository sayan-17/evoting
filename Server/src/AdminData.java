import javafx.util.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

public class AdminData {
	private final String username, password, email;
	
	private static HashMap<String,Pair<AdminData,String>> queue = new HashMap<>();
	
	private static final String HOST_MAIL = "sayan2000paul@gmail.com";
	
	private static final String[] USER_DATA_FIELDS = {"username", "password", "email", "code"};
	private static final int INDEX_USERNAME = 0;
	private static final int INDEX_PASSWORD = 1;
	private static final int INDEX_EMAIL = 2;
	private static final int INDEX_CODE = 3;
	
	public AdminData(String username, String password, String email){
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public static String retrieveAdminData(String jsonData) throws Exception {
		try {
			JSONParser parser = new JSONParser();
			JSONObject admin = (JSONObject) parser.parse(jsonData);
			String username = (String) admin.get(USER_DATA_FIELDS[INDEX_USERNAME]);
			String password = (String) admin.get(USER_DATA_FIELDS[INDEX_PASSWORD]);
			String email = (String) admin.get(USER_DATA_FIELDS[INDEX_EMAIL]);
			AdminData newAdmin = new AdminData(username, password, email);
			System.out.println(username + " : " + password + " : " + email);
			sendEmailWithVerificationCode(newAdmin);
		}catch (Exception e){
			return "failed";
		}
		return "retrieved";
	}
	
	public static String storeData(String jsonData) throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject verifier = (JSONObject) parser.parse(jsonData);
		String email = (String) verifier.get(USER_DATA_FIELDS[INDEX_EMAIL]);
		String code = (String) verifier.get(USER_DATA_FIELDS[INDEX_CODE]);
		if(queue.containsKey(email))
			if(queue.get(email).getValue().equals(code)) {
				String result = DataStorageManager.storeData(queue.get(email).getKey());
				queue.remove(email);
				showQueue();
				return result;
			} else
				return "incorrect code";
		else
			return "no such email in queue";
	}
	
	private static void sendEmailWithVerificationCode(AdminData newAdmin) throws Exception {
		String verificationCode = VerificationCodeGenerator.generateVerificationCode();
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");
		Session session = Session.getDefaultInstance(properties, new Authenticator(){
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(HOST_MAIL, "pxypzlzlibhicuag");
			}
		});
		MimeMessage mailBody = new MimeMessage(session);
		mailBody.addFrom(new InternetAddress[]{new InternetAddress(HOST_MAIL)});
		mailBody.addRecipients(Message.RecipientType.TO, InternetAddress.parse(newAdmin.email));
		mailBody.setSubject(verificationCode + " is your Voting Admin verification code.");
		mailBody.setContent("<head>" +
				"<style>" +
				"body{background : beige; padding : 25px; border-radius : 2px;}" +
				"</style></head><body><h3>Confirm your Email</h3>" +
				"<p>Hi " + newAdmin.username + " ! There is one quick step you need to complete before creating your account. " +
				"<br>Please enter this verification code to get started:</p><br>" +
				"<h2>" + verificationCode + "</h2>" +
				"<br>" +
				"<b>Verification codes expire after one hour</b>.", "text/html");
		Transport.send(mailBody);
		queue.put(newAdmin.email, new Pair<>(newAdmin, verificationCode));
	}
	
	public static void showQueue(){
		for (Map.Entry<String,Pair<AdminData,String>> entry : queue.entrySet()){
			System.out.println(entry.getValue().getKey().toString());
		}
	}
	
	@Override
	public String toString(){
		return username + " : " + " : " + password + " : " + email;
	}
	
	public String toSQLString(){
		return "\"" + username + "\",\"" + email + "\",\"" + password + "\"";
	}
}
