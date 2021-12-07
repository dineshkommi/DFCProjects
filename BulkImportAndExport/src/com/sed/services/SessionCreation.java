package com.sed.services;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.IDfLoginInfo;

public class SessionCreation {

	private static IDfSessionManager createSessionManager(String docbase, String username, String password)
			throws Exception {
		System.out.println("Start of createSessionManager");
		IDfClientX clientx = new DfClientX();
		IDfClient client = clientx.getLocalClient();
		IDfSessionManager sMgr = client.newSessionManager();
		IDfLoginInfo loginInfoObj = clientx.getLoginInfo();
		loginInfoObj.setUser(username);
		loginInfoObj.setPassword(password);
		loginInfoObj.setDomain(null);
		sMgr.setIdentity(docbase, loginInfoObj);
		System.out.println("Finish createSessionManager");
		return sMgr;
	}

	public static void main(String[] args) {
		System.out.println("Code started..!");

		IDfSession session = null;
		String documentumFolderPath = "/Temp/test";
		String localFolderPath = "D:\\TestFiles";
		String docbase = "****REPO";
		String username = "***USER";
		String password = "***PASSWORD";
		try {
			session = createSessionManager(docbase, username, password).getSession(docbase);
//			FileImport.doImport(localFolderPath, documentumFolderPath, session);
			FileExport.doExport(localFolderPath, session);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				IDfSessionManager sMgr = session.getSessionManager();
				sMgr.release(session);
			}
		}
		System.out.println("Code completed..!");
	}

}
