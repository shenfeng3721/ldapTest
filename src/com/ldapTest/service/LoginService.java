package com.ldapTest.service;

import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;

import org.apache.commons.lang.StringUtils;

import com.ldapTest.bean.LoginInfo;
import com.ldapTest.dao.LoginRecordDao;

public class LoginService {

	private static final String LDAP_DOMAIN = "ldap.sing.seagate.com";
	private static final int LDAP_PORT = 389;
	private static final String LDAP_STR = "ou=People,o=seagate.com,o=SDS";
	private LoginRecordDao loginRecordDao;
	
	

	public LoginService(LoginRecordDao dao) {
		// TODO Auto-generated constructor stub
		this.loginRecordDao = dao;
	}

	public LoginInfo login(String username, String password) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return null;
		}
		String uidStr = "uid=" + username;

		LoginInfo loginInfo = new LoginInfo();
		LDAPConnection sslCon = new LDAPConnection();
		try {
			sslCon.connect(LDAP_DOMAIN, LDAP_PORT, uidStr + "," + LDAP_STR, password);
			loginInfo.setUid(username);
			LDAPSearchResults sr = sslCon.search(LDAP_STR, LDAPConnection.SCOPE_ONE, uidStr, null, false);
			if (sr.hasMoreElements()) {
				LDAPEntry entry = sr.next();

				// System.out.println(getAttr(entry, "cn"));
				loginInfo.setName(getFullName(entry));
				loginInfo.setMail(getAttr(entry, "mail"));
				loginInfo.setManager(getManagerByStr(sslCon, getAttr(entry, "manager")));
				loginInfo.setLocation(getAttr(entry, "l"));
				loginInfo.setDepartment(getAttr(entry, "departmentname"));
				loginInfo.setTitle(getAttr(entry, "title"));
				loginInfo.setTelephone(getAttr(entry, "telephonenumber"));
			}

		} catch (LDAPException e) {
			return null;
		} finally {
			try {
				sslCon.disconnect();
			} catch (LDAPException e) {
				e.printStackTrace();
			}
		}
		loginRecordDao.save(loginInfo);
		return loginInfo;
	}

	private String getAttr(LDAPEntry entry, String key) {
		try {
			LDAPAttribute ldapAttribute = entry.getAttribute(key);
			return ldapAttribute.getStringValues().nextElement().toString();
		} catch (Exception e) {
		}
		return "";
	}

	private String getManagerByStr(LDAPConnection sslCon, String mgrStr) {
		try {
			String uidStr = mgrStr.split(",")[0];
			LDAPSearchResults sr = sslCon.search(LDAP_STR, LDAPConnection.SCOPE_ONE, uidStr, null, false);
			if (sr.hasMoreElements()) {
				return uidStr + "," + getFullName(sr.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private String getFullName(LDAPEntry entry) {
		return getAttr(entry, "givenname") + " " + getAttr(entry, "sn");
	}

}