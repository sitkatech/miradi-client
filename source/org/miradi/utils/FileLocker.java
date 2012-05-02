/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

public class FileLocker
{
	public void lock(File fileToLock) throws IOException, AlreadyLockedException
	{
		lockedFile = fileToLock;
		FileOutputStream tempLockStream = new FileOutputStream(lockedFile);
		FileLock lock = tempLockStream.getChannel().tryLock();
		if(lock == null)
			throw new AlreadyLockedException();
		lockedStream = tempLockStream;
	}
	
	public void close() throws IOException
	{
		if(lockedStream == null)
			return;
		
		FileOutputStream tempLockStream = lockedStream;
		lockedStream = null;
		tempLockStream.close();
		lockedFile.delete();
	}
	
	public boolean isLockActive()
	{
		return (lockedStream != null);
	}
	
	public boolean attemptToLock(File file) throws IOException
	{
		try
		{
			lock(file);
			return true;
		}
		catch(AlreadyLockedException e)
		{
			return false;
		}
	}
	
	public static class AlreadyLockedException extends Exception {}

	private File lockedFile;
	private FileOutputStream lockedStream;
}
