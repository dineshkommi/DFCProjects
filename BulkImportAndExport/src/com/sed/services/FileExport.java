package com.sed.services;

import java.io.File;

import org.apache.log4j.Logger;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.operations.IDfExportNode;
import com.documentum.operations.IDfExportOperation;

public class FileExport {
	
	static Logger logger = Logger.getLogger(FileExport.class.getName());

	public static void doExport(String sourcePath, IDfSession session) throws DfException {

		IDfClientX clientx = new DfClientX();
		String dql = "SELECT r_object_id,object_name FROM dm_document WHERE folder('/Temp/test/TestFiles');";
		IDfQuery query = new DfQuery();
		query.setDQL(dql);
		IDfCollection collection = null;
		String objectId = null;
		try {
			collection = query.execute(session, IDfQuery.DF_READ_QUERY);
			while (collection.next()) {
				objectId = collection.getString("r_object_id");
				String objectName = collection.getString("object_name");
				System.out.println("OBject ID: " + objectId);
				logger.info("OBject ID: " + objectId);

				String destDir = sourcePath;
				IDfSysObject sysObj = (IDfSysObject) session.getObject(new DfId(objectId));
				if (sysObj == null) {
					System.out.println("Object " + objectId + " not found.");
					logger.info("Object " + objectId + " not found.");
					break;
				} else {
					System.out.println("ObjectID " + objectId + " has been found.");
					System.out.println("ObjectName file: " + sysObj.getObjectName());
					logger.info("ObjectName file: " + sysObj.getObjectName());
					String objName = objectId + " " + sysObj.getObjectName();
					sysObj.setObjectName(objName);
					System.out.println("New Object Name: " + sysObj.getObjectName());
					logger.info("New Object Name: " + sysObj.getObjectName());
					try {
						IDfExportOperation operation = clientx.getExportOperation();
						if (new File(destDir).exists()) {
						} else {
							new File(destDir).mkdirs();
						}
						operation.setDestinationDirectory(destDir);
						IDfExportNode node = (IDfExportNode) operation.add(sysObj);

						operation.execute();

					} catch (DfException exportError) {
						System.out.println("Exception encountered during Document export:  " + exportError);
					}
				}
			}
		} finally {
			// Closing collection
			if (collection != null) {
				collection.close();
			}
		}

	}
}
