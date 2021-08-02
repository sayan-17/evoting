import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.xml.transform.Templates;
import java.util.HashMap;

public class Campaign {
	
	private String ID, status, name, startTime, startDate, admin_id, email, voterMeta, contestantMeta;
	int duration;
	
	private static final String  NAME = "name";
	private static final String  STATUS = "status";
	private static final String  START_TIME = "start-date";
	private static final String  START_DATE = "start-time";
	private static final String  DURATION = "duration";
	
	private static final String CAMPAIGN_TABLE = "campaign";
	private static final String[] USER_DATA_FIELDS = {
			"name", "status", "start-date", "start-time", "duration", "voter-meta", "contestant-meta", "email"
	};
	private static final int NAME_INDEX = 0;
	private static final int STATUS_INDEX = 1;
	private static final int START_DATE_INDEX = 2;
	private static final int START_TIME_INDEX = 3;
	private static final int DURATION_INDEX = 4;
	private static final int VOTER_META_INDEX = 5;
	private static final int CONTESTANT_META_INDEX = 6;
	private static final int EMAIL_INDEX = 7;
	
	public Campaign(String status, String name, int duration, String startDate,
	                String startTime, String voterMeta, String contestantMeta){
		this.ID = IDGenerator.generateID();
		this.duration = duration;
		this.startDate = startDate;
		this.status = status;
		this.startTime = startTime;
		this.name = name;
		this.voterMeta = voterMeta;
		this.contestantMeta = contestantMeta;
	}
	
	public Campaign(HashMap<String,String> json) {
		this.ID = IDGenerator.generateID();
		this.duration = Integer.parseInt(json.get(USER_DATA_FIELDS[DURATION_INDEX]));
		this.startDate = json.get(USER_DATA_FIELDS[START_DATE_INDEX]);
		this.status = "UPCOMING";
		this.startTime = json.get(USER_DATA_FIELDS[START_TIME_INDEX]);
		this.name = json.get(USER_DATA_FIELDS[NAME_INDEX]);
		System.out.println(this.email = json.get(USER_DATA_FIELDS[EMAIL_INDEX]));
		this.voterMeta = json.get(USER_DATA_FIELDS[VOTER_META_INDEX]);
		this.contestantMeta = json.get(USER_DATA_FIELDS[CONTESTANT_META_INDEX]);
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
	
	public static String storeCampaignData(String jsonData) throws Exception {
		System.out.println(jsonData);
		Campaign campaign = new Campaign(parseFromJSONString(jsonData));
		campaign.setAdmin_id(AdminData.getAdminID(campaign.email));
		try{
			DataStorageManager.storeData(CAMPAIGN_TABLE, campaign.toSQLString());
		}catch (Exception e){
			return "not-stored";
		}
		return "stored";
	}
	
	public void setAdmin_id(String admin_id){
		this.admin_id = admin_id;
	}
	
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		json.put(NAME, name);
		json.put(START_DATE, startDate);
		json.put(STATUS, status);
		json.put(START_TIME, startTime);
		json.put(DURATION, duration);
		return json;
	}
	
	public String toSQLString(){
		return "\"" + ID + "\",\"" + admin_id + "\",\"" + status + "\"," + duration + ",\""
				+ startTime + "\",\"" + startDate + "\",\"" + name + "\"";
	}
}
