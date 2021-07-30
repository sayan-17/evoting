const DASH_CAMPAIGN_TOTAL = "admin-dashboard-total-campaigns";
const DASH_CAMPAIGN_ACTIVE = "admin-dashboard-active-campaigns";
const DASH_CAMPAIGN_SUCCESSFUL = "admin-dashboard-successful-campaigns";
const DASH_CAMPAIGN_UPCOMING = "admin-dashboard-upcoming-campaigns";
const DASH_CAMPAIGN_LIST = "dashboard-campaign-list";
const CAMPAIGN_FIELD_LIST_VOTER =  "campaign-field-list-voter";
const CAMPAIGN_FIELD_LIST_CONTESTANT =  "campaign-field-list-contestant";

const CREATE_FIELD_NAME = "create-field-name";
const CREATE_FIELD_ID = "create-field-id";
const CREATE_FIELD_EMAIL = "create-field-email";
const CREATE_FIELD_PHONE_NUMBER = "create-field-phone-number";
const CREATED_FIELD_NAME_VOTER = "create-field-name-created-voter";
const CREATD_FIELD_ID_VOTER = "create-field-id-created-voter";
const CREATED_FIELD_EMAIL_VOTER = "create-field-email-created-voter";
const CREATED_FIELD_PHONE_NUMBER_VOTER = "create-field-phone-number-created-voter";
const CREATED_FIELD_NAME_CONTESTANT = "create-field-name-created-contestant";
const CREATD_FIELD_ID_CONTESTANT = "create-field-id-created-contestant";
const CREATED_FIELD_EMAIL_CONTESTANT = "create-field-email-created-contestant";
const CREATED_FIELD_PHONE_NUMBER_CONTESTANT = "create-field-phone-number-created-contestant";
const VOTER_CAPTION_NAME = "voter-Name-caption";
const VOTER_CAPTION_ID = "voter-ID-caption";
const VOTER_CAPTION_EMAIL = "voter-Email-caption";
const VOTER_CAPTION_PHONE_NUMBER = "voter-Phone Number-caption";
const CONTESTANT_CAPTION_NAME = "contestant-Name-caption";
const CONTESTANT_CAPTION_ID = "contestant-ID-caption";
const CONTESTANT_CAPTION_EMAIL = "contestant-Email-caption";
const CONTESTANT_CAPTION_PHONE_NUMBER = "contestant-Phone Number-caption";
const CAMPAIGN_NAME = "campaign-name";
const CAMPAIGN_START_DATE = "campaign-start-date";
const CAMPAIGN_START_TIME = "campaign-start-time";
const CAMPAIGN_DURATION = "campaign-duration";
const ERROR_MESSAGE_VOTER = "error-message-voter";
const ERROR_MESSAGE_CONTESTANT = "error-message-contestant";

const HTML_TEXT_REPLACE = 0;
const HTML_TEXT_APPEND = 1;

function confirmLogout(){
    window.alert("Are you sure you want to logout?");
}

function setHTMLText(id, text, type){
    if(type == HTML_TEXT_REPLACE)
        document.getElementById(id).innerHTML = text;
    else if(type == HTML_TEXT_APPEND){
        var div = document.createElement("div");
        div.innerHTML = text;
        document.getElementById(id).appendChild(div);
    }
}

function appendHTMLNode(id, text, classname, tagID){
    var div = document.createElement("div");
    div.className = classname;
    div.innerHTML = text;
    div.id = tagID;
    document.getElementById(id).appendChild(div);
}

function createCampaignCard(institutionName, status){
    var htmlText = "<div class=\"card-body\">" +
                            "<div class=\"row\">" + 
                                "<p class=\"col-sm-10\">" + institutionName + "</p>" + 
                                "<p class=\"col-sm-2\">" + status + "</p>" + 
                            "</div>" + 
                            "<a href=\"#\">View Details</a>" + 
                        "</div>";
    appendHTMLNode(DASH_CAMPAIGN_LIST, htmlText, "card campaign");
}

function onPageLoad_Dash(){
    var para = new URLSearchParams(window.location.search);
    var pass = para.get("KEY");
    console.log(pass);
    //makeGetRequest();
}

function makeGetRequest(query, callFunc){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function(){
        if(this.readyState == 4 && this.status == 200){
            callFunc(this.responseText);
        }
    };
    xhttp.open("GET","http://127.0.0.1:8000" + query);
    xhttp.send();
}

function createNewField(who, type, feildName){
    var txt = "<div class=\"col-sm-2\">" + feildName + "</div>" +
                    "<div class=\"col-sm-9\">" + 
                        "<input type=\"text\" placeholder=\"Enter caption\" id=\"" + who + "-" + feildName + "-caption\" style=\"width: 100%;\">" +
                    "</div>" +
                    "<div class=\"col-sm-1\">" +
                        "<i class=\"fa fa-minus-circle\" aria-hidden=\"true\" title=\"Remove\" onclick=\"removeField('" + type + "-created-" + who + "')\"></i>" +
                    "</div>";
    if(who == "voter")
        appendHTMLNode(CAMPAIGN_FIELD_LIST_VOTER, txt, "row campaign-field", type+"-created-"+who);
    else if(who == "contestant")
        appendHTMLNode(CAMPAIGN_FIELD_LIST_CONTESTANT, txt, "row campaign-field", type+"-created-"+who);
    document.getElementById(type).disabled = true;
}

function removeField(id){
    var element = document.getElementById(id);
    element.parentNode.removeChild(element);
    id = id.substring(0,id.lastIndexOf("created-voter")-1);
    document.getElementById(id).disabled = false;
}

function submitCampaign(){
    var name = document.getElementById(CAMPAIGN_NAME).value;
    var startdate = document.getElementById(CAMPAIGN_START_DATE).value;
    var starttime = document.getElementById(CAMPAIGN_START_TIME).value;
    var voter_meta = generateVoterMeta();
    var contestant_meta = generateContestantMeta();
    if(voter_meta == "{}")
        setHTMLText(ERROR_MESSAGE_VOTER,"Choose one from email and phone number",HTML_TEXT_REPLACE);
    else if(contestant_meta == "{}")
        setHTMLText(ERROR_MESSAGE_CONTESTANT,"Choose one from email and phone number",HTML_TEXT_REPLACE);
    else{
        var obj = {
            "name" : name,
            "start-date" : startdate,
            "start-time" : starttime,
            "voter-meta" : voter_meta,
            "contestant_meta" : contestant_meta
        }
        window.alert(JSON.stringify(obj));
    }
}

function generateVoterMeta(){
    var e_meta = "{";
    var email = document.getElementById(VOTER_CAPTION_EMAIL);
    if(email != null){
        e_meta += email;
    }
    var name = document.getElementById(VOTER_CAPTION_NAME).value;
    if(name != null){
        e_meta += name;
    }
    var ID = document.getElementById(VOTER_CAPTION_ID).value;
    if(ID != null){
        e_meta += ID;
    }
    var phone = document.getElementById(VOTER_CAPTION_PHONE_NUMBER).value;
    if(phone != null){
        e_meta += phone;
    }
    if(email == null && phone == null)
        return "{}";
    return e_meta + "}";
    

}

function generateContestantMeta(){
    var c_meta = "{";
    var email = document.getElementById(CONTESTANT_CAPTION_EMAIL);
    if(email != null){
        c_meta += email;
    }
    var name = document.getElementById(CONTESTANT_CAPTION_NAME).value;
    if(name != null){
        c_meta += name;
    }
    var ID = document.getElementById(CONTESTANT_CAPTION_ID).value;
    if(ID != null){
        c_meta += ID;
    }
    var phone = document.getElementById(CONTESTANT_CAPTION_PHONE_NUMBER).value;
    if(phone != null){
        c_meta += phone;
    }
    if(email == null && phone == null)
        return "{}";
    return c_meta + "}";
}