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

import org.miradi.objecthelpers.ORef;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.DoubleUtilities;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;
import org.w3c.dom.Node;

// FIXME Low: Can we get rid of this and just use NumberData instead?
public class FloatData extends ObjectData
{
	public FloatData(String tagToUse)
	{
		super(tagToUse);
		
		value = 0;
	}
	
	@Override
	public void set(String newValue) throws Exception
	{
		if(newValue.length() == 0)
		{
			value = 0;
			return;
		}
		
		value = DoubleUtilities.toDoubleFromDataFormat(newValue);
	}
	
	@Override
	public String get()
	{
		if(value == 0.0)
			return "";
		
		return DoubleUtilities.toStringForData(value);
	}
	
	public double asFloat()
	{
		return value;
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof FloatData))
			return false;
		
		FloatData other = (FloatData)rawOther;
		return new Float(value).equals(new Float(other.value));
	}

	@Override
	public int hashCode()
	{
		return new Float(value).hashCode();
	}
	
	@Override
	public void writeAsXmpz2XmlData(Xmpz2XmlWriter writer, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		writer.writeFloatData(baseObjectSchema, fieldSchema, get());
	}
	
	@Override
	public void readAsXmpz2XmlData(Xmpz2XmlImporter importer, Node node, ORef destinationRefToUse, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		importer.importStringField(node, baseObjectSchema.getXmpz2ElementName(), destinationRefToUse, fieldSchema.getTag());
	}

	private double value;
}
