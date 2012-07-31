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
	public ZipEntry getEntry(String name)
	{
		ZipEntry entry = super.getEntry(name);
		if (entry != null)
			return entry;
		
		final ZipEntry withOtherPossibleLeadingChar = super.getEntry(replaceWithOtherPossibleLeadingChar(name, FileUtilities.SEPARATOR));
		if (withOtherPossibleLeadingChar != null)
			return withOtherPossibleLeadingChar;
		
		final String reversedPathSeparator = attemptUsingReversedPathSeparator(name);
		if (reversedPathSeparator != null)
			return super.getEntry(reversedPathSeparator);
		
		return null;
	}

	public static String attemptUsingReversedPathSeparator(String name)
	{
		if (name.contains(FileUtilities.SEPARATOR))
			return name.replaceAll(FileUtilities.SEPARATOR, "\\\\");
		
		if (name.contains("\\"))
			return name.replaceAll("\\\\", FileUtilities.SEPARATOR);
		
		return null;
	}

	public static String replaceWithOtherPossibleLeadingChar(String name, final String separator)
	{
		if (name.startsWith(separator))
			return name.replaceFirst(separator, "");
		
		return separator + name;
	}
}
