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

import java.io.IOException;

import org.martus.util.UnicodeWriter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceQuestion;

abstract public class ObjectPoolExporter extends AbstractXmlExporter
{
	public ObjectPoolExporter(WcsXmlExporter wcsXmlExporterToUse, String poolNameToUse, int objectTypeToUse)
	{
		super(wcsXmlExporterToUse);
		
		poolName = poolNameToUse;
		objectType = objectTypeToUse;
	}

	@Override
	public void exportXml() throws Exception
	{
		getWcsXmlExporter().writeStartPoolElement(poolName);
		ORefList refs = getProject().getPool(getObjectType()).getSortedRefList();
		for (int index = 0; index < refs.size(); ++index)
		{
			BaseObject baseObject = BaseObject.find(getProject(), refs.get(index));
			writeObjectStartElement(baseObject);
			
			exportFields(getWriter(), baseObject);
			
			getWcsXmlExporter().writeEndElement(getWriter(), poolName);
		}
		
		getWcsXmlExporter().writeEndPoolElement(poolName);
	}

	protected void writeObjectStartElement(BaseObject baseObject) throws Exception
	{
		getWcsXmlExporter().writeStartElement(poolName);
	}

	protected UnicodeWriter getWriter()
	{
		return getWcsXmlExporter().getWriter();
	}
	
	protected void writeOptionalElementWithSameTag(BaseObject baseObject, String tag) throws Exception
	{
		getWcsXmlExporter().writeOptionalElementWithSameTag(getPoolName(), baseObject, tag);
	}
	
	protected void writeElementWithSameTag(BaseObject baseObject, String tag) throws Exception
	{
		getWcsXmlExporter().writeElementWithSameTag(getPoolName(), baseObject, tag);
	}
	
	protected void writeIds(String idsElementName, String idElementName, ORefList refs) throws Exception
	{
		getWcsXmlExporter().writeIds(getPoolName(), idsElementName, idElementName + WcsXmlConstants.ID, refs);
	}
	
	protected void writeCodeListElement(String codeListElementName, BaseObject baseObject, String tag) throws Exception
	{
		getWcsXmlExporter().writeCodeListElement(getPoolName(), codeListElementName, baseObject, tag);
	}
	
	protected void writeCodeElementSameAsTag(BaseObject baseObject, String tag, ChoiceQuestion question) throws Exception
	{
		writeCodeElement(tag, baseObject, tag, question);
	}
	protected void writeCodeElement(String codeListElementName, BaseObject baseObject, String tag, ChoiceQuestion question) throws Exception
	{
		writeCodeElement(codeListElementName, question, baseObject.getData(tag));
	}
	
	protected void writeOptionalCodeElementSameAsTag(BaseObject baseObject, String tag, ChoiceQuestion question) throws Exception
	{
		writeOptionalCodeElement(tag, question, baseObject.getData(tag));
	}
	
	protected void writeCodeElement(String codeElementName, ChoiceQuestion question, String code) throws Exception
	{
		getWcsXmlExporter().writeCodeElement(getPoolName(), codeElementName, question, code);
	}
	
	protected void writeOptionalCodeElement(String codeElementName, ChoiceQuestion question, String code) throws Exception
	{
		getWcsXmlExporter().writeOptionalCodeElement(getPoolName(), codeElementName, question, code);
	}
	
	protected void exportId(ORef ref, String idElementName) throws Exception, IOException
	{
		if (ref.isValid())
		{
			getWcsXmlExporter().writeStartElement(getPoolName() + idElementName);
			getWcsXmlExporter().writeStartElement(idElementName);
			
			getWriter().write(ref.getObjectId().toString());
			
			getWcsXmlExporter().writeEndElement(idElementName);
			getWcsXmlExporter().writeEndElement(getPoolName() + idElementName);
		}
	}
	
	protected String getPoolName()
	{
		return poolName;
	}
	
	private int getObjectType()
	{
		return objectType;
	}
	
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		writeOptionalElementWithSameTag(baseObject, BaseObject.TAG_LABEL);
	}
	
	private int objectType;
	private String poolName;
}
