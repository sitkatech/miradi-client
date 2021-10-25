/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import org.miradi.main.EAM;

public class UserDefinedFileFilter extends GenericMiradiFileFilter
{
	public UserDefinedFileFilter(final String extensionToUse)
	{
		extension = extensionToUse;
		possiblyPrefixDot();
	}
	
	private void possiblyPrefixDot()
	{
		if (hasExtension() && !extension.startsWith("."))
		{
			extension = "." + extension;
		}
	}
	
	private boolean hasExtension()
	{
		return getFileExtension().length() > 0;
	}

	public String getFileExtension()
	{
		return extension;
	}

	@Override
	public String getDescription()
	{
		return EAM.text("User Defined Extension");
	}
	
	private String extension;
}
