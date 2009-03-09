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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

public class MiradiRemoteFileSystem implements MiradiFileSystem
{
	public MiradiRemoteFileSystem(String server, int port, String applicationPath)
	{
		serverName = server;
		serverPort = port;
		serverApplicationPath = applicationPath;
	}
	
	public Set<String> getProjectList() throws Exception
	{
		HttpTransaction get = new HttpGet(serverName, serverPort, serverApplicationPath, null, null, null);
		if(get.getResultCode() != HTTP_SUCCESS)
			throw new IOException(get.getResultMessage());
		
		HashSet<String> projectList = new HashSet<String>();
		BufferedReader reader = new BufferedReader(new StringReader(get.getResultData()));
		while(true)
		{
			String projectName = reader.readLine();
			if(projectName == null)
				break;
			projectList.add(projectName);
		}
		return projectList;
	}
	
	/* (non-Javadoc)
	 * @see org.miradi.network.MiradiFileSystem#doesProjectExist(java.lang.String)
	 */
	public boolean doesProjectDirectoryExist(String projectName) throws Exception
	{
		HttpTransaction get = new HttpGet(serverName, serverPort, serverApplicationPath, projectName, new String[] {EXISTS});
		if(get.getResultCode() != HTTP_SUCCESS)
			throw new IOException(get.getResultMessage());
		return (get.getResultData().startsWith(EXISTS));
	}
	
	/* (non-Javadoc)
	 * @see org.miradi.network.MiradiFileSystem#createProject(java.lang.String)
	 */
	public void createProject(String projectName) throws Exception
	{
		HttpPost post = HttpPost.createProject(serverName, serverPort, serverApplicationPath, projectName);
		if(post.getResultCode() != HTTP_SUCCESS)
			throw new IOException(post.getResultMessage());
	}
	
	/* (non-Javadoc)
	 * @see org.miradi.network.MiradiFileSystem#deleteProject(java.lang.String)
	 */
	public void deleteProject(String projectName) throws Exception
	{
		HttpTransaction delete = HttpDelete.deleteProject(serverName, serverPort, serverApplicationPath, projectName);
		if(delete.getResultCode() != HTTP_SUCCESS)
			throw new IOException(delete.getResultMessage());
	}
	
	/* (non-Javadoc)
	 * @see org.miradi.network.MiradiFileSystem#doesFileExist(java.lang.String, java.io.File)
	 */
	public boolean doesFileExist(String projectName, File file) throws Exception
	{
		HttpTransaction get = new HttpGet(serverName, serverPort, serverApplicationPath, projectName, file, new String[] {EXISTS});
		if(get.getResultCode() != HTTP_SUCCESS)
			throw new IOException(get.getResultMessage());
		return (get.getResultData().startsWith(EXISTS));
	}
	
	/* (non-Javadoc)
	 * @see org.miradi.network.MiradiFileSystem#readFile(java.lang.String, java.io.File)
	 */
	public String readFile(String projectName, File file) throws Exception
	{
		HttpTransaction get = new HttpGet(serverName, serverPort, serverApplicationPath, projectName, file);
		if(get.getResultCode() != HTTP_SUCCESS)
			throw new IOException(get.getResultMessage());
		return get.getResultData();
	}
	
	/* (non-Javadoc)
	 * @see org.miradi.network.MiradiFileSystem#writeFile(java.lang.String, java.io.File, java.lang.String)
	 */
	public void writeFile(String projectName, File file, String contents) throws Exception
	{
		HttpTransaction post = HttpPost.writeFile(serverName, serverPort, serverApplicationPath, projectName, file, contents);
		if(post.getResultCode() != HTTP_SUCCESS)
			throw new IOException(post.getResultMessage());
	}
	
	/* (non-Javadoc)
	 * @see org.miradi.network.MiradiFileSystem#deleteFile(java.lang.String, java.io.File)
	 */
	public void deleteFile(String projectName, File file) throws Exception
	{
		HttpTransaction delete = HttpDelete.deleteFile(serverName, serverPort, serverApplicationPath, projectName, file);
		if(delete.getResultCode() != HTTP_SUCCESS)
			throw new IOException(delete.getResultMessage());
	}

	private static final int HTTP_SUCCESS = 200;
	private static final String EXISTS = "Exists";

	private String serverName;
	private int serverPort;
	private String serverApplicationPath;
}
