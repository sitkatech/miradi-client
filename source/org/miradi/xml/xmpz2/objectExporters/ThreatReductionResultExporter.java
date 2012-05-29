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

package org.miradi.xml.xmpz2.objectExporters;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.ThreatReductionResultSchema;
import org.miradi.xml.xmpz2.BaseObjectExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

public class ThreatReductionResultExporter extends BaseObjectExporter
{
	public ThreatReductionResultExporter(Xmpz2XmlWriter writerToUse)
	{
		super(writerToUse, ThreatReductionResultSchema.getObjectType());
	}
	
	@Override
	protected void writeFields(BaseObject baseObject, BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);
		
		ThreatReductionResult threatReductionReslt = (ThreatReductionResult) baseObject;
		final String objectName = baseObjectSchema.getObjectName();
		writeRelatedThreatId(objectName, threatReductionReslt.getRelatedThreatRef());
	}
	
	private void writeRelatedThreatId(String objectName, ORef relatedThreatRef) throws Exception
	{
		if (relatedThreatRef.isValid())
		{
			getWriter().writeStartElement(getPoolName() + RELATED_THREAT_ID);
			getWriter().writeStartElement(getPoolName() + THREAT_ID);
			getWriter().writeStartElement(THREAT_ID);
			getWriter().writeXmlText(relatedThreatRef.getObjectId().toString());
			getWriter().writeEndElement(THREAT_ID);
			getWriter().writeEndElement(getPoolName() + THREAT_ID);
			getWriter().writeEndElement(getPoolName() + RELATED_THREAT_ID);
		}
	}
	
	private String getPoolName()
	{
		return ThreatReductionResultSchema.OBJECT_NAME;
	}

	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF))
			return true;
		
		return super.doesFieldRequireSpecialHandling(tag);
	}
}
