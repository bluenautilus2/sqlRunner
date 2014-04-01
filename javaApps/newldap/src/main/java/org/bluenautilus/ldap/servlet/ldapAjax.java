package org.bluenautilus.ldap.servlet;

import com.unboundid.ldap.sdk.Control;
import com.unboundid.ldap.sdk.DereferencePolicy;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.ReadOnlySearchRequest;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bluenautilus.util.ConfigUtil;
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
import java.util.List;

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

		String dn = "";

		try {
			LDAPConnection conn1 = LdapUtil.getLDAPConnectionAdmin(this.context);
			dn = LdapUtil.getDnOfUser(username, conn1);
			conn1.close();

			if (dn != "") {
				outputString.append("Found user in ldap: " + dn + "<br/>" + "Attempting to login as user...<br/>");
			}

			LDAPConnection conn2 = LdapUtil.getLDAPConnectionUser(dn, oldpass,this.context);
			if(conn2.isConnected()){
				outputString.append("Successfully logged in<br/>");
			}


		} catch (Exception e) {
			return Response.status(200).entity(outputString.toString() + "<br/> + " + e.toString()).build();
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
