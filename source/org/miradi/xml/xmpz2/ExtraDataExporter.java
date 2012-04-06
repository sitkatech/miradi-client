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

package org.miradi.xml.xmpz2;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.schemas.TableSettingsSchema;
import org.miradi.schemas.ViewDataSchema;
import org.miradi.xml.wcs.XmpzXmlConstants;

public class ExtraDataExporter implements XmpzXmlConstants
{
	public ExtraDataExporter(final Project projectToUse, final Xmpz2XmlUnicodeWriter writerToUse)
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
			String extraDataItemName = baseObject.getTypeName() + TYPE_ID_TAG_SPLIT_TOKEN + baseObject.getId() + TYPE_ID_TAG_SPLIT_TOKEN + fieldTag;
			String data = baseObject.getData(fieldTag);
			if (data.length() > 0)
			{
				getWriter().writeStartElementWithAttribute(EXTRA_DATA_ITEM, EXTRA_DATA_ITEM_NAME, extraDataItemName);
				getWriter().writeElement(EXTRA_DATA_ITEM_VALUE, data);
				getWriter().writeEndElement(EXTRA_DATA_ITEM);
			}
		}
	}
	
	private Xmpz2XmlUnicodeWriter getWriter()
	{
		return out;
	}

	public Project getProject()
	{
		return project;
	}
	
	private Project project;
	private Xmpz2XmlUnicodeWriter out;
	private static final String TYPE_ID_TAG_SPLIT_TOKEN = ".";
}
