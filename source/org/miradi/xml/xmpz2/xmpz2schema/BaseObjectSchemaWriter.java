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

package org.miradi.xml.xmpz2.xmpz2schema;

import org.miradi.objectdata.ObjectData;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.generic.SchemaWriter;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

public class BaseObjectSchemaWriter implements Xmpz2XmlConstants
{
	public BaseObjectSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		creator = creatorToUse;
		baseObjectSchema = baseObjectSchemaToUse;
	}
	
	public void writeFields(SchemaWriter writer) throws Exception
	{
		writer.printIndented("attribute " + ID + " "+ "{xsd:integer} &");
		writer.println();
		int indexCount = 0;
		for(AbstractFieldSchema fieldSchema : getBaseObjectSchema())
		{
			if (!doesFieldRequireSpecialHandling(fieldSchema.getTag()))
			{
				writeFieldSchema(fieldSchema);
				if (indexCount < getBaseObjectSchema().numberOfFields() - 1)
				{
					getCreator().getSchemaWriter().print(" &");
					getCreator().getSchemaWriter().println();
				}
				++indexCount;
			}
		}
	}

	private void writeFieldSchema(AbstractFieldSchema fieldSchema) throws Exception
	{
		ObjectData objectData = fieldSchema.createField(null);
		objectData.writeAsXmpz2SchemaElement(creator, baseObjectSchema, fieldSchema);
	}

	private boolean doesFieldRequireSpecialHandling(String tag)
	{
		return false;
	}
	
	public Xmpz2XmlSchemaCreator getCreator()
	{
		return creator;
	}

	public String getPoolName()
	{
		return Xmpz2XmlWriter.createPoolElementName(getXmpz2ElementName());
	}
	
	public String getXmpz2ElementName()
	{
		return getBaseObjectSchema().getXmpz2ElementName();
	}
	
	public BaseObjectSchema getBaseObjectSchema()
	{
		return baseObjectSchema;
	}
	
	private Xmpz2XmlSchemaCreator creator;
	private BaseObjectSchema baseObjectSchema;
}
