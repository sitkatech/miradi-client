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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.martus.util.UnicodeReader;

class HttpTransaction
{

	public int getResultCode()
	{
		return resultCode;
	}

	public String getResultMessage()
	{
		return resultMessage;
	}

	public String getResultData()
	{
		return resultData;
	}

	protected HttpURLConnection createConnection(String serverName, int port,
			String applicationPath, String projectName, File file, String[] parameters)
			throws Exception
	{
		URL serverURL = new URL("http://" + serverName + ":" + port + applicationPath);
		String relativePath = "";
		if(projectName != null)
			relativePath += projectName;
		if(file != null)
			relativePath += file;
		URI uri = new URI(serverURL + relativePath + buildParameterString(parameters));
		HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
		return connection;
	}

	protected HttpURLConnection createConnection(String serverName, int port,
			String applicationPath, String projectName, String[] parameters)
			throws Exception
	{
		URL serverURL = new URL("http://" + serverName + ":" + port + applicationPath);
		URI uri = new URI(serverURL + projectName +	buildParameterString(parameters));
		HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
		return connection;
	}

	protected HttpURLConnection createConnection(String serverName, int port, 
			String applicationPath, String[] parameters) throws Exception
	{
		URL serverURL = new URL("http://" + serverName + ":" + port + applicationPath);
		URI uri = new URI(serverURL + buildParameterString(parameters));
		HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
		return connection;
	}

	protected void performRequest(HttpURLConnection connection) throws IOException
	{
		resultCode = connection.getResponseCode();
		resultMessage = connection.getResponseMessage();
		UnicodeReader reader = new UnicodeReader(connection.getInputStream());
		resultData = reader.readAll();
		reader.close();
	}

	private String buildParameterString(String[] parameters)
	{
		if(parameters == null)
			return "";

		String parameterString = "";
		for(int i = 0; i < parameters.length; ++i)
		{
			if(i == 0)
				parameterString += "?";
			else
				parameterString += "&";
			parameterString += parameters[i];
		}
		return parameterString;
	}

	private int resultCode;
	private String resultMessage;
	private String resultData;
	
}