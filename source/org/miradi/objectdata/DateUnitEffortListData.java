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
package org.miradi.objectdata;

import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2XmlSchemaCreator;

public class DateUnitEffortListData extends ObjectData
{
	public DateUnitEffortListData(String tagToUse)
	{
		super(tagToUse);
		dateUnitEffortList = new DateUnitEffortList();
	}
	
	public DateUnitEffortList getDateUnitEffortList()
	{
		return dateUnitEffortList; 
	}

	@Override
	public String get()
	{
		return dateUnitEffortList.toString();
	}

	@Override
	public void set(String newValue) throws Exception
	{
		set(new DateUnitEffortList(newValue));	
	}
	
	private void set(DateUnitEffortList dateUnitEffortToUse)
	{
		dateUnitEffortList = dateUnitEffortToUse;
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof DateUnitEffortListData))
			return false;
		
		return rawOther.toString().equals(toString());
	}

	@Override
	public String createXmpz2SchemaElementString(Xmpz2XmlSchemaCreator creator, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		return creator.createDateUnitEffortListSchemaElement(baseObjectSchema, fieldSchema);
	}
	
	private DateUnitEffortList dateUnitEffortList;
}
