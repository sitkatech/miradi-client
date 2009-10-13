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

package org.miradi.xml.generic;

import java.io.IOException;

import org.miradi.xml.wcs.WcsXmlConstants;

public class ObjectContainerSchemaElement extends ObjectSchemaElement
{
	public ObjectContainerSchemaElement(ObjectSchemaElement elementToWrap)
	{
		super(getContainerName(elementToWrap.getObjectTypeName()));
		objectSchemaElement = elementToWrap;
	}

	@Override
	public void output(SchemaWriter writer) throws IOException
	{
		String result = WcsXmlConstants.ELEMENT_NAME + WcsXmlConstants.PREFIX + getObjectTypeName() + " { " + getDotElement(objectSchemaElement.getObjectTypeName()) + "* }";
		writer.defineAlias(getDotElement(getObjectTypeName()), result);
		objectSchemaElement.output(writer);
	}
	
	@Override
	public String getObjectTypeName()
	{
		return getContainerName(objectSchemaElement.getObjectTypeName());
	}

	private static String getContainerName(String wrappedObjectTypeName)
	{
		return wrappedObjectTypeName + WcsXmlConstants.CONTAINER_ELEMENT_TAG;
	}

	private ObjectSchemaElement objectSchemaElement;
}
