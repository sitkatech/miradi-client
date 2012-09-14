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

import org.miradi.main.EAM;

public class MpfFileFilter extends GenericMiradiFileFilter
{
	@Override
	public boolean accept(File pathname)
	{
		return doesFileEndWithCorrectExtension(pathname);
	}
	
	@Override
	public String getDescription()
	{
		return EAM.substitute(EAM.text("FileFilter|Miradi (*%s)"),getFileExtension());
	}
	
	public String getFileExtension()
	{
		return EXTENSION;
	}
	
	public static String createNameWithExtension(String name)
	{
		return name + EXTENSION;
	}

	public static final String EXTENSION = ".Miradi";
}