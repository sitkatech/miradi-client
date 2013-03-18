/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class MiradiZipFile extends ZipFile
{
	public MiradiZipFile(File file) throws ZipException, IOException
	{
		super(file);
	}
	
	@Override
	public ZipEntry getEntry(String searchFor)
	{
		ZipEntry entry = super.getEntry(searchFor);
		if (entry != null)
			return entry;

		String normalizedSearchFor = ZipUtilities.getNormalizedWithoutLeadingSlash(searchFor);
		Enumeration<? extends ZipEntry> entries = entries();
		while(entries.hasMoreElements())
		{
			ZipEntry thisEntry = entries.nextElement();
			String rawEntryName = thisEntry.getName();
			String normalizedThisName = ZipUtilities.getNormalizedWithoutLeadingSlash(rawEntryName);
			if(normalizedThisName.equals(normalizedSearchFor))
				return thisEntry;
		}
		
		return null;
	}
}
