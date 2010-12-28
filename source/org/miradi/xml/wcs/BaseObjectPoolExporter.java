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

import java.awt.Point;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;

abstract public class BaseObjectPoolExporter extends ObjectPoolExporter
{
	public BaseObjectPoolExporter(XmpzXmlExporter wcsXmlExporterToUse, String containerNameToUse, int objectTypeToUse)
	{
		super(wcsXmlExporterToUse, containerNameToUse, objectTypeToUse);
	}

	@Override
	protected void writeObjectStartElement(BaseObject baseObject) throws Exception
	{
		getWcsXmlExporter().writeStartElementWithAttribute(getWriter(), getPoolName(), XmpzXmlConstants.ID, baseObject.getId().toString());
	}
	
	protected void writeProgressReportIds(BaseObject baseObject) throws Exception
	{
		writeOptionalIds(XmpzXmlConstants.PROGRESS_REPORT_IDS, XmpzXmlConstants.PROGRESS_REPORT, baseObject.getRefListData(BaseObject.TAG_PROGRESS_REPORT_REFS));
	}
	
	protected void writeProgressPercetIds(ORefList progressPercentRefs) throws Exception
	{
		writeOptionalIds(XmpzXmlConstants.PROGRESS_PERCENT_IDS, XmpzXmlConstants.PROGRESS_PERCENT, progressPercentRefs);
	}
	
	protected void writeExpenseAssignmentIds(BaseObject baseObject) throws Exception
	{
		writeOptionalIds(XmpzXmlConstants.EXPENSE_IDS, XmpzXmlConstants.EXPENSE_ASSIGNMENT, baseObject.getRefListData(BaseObject.TAG_EXPENSE_ASSIGNMENT_REFS));
	}
	
	protected void writeResourceAssignmentIds(BaseObject baseObject) throws Exception
	{
		writeOptionalIds(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, XmpzXmlConstants.RESOURCE_ASSIGNMENT, baseObject.getResourceAssignmentRefs());
	}
	
	protected void writeOptionalIndicatorIds(ORefList indicatorRefs) throws Exception
	{
		writeOptionalIndicatorIds("IndicatorIds", indicatorRefs);
	}

	protected void writeOptionalIndicatorIds(String idsElementName, ORefList indicatorRefs) throws Exception
	{
		writeOptionalIds(idsElementName, XmpzXmlConstants.INDICATOR, indicatorRefs);
	}
	
	protected String getFactorTypeName(Factor wrappedFactor)
	{
		if (Target.is(wrappedFactor))
			return XmpzXmlConstants.BIODIVERSITY_TARGET;
		
		if (Cause.is(wrappedFactor))
			return XmpzXmlConstants.CAUSE;
		
		return wrappedFactor.getTypeName();
	}
	
	protected void writeDiagramPoint(Point point) throws Exception
	{
		getWcsXmlExporter().writeStartElement(DIAGRAM_POINT_ELEMENT_NAME);
		getWcsXmlExporter().writeElement(getWriter(), X_ELEMENT_NAME, point.x);
		getWcsXmlExporter().writeElement(getWriter(), Y_ELEMENT_NAME, point.y);
		getWcsXmlExporter().writeEndElement(DIAGRAM_POINT_ELEMENT_NAME);
	}
}
