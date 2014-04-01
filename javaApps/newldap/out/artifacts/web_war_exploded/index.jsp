<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>The PA IT LDAP Password Change Site</title>
    <script src="formstuff.js"></script>
    <link type="text/css" rel="stylesheet" href="ldap.css"/>
</head>
<body>
<H1> LDAP Password changer</H1>

<p><i>If it breaks, go get Beth</i></p>

<p class="standout">Type your new Gerrit Password Twice</p>


<input type="password" id="topNewPassField" name="pass" placeholder="Enter new LDAP password"/>
<br/>
<input type="password" id="bottomNewPassField" name="pass" placeholder="Type it again"
       onkeyup="keyTyped()"/>

<p class="redtext" id="passwordfeedbackerror"></p>

<p class="greentext" id="passwordfeedbackgood"></p>
<br/>

<p class="standout">Enter Gerrit Login and Old Password</p>

<form id="oldPassForm">
    <input type="text" id="login" name="login" placeholder="ex: jsmith"/> <br/>
    <input type="password" id="pass" name="pass" placeholder="Enter old LDAP Password"/> <br/>
    <input type="button" onClick="oldPassSubmit(this.form)" value="Submit"/> <br/>
</form>


<div id="output">
    Output should display here
</div>


</body>
</html>