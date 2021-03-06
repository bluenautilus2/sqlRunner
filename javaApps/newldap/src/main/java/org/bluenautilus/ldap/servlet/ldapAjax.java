package org.bluenautilus.ldap.servlet;


import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.ResultCode;
import org.bluenautilus.util.LdapUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;

//http://localhost:8080/ldap/jersey/ldapajax/


@Path("/ldapajax")
public class ldapAjax {

	private ServletContext context;

	@POST
	@Consumes("application/json")

	public Response postLdap(String jsonString) {
		StringBuilder outputString = new StringBuilder();

		JSONObject json = null;
		try {
			json = (JSONObject) new JSONParser().parse(jsonString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String username = (json.get("username")).toString();
		String oldpass = (json.get("oldpass")).toString();
		String newpass = (json.get("newpass")).toString();

		if (newpass == null || newpass.equals("") || newpass.equals("1")) {
			return Response.status(200).entity("Your new password is blank. Try again").build();
		}
		if (newpass.equals("-1")) {
			return Response.status(200).entity("Your new passwords don't match. Try again").build();
		}

		LDAPConnection conn1 = null;
		LDAPConnection conn2 = null;
		try {
			conn1 = LdapUtil.getLDAPConnectionAdmin(this.context);
			String dn = LdapUtil.getDnOfUser(username, conn1);
			conn1.close();

			if (dn != "") {
				outputString.append("Found user in ldap: " + dn + "<br/>" + "Attempting to login as user...<br/>");
			}

			conn2 = LdapUtil.getLDAPConnectionUser(dn, oldpass, this.context);
			if (conn2.isConnected()) {
				outputString.append("Successfully logged in<br/>");
			}

			outputString.append("Attempting to update password..<br/>");
			ResultCode code = LdapUtil.changeUserPw(conn2, dn, newpass);

			outputString.append("Result code from LDAP: " + code.getName() + "<br/>");
			conn2.close();

			if (code.getName().equals("success")) {
				outputString.append("<br/>Go to Gerrit and make sure your new Password works<br/>");
			}

		} catch (Exception e) {
			return Response.status(200).entity(outputString.toString() + "<br/> + " + e.toString()).build();
		} finally {
			if (conn1 != null) {
				conn1.close();
			}
			if (conn2 != null) {
				conn2.close();
			}
		}

		return Response.status(200).entity(outputString.toString()).build();

	}

	@Context
	public void setServletContext(ServletContext context) {
		System.out.println("servlet context set here");
		this.context = context;

	}

	@GET
	public Response getLdap(@QueryParam(value = "id") final String id) {

		String output = "GET not supported";

		return Response.status(400).entity(output).build();

	}

}
