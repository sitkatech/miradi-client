/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.objectdata;

import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2XmlSchemaCreator;

public class SingleLineUserTextData extends UserTextData
{
	public SingleLineUserTextData(String tagToUse)
	{
		super(tagToUse);
	}

	@Override
	public boolean isSingleLineUserText()
	{
		return true;
	}
	
	@Override
	public String createXmpz2SchemaElementString(Xmpz2XmlSchemaCreator creator, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		return creator.createStringSchemaElement(baseObjectSchema, fieldSchema);
	}
}
