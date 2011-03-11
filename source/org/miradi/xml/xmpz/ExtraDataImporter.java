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

package org.miradi.xml.xmpz;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TableSettings;
import org.miradi.objects.ViewData;
import org.miradi.xml.AbstractXmpzObjectImporter;
import org.miradi.xml.wcs.ExtraDataExporter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExtraDataImporter extends AbstractXmpzObjectImporter
{
	public ExtraDataImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, EXTRA_DATA);
	}

	@Override
	public void importElement() throws Exception
	{
		Node extraDataNode = getImporter().getNode(getImporter().getRootNode(), EXTRA_DATA);
		NodeList extraDataNodes = getImporter().getNodes(extraDataNode, new String[]{EXTRA_DATA_ITEM, });
		
		for (int index = 0; index < extraDataNodes.getLength(); ++index)
		{
			Node extraDataItemNode = extraDataNodes.item(index);
			String extraDataName = getImporter().getAttributeValue(extraDataItemNode, EXTRA_DATA_ITEM_NAME);
			final String ESCAPE_CHAR = "\\";
			String[] splitValues = extraDataName.split(ESCAPE_CHAR + ExtraDataExporter.TYPE_ID_TAG_SPLIT_TOKEN);
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
		if (typeName.equals(ViewData.OBJECT_NAME))
			return ViewData.getObjectType();
		
		if (typeName.equals(TableSettings.OBJECT_NAME))
			return TableSettings.getObjectType();
		
		throw new RuntimeException("Object type name is not recognized as type, " + typeName);
	}
}
