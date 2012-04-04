/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.objecthelpers.CodeToCodeListMap;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlUnicodeWriter;

public class CodeToCodeListMapData extends ObjectData
{
	public CodeToCodeListMapData(String tagToUse)
	{
		super(tagToUse);
		
		data = new CodeToCodeListMap();
	}

	@Override
	public String get()
	{
		return getStringToCodeListMap().toJsonString();
	}
	
	public CodeToCodeListMap getStringToCodeListMap()
	{
		return data;
	}

	@Override
	public void set(String newValue) throws Exception
	{
		data = new CodeToCodeListMap(newValue);
	}

	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof CodeToCodeListMapData))
			return false;

		CodeToCodeListMapData other = (CodeToCodeListMapData) rawOther;
		return other.data.equals(data);
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	@Override
	public boolean isCodeToCodeListMapData()
	{
		return true;
	}
	
	@Override
	public void writeAsXmpz2XmlData(Xmpz2XmlUnicodeWriter writer, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		throw new RuntimeException("This method should not be called since the field is only used by TableSettings, which is not exported");
	}
	
	private CodeToCodeListMap data;
}
