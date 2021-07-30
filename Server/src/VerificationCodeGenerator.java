import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class VerificationCodeGenerator{
	
	private static final String HOST_MAIL = "sayan2000paul@gmail.com";
	
	public static final int EMAIL_TYPE_AT_CREATE = 0;
	public static final int EMAIL_TYPE_WHEN_PASSWORD_FORGOT = 1;
	
	public static String generateVerificationCode(){
		Random random = new Random();
		int a = random.nextInt(9);
		int b = random.nextInt(9);
		int c = random.nextInt(9);
		int d = random.nextInt(9);
		return a + "" + b + "" + c + "" + d;
	}
	
	public static String sendEmailWithVerificationCode(int emailType, String username, String email) {
		String verificationCode;
		try {
			verificationCode = generateVerificationCode();
			Properties properties = new Properties();
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.socketFactory.port", "465");
			properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.port", "465");
			Session session = Session.getDefaultInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(HOST_MAIL, "pxypzlzlibhicuag");
				}
			});
			MimeMessage mailBody = new MimeMessage(session);
			mailBody.addFrom(new InternetAddress[]{new InternetAddress(HOST_MAIL)});
			mailBody.addRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			mailBody.setSubject(verificationCode + " is your Voting Admin verification code.");
			mailBody.setContent(getEmailText(emailType, username, verificationCode), "text/html");
			Transport.send(mailBody);
		}catch (Exception e){
			return "email could not be sent";
		}
		return verificationCode;
	}
	
	public static boolean checkCodeValidity(Date timeGiven){
		Date presentTime = java.util.Calendar.getInstance().getTime();
		return presentTime.before(timeGiven);
	}
	
	private static String getEmailText(int emailType, String username, String verificationCode){
		StringBuilder emailText =  new StringBuilder("<body><h3>Confirm your Email</h3>" + "" +
									"<br>" +
									"<p>Hi " + username + " !<br>");
		switch(emailType){
			case EMAIL_TYPE_AT_CREATE :
				emailText.append("There is one quick step you need to complete before creating your account. " +
									"<br>Please enter this verification code to get started:</p><br>" +
									"<h2>" + verificationCode + "</h2>" +
									"<br>" +
									"<b>Verification codes expire after one hour</b>.");
				break;
			case EMAIL_TYPE_WHEN_PASSWORD_FORGOT :
				emailText.append("We sent you this email because you requested to change your password. " +
						"<br>Please enter this verification code to get started:</p><br>" +
						"<h2>" + verificationCode + "</h2>" +
						"<br>" +
						"<b>Verification codes expire after one hour</b>." +
						"<p>If this was not you, please ignore this email.");
				break;
				
		}
		return emailText.toString();
	}
}
