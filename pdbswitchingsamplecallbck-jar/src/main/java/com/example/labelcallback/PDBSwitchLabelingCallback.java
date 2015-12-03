package com.example.labelcallback;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import oracle.ucp.jdbc.ConnectionLabelingCallback;
import oracle.ucp.jdbc.LabelableConnection;

public class PDBSwitchLabelingCallback implements ConnectionLabelingCallback {

	
	public PDBSwitchLabelingCallback() {
		System.out.println("PDBSwitchLabelingCallback initialized");
	}

	public int cost(Properties reqLabels, Properties currentLabels) {
		// Case 1: exact match
		if (reqLabels.equals(currentLabels)) {
			System.out.println("## Exact match found = "
					+ (String) reqLabels.get("pdbname"));
			return 0;
		}
		// Case 2: some labels match with no unmatched labels
		String val1 = (String) reqLabels.get("pdbname");
		String val2 = (String) currentLabels.get("pdbname");
		// No label matches to application's preference.
		// Do not choose this connection.
		System.out.println("## No match '" + val1 + "' != '" + val2 + "'##");
		return Integer.MAX_VALUE;
	}

	public boolean configure(Properties reqLabels, Object conn) {
		// Only called if not a match
		try {
			String valStr = (String) reqLabels.get("pdbname");
			Statement s = ((Connection) conn).createStatement();
			s.executeUpdate("ALTER SESSION SET CONTAINER = " + valStr);
			LabelableConnection lconn = (LabelableConnection) conn;
			System.out.println("## In configure for value " + valStr);
			lconn.applyConnectionLabel("pdbname", valStr);
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Properties getRequestedLabels() {
		Properties labels = new Properties();
		try {
			Properties appProp = new Properties();
			appProp.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties"));
			labels.put("pdbname", appProp.get("pdbname"));
			System.out.println("label is set: pdbname is " + appProp.get("pdbname"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return labels;
	}

}
