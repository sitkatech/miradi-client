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

package org.miradi.xml.xmpz2.objectExporters;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.schemas.ReportTemplateSchema;
import org.miradi.schemas.TableSettingsSchema;
import org.miradi.schemas.ViewDataSchema;
import org.miradi.schemas.XslTemplateSchema;
import org.miradi.utils.StringUtilities;
import org.miradi.utils.XmlUtilities2;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

public class ExtraDataExporter implements Xmpz2XmlConstants
{
	public ExtraDataExporter(final Project projectToUse, final Xmpz2XmlWriter writerToUse)
	{
		project = projectToUse;
		out = writerToUse;
	}
	
	public void exportExtraData() throws Exception
	{
		getWriter().writeStartElement(EXTRA_DATA);
		getWriter().writeStartElementWithAttribute(EXTRA_DATA_SECTION, EXTRA_DATA_SECTION_OWNER_ATTRIBUTE, MIRADI_CLIENT_EXTRA_DATA_SECTION);
		exportPool(ViewDataSchema.getObjectType());
		exportPool(TableSettingsSchema.getObjectType());
		exportPool(XslTemplateSchema.getObjectType());
		exportPool(ReportTemplateSchema.getObjectType());
		getWriter().writeEndElement(EXTRA_DATA_SECTION);
		getWriter().writeEndElement(EXTRA_DATA);
	}
	
	private void exportPool(int poolTypeToImport) throws Exception
	{
		ORefList objectRefs = getProject().getPool(poolTypeToImport).getSortedRefList();
		for (int index = 0; index < objectRefs.size(); ++index)
		{
			BaseObject baseObject = BaseObject.find(getProject(), objectRefs.get(index));
			writeExtraDataElement(baseObject);
		}
	}

	private void writeExtraDataElement(BaseObject baseObject) throws Exception
	{
		String[] fieldTags = baseObject.getFieldTags();
		for (int index = 0; index < fieldTags.length; ++index)
		{
			String fieldTag = fieldTags[index];
			String extraDataItemName = getExtraDataItemName(baseObject.getTypeName(), baseObject.getId(), fieldTag);
			String data = baseObject.getData(fieldTag);
			if (data.length() > 0)
			{
				getWriter().writeStartElementWithAttribute(EXTRA_DATA_ITEM, EXTRA_DATA_ITEM_NAME, extraDataItemName);
				data = StringUtilities.escapeQuotesWithBackslash(data);
				data = XmlUtilities2.getXmlEncoded(data);
				getWriter().writeElement(EXTRA_DATA_ITEM_VALUE, data);
				getWriter().writeEndElement(EXTRA_DATA_ITEM);
			}
		}
	}

	public static String getExtraDataItemName(String typeName, BaseId objectId, String fieldTag)
	{
		return typeName + TYPE_ID_TAG_SPLIT_TOKEN + objectId + TYPE_ID_TAG_SPLIT_TOKEN + fieldTag.replace(TYPE_ID_TAG_SPLIT_TOKEN, FIELD_TAG_ESCAPE_TOKEN);
	}

	private Xmpz2XmlWriter getWriter()
	{
		return out;
	}

	public Project getProject()
	{
		return project;
	}
	
	private Project project;
	private Xmpz2XmlWriter out;
	public static final String TYPE_ID_TAG_SPLIT_TOKEN = ".";
	public static final String FIELD_TAG_ESCAPE_TOKEN = ":";
}
