package org.bluenautilus.util;

import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import javax.servlet.ServletContext;

public class LdapUtil {

	public static String makeDnString(String[] thearray) {
		String ans = "";
		for (String s : thearray) {
			ans = ans + s + ",";
		}

		if (!ans.equals("")) {
			return ans.substring(0, ans.length() - 1);
		} else {
			return ans;
		}
	}

	/**
	 * assumes connection is good
	 *
	 * @param username
	 * @param conn
	 * @return
	 */
	public static String getDnOfUser(String username, LDAPConnection conn) throws LDAPException, LDAPSearchException {

		String baseDN = "ou=people,dc=peopleanswers,dc=com";
		Filter filter = Filter.createEqualityFilter("uid", username);
		SearchRequest req = new SearchRequest(baseDN, SearchScope.SUB, filter, "dn");

		SearchResult searchResult;
		String name = "";

		searchResult = conn.search(req);

		for (SearchResultEntry entry : searchResult.getSearchEntries()) {
			name = entry.getDN();
		}
		return name;
	}

	public static LDAPConnection getLDAPConnectionAdmin(ServletContext context) throws ConfigurationException, LDAPException {
		ConfigUtil config = ConfigUtil.getFromContext(context);

		PropertiesConfiguration props = config.getWebConfig();

		String ldapserver = props.getString("ldap.server");
		String portString = props.getString("ldap.port");
		String bindDN[] = props.getStringArray("ldap.binddn.admin");
		String dn = LdapUtil.makeDnString(bindDN);
		String adminpass = props.getString("ldap.admin.pass");

		int port = 389;
		port = Integer.parseInt(portString);


		LDAPConnection conn = new LDAPConnection(ldapserver, port, dn, adminpass);

		if (conn.isConnected()) {
			return conn;
		} else {
			throw new LDAPException(ResultCode.LOCAL_ERROR, "Ldap didn't throw an exception, but the connection isn't connected");
		}

	}

	public static LDAPConnection getLDAPConnectionUser(String dn, String password, ServletContext context) throws ConfigurationException, LDAPException {
		ConfigUtil config = ConfigUtil.getFromContext(context);

		PropertiesConfiguration props = config.getWebConfig();

		String ldapserver = props.getString("ldap.server");
		String portString = props.getString("ldap.port");
		String pass = password;

		int port = 389;
		port = Integer.parseInt(portString);


		LDAPConnection conn = new LDAPConnection(ldapserver, port, dn, pass);

		if (conn.isConnected()) {
			return conn;
		} else {
			throw new LDAPException(ResultCode.LOCAL_ERROR, "Ldap didn't throw an exception, but the connection isn't connected");
		}
	}
}
