/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.xmpz2.objectImporters;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.schemas.*;
import org.miradi.utils.StringUtilities;
import org.miradi.utils.XmlUtilities2;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.miradi.xml.xmpz2.objectExporters.ExtraDataExporter.FIELD_TAG_ESCAPE_TOKEN;
import static org.miradi.xml.xmpz2.objectExporters.ExtraDataExporter.TYPE_ID_TAG_SPLIT_TOKEN;

public class Xmpz2ExtraDataImporter extends AbstractXmpz2ObjectImporter
{
	public Xmpz2ExtraDataImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse);
	}
	
	public void importFields() throws Exception
	{
		Node extraDataNode = getImporter().getNamedChildNode(getImporter().getRootNode(), EXTRA_DATA);
		Node extraDataSection = getImporter().getNamedChildNode(extraDataNode, EXTRA_DATA_SECTION);
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
			String[] splitValues = extraDataName.split(TYPE_ID_TAG_SPLIT_TOKEN_FOR_REGULAR_EXPRESSION);
			if (splitValues.length != 3)
				throw new RuntimeException("Incorrect number if values split of extra data name, Raw name before split = " + extraDataName);
			String typeName = splitValues[0];
			String id = splitValues[1];
			String tag = splitValues[2].replace(FIELD_TAG_ESCAPE_TOKEN, TYPE_ID_TAG_SPLIT_TOKEN);
			
			BaseObject baseObject = createOrGetExisting(typeName, id);
			Node extraDataItemValueNode = getImporter().getNamedChildNode(extraDataItemNode, EXTRA_DATA_ITEM_VALUE);
			if (extraDataItemValueNode != null)
			{
				String value = extraDataItemValueNode.getTextContent();
				if (doesFieldRequireDataToBeXmlDecoded(typeName, tag))
					value = XmlUtilities2.getXmlDecoded(value);
				value = StringUtilities.unescapeQuotesWithBackslash(value);
				baseObject.setData(tag, value);
			}
		}
	}

	private BaseObject createOrGetExisting(String typeName, String idAsString) throws Exception
	{
		int type = convertTypeNameToObjectType(typeName);
		BaseId id = new BaseId(Integer.parseInt(idAsString));

		if (isSingleton(typeName))
		{
			BaseObject foundObject = getSingletonObject(typeName);
			if (foundObject != null)
				return foundObject;

			ORef createObjectRef = createSingletonObject(typeName);
			return BaseObject.find(getProject(), createObjectRef);
		}
		else
		{
			BaseObject foundObject = getProject().getPool(type).findObject(id);
			if (foundObject != null)
				return foundObject;
			ORef createObjectRef = getProject().createObject(type, id);
			return BaseObject.find(getProject(), createObjectRef);
		}
	}

	private int convertTypeNameToObjectType(String typeName)
	{
		if (typeName.equals(ViewDataSchema.OBJECT_NAME))
			return ViewDataSchema.getObjectType();
		
		if (typeName.equals(ProjectMetadataSchema.OBJECT_NAME))
			return ProjectMetadataSchema.getObjectType();

		if (typeName.equals(TableSettingsSchema.OBJECT_NAME))
			return TableSettingsSchema.getObjectType();
		
		if (typeName.equals(XslTemplateSchema.OBJECT_NAME))
			return XslTemplateSchema.getObjectType();
		
		if (typeName.equals(ReportTemplateSchema.OBJECT_NAME))
			return ReportTemplateSchema.getObjectType();
		
		if (typeName.equals(IndicatorSchema.OBJECT_NAME))
			return IndicatorSchema.getObjectType();

		if (typeName.equals(TaggedObjectSetSchema.OBJECT_NAME))
			return TaggedObjectSetSchema.getObjectType();

		throw new RuntimeException("Object type name is not recognized as type, " + typeName);
	}

	private boolean isSingleton(String typeName)
	{
		if (typeName.equals(ProjectMetadataSchema.OBJECT_NAME))
			return true;

		return false;
	}

	private BaseObject getSingletonObject(String typeName)
	{
		if (typeName.equals(ProjectMetadataSchema.OBJECT_NAME))
			return getProject().getMetadata();

		throw new RuntimeException("Do not know how to retrieve singleton object for type name, " + typeName);
	}

	private ORef createSingletonObject(String typeName) throws Exception
	{
		if (typeName.equals(ProjectMetadataSchema.OBJECT_NAME))
		{
			getProject().createProjectMetadata();
			return getProject().getMetadata().getRef();
		}

		throw new RuntimeException("Do not know how to create singleton object for type name, " + typeName);
	}

	private boolean doesFieldRequireDataToBeXmlDecoded(String typeName, String tag)
	{
		if (typeName.equals(ProjectMetadataSchema.OBJECT_NAME) && tag.equals(ProjectMetadata.TAG_PROJECT_STATUS))
			return true;

		if (typeName.equals(ProjectMetadataSchema.OBJECT_NAME) && tag.equals(ProjectMetadata.TAG_TNC_LESSONS_LEARNED))
			return true;

		if (typeName.equals(ProjectMetadataSchema.OBJECT_NAME) && tag.equals(ProjectMetadata.TAG_NEXT_STEPS))
			return true;

		if (typeName.equals(IndicatorSchema.OBJECT_NAME) && tag.equals(Indicator.TAG_ASSIGNED_LEADER_RESOURCE))
			return true;

		if (typeName.equals(IndicatorSchema.OBJECT_NAME) && tag.equals(Indicator.TAG_RESOURCE_ASSIGNMENT_IDS))
			return true;

		if (typeName.equals(IndicatorSchema.OBJECT_NAME) && tag.equals(Indicator.TAG_EXPENSE_ASSIGNMENT_REFS))
			return true;

		if (typeName.equals(TaggedObjectSetSchema.OBJECT_NAME) && tag.equals(TaggedObjectSet.TAG_TAGGED_OBJECT_REFS))
			return true;

		return false;
	}

	private static final String TYPE_ID_TAG_SPLIT_TOKEN_FOR_REGULAR_EXPRESSION = "\\" + TYPE_ID_TAG_SPLIT_TOKEN;
}
