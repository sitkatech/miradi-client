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
import java.net.URLEncoder;
import java.util.Map;

import org.martus.util.UnicodeWriter;

class HttpPost extends HttpTransaction
{
	public static HttpPost writeFile(URL serverURL, String projectName, File file, String data) throws Exception
	{
		HttpPost post = new HttpPost(serverURL, projectName, file);
		UnicodeWriter writer = new UnicodeWriter(post.connection.getOutputStream());
		String encoded = URLEncoder.encode(data, "UTF-8");
		writer.write("data=" + encoded);
		writer.close();
		post.performRequest(post.connection);
		return post;
	}
	
	public static HttpPost writeMultiple(URL serverURL, String projectName, Map<File, String> fileContentsMap) throws Exception
	{
		StringBuffer data = new StringBuffer();
		for(File file : fileContentsMap.keySet())
		{
			data.append("File." + URLEncoder.encode(file.getPath(), "UTF-8"));
			data.append(URLEncoder.encode("=", "UTF-8"));
			data.append(URLEncoder.encode(fileContentsMap.get(file), "UTF-8"));
			data.append("&");
		}
		
		HttpPost post = new HttpPost(serverURL, projectName, null, new String[] {WRITE_MULTIPLE});
		UnicodeWriter writer = new UnicodeWriter(post.connection.getOutputStream());
		writer.write(data.toString());
		writer.close();
		
		post.performRequest(post.connection);
		return post;
	}
	
	public static HttpTransaction lockFile(URL serverURL, String projectName, File file) throws Exception
	{
		HttpPost post = new HttpPost(serverURL, projectName, file, new String[] {LOCK});
		UnicodeWriter writer = new UnicodeWriter(post.connection.getOutputStream());
		writer.close();
		post.performRequest(post.connection);
		return post;
	}

	public static HttpPost createProject(URL serverURL, String projectName) throws Exception
	{
		String[] parameters = new String[] {CREATE_PROJECT + "=" + projectName};
		HttpPost post = new HttpPost(serverURL, parameters);
		post.performRequest(post.connection);
		return post;
	}
	
	private HttpPost(URL serverURL, String projectName, File file) throws Exception
	{
		connection = createConnection(serverURL, projectName, file, new String[0]);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setChunkedStreamingMode(0);
	}
	
	private HttpPost(URL serverURL, String[] parameters) throws Exception
	{
		connection = createConnection(serverURL, parameters);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setChunkedStreamingMode(0);
	}
	
	public HttpPost(URL serverURL, String projectName, File file, String[] parameters) throws Exception
	{
		connection = createConnection(serverURL, projectName, file, parameters);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setChunkedStreamingMode(0);
	}

	private static final String CREATE_PROJECT = "CreateProject";
	private static final String LOCK = "Lock";
	private static final String WRITE_MULTIPLE = "WriteMultiple=true";
	
	private HttpURLConnection connection;

}