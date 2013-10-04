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

class HttpGet extends HttpTransaction
{
	public static HttpTransaction readFile(URL serverURL, String projectName, File file) throws Exception
	{
		HttpGet get = new HttpGet(serverURL, projectName, file);
		get.performRequest(get.connection);
		return get;
	}
	
	public static HttpTransaction readFiles(URL serverURL, String projectName, String[] parameters) throws Exception
	{
		HttpGet get = new HttpGet(serverURL, projectName, parameters);
		get.performRequest(get.connection);
		return get;
	}
	
	public static HttpTransaction getFileInformation(URL serverURL, String projectName, File file, String[] parameters) throws Exception
	{
		HttpGet get = new HttpGet(serverURL, projectName, file, parameters);
		get.performRequest(get.connection);
		return get;
	}
	
	public static HttpTransaction getProjectList(URL serverURL) throws Exception
	{
		HttpGet get = new HttpGet(serverURL, "projects", new String[0]);
		get.performRequest(get.connection);
		return get;
	}

	private HttpGet(URL serverURL, String projectName, File file) throws Exception
	{
		connection = createConnection(serverURL, projectName, file);
	}
	
	private HttpGet(URL serverURL, String projectName, String[] parameters) throws Exception
	{
		connection = createConnection(serverURL, projectName, parameters);
	}

	private HttpGet(URL serverURL, String projectName, File file, String[] parameters) throws Exception
	{
		connection = createConnection(serverURL, projectName, file, parameters);
	}

	private HttpGet(URL serverURL) throws Exception
	{
		connection = createConnection(serverURL);
	}

	private HttpURLConnection connection;

}
