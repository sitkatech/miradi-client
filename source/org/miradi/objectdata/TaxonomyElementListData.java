/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objecthelpers.TaxonomyElementList;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2XmlSchemaCreator;
import org.w3c.dom.Node;

public class TaxonomyElementListData extends ObjectData
{
	public TaxonomyElementListData(String tagToUse)
	{
		super(tagToUse);
		
		data = new TaxonomyElementList();
	}

	@Override
	public String get()
	{
		return getTaxonomyElementList().toJsonString();
	}
	
	public TaxonomyElementList getTaxonomyElementList()
	{
		return new TaxonomyElementList(data);
	}

	@Override
	public void set(String newValue) throws Exception
	{
		data = new TaxonomyElementList(newValue);
	}

	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof TaxonomyElementListData))
			return false;

		TaxonomyElementListData other = (TaxonomyElementListData) rawOther;
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
		writer.writeTaxonomyElementList(baseObjectSchema, fieldSchema, getTaxonomyElementList());
	}
	
	@Override
	public void readAsXmpz2XmlData(Xmpz2XmlImporter importer, Node node, ORef destinationRefToUse, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		importer.importTaxonomyElementList(node, destinationRefToUse, baseObjectSchema, fieldSchema);
	}
	
	@Override
	public String createXmpz2SchemaElementString(Xmpz2XmlSchemaCreator creator, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		return creator.getSchemaWriter().createRequiredParentAndZeroOrMoreElementDefinition(baseObjectSchema.getXmpz2ElementName() + fieldSchema.getXmpz2ElementName(), Xmpz2XmlConstants.TAXONOMY_ELEMENT);
	}
	
	private TaxonomyElementList data;
}
