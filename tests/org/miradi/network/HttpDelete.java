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
/**
 * 
 */
package org.miradi.network;

import java.io.File;
import java.net.HttpURLConnection;

class HttpDelete extends HttpTransaction
{
	private HttpDelete(String serverName, int port, String applicationPath, 
			String projectName, File file, String[] parameters) throws Exception
	{
		HttpURLConnection connection = createConnection(serverName, port,
				applicationPath, projectName, file, parameters);
		connection.setRequestMethod("DELETE");
		performRequest(connection);
	}

	public static HttpDelete deleteFile(String serverName, int port, String applicationPath, 
			String projectName, File file) throws Exception
	{
		return new HttpDelete(serverName, port, applicationPath, projectName, file, new String[0]);
	}

	public static HttpDelete deleteProject(String serverName, int port, String applicationPath, 
			String projectName) throws Exception
	{
		return new HttpDelete(serverName, port, applicationPath, projectName, null, new String[] {DELETE_PROJECT});
	}
	
	private static final String DELETE_PROJECT = "DeleteProject";

}