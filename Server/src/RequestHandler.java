import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javafx.util.Pair;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Scanner;

public class RequestHandler implements HttpHandler {
	
	private static final String GET_METHOD = "GET";
	private static final String POST_METHOD = "POST";
	private static final String PUT_METHOD = "PUT";
	
	private static final int EMAIL_TYPE_AT_CREATE = 0;
	private static final int EMAIL_TYPE_WHEN_PASSWORD_FORGOT = 1;
	
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String response = "Request Received";
		String method = httpExchange.getRequestMethod();
		try {
			if (method.equals(GET_METHOD)) {
				response = handleGetRequest(httpExchange);
			} else if (method.equals(POST_METHOD)) {
				response = handlePostMethod(httpExchange);
			} else if (method.equals(PUT_METHOD)) {
				response = handlePutRequest(httpExchange);
			}
		}catch (Exception e){
			System.out.println(e);
			response = e.toString();
		}
		Headers responseHeaders = httpExchange.getResponseHeaders();
		responseHeaders.add("Access-Control-Allow-Origin", "*");
		responseHeaders.add("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
		responseHeaders.add("Access-Control-Allow-Credentials", "true");
		responseHeaders.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		httpExchange.sendResponseHeaders(200, response.length());
		OutputStream outStream = httpExchange.getResponseBody();
		outStream.write(response.getBytes());
		outStream.close();
	}
	
	private String handlePutRequest(HttpExchange httpExchange) throws Exception {
		InputStream inStream = httpExchange.getRequestBody();
		Scanner scanner = new Scanner(inStream);
		String query = scanner.nextLine();
		String jsonData = scanner.nextLine();
		switch(query){
			case "change-password-check-code" :
				if(AdminData.checkIfCodeInQueue(jsonData)){
					return "verified";
				}else
					return "invalid code";
			case "verification" :
				return AdminData.storeAdminData(jsonData);
			case "change-password" :
				AdminData.updateRecord(jsonData);
		}
		return "Invalid query";
	}
	
	private String handlePostMethod(HttpExchange httpExchange) throws Exception {
		InputStream inStream = httpExchange.getRequestBody();
		Scanner scanner = new Scanner(inStream);
		String jsonData = scanner.nextLine();
		String result = AdminData.addAdminDataToQueue(jsonData);
		return result;
	}
	
	private String handleGetRequest(HttpExchange httpExchange) throws Exception {
		URI getParameters = httpExchange.getRequestURI();
		String askedFor = getParameters.toString().substring(getParameters.toString().indexOf("/")+1,
				getParameters.toString().indexOf("?"));
		String query = getParameters.getQuery();
		switch (askedFor) {
			case "admin" :
				HashMap<String,String> emailAndPassword = parseQuery(query);
				return DataStorageManager.getAdminData(emailAndPassword.get("email"), emailAndPassword.get("password"));
			case "admin-forgot-password" :
				HashMap<String,String> email = parseQuery(query);
				String username = DataStorageManager.getUsernameForEmail(email.get("email"));
				if(username.equals("account doesn't exist"))
					return "account doesn't exist";
				AdminData.sendEmailWithVerificationCode(VerificationCodeGenerator.EMAIL_TYPE_WHEN_PASSWORD_FORGOT,
						username, email.get("email"));
				return email.get("email");
		}
		return "not a valid query";
	}
	
	private static HashMap<String,String> parseQuery(String query){
		HashMap<String,String> queryDictionary = new HashMap<>();
		int startIndex = 0, endIndex = 0, i = 0;
		String key, val;
		while(startIndex < query.length() && (endIndex = query.indexOf("&", startIndex)) != -1){
			key = query.substring(startIndex, query.indexOf("="));
			val = query.substring(query.indexOf("=")+1, endIndex);
			queryDictionary.put(key, val);
			startIndex = endIndex + 1;
		}
		key = query.substring(startIndex, query.indexOf("=", startIndex));
		val = query.substring(query.indexOf("=", startIndex)+1);
		queryDictionary.put(key, val);
		return queryDictionary;
	}
}