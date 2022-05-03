/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import java.io.FilenameFilter;

import javax.swing.filechooser.FileFilter;

import org.miradi.main.EAM;
import org.miradi.main.Miradi;

public class LanguagePackFileFilter extends FileFilter implements MiradiFileFilter, FilenameFilter
{
	public LanguagePackFileFilter() 
	{
		super();
	}

	public boolean accept(File dir, String name)
	{
		return acceptMatchingFile(new File(dir, name));
	}

	@Override
	public boolean accept(File pathname)
	{
		// NOTE: Must accept directories to populate the file open dialog
		return acceptDirectoryOrMatchingFile(pathname);		
	}

	private boolean acceptDirectoryOrMatchingFile(File pathname)
	{
		if(pathname.isDirectory())
			return true;
		return acceptMatchingFile(pathname);
	}

	private boolean acceptMatchingFile(File pathname)
	{
		String filename = pathname.getName();
		if(!filename.toLowerCase().endsWith(EXTENSION))
			return false;
		
		String regexp = convertLanguagePackPrefixToRegularExpression();
		if(filename.matches(regexp))
			return true;
		
		return false;
	}

	@Override
	public String getDescription()
	{
		String wildcardSpec = Miradi.LANGUAGE_PACK_PREFIX + "*.jar";
		return EAM.substituteSingleString(EAM.text("FileFilter|Miradi Language Pack (%s)"), wildcardSpec);
	}
	
	public String getFileExtension()
	{
		return EXTENSION;
	}

	public String convertLanguagePackPrefixToRegularExpression()
	{
		String regexp = Miradi.LANGUAGE_PACK_PREFIX.replaceAll("\\.", "\\\\.");
		String JAR_EXTENSION_AS_REGEXP = "\\.jar";
		String REGEXP_FOR_LANGUAGE_CODE = "[A-Za-z][A-Za-z][A-Za-z]?";
		regexp += REGEXP_FOR_LANGUAGE_CODE + JAR_EXTENSION_AS_REGEXP;
		return regexp;
	}

	public static final String EXTENSION = ".jar";
}
