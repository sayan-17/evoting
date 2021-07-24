import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Scanner;

public class RequestHandler implements HttpHandler {
	
	private static final String GET_METHOD = "GET";
	private static final String POST_METHOD = "POST";
	private static final String PUT_METHOD = "PUT";
	
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
				response = handlePatchRequest(httpExchange);
			}
		}catch (Exception e){
			System.out.println(e);
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
	
	private String handlePatchRequest(HttpExchange httpExchange) throws Exception {
		InputStream inStream = httpExchange.getRequestBody();
		Scanner scanner = new Scanner(inStream);
		String jsonData = scanner.nextLine();
		return AdminData.storeData(jsonData);
	}
	
	private String handlePostMethod(HttpExchange httpExchange) throws Exception {
		InputStream inStream = httpExchange.getRequestBody();
		Scanner scanner = new Scanner(inStream);
		String jsonData = scanner.nextLine();
		String result = AdminData.retrieveAdminData(jsonData);
		return result;
	}
	
	private String handleGetRequest(HttpExchange httpExchange) throws Exception {
		URI getParameters = httpExchange.getRequestURI();
		String askedFor = getParameters.toString().substring(getParameters.toString().indexOf("/")+1,
				getParameters.toString().indexOf("?"));
		String query = getParameters.getQuery();
		System.out.println(askedFor);
		switch (askedFor) {
			case "admin" :
				HashMap<String,String> emailAndPassword = parseQuery(query);
				return DataStorageManager.getAdminData(emailAndPassword.get("email"), emailAndPassword.get("password"));
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