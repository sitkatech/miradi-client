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


import org.miradi.objecthelpers.ORef;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.DoubleUtilities;
import org.miradi.utils.InvalidNumberException;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2XmlSchemaCreator;
import org.w3c.dom.Node;

public class NumberData extends ObjectData
{
	public NumberData(String tagToUse)
	{
		super(tagToUse);

		value = Double.NaN;
	}

	@Override
	public boolean isNumberData()
	{
		return true;
	}
	
	@Override
	public void set(String newValue) throws Exception
	{
		if(newValue.length() == 0)
		{
			value = Double.NaN;
			return;
		}
		
		try
		{
			value = DoubleUtilities.toDoubleFromDataFormat(newValue);
		}
		catch (NumberFormatException e)
		{
			throw new InvalidNumberException(e);
		}
	}

	@Override
	public String get()
	{
		if(new Double(value).isNaN())
			return "";

		return DoubleUtilities.toStringForData(value);
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof NumberData))
			return false;
		
		NumberData other = (NumberData)rawOther;
		return new Double(value).equals(new Double(other.value));
	}

	@Override
	public int hashCode()
	{
		return (int)value;
	}
	
	public double getSafeValue()
	{
		if (new Double(value).isNaN())
			return 0;

		return value;
	}
	
	@Override
	public void writeAsXmpz2XmlData(Xmpz2XmlWriter writer, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		writer.writeNumberData(baseObjectSchema, fieldSchema, get());
	}
	
	@Override
	public void readAsXmpz2XmlData(Xmpz2XmlImporter importer, Node node, ORef destinationRefToUse, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		importer.importStringField(node, baseObjectSchema.getXmpz2ElementName(), destinationRefToUse, fieldSchema.getTag());
	}
	
	@Override
	public String createXmpz2SchemaElementString(Xmpz2XmlSchemaCreator creator, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		return creator.createNumberSchemaElement(baseObjectSchema, fieldSchema);
	}
	
	double value;
}
