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

import java.util.HashMap;
import java.util.Vector;

import org.martus.util.UnicodeWriter;
import org.miradi.objecthelpers.CategorizedQuantity;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;
import org.miradi.questions.StrategyFeasibilityQuestion;
import org.miradi.questions.StrategyImpactQuestion;
import org.miradi.questions.StrategyTaxonomyQuestion;
import org.miradi.utils.DateRange;
import org.miradi.utils.OptionalDouble;

public class StrategyPoolExporter extends FactorPoolExporter
{
	public StrategyPoolExporter(XmpzXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, STRATEGY, Strategy.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		Strategy strategy = (Strategy) baseObject;
		
		writeObjectiveIds(strategy);
		writeOptionalIds(Strategy.TAG_ACTIVITY_IDS, XmpzXmlConstants.ACTIVITY, strategy.getActivityRefs());
		writeOptionalElementWithSameTag(strategy, Strategy.TAG_STATUS);
		writeCodeElementSameAsTag(strategy, Strategy.TAG_TAXONOMY_CODE, new StrategyTaxonomyQuestion());		
		writeCodeElementSameAsTag(strategy, Strategy.TAG_IMPACT_RATING, new StrategyImpactQuestion());
		writeCodeElementSameAsTag(strategy, Strategy.TAG_FEASIBILITY_RATING, new StrategyFeasibilityQuestion());
		writeOptionalElementWithSameTag(strategy, Strategy.TAG_LEGACY_TNC_STRATEGY_RANKING);
		writeProgressReportIds(strategy);
		writeExpenseAssignmentIds(strategy);
		writeResourceAssignmentIds(strategy);
		writeIndicatorIds(strategy);
		writeOptionalCalculatedTimePeriodCosts(strategy);
	}

	protected void writeOptionalCalculatedTimePeriodCosts(Strategy strategy) throws Exception
	{
		TimePeriodCostsMap expenseAssignmentTimePeriodCostsMap = strategy.getExpenseAssignmentsTimePeriodCostsMap();
		TimePeriodCostsMap resourceAssignmentTimePeriodCostsMap = strategy.getResourceAssignmentsTimePeriodCostsMap();
		
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

	private void writeResourceAssignmentTimePeriodCosts(HashMap<DateUnit, TimePeriodCosts> dateUnitTimePeriodCostsMap) throws Exception
	{
		getWcsXmlExporter().writeStartElement(CALCULATED_WORK_UNITS_ENTRIES);
		for (DateUnit dateUnit : dateUnitTimePeriodCostsMap.keySet())
		{
			TimePeriodCosts timePeriodCosts = dateUnitTimePeriodCostsMap.get(dateUnit);
			Vector<CategorizedQuantity> categorizedQuantaties = timePeriodCosts.getWorkUnitCategorizedQuantities();
			writeCategorizedQuantaties(dateUnit, categorizedQuantaties);
		}
		
		getWcsXmlExporter().writeEndElement(CALCULATED_WORK_UNITS_ENTRIES);
	}

	private void writeCategorizedQuantaties(DateUnit dateUnit, Vector<CategorizedQuantity> categorizedQuantaties) throws Exception
	{
		for (CategorizedQuantity categorizedQuantity : categorizedQuantaties)
		{
			getWcsXmlExporter().writeStartElement(WORK_UNITS_ENTRY);
			
			exportId(categorizedQuantity.getResourceRef(), WORK_UNITS_ENTRY, RESOURCE_ID);
			exportId(categorizedQuantity.getFundingSourceRef(), WORK_UNITS_ENTRY, FUNDING_SOURCE_ID);
			exportId(categorizedQuantity.getAccountingCodeRef(), WORK_UNITS_ENTRY, ACCOUNTING_CODE_ID);
			exportId(categorizedQuantity.getCategoryOneRef(), WORK_UNITS_ENTRY, BUDGET_CATEGORY_ONE_ID);
			exportId(categorizedQuantity.getCategoryTwoRef(), WORK_UNITS_ENTRY, BUDGET_CATEGORY_TWO_ID);
			
			//FIXME urgent - Needs to export details
			
			getWcsXmlExporter().writeEndElement(WORK_UNITS_ENTRY);
		}
	}

	private void writeExpenseAssignmentTimePeriodCosts(HashMap<DateUnit, TimePeriodCosts> dateUnitTimePeriodCostsMap)
	{
		//FIXME urgent - Needs to export expense entries
	}
}
