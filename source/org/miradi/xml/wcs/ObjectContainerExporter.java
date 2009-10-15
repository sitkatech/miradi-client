/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import org.martus.util.UnicodeWriter;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

abstract public class ObjectContainerExporter implements WcsXmlConstants
{
	public ObjectContainerExporter(WcsXmlExporter wcsXmlExporterToUse, String containerNameToUse, int objectTypeToUse)
	{
		wcsXmlExporter = wcsXmlExporterToUse;
		containerName = containerNameToUse;
		objectType = objectTypeToUse;
	}

	public void exportBaseObjectContainer() throws Exception
	{
		getWcsXmlExporter().writeStartContainerElement(containerName);
		ORefList refs = getProject().getPool(getObjectType()).getSortedRefList();
		for (int index = 0; index < refs.size(); ++index)
		{
			BaseObject baseObject = BaseObject.find(getProject(), refs.get(index));
			writeObjectStartElement(baseObject);
			
			exportFields(getWriter(), baseObject);
			
			getWcsXmlExporter().writeEndElement(getWriter(), containerName);
		}
		
		getWcsXmlExporter().writeEndContainerElement(containerName);
	}

	protected void writeObjectStartElement(BaseObject baseObject) throws Exception
	{
		getWcsXmlExporter().writeStartElement(containerName);
	}

	protected UnicodeWriter getWriter()
	{
		return getWcsXmlExporter().getWriter();
	}
	
	protected void writeOptionalElementWithSameTag(BaseObject baseObject, String tag) throws Exception
	{
		getWcsXmlExporter().writeOptionalElementWithSameTag(getContainerName(), baseObject, tag);
	}
	
	protected void writeElementWithSameTag(BaseObject baseObject, String tag) throws Exception
	{
		getWcsXmlExporter().writeElementWithSameTag(getContainerName(), baseObject, tag);
	}
	
	protected void writeIds(String idsElementName, String idElementName, ORefList refs) throws Exception
	{
		getWcsXmlExporter().writeIds(getContainerName(), idsElementName, idElementName + WcsXmlConstants.ID, refs);
	}
	
	protected void writeCodeListElement(String codeListElementName, BaseObject baseObject, String tag) throws Exception
	{
		getWcsXmlExporter().writeCodeListElement(getContainerName(), codeListElementName, baseObject, tag);
	}
	
	protected void writeCodeElementSameAsTag(BaseObject baseObject, String tag) throws Exception
	{
		writeCodeElement(tag, baseObject.getData(tag));
	}
	
	protected void writeCodeElement(String codeElementName, String code) throws Exception
	{
		getWcsXmlExporter().writeCodeElement(getContainerName(), codeElementName, code);
	}
	
	protected String getContainerName()
	{
		return containerName;
	}
	
	private int getObjectType()
	{
		return objectType;
	}
	
	protected Project getProject()
	{
		return wcsXmlExporter.getProject();
	}
	
	protected WcsXmlExporter getWcsXmlExporter()
	{
		return wcsXmlExporter;
	}
	
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		writeOptionalElementWithSameTag(baseObject, BaseObject.TAG_LABEL);
	}
	
	private WcsXmlExporter wcsXmlExporter;
	private int objectType;
	private String containerName;
}
