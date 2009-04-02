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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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

	protected HttpURLConnection createConnection(URL serverURL,
			String projectName, File file, String[] parameters)
			throws URISyntaxException, IOException, MalformedURLException
	{
		String relativePath = "";
		if(projectName != null)
			relativePath += projectName;
		if(file != null)
		{
			if(!file.getPath().startsWith("/"))
				relativePath += "/";
			relativePath += file;
		}
		URI uri = new URI(serverURL + relativePath + buildParameterString(parameters));
		HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
		return connection;
	}

	protected HttpURLConnection createConnection(URL serverURL,
			String projectName, String[] parameters) throws URISyntaxException,
			IOException, MalformedURLException
	{
		return createConnection(serverURL, projectName, null, parameters);
	}

	protected HttpURLConnection createConnection(URL serverURL,
			String[] parameters) throws URISyntaxException, IOException,
			MalformedURLException
	{
		return createConnection(serverURL, null, null, parameters);
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