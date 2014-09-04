/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objecthelpers.TaxonomyClassificationMap;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2XmlSchemaCreator;
import org.w3c.dom.Node;

public class TaxonomyClassificationMapData extends ObjectData
{
	public TaxonomyClassificationMapData(String tagToUse)
	{
		super(tagToUse);
		
		data = new TaxonomyClassificationMap();
	}
	
	@Override
	public boolean isTaxonomyClassificationMapData()
	{
		return true;
	}

	@Override
	public String get()
	{
		return getTaxonomyClassifications().toJsonString();
	}
	
	public TaxonomyClassificationMap getTaxonomyClassifications()
	{
		return data;
	}

	@Override
	public void set(String newValue) throws Exception
	{
		data = new TaxonomyClassificationMap(newValue);
	}

	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof TaxonomyClassificationMapData))
			return false;

		TaxonomyClassificationMapData other = (TaxonomyClassificationMapData) rawOther;
		return other.data.equals(data);
	}

	@Override
	public int hashCode()
	{
		return data.hashCode();
	}
	
	@Override
	public void writeAsXmpz2XmlData(Xmpz2XmlWriter writer, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		writer.writeTaxonomyClassifications(baseObjectSchema, fieldSchema, data);
	}
	
	@Override
	public void readAsXmpz2XmlData(Xmpz2XmlImporter importer, Node node, ORef destinationRefToUse, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		importer.importTaxonomyClassificationList(node, destinationRefToUse, baseObjectSchema, fieldSchema);
	}
	
	@Override
	public String createXmpz2SchemaElementString(Xmpz2XmlSchemaCreator creator, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		return creator.getSchemaWriter().createOptionalDotElement(fieldSchema.getXmpz2ElementName());
	}
	
	private TaxonomyClassificationMap data;
}
