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


abstract public class AbstractNonRemoteMiradiFileSystem implements MiradiFileSystem
{
	public void beginTransaction(String projectName) throws Exception
	{
	}
	
	public void endTransaction() throws Exception
	{
	}
	
	protected boolean wasWriteHandledByTransaction(String projectName, File file, String contents)
	{
		return false;
	}

	protected boolean wasDeleteHandledByTransaction(String projectName, File file)
	{
		return false;
	}

	protected boolean isObjectDirectory(File file)
	{
		return (getTypeOfObjectDirectory(file) != -1);
	}
	
	protected int getTypeOfObjectDirectory(File possibleRelativeObjectDirectory)
	{
		String name = possibleRelativeObjectDirectory.getName();
		final String PREFIX = "objects-";
		final int PREFIX_LENGTH = PREFIX.length();
		if(!name.startsWith(PREFIX))
			return -1;
		return Integer.parseInt(name.substring(PREFIX_LENGTH));
	}

}
