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
package org.miradi.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.miradi.main.EAM;
import org.miradi.main.Miradi;

public class LanguagePackFileFilter extends FileFilter implements MiradiFileFilter
{
	public LanguagePackFileFilter() 
	{
		super();
	}

	public boolean accept(File pathname)
	{
		if(pathname.isDirectory())
			return true;
		if(!pathname.getName().toLowerCase().endsWith(EXTENSION))
			return false;
		
		String regexp = Miradi.LANGUAGE_PACK_PREFIX.replaceAll("\\.", "\\\\.") + "..\\.jar";
		if(pathname.getName().matches(regexp))
			return true;
		
		return false;
		
	}

	public String getDescription()
	{
		String wildcardSpec = Miradi.LANGUAGE_PACK_PREFIX + "*.jar";
		return EAM.substitute(EAM.text("FileFilter|Miradi Language Pack (%s)"), wildcardSpec);
	}
	
	public String getFileExtension()
	{
		return EXTENSION;
	}

	public static final String EXTENSION = ".jar";
}
