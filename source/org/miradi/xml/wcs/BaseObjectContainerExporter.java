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

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;

abstract public class BaseObjectContainerExporter extends ObjectContainerExporter
{
	public BaseObjectContainerExporter(WcsXmlExporter wcsXmlExporterToUse, String containerNameToUse, int objectTypeToUse)
	{
		super(wcsXmlExporterToUse, containerNameToUse, objectTypeToUse);
	}

	@Override
	protected void writeObjectStartElement(BaseObject baseObject) throws Exception
	{
		getWcsXmlExporter().writeStartElementWithAttribute(getWriter(), getContainerName(), WcsXmlConstants.ID, baseObject.getId().toString());
	}
	
	protected void writeProgressReportIds(BaseObject baseObject) throws Exception
	{
		writeIds(WcsXmlConstants.PROGRESS_REPORT_IDS, WcsXmlConstants.PROGRESS_REPORT, baseObject.getProgressReportRefs());
	}
	
	protected void writeProgressPercetIds(ORefList progressPercentRefs) throws Exception
	{
		writeIds(WcsXmlConstants.PROGRESS_PERCENT_IDS, WcsXmlConstants.PROGRESS_PERCENT, progressPercentRefs);
	}
	
	protected void writeExpenseAssignmentIds(BaseObject baseObject) throws Exception
	{
		writeIds(WcsXmlConstants.EXPENSE_IDS, WcsXmlConstants.EXPENSE_ASSIGNMENT, baseObject.getExpenseAssignmentRefs());
	}
	
	protected void writeResourceAssignmentIds(BaseObject baseObject) throws Exception
	{
		writeIds(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, WcsXmlConstants.RESOURCE_ASSIGNMENT, baseObject.getResourceAssignmentRefs());
	}
	
	protected void writeIndicatorIds(ORefList indicatorRefs) throws Exception
	{
		writeIndicatorIds("IndicatorIds", indicatorRefs);
	}

	protected void writeIndicatorIds(String idsElementName, ORefList indicatorRefs) throws Exception
	{
		writeIds(idsElementName, WcsXmlConstants.INDICATOR, indicatorRefs);
	}
}
