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

import org.martus.util.UnicodeWriter;

class HttpPost extends HttpTransaction
{
	public static HttpPost writeFile(String serverName, int port, String applicationPath, 
			String projectName, File file, String data) throws Exception
	{
		HttpPost post = new HttpPost(serverName, port, applicationPath, projectName, file);
		UnicodeWriter writer = new UnicodeWriter(post.connection.getOutputStream());
		writer.write("data=" + data);
		writer.close();
		post.performRequest(post.connection);
		return post;
	}
	
	public static HttpPost createProject(String serverName, int port, String applicationPath, 
			String projectName) throws Exception
	{
		String[] parameters = new String[] {CREATE_PROJECT + "=" + projectName};
		HttpPost post = new HttpPost(serverName, port, applicationPath, parameters);
		post.performRequest(post.connection);
		return post;
	}
	
	private HttpPost(String serverName, int port, String applicationPath, 
			String projectName, File file) throws Exception
	{
		connection = createConnection(serverName, port,	applicationPath, projectName, file, new String[0]);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setChunkedStreamingMode(0);
	}
	
	private HttpPost(String serverName, int port, String applicationPath, String[] parameters) throws Exception
	{
		connection = createConnection(serverName, port, applicationPath, parameters);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setChunkedStreamingMode(0);
	}
	
	private static final String CREATE_PROJECT = "CreateProject";
	
	private HttpURLConnection connection;
}