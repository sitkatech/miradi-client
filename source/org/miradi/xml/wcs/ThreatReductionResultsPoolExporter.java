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
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ThreatReductionResult;

public class ThreatReductionResultsPoolExporter extends FactorPoolExporter
{
	public ThreatReductionResultsPoolExporter(XmpzXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, THREAT_REDUCTION_RESULTS, ThreatReductionResult.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		ThreatReductionResult threatReductionResult = (ThreatReductionResult) baseObject;
		writeOptionalThreatId(threatReductionResult.getRelatedThreatRef());
		writeObjectiveIds(threatReductionResult);
		writeIndicatorIds(threatReductionResult);
	}

	private void writeOptionalThreatId(ORef relatedThreatRef) throws Exception
	{
		if (relatedThreatRef.isValid())
		{
			getWcsXmlExporter().writeStartElement(getPoolName() + RELATED_THREAT_ID);
			getWcsXmlExporter().writeStartElement(getPoolName() + THREAT_ID);
			getWcsXmlExporter().writeStartElement(THREAT_ID);
			getWcsXmlExporter().writeXmlEncodedData(getWriter(), relatedThreatRef.getObjectId().toString());
			getWcsXmlExporter().writeEndElement(THREAT_ID);
			getWcsXmlExporter().writeEndElement(getPoolName() + THREAT_ID);
			getWcsXmlExporter().writeEndElement(getPoolName() + RELATED_THREAT_ID);
		}
	}
}
