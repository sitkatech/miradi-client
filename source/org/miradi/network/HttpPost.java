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
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;

public class HttpPost extends HttpTransaction
{
	public static HttpPost writeFile(URL serverURL, String projectName, File file, String contents) throws Exception
	{
		HttpPost post = new HttpPost(serverURL, projectName, file);
		post.setPostContents(contents);
		post.performRequest(post.connection);
		return post;
	}

	public static HttpTransaction appendToFile(URL serverURL, String projectName, File relativeFile, String textToAppend) throws Exception
	{
		HttpPost post = new HttpPost(serverURL, projectName, relativeFile);
		post.setPostContents(textToAppend);
		post.performRequest(post.connection);
		return post;
	}

	public static HttpTransaction lockFile(URL serverURL, String projectName, File file) throws Exception
	{
		HttpPost post = new HttpPost(serverURL, projectName, file, new String[] {LOCK});
		post.setPostContents("");
		post.performRequest(post.connection);
		return post;
	}

	public static HttpPost createProject(URL serverURL, String projectName) throws Exception
	{
		String[] parameters = new String[] {CREATE_PROJECT + "=" + projectName};
		HttpPost post = new HttpPost(serverURL, parameters);
		post.setPostContents("");
		post.performRequest(post.connection);
		return post;
	}
	
	public static HttpTransaction deleteProject(URL serverURL, String projectName) throws Exception
	{
		String[] parameters = new String[] {DELETE};
		HttpPost post = new HttpPost(serverURL, projectName, parameters);
		post.setPostContents("");
		post.performRequest(post.connection);
		return post;
	}

	public static HttpTransaction deleteFile(URL serverURL, String projectName, File file) throws Exception
	{
		String[] parameters = new String[] {DELETE};
		HttpPost post = new HttpPost(serverURL, projectName, file, parameters);
		post.setPostContents("");
		post.performRequest(post.connection);
		return post;
	}

	public static HttpTransaction deleteFiles(URL serverURL, String projectName, HashSet<File> filesToDelete) throws Exception
	{
		StringBuffer data = new StringBuffer();
		for(File file : filesToDelete)
		{
			data.append("File." + file.getPath());
			data.append("&");
		}
		
		String postData = data.toString().replaceAll("\\\\", "/"); 
		HttpPost post = new HttpPost(serverURL, projectName, new String[] {DELETE_MULTIPLE});
		post.setPostContents(postData);
		post.performRequest(post.connection);
		return post;
	}

	private HttpPost(URL serverURL, String projectName, File file) throws Exception
	{
		connection = createConnection(serverURL, projectName, file);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setUseCaches(false);
	}
	
	private HttpPost(URL serverURL, String[] parameters) throws Exception
	{
		connection = createConnection(serverURL, parameters);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setUseCaches(false);
	}
	
	private HttpPost(URL serverURL, String projectName, String[] parameters) throws Exception
	{
		connection = createConnection(serverURL, projectName, parameters);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setUseCaches(false);
	}
	
	public HttpPost(URL serverURL, String projectName, File file, String[] parameters) throws Exception
	{
		connection = createConnection(serverURL, projectName, file, parameters);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setUseCaches(false);
	}

	private void setPostContents(String contents) throws UnsupportedEncodingException, IOException
	{
		String postData = "data=" + contents;
		// TODO: Confirm that this will be ok for multi-byte UTF strings
		connection.setRequestProperty("Content-Length", Integer.toString(postData.length()));
		byte[] bytes = URLEncoder.encode(postData, "UTF-8").getBytes();
		OutputStream out = connection.getOutputStream();
		out.write(bytes);
		out.close();
	}
	
	private static final String CREATE_PROJECT = "CreateProject";
	private static final String LOCK = "Lock";
	private static final String DELETE = "Delete";
	private static final String DELETE_MULTIPLE = "DeleteMultiple";
	
	private HttpURLConnection connection;

}