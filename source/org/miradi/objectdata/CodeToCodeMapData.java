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

import org.miradi.objecthelpers.CodeToCodeMap;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlUnicodeWriter;

public class CodeToCodeMapData extends AbstractCodeToStringMapData
{
	public CodeToCodeMapData(String tagToUse)
	{
		super(tagToUse);
		
		data = new CodeToCodeMap();
	}

	@Override
	public String get()
	{
		return getCodeToCodeMap().toJsonString();
	}
	
	public CodeToCodeMap getCodeToCodeMap()
	{
		return data;
	}

	@Override
	public void set(String newValue) throws Exception
	{
		data = new CodeToCodeMap(newValue);
	}

	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof CodeToCodeMapData))
			return false;

		CodeToCodeMapData other = (CodeToCodeMapData) rawOther;
		return other.data.equals(data);
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	@Override
	public boolean isCodeToCodeMapData()
	{
		return true;
	}
	
	@Override
	public void writeAsXmpz2XmlData(Xmpz2XmlUnicodeWriter writer, BaseObjectSchema schema, AbstractFieldSchema fieldSchema) throws Exception
	{
		writer.writeCodeToCodeMapData(schema, fieldSchema, get());
	}
	
	private CodeToCodeMap data;
}
