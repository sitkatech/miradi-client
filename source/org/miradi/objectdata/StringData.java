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
package org.miradi.objectdata;

import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlUnicodeWriter;

abstract public class StringData extends ObjectData
{
	protected StringData(String tagToUse)
	{
		super(tagToUse);
		value = "";
	}
	
	@Override
	public void set(String newValue) throws Exception
	{
		value = newValue;
	}

	@Override
	public String get()
	{
		return value;
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof StringData))
			return false;
		
		StringData other = (StringData)rawOther;
		return get().equals(other.get());
	}

	@Override
	public int hashCode()
	{
		return get().hashCode();
	}
	
	@Override
	public void writeAsXmpz2XmlData(Xmpz2XmlUnicodeWriter writer, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		writer.writeStringData(baseObjectSchema, fieldSchema, get());
	}

	private String value;
}
