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

import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

public class BaseObjectImporter extends AbstractXmpz2ObjectImporter
{
	public BaseObjectImporter(final Xmpz2XmlImporter importerToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(importerToUse);
		
		baseObjectSchema = baseObjectSchemaToUse; 
	}
	
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		BaseObject baseObject = BaseObject.find(getProject(), refToUse);
		
		for(AbstractFieldSchema fieldSchema : getBaseObjectSchema())
		{
			if (fieldSchema.isPseudoField())
				continue;

			if (isCustomImportField(fieldSchema.getTag()))
				continue;

			ObjectData objectData = fieldSchema.createField(baseObject);
			objectData.readAsXmpz2XmlData(getImporter(), baseObjectNode, refToUse, baseObjectSchema, fieldSchema);
		}
	}
	
	protected boolean isCustomImportField(String tag)
	{
		return false;
	}

	public void postCreateFix(ORef ref, Node baseObjectNode) throws Exception
	{
	}
	
	protected String getPoolName()
	{
		return getBaseObjectSchema().getXmpz2ElementName();
	}

	public BaseObjectSchema getBaseObjectSchema()
	{
		return baseObjectSchema;
	}
	
	private BaseObjectSchema baseObjectSchema;
}
