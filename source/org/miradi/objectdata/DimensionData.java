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

import java.awt.Dimension;

import org.miradi.objecthelpers.ORef;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;
import org.w3c.dom.Node;

public class DimensionData extends ObjectData
{

	public DimensionData(String tagToUse)
	{
		this(tagToUse, new Dimension(0, 0));
	}
	
	public DimensionData(String tagToUse, Dimension dimensionToUse)
	{
		super(tagToUse);
		
		setDimension(dimensionToUse);
	}

	public Dimension getDimension()
	{
		return dimension;
	}
	
	public void setDimension(Dimension dimensionToUse)
	{
		dimension = dimensionToUse;
	}

	@Override
	public void set(String newValue) throws Exception
	{
		if(newValue.length() == 0)
		{
			dimension = new Dimension(0, 0);
			return;
		}
		
		dimension = EnhancedJsonObject.convertToDimension(newValue);		
	}

	@Override
	public String get()
	{
		if(dimension == null)
			return "";
		
		return EnhancedJsonObject.convertFromDimension(dimension);
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof DimensionData))
			return false;
		
		DimensionData other = (DimensionData)rawOther;
		return dimension.equals(other.dimension);
	}

	@Override
	public int hashCode()
	{
		return dimension.hashCode();
	}
	
	@Override
	public void writeAsXmpz2XmlData(Xmpz2XmlWriter writer, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		writer.writeDimensionData(baseObjectSchema, fieldSchema, getDimension());
	}
	
	@Override
	public void readAsXmpz2XmlData(Xmpz2XmlImporter importer, Node node, ORef destinationRefToUse, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		importer.importDimensionField(node, destinationRefToUse, baseObjectSchema, fieldSchema);
	}
	
	Dimension dimension;
}
