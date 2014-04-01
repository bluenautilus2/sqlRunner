function oldPassSubmit(form) {
    var oldpass = form.pass.value;
    var username = form.login.value;
    var newpass = getNewPassword();

    sendToServer(username, oldpass, newpass);

}

// If the two passwords don't match, returns -1.
// If they don't match but one or more is empty, return 1
// If they do match and are non-zero, returns the password.
function getNewPassword() {

    var inputField1 = this.document.getElementById("topNewPassField");
    var string1 = inputField1.value;
    var inputField2 = this.document.getElementById("bottomNewPassField");
    var string2 = inputField2.value;

    if (string1.length == 0 || string2.length == 0) {
        return 1;
    }

    if (string1 === string2) {
        return string1;
    } else {
        return -1;
    }

}

function keyTyped() {
    var result = getNewPassword();

    var badOutput = this.document.getElementById("passwordfeedbackerror");
    var goodOutput = this.document.getElementById("passwordfeedbackgood");

    if (result == "-1") {
        goodOutput.innerHTML = "";
        badOutput.innerHTML = "Passwords do not match";
    } else if (result == "1") {
        goodOutput.innerHTML = "";
        badOutput.innerHTML = "";
    } else {
        goodOutput.innerHTML = "Passwords match";
        badOutput.innerHTML = "";
    }
}

function sendToServer(username, oldpass, newpass) {

    var url = "http://localhost:8081/ldap/jersey/ldapajax/";

    var params = {
        username : username,
        oldpass : oldpass,
        newpass : newpass
    };

    var stuff = params.username;

    var paramString = JSON.stringify(params);

    callBackend(url,paramString);

}

function callBackend(url, params) {


    var xmlhttp = new XMLHttpRequest();


    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            document.getElementById("output").innerHTML = xmlhttp.responseText;
        }else{
            var errorstring = "http status:"+  xmlhttp.status + " responsetext: "+ xmlhttp.responseText;
            document.getElementById("output").innerHTML = errorstring;
        }
    }

    xmlhttp.open("POST", url, true);
    xmlhttp.setRequestHeader("Content-type","application/json");
    //send(string) for post requests

    xmlhttp.send(params);


}