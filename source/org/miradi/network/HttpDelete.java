/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.network;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

class HttpDelete extends HttpTransaction
{
	public static HttpTransaction unlockFile(URL serverURL, String projectName, File file) throws Exception
	{
		HttpDelete delete = new HttpDelete(serverURL, projectName, file, new String[] {UNLOCK});
		delete.performRequest(delete.connection);
		return delete;
	}

	public static HttpDelete deleteProject(URL serverURL, String projectName) throws Exception
	{
		HttpDelete delete = new HttpDelete(serverURL, projectName, null, new String[] {DELETE_PROJECT});
		delete.performRequest(delete.connection);
		return delete;
	}
	
	public static HttpDelete deleteFile(URL serverURL, String projectName, File file) throws Exception
	{
		HttpDelete delete = new HttpDelete(serverURL, projectName, file, new String[0]);
		delete.performRequest(delete.connection);
		return delete;
	}

	public static HttpTransaction deleteFiles(URL serverURL, String projectName, HashSet<File> filesToDelete) throws Exception
	{
		HashSet<String> parameters = new HashSet<String>();
		parameters.add(DELETE_MULTIPLE);
		for(File file : filesToDelete)
		{
			parameters.add("File." + file.getPath());
		}
		return new HttpDelete(serverURL, projectName, parameters.toArray(new String[0]));
	}

	private HttpDelete(URL serverURL, String projectName, File file, String[] parameters) throws Exception
	{
		connection = createConnection(serverURL, projectName, file, parameters);
		connection.setRequestMethod("DELETE");
	}

	private HttpDelete(URL serverURL, String projectName, String[] parameters) throws Exception
	{
		connection = createConnection(serverURL, projectName, parameters);
		connection.setRequestMethod("DELETE");
	}

	private static final String DELETE_PROJECT = "DeleteProject";
	private static final String UNLOCK = "Unlock";
	private static final String DELETE_MULTIPLE = "DeleteMultiple";
	
	private HttpURLConnection connection;
}
