const DASH_CAMPAIGN_TOTAL = "admin-dashboard-total-campaigns";
const DASH_CAMPAIGN_ACTIVE = "admin-dashboard-active-campaigns";
const DASH_CAMPAIGN_SUCCESSFUL = "admin-dashboard-successful-campaigns";
const DASH_CAMPAIGN_UPCOMING = "admin-dashboard-upcoming-campaigns";
const DASH_CAMPAIGN_LIST = "dashboard-campaign-list";
const CAMPAIGN_FIELD_LIST_VOTER =  "campaign-field-list-voter";
const CAMPAIGN_FIELD_LIST_CONTESTANT =  "campaign-field-list-contestant";

const STATUS_ALL_CAMPAIGN_LIST = "for-all-campaign-list";
const STATUS_ALL_EMPTY_LIST = "for-all-dashboard-empty";
const STATUS_ACTIVE_CAMPAIGN_LIST = "for-active-campaign-list";
const STATUS_ACTIVE_EMPTY_LIST = "for-active-dashboard-empty";
const STATUS_UPCOMING_CAMPAIGN_LIST = "for-upcoming-campaign-list";
const STATUS_UPCOMING_EMPTY_LIST = "for-upcoming-dashboard-empty";
const STATUS_SUCCESSFUL_CAMPAIGN_LIST = "for-successful-campaign-list";
const STATUS_SUCCESSFUL_EMPTY_LIST = "for-successful-dashboard-empty";

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
const CREATE_CAMPAIGN_ERROR_MESSAGE = "create-campaign-error-message";
const DASH_EMPTY = "admin-dashboard-empty";
const SUBMIT_BUTTON = "create-campaign-submit-button";
const INITIALS = "initial";
const NAME = "dash-name";

const HTML_TEXT_REPLACE = 0;
const HTML_TEXT_APPEND = 1;

var temp;

function confirmLogout(){
    window.alert("Are you sure you want to logout?");
}

function getURLKey(){
    var para = new URLSearchParams(window.location.search);
    return para.get("q");
}

function openPage(page){
    var query = new URLSearchParams();
    query.append("q", getURLKey());
    location.href = page + "?" + query.toString();
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

function onPageLoad(query, status, callFunction){
    if(status.length == 0)
        makeGetRequest(query + "?email=" + getURLKey(), callFunction);
    else{
        makeGetRequest(query + "?email=" + getURLKey() + "&status=" + status, callFunction);
    }
}

function onPageLoad_Dash(){
    onPageLoad("admin-campaign" ,"", dash_make);
}

function onPageLoad_withStatus(status){
    temp = status;
    onPageLoad("getCampaignsForStatus" ,status, make_page);
}

function make_page(response){
    const obj = JSON.parse(response);
    var campaignArray = storeFromCampaignList(obj.campaigns);
    switch(temp){
        case "all" :
            displayCampaigns(STATUS_ALL_EMPTY_LIST, STATUS_ALL_CAMPAIGN_LIST, campaignArray);
            break;
        case "successful" :
            displayCampaigns(STATUS_SUCCESSFUL_EMPTY_LIST, STATUS_SUCCESSFUL_CAMPAIGN_LIST, campaignArray);
            break;
        case "upcoming" :
            displayCampaigns(STATUS_UPCOMING_EMPTY_LIST, STATUS_UPCOMING_CAMPAIGN_LIST, campaignArray);
            break;
        case "active" :
            displayCampaigns(STATUS_ACTIVE_EMPTY_LIST, STATUS_ACTIVE_CAMPAIGN_LIST, campaignArray);
            break;
    }
}

function storeFromCampaignList(list){
    var campaignArray = [];
    for(var i = 0; i < list.length;){
        const o = list.substring(list.indexOf("{",i),list.indexOf("}",i)+1);
        i += o.length + 1;
        var campaign = JSON.parse(o);
        campaignArray.push(campaign);
    }
    return campaignArray;
}

function dash_make(response){
    const obj = JSON.parse(response);
    var campaignArray = storeFromCampaignList(obj.campaigns);
    setHTMLText(DASH_CAMPAIGN_TOTAL, obj.total, HTML_TEXT_REPLACE);
    setHTMLText(DASH_CAMPAIGN_ACTIVE, obj.active, HTML_TEXT_REPLACE);
    setHTMLText(DASH_CAMPAIGN_UPCOMING, obj.upcoming, HTML_TEXT_REPLACE);
    setHTMLText(DASH_CAMPAIGN_SUCCESSFUL, obj.successful, HTML_TEXT_REPLACE);
    setHTMLText(NAME, obj.userName + "'s Dashboard", HTML_TEXT_REPLACE);
    displayCampaigns(DASH_EMPTY, DASH_CAMPAIGN_LIST, campaignArray);
}

function displayCampaigns(empty, list, campaignArray){
    if(campaignArray.length == 0)
        setHTMLText(empty ,"<img src=\"Resources\\Graphics\\nothing-to-show.png\" class=\"image small-image\" title=\"https://www.pixeltrue.com/packs\">", HTML_TEXT_APPEND);
    for(var i=0; i<campaignArray.length; ++i){
        setHTMLText(list ,makeCampaignCard(campaignArray[i]), HTML_TEXT_APPEND);
    }   
}

function makeCampaignCard(campaign){
    var html =  "<div class=\"card campaign\">" + 
                    "<div class=\"card-body\">" + 
                        "<div class=\"row\">" + 
                            "<p class=\"col-sm-10\">" + campaign.NAME + "</p>" + 
                            "<p class=\"col-sm-2\">" + campaign.STATUS + "</p>" +
                        "</div>" +
                        "<a href=\"#\">View Details</a>" +
                    "</div>" +
                "</div>";
    return html;
}

function makeGetRequest(query, callFunc){
    var xhttp = new XMLHttpRequest();
    console.log(query);
    xhttp.open("GET","http://127.0.0.1:8000/" + query);
    xhttp.send();
    xhttp.onreadystatechange = function(){
        if(this.readyState == 4 && this.status == 200){
            callFunc(this.responseText);
        }
    };
}

function createNewField(who, type, feildName){
    var txt = "<div class=\"col-sm-2\">" + feildName + "</div>" +
                    "<div class=\"col-sm-9\">" + 
                        "<input type=\"text\" placeholder=\"Enter caption\" id=\"" + who + "-" + feildName + "-caption\" value=\"Enter " + feildName + "\" style=\"width: 100%;\">" +
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
    id = id.substring(0,id.lastIndexOf("created")-1);
    document.getElementById(id).disabled = false;
}

function submitCampaign(){
    var name = document.getElementById(CAMPAIGN_NAME).value;
    var startdate = document.getElementById(CAMPAIGN_START_DATE).value;
    var starttime = document.getElementById(CAMPAIGN_START_TIME).value;
    var duration = document.getElementById(CAMPAIGN_DURATION).value;
    var voter_meta = generateVoterMeta();
    var contestant_meta = generateContestantMeta();
    if(name.length == 0 || startdate.length == 0 || starttime == 0)
        setHTMLText(CREATE_CAMPAIGN_ERROR_MESSAGE, "Name, Start date and Start time are mandatory fields.", HTML_TEXT_REPLACE);
    else if(duration < 1)
        setHTMLText(CREATE_CAMPAIGN_ERROR_MESSAGE, "Minimum duration of 1 hour is required.", HTML_TEXT_REPLACE);
    else
        postCampaign(name, startdate, starttime, voter_meta, contestant_meta, duration);
}

function postCampaign(name, startdate, starttime, voter_meta, contestant_meta, duration){
    if(voter_meta == "{}" || contestant_meta == "{}")
        setHTMLText(CREATE_CAMPAIGN_ERROR_MESSAGE,"Please, choose one from email and phone number for both voter and contestant.",HTML_TEXT_REPLACE);
    else{
        console.log(getURLKey());
        var obj = {
            "name" : name,
            "start-date" : startdate,
            "start-time" : starttime,
            "voter-meta" : voter_meta,
            "contestant_meta" : contestant_meta,
            "duration" : duration,
            "email" : getURLKey()
        }
        setHTMLText("submit-message","<div id=\'processing-message'><b>Processing your data &#128512;</b>&nbsp&nbsp<i class=\"fa fa-spinner fa-pulse fa-3x fa-fw\"></i></div>", HTML_TEXT_APPEND);
        document.getElementById(SUBMIT_BUTTON).disabled = true;
        makePostRequest(obj);
    }
}

function makePostRequest(obj){
    var xhttp = new XMLHttpRequest();
    xhttp.open("POST", "http://127.0.0.1:8000", true);
    xhttp.send("save-campaign\n" + JSON.stringify(obj));

    xhttp.onreadystatechange= function(){
        if (this.readyState == 4 && this.status == 200) {
            setHTMLText("submit-message","",HTML_TEXT_REPLACE);
            setHTMLText(CREATE_CAMPAIGN_ERROR_MESSAGE,"",HTML_TEXT_REPLACE);
            console.log(this.responseText);
            if(this.responseText == "not-stored"){
                setHTMLText(CREATE_CAMPAIGN_ERROR_MESSAGE, "Some internal error occurred. Please try again.", HTML_TEXT_REPLACE);
            }else{
                setHTMLText("submit-message", "Campaign created &#128516;", HTML_TEXT_REPLACE);
            }
            document.getElementById(SUBMIT_BUTTON).disabled = false;
        }
    };
}

function generateVoterMeta(){
    var e_meta = "{";
    var email = document.getElementById(VOTER_CAPTION_EMAIL);
    if(email != null){
        e_meta += email.value;
    }
    var name = document.getElementById(VOTER_CAPTION_NAME);
    if(name != null){
        e_meta += name.value;
    }
    var ID = document.getElementById(VOTER_CAPTION_ID);
    if(ID != null){
        e_meta += ID.value;
    }
    var phone = document.getElementById(VOTER_CAPTION_PHONE_NUMBER);
    if(phone != null){
        e_meta += phone.value;
    }
    if(email == null && phone == null)
        return "{}";
    return e_meta + "}";
    

}

function generateContestantMeta(){
    var c_meta = "{";
    var email = document.getElementById(CONTESTANT_CAPTION_EMAIL);
    if(email != null){
        c_meta += email.value;
    }
    var name = document.getElementById(CONTESTANT_CAPTION_NAME);
    if(name != null){
        c_meta += name.value;
    }
    var ID = document.getElementById(CONTESTANT_CAPTION_ID);
    if(ID != null){
        c_meta += ID.value;
    }
    var phone = document.getElementById(CONTESTANT_CAPTION_PHONE_NUMBER);
    if(phone != null){
        c_meta += phone.value;
    }
    if(email == null && phone == null)
        return "{}";
    return c_meta + "}";
}