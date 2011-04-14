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
import java.util.HashMap;

import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;
import org.miradi.utils.DateRange;
import org.miradi.utils.OptionalDouble;

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
	
	protected void writeWrappedFactorId(Factor wrappedFactor) throws Exception
	{
		getWcsXmlExporter().writeStartElement(DIAGRAM_FACTOR + WRAPPED_FACTOR_ID_ELEMENT_NAME);
		writeWrappedFactorIdElement(wrappedFactor);
		getWcsXmlExporter().writeEndElement(DIAGRAM_FACTOR + WRAPPED_FACTOR_ID_ELEMENT_NAME);
	}

	private void writeWrappedFactorIdElement(Factor wrappedFactor) throws Exception
	{
		getWcsXmlExporter().writeStartElement(WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME);
		
		String factorTypeName = getFactorTypeName(wrappedFactor);
		getWcsXmlExporter().writeElement(factorTypeName, ID_ELEMENT_NAME, wrappedFactor.getFactorId().toString());
		
		getWcsXmlExporter().writeEndElement(WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME);
	}
	
	protected void writeFactorIds(String idsElementName, ORefList refs) throws Exception
	{
		writeFactorIds(getPoolName(), idsElementName, refs);
	}
	
	private void writeFactorIds(String parentElementName, String childElementName, ORefList refList) throws Exception
	{
		getWcsXmlExporter().writeStartElement(getWriter(), getWcsXmlExporter().createParentAndChildElementName(parentElementName, childElementName));
		for (int index = 0; index < refList.size(); ++index)
		{
			Factor factor = Factor.findFactor(getProject(), refList.get(index));
			writeWrappedFactorIdElement(factor);
		}
		
		getWcsXmlExporter().writeEndElement(getWriter(), getWcsXmlExporter().createParentAndChildElementName(parentElementName, childElementName));
	}
	
	protected void writeDiagramPoint(Point point) throws Exception
	{
		getWcsXmlExporter().writeStartElement(DIAGRAM_POINT_ELEMENT_NAME);
		getWcsXmlExporter().writeElement(getWriter(), X_ELEMENT_NAME, point.x);
		getWcsXmlExporter().writeElement(getWriter(), Y_ELEMENT_NAME, point.y);
		getWcsXmlExporter().writeEndElement(DIAGRAM_POINT_ELEMENT_NAME);
	}
	
	protected void writeOptionalCalculatedTimePeriodCosts(BaseObject baseObject) throws Exception
	{
		TimePeriodCostsMap expenseAssignmentTimePeriodCostsMap = baseObject.getExpenseAssignmentsTimePeriodCostsMap();
		TimePeriodCostsMap resourceAssignmentTimePeriodCostsMap = baseObject.getResourceAssignmentsTimePeriodCostsMap();
		
		OptionalDouble totalExpenseAssignmentCost = expenseAssignmentTimePeriodCostsMap.calculateTotalBudgetCost(getProject());
		OptionalDouble totalResourceAssignmentCost = resourceAssignmentTimePeriodCostsMap.calculateTotalBudgetCost(getProject());
		if (totalExpenseAssignmentCost.hasValue() || totalResourceAssignmentCost.hasValue())
		{
			getWcsXmlExporter().writeStartElement(getPoolName() + TIME_PERIOD_COSTS);
			
			getWcsXmlExporter().writeStartElement(TIME_PERIOD_COSTS);
			getWcsXmlExporter().writeElement(getWriter(), CALCULATED_EXPENSE_TOTAL, totalExpenseAssignmentCost.toString());
			getWcsXmlExporter().writeElement(getWriter(), CALCULATED_WORK_UNITS_TOTAL, totalResourceAssignmentCost.toString());
			
			DateRange expenseTotalDateRange = expenseAssignmentTimePeriodCostsMap.getRolledUpDateRange(getProject().getProjectCalendar().getProjectPlanningDateRange());
			getWcsXmlExporter().writeElement(getWriter(), CALCULATED_START_DATE, expenseTotalDateRange.getStartDate().toIsoDateString());
			getWcsXmlExporter().writeElement(getWriter(), CALCULATED_END_DATE, expenseTotalDateRange.getEndDate().toIsoDateString());
			writeExpenseAssignmentTimePeriodCosts(expenseAssignmentTimePeriodCostsMap.getDateUnitTimePeriodCostsMap());
			writeResourceAssignmentTimePeriodCosts(resourceAssignmentTimePeriodCostsMap.getDateUnitTimePeriodCostsMap());
			
			
			getWcsXmlExporter().writeEndElement(TIME_PERIOD_COSTS);
			
			getWcsXmlExporter().writeEndElement(getPoolName() + TIME_PERIOD_COSTS);
		}
	}
	
	private void writeExpenseAssignmentTimePeriodCosts(HashMap<DateUnit, TimePeriodCosts> dateUnitTimePeriodCostsMap) throws Exception
	{
		final ExpenseEntryWriter timePeriodCostsWriter = new ExpenseEntryWriter(getWcsXmlExporter());
		timePeriodCostsWriter.writeExpenseAssignmentTimePeriodCosts(dateUnitTimePeriodCostsMap);
	}

	private void writeResourceAssignmentTimePeriodCosts(HashMap<DateUnit, TimePeriodCosts> dateUnitTimePeriodCostsMap) throws Exception
	{
		final WorkUnitsEntryWriter timePeriodCostsWriter = new WorkUnitsEntryWriter(getWcsXmlExporter());
		timePeriodCostsWriter.writeResourceAssignmentTimePeriodCosts(dateUnitTimePeriodCostsMap);
	}
}
