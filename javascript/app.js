const SIGNUP_SHOW_ERROR = "signup-error-text";
const LOGIN_SHOW_ERROR = "login-error-text";
const VERIFICATION_SHOW_ERROR = "verification-error-text";
const FORGOT_PASSWORD_CHANGEABLE = "forgot-password-changeable";
const FORGOT_PASSWORD_SHOW_ERROR = "forgot-password-error-text";
const CHANGE_PASSWORD_SHOW_ERROR = "change-password-error-text";
const CHANGE_PASWWORD_TEXT = "change-password-text";

const LOGIN_EMAIL = "login-email";
const LOGIN_PASSWORD = "login-password";
const SIGNUP_USERNAME = "signup-username";
const SIGNUP_EMAIL = "signup-email";
const SIGNUP_PASSWORD = "signup-password";
const SIGNUP_PASSWORD_RETYPE = "signup-retype-password";
const VERIFICATION_CODE = "verification-code";
const FORGOT_PASSWORD_EMAIL = "forgot-password-email";
const FORGOT_PASSWORD_CODE = "forgot-password-code";
const CHANGE_PASSWORD_PASSWORD = "change-password-password";
const CHANGE_PASSWORD_RETYPE_PASSWORD = "change-password-retype-password";

const ERROR_NO_SUCH_ACCOUNT = "account doesn't exist";
const ERROR_INCORRECT_PASSWORD = "incorrect password"

var temp;

function isValidEmail(email){
    var regex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
    if(regex.test(email))
        return true;
    else
        return false;
}

function updateHTMLElement(id, value){
    document.getElementById(id).innerHTML = value;
}

function loadVerificationPage(){
    var id = "verification-page-text";
    var url = window.location.href;
    var email = url.substring(url.indexOf("#")+1);
    var html = "Enter the verification code we sent to " + email + 
                ". If you don't see the message then check your spam folder, or try signing up again.";
    updateHTMLElement(id, html);
}

function loginAction(){
    updateHTMLElement(LOGIN_SHOW_ERROR, "");
    var email = document.getElementById(LOGIN_EMAIL).value;
    var password = document.getElementById(LOGIN_PASSWORD).value;
    makeGetRequest("/admin?email=" + email + "&password=" + password, afterLoginResponse);
}

function signupAction(){
    updateHTMLElement(SIGNUP_SHOW_ERROR, "");
    var username = document.getElementById(SIGNUP_USERNAME).value;
    var email = document.getElementById(SIGNUP_EMAIL).value;
    var password = document.getElementById(SIGNUP_PASSWORD).value;
    var retypePassword = document.getElementById(SIGNUP_PASSWORD_RETYPE).value;

    if(username.length == 0)
        updateHTMLElement(SIGNUP_SHOW_ERROR, "User name cannot be empty.")
    else if(!isValidEmail(email)){
        updateHTMLElement(SIGNUP_SHOW_ERROR, "Please enter a valid email ID.");
    }else if(retypePassword != password){
        updateHTMLElement(SIGNUP_SHOW_ERROR, "Passwords don't match.");
    }else{
        makePostRequest({
            "username" : username,
            "email" : email,
            "password" : password
        }, email);
    }
}

function verifyAction(){
    document.getElementById(VERIFICATION_SHOW_ERROR).innerHTML = "";
    var code = document.getElementById(VERIFICATION_CODE).value;
    var email = getEmailFromURL();
    makePutRequest("verification",{
        "email" : email,
        "code" : code
    }, loadAdmin);
}

function sendCodeAction(){
    var email = document.getElementById(FORGOT_PASSWORD_EMAIL).value;
    makeGetRequest("/admin-forgot-password?email=" + email, afterForgotPasswordResponse);
}

function makeGetRequest(query, functionToCall){
    var xhttp = new XMLHttpRequest();
    
    xhttp.open("GET", "http://127.0.0.1:8000" + query);
    xhttp.send();
    return xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            functionToCall(this.responseText);
        }
    };
}

function afterLoginResponse(response){
    if(response == ERROR_NO_SUCH_ACCOUNT || response == ERROR_INCORRECT_PASSWORD)
        updateHTMLElement(LOGIN_SHOW_ERROR, response);
    else{
        var query = new URLSearchParams();
        query.append("key", response);
        location.href = "admin-dashboard.html?" + query.toString();
        document.getElementById("admin-body").innerHTML = response;
    }
}

function afterForgotPasswordResponse(response){
    console.log("90--->" + response);
    if(response == ERROR_NO_SUCH_ACCOUNT)
        updateHTMLElement(FORGOT_PASSWORD_SHOW_ERROR, response);
    else{
        var htmlText = "<input type=\"text\" name=\"email\" placeholder=\"Enter Code\" id=\"forgot-password-code\"><br/>" +
                        "<div><br></div>" + 
                        "<div id=\"forgot-password-changeable\">" + 
                        "<button onclick=\"forgotPasswordSendCode()\">Send code</button><br/>" + 
                        "</div>";
        updateHTMLElement(FORGOT_PASSWORD_CHANGEABLE, htmlText);
        temp = response;
    }
}

function forgotPasswordSendCode(){
    var code = document.getElementById(FORGOT_PASSWORD_CODE).value;
    makePutRequest("change-password-check-code\\"+temp, {
        "email" : temp,
        "code" : code
    }, loadchangePassword);
}

function makePostRequest(obj, email){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.responseText);
            if(this.responseText == "sent mail"){
                window.open("verification.html" + "#" + email,'_self');
            }else if(this.responseText == "account already exists"){
                updateHTMLElement(SIGNUP_SHOW_ERROR, this.responseText);
            }
        }
    };
    xhttp.open("POST", "http://127.0.0.1:8000", true); 
    xhttp.send(JSON.stringify(obj));
}

function makePutRequest(query, obj, func){
    var email = "";
    if(query.indexOf("\\") != -1){
        email = query.substring(query.indexOf("\\"));
        query = query.substring(0,query.indexOf("\\"));
    }
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            func(this.responseText + email);
        }
    };
    xhttp.open("PUT", "http://127.0.0.1:8000", true);
    xhttp.send(query + "\n" + JSON.stringify(obj));
}

function loadchangePassword(response){
    if(response == "invalid code"){
        updateHTMLElement(FORGOT_PASSWORD_SHOW_ERROR,"Invalid Code");
    }else{
        window.open("change-password.html" + "#" + response.substring(response.indexOf("\\")+1), '_self');
    }
}

function loadAdmin(response){
    if(response == "invalid code"){
        updateHTMLElement(VERIFICATION_SHOW_ERROR,"Invalid Code");
    }else{
        console.log("response - " + response);
        window.open("admin-dashboard.html" + "/admin?username=" + response, '_self');
    }
}

function changePasswordAction(){
    var password = document.getElementById(CHANGE_PASSWORD_PASSWORD).value;
    var retypePassword = document.getElementById(CHANGE_PASSWORD_RETYPE_PASSWORD).value;
    if(password == retypePassword){
        var email = getEmailFromURL();
        makePutRequest("change-password",{
            "email" : email,
            "password" : password
        }, setText);
    }else{
        updateHTMLElement(CHANGE_PASSWORD_SHOW_ERROR,"Passwords don't match");
    }
}

function setText(){
    var htmlText = "Passwords changed. <a href=\"index.html\">Login<//a>.";
    updateHTMLElement(CHANGE_PASWWORD_TEXT, htmlText);
}

function getEmailFromURL(){
    var url = window.location.href;
    return url.substring(url.indexOf("#")+1);
}