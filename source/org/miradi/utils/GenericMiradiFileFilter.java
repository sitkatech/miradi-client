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

//NOTE Due to java having two FileFiler s and miradi needing both we have extended and implemented both FileFilers here
abstract public class GenericMiradiFileFilter extends FileFilter implements MiradiFileFilter, java.io.FileFilter 
{
	public boolean accept(File pathname)
	{
		if (pathname.isDirectory())
			return true;
		
		return doesFileEndWithCorrectExtension(pathname);
	}

	protected boolean doesFileEndWithCorrectExtension(File pathname)
	{
		return (pathname.getName().toLowerCase().endsWith(getFileExtension()));
	}
}
