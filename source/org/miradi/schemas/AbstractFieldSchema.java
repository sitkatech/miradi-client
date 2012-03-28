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

package org.miradi.schemas;

import org.miradi.objectdata.ObjectData;
import org.miradi.objects.BaseObject;

abstract public class AbstractFieldSchema
{
	public AbstractFieldSchema(final String tagToUse)
	{
		tag = tagToUse;
	}
	
	protected String getTag()
	{
		return tag;
	}
	
	public void setNavigationField()
	{
		isNavigationField = true;
	}
	
	public boolean isNavigationField()
	{
		return isNavigationField;
	}
	
	public String getXmpz2ElementName()
	{
		return getTag();
	}
	
	abstract public ObjectData createField(final BaseObject baseObjectToUse);

	private String tag;
	private boolean isNavigationField;
}
