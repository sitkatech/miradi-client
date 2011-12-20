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

package org.miradi.xml.wcs;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TableSettings;
import org.miradi.objects.ViewData;


public class ExtraDataExporter extends AbstractXmlExporter
{
	public ExtraDataExporter(XmpzXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse);
	}

	@Override
	public void exportXml() throws Exception
	{
		getWcsXmlExporter().writeStartElement(EXTRA_DATA);
		getWcsXmlExporter().writeStartElementWithAttribute(getWcsXmlExporter().getWriter(), EXTRA_DATA_SECTION, EXTRA_DATA_SECTION_OWNER_ATTRIBUTE, MIRADI_CLIENT_EXTRA_DATA_SECTION);
		exportPool(ViewData.getObjectType());
		exportPool(TableSettings.getObjectType());
		getWcsXmlExporter().writeEndElement(EXTRA_DATA_SECTION);
		getWcsXmlExporter().writeEndElement(EXTRA_DATA);
	}

	private void exportPool(int poolTypeToImport) throws Exception
	{
		ORefList objectRefs = getProject().getPool(poolTypeToImport).getRefList();
		objectRefs.sort();
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
				getWcsXmlExporter().writeStartElementWithAttribute(getWcsXmlExporter().getWriter(), EXTRA_DATA_ITEM, EXTRA_DATA_ITEM_NAME, extraDataItemName);
				getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), EXTRA_DATA_ITEM_VALUE, data);
				getWcsXmlExporter().writeEndElement(EXTRA_DATA_ITEM);
			}
		}
	}
	
	public static final String TYPE_ID_TAG_SPLIT_TOKEN = ".";
	public static final String TYPE_ID_TAG_SPLIT_TOKEN_FOR_REGULAR_EXPRESSION = "\\."; 
}
