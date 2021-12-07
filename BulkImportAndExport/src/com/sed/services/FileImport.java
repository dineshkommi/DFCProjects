package com.sed.services;

import org.apache.log4j.Logger;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfList;
import com.documentum.operations.IDfFile;
import com.documentum.operations.IDfImportNode;
import com.documentum.operations.IDfImportOperation;

public class FileImport {

	static Logger logger = Logger.getLogger(FileImport.class.getName());

	public static void doImport(String srcFileOrDir, String destFolderPath, IDfSession session) throws DfException {
		IDfClientX clientx = new DfClientX();
		IDfImportOperation operation = clientx.getImportOperation();
		// the Import Operation requires a session
		operation.setSession(session);

		IDfFolder folder = null;
		folder = session.getFolderByPath(destFolderPath);
		// note: the destination folder must exist- or throws nullPointerException
		if (folder == null) {
			System.out.println("Folder or cabinet " + destFolderPath + " does not exist in the Docbase!");
			logger.info("Folder or cabinet " + destFolderPath + " does not exist in the Docbase!");
			return;
		}
		operation.setDestinationFolderId(folder.getObjectId());

		// check if file or directory on file system exists
		IDfFile myFile = clientx.getFile(srcFileOrDir);
		if (myFile.exists() == false) {
			System.out.println("File or directory " + srcFileOrDir + " does not exist in the file system!");
			logger.info("File or directory " + srcFileOrDir + " does not exist in the file system!");
			return;
		}
		// add the file or directory to the operation
		IDfImportNode node = (IDfImportNode) operation.add(srcFileOrDir);
		operation.execute();

		// work with resulting nodes here
		IDfList myNodes = operation.getNodes();
		int iCount = myNodes.getCount();
		System.out.println("Number of nodes after operation: " + iCount);
		logger.info("Number of nodes after operation: " + iCount);
		for (int i = 0; i < iCount; ++i) {
			IDfImportNode aNode = (IDfImportNode) myNodes.get(i);
			System.out.print("Object ID " + i + ": " + aNode.getNewObjectId().toString() + " ");
			logger.info("Object ID " + i + ": " + aNode.getNewObjectId().toString() + " ");
			// aNode.getNewObjectName() returns null value so using sysobject instead
			IDfSysObject sysObj = (IDfSysObject) session.getObject(aNode.getNewObjectId());
			System.out.println("Object name: " + sysObj.getObjectName());
			logger.info("Object name: " + sysObj.getObjectName());
		}
	}
}