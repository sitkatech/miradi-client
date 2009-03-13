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
import java.util.HashMap;
import java.util.HashSet;


abstract public class MiradiFileSystemWithTransactions implements MiradiFileSystem
{

	public void beginTransaction(String projectName) throws Exception
	{
		if(isInTransaction())
			throw new Exception("RemoteFileSystem cannot nest transactions");
		pendingWrites = new HashMap<File, String>();
		pendingDeletes = new HashSet<File>();
		transactionProjectName = projectName;
	}

	public void endTransaction() throws Exception
	{
		if(!isInTransaction())
			throw new Exception("RemoteFileSystem extra endTransaction");
		String projectName = transactionProjectName;
		transactionProjectName = null;
		performPendingWrites(projectName);
		performPendingDeletes(projectName);
	}
	
	protected boolean wasWriteHandledByTransaction(String projectName, File file, String contents) throws Exception
	{
		if(!isInTransaction())
			return false;
		
		if(!projectName.equals(transactionProjectName))
			throw new Exception("Started transaction on " + transactionProjectName + " but writing to " + projectName);
		
		pendingWrites.put(file, contents);
		return true;
	}

	protected boolean wasDeleteHandledByTransaction(String projectName, File file) throws Exception
	{
		if(!isInTransaction())
			return false;
		
		if(!projectName.equals(transactionProjectName))
			throw new Exception("Started transaction on " + transactionProjectName + " but writing to " + projectName);
		
		pendingDeletes.add(file);
		return true;
	}

	private void performPendingWrites(String projectName) throws Exception
	{
		writeMultipleFiles(projectName, pendingWrites);
		pendingWrites = null;
	}

	private void performPendingDeletes(String projectName) throws Exception
	{
		deleteMultipleFiles(projectName, pendingDeletes);
		pendingDeletes = null;
	}

	protected boolean isInTransaction()
	{
		return (transactionProjectName != null);
	}

	private String transactionProjectName;
	private HashMap<File, String> pendingWrites;
	private HashSet<File> pendingDeletes;
}
