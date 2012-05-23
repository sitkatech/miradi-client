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

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.schemas.TableSettingsSchema;
import org.miradi.schemas.ViewDataSchema;
import org.miradi.xml.wcs.ExtraDataExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Xmpz2ExtraDataImporter extends BaseObjectImporter
{
	public Xmpz2ExtraDataImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse, null);
	}
	
	@Override
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		importFields();
	}
	
	public void importFields() throws Exception
	{
		Node extraDataNode = getImporter().getNode(getImporter().getRootNode(), EXTRA_DATA);
		Node extraDataSection = getImporter().getNode(extraDataNode, EXTRA_DATA_SECTION);
		if (extraDataSection == null)
			return;
		
		String sectionOwner = getImporter().getAttributeValue(extraDataSection, EXTRA_DATA_SECTION_OWNER_ATTRIBUTE);
		if (!sectionOwner.equals(MIRADI_CLIENT_EXTRA_DATA_SECTION))
			return;
		
		NodeList extraDataNodes = getImporter().getNodes(extraDataSection, new String[]{EXTRA_DATA_ITEM, });
		for (int index = 0; index < extraDataNodes.getLength(); ++index)
		{
			Node extraDataItemNode = extraDataNodes.item(index);
			String extraDataName = getImporter().getAttributeValue(extraDataItemNode, EXTRA_DATA_ITEM_NAME);
			String[] splitValues = extraDataName.split(ExtraDataExporter.TYPE_ID_TAG_SPLIT_TOKEN_FOR_REGULAR_EXPRESSION);
			if (splitValues.length != 3)
				throw new RuntimeException("Incorrect number if values split of extra data name, Raw name before split = " + extraDataName);
			String typeName = splitValues[0];
			String id = splitValues[1];
			String tag = splitValues[2];
			
			BaseObject baseObject = createOrGetExisting(typeName, id);
			Node extraDataItemValueNode = getImporter().getNode(extraDataItemNode, EXTRA_DATA_ITEM_VALUE);
			if (extraDataItemValueNode != null)
			{
				String value = extraDataItemValueNode.getTextContent();
				baseObject.setData(tag, value);
			}
		}
	}

	private BaseObject createOrGetExisting(String typeName, String idAsString) throws Exception
	{
		int type = convertTypeNameToObjectType(typeName);
		BaseId id = new BaseId(Integer.parseInt(idAsString));
		BaseObject foundObject = getProject().getPool(type).findObject(id);
		if (foundObject != null)
			return foundObject;
		
		ORef createObjectRef = getProject().createObject(type, id);
		
		return BaseObject.find(getProject(), createObjectRef);
	}

	private int convertTypeNameToObjectType(String typeName)
	{
		if (typeName.equals(ViewDataSchema.OBJECT_NAME))
			return ViewDataSchema.getObjectType();
		
		if (typeName.equals(TableSettingsSchema.OBJECT_NAME))
			return TableSettingsSchema.getObjectType();
		
		throw new RuntimeException("Object type name is not recognized as type, " + typeName);
	}
}
