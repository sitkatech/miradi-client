/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz2.objectImporters;

import org.miradi.objecthelpers.ORef;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.wcs.TagToElementNameMap;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

public class BaseObjectImporter implements XmpzXmlConstants
{
	public BaseObjectImporter(final Xmpz2XmlImporter importerToUse, final BaseObjectSchema baseObjectSchemaToUse)
	{
		importer = importerToUse;
		baseObjectSchema = baseObjectSchemaToUse; 
	}
	
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		for(AbstractFieldSchema fieldSchema : getBaseObjectSchema())
		{
			final String fieldTag = fieldSchema.getTag();
			importField(baseObjectNode, refToUse, fieldTag);
		}
	}
	
	protected void importField(Node node, ORef destinationRef, String destinationTag) throws Exception
	{
		TagToElementNameMap map = new TagToElementNameMap();
		String elementName = map.findElementName(getBaseObjectSchema().getXmpz2ElementName(), destinationTag);
		getImporter().importField(node, getBaseObjectSchema().getXmpz2ElementName() + elementName, destinationRef, destinationTag);
	}

	protected Xmpz2XmlImporter getImporter()
	{
		return importer;
	}
	
	protected BaseObjectSchema getBaseObjectSchema()
	{
		return baseObjectSchema;
	}
	
	private Xmpz2XmlImporter importer;
	private BaseObjectSchema baseObjectSchema;
}
