const SIGNUP_SHOW_ERROR = "signup-error-text";
const LOGIN_SHOW_ERROR = "login-error-text";
const VERIFICATION_SHOW_ERROR = "verification-error";

const LOGIN_EMAIL = "login-email";
const LOGIN_PASSWORD = "login-password";
const SIGNUP_USERNAME = "signup-username";
const SIGNUP_EMAIL = "signup-email";
const SIGNUP_PASSWORD = "signup-password";
const SIGNUP_PASSWORD_RETYPE = "signup-retype-password";
const VERIFICATION_CODE = "verification-code";

const ERROR_NO_SUCH_ACCOUNT = "error no such account";

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
    makeGetRequest(email, password);
}

function signupAction(){
    updateHTMLElement(SIGNUP_SHOW_ERROR, "");
    var username = document.getElementById(SIGNUP_USERNAME).value;
    var email = document.getElementById(SIGNUP_EMAIL).value;
    var password = document.getElementById(SIGNUP_PASSWORD).value;
    var retypePassword = document.getElementById(SIGNUP_PASSWORD_RETYPE).value;

    if(!isValidEmail(email)){
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
    var url = window.location.href;
    var email = url.substring(url.indexOf("#")+1);
    makePutRequest({
        "email" : email,
        "code" : code
    });
}

function makeGetRequest(email, password){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            if(this.responseText == "error no such account"){
                updateHTMLElement(LOGIN_SHOW_ERROR, "No such account exists.");
            }else{
                updateHTMLElement(LOGIN_SHOW_ERROR, this.responseText);
            }
        }
    };
    xhttp.open("GET", "http://127.0.0.1:8000" + "/admin?email=" + email + "&password=" + password);
    xhttp.send();
}

function makePostRequest(obj, email){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.responseText);
            if(this.responseText == "retrieved"){
                window.open("verification.html" + "#" + email,'_self');
            }
        }
    };
    xhttp.open("POST", "http://127.0.0.1:8000", true); 
    xhttp.send(JSON.stringify(obj));
}

function makePutRequest(obj){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.responseText);
        }
    };
    xhttp.open("PUT", "http://127.0.0.1:8000", true);
    xhttp.send(JSON.stringify(obj));
}