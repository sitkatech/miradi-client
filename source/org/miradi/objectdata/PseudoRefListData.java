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

package org.miradi.objectdata;

import org.miradi.objects.BaseObject;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

public class PseudoRefListData extends RefListData
{
	public PseudoRefListData(BaseObject owningObject, String tag)
	{
		super(tag);
		
		object = owningObject;
	}

	@Override
	public boolean isPseudoField()
	{
		return true;
	}

	@Override
	public void set(String newValue) throws Exception
	{
		if (newValue.length()!=0)
			throw new RuntimeException("Set not allowed in a pseuod field");
	}

	@Override
	public String get()
	{
		return object.getPseudoData(getTag());
	}
	
	@Override
	public void writeAsXmpz2XmlData(Xmpz2XmlWriter writer, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		writer.writePseudoRefListData(baseObjectSchema, fieldSchema, get());
	}
	
	private BaseObject object;
}