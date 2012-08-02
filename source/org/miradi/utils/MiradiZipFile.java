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
import java.util.regex.Pattern;
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
		
		entry = attemptWithNormalizingForwardSlashes(name);
		if (entry != null)
			return entry; 
		
		entry = attemptWithNormalizingBackwardSlashes(name);
		if (entry != null)
			return entry;
		
		return null;
	}

	private ZipEntry attemptWithNormalizingBackwardSlashes(String name)
	{
		final String matchTo = FileUtilities.SEPARATOR;
		final String replacement = FileUtilities.REGULAR_EXPRESSION_BACKSLASH;
		final String normalizeTo = FileUtilities.BACKWARD_SLASH;

		String nameWithBackwardSlashes = normalizeSlashes(name, matchTo, replacement);
		return attemptGetEntry(nameWithBackwardSlashes, normalizeTo);
	}

	private ZipEntry attemptWithNormalizingForwardSlashes(String name)
	{
		final String matchTo = FileUtilities.REGULAR_EXPRESSION_BACKSLASH;
		final String replacement = FileUtilities.SEPARATOR;
		final String normalizeTo = FileUtilities.SEPARATOR;
		
		String nameWithForwardSlashes = normalizeSlashes(name, matchTo, replacement);
		return attemptGetEntry(nameWithForwardSlashes, normalizeTo);
	}

	private ZipEntry attemptGetEntry(String name, final String separator)
	{
		String nameWithoutLeadingSlash = removeLeadingSlash(name, separator);
		ZipEntry entry = super.getEntry(nameWithoutLeadingSlash);
		if (entry != null)
			return entry;
		
		String nameWithLeadingSlash = addLeadingSlash(nameWithoutLeadingSlash, separator);
		entry = super.getEntry(nameWithLeadingSlash);
		if (entry != null)
			return entry;
		
		return null;
	}
	
	public static String normalizeSlashes(String name, final String match, final String replacement)
	{
		return name.replaceAll(match, replacement);
	}

	public static String removeLeadingSlash(String name, final String separator)
	{
		if (name.startsWith(separator))
			return replaceFirst(separator, name, "");

		return name;		
	}
	
	public static String addLeadingSlash(String name, final String separator)
	{
		if (!name.startsWith(separator))
			return separator + name;
		
		return name;
	}
	
	private static String replaceFirst(String regex, String text, String replacement)
	{
		final Pattern compiledRegex = Pattern.compile(regex, Pattern.LITERAL);
		
		return compiledRegex.matcher(text).replaceFirst(replacement);
	}
}
