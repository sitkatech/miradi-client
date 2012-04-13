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

import java.util.Set;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;
import org.miradi.questions.StrategyFeasibilityQuestion;
import org.miradi.questions.StrategyImpactQuestion;
import org.miradi.questions.StrategyTaxonomyQuestion;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.utils.DateRange;
import org.miradi.utils.OptionalDouble;
import org.miradi.xml.xmpz2.BaseObjectExporter;
import org.miradi.xml.xmpz2.ExpenseTimePeriodCostsWriter2;
import org.miradi.xml.xmpz2.WorkUnitsTimePeriodCostsWriter2;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

public class StrategyExporter extends BaseObjectExporter
{
	public StrategyExporter(Xmpz2XmlWriter writerToUse)
	{
		super(writerToUse, StrategySchema.getObjectType());
	}
	
	@Override
	protected void writeFields(BaseObject baseObject, BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);
		
		final Strategy strategy = (Strategy) baseObject;
		writeMethodRefs(baseObjectSchema, strategy);
		writeOptionalCalculatedTimePeriodCosts(strategy, baseObjectSchema);
		
		getWriter().writeNonOptionalCodeElement(baseObjectSchema.getObjectName(), Strategy.TAG_TAXONOMY_CODE, new StrategyTaxonomyQuestion(), strategy.getTaxonomyCode());
		getWriter().writeNonOptionalCodeElement(baseObjectSchema.getObjectName(), Strategy.TAG_IMPACT_RATING, new StrategyImpactQuestion(), strategy.getChoiceItemData(Strategy.TAG_IMPACT_RATING).getCode());
		getWriter().writeNonOptionalCodeElement(baseObjectSchema.getObjectName(), Strategy.TAG_FEASIBILITY_RATING, new StrategyFeasibilityQuestion(), strategy.getChoiceItemData(Strategy.TAG_FEASIBILITY_RATING).getCode());		
	}
	
	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(Strategy.TAG_ACTIVITY_IDS))
			return true;
		
		if (tag.equals(Strategy.TAG_TAXONOMY_CODE))
			return true;
		
		if (tag.equals(Strategy.TAG_IMPACT_RATING))
			return true;
		
		if (tag.equals(Strategy.TAG_FEASIBILITY_RATING))
			return true;
		
		return super.doesFieldRequireSpecialHandling(tag);
	}
	
	private void writeMethodRefs(BaseObjectSchema baseObjectSchema, final Strategy strategy) throws Exception
	{
		getWriter().writeReflist(baseObjectSchema.getObjectName() + SORTED_ACTIVITY_IDS, ACTIVITY, strategy.getActivityRefs());
	}
	
	protected void writeOptionalCalculatedTimePeriodCosts(BaseObject baseObject, BaseObjectSchema baseObjectSchema) throws Exception
	{
		TimePeriodCostsMap totalBudgetCostsTimePeriodCostsMap = baseObject.getTotalTimePeriodCostsMap();
		TimePeriodCosts totalBudgetCost = totalBudgetCostsTimePeriodCostsMap.calculateTotalBudgetCost();
		
		final OptionalDouble totalCostValue = totalBudgetCost.calculateTotalCost(getProject());
		if (totalCostValue.hasValue())
		{
			final DateRange projectPlanningDateRange = getProject().getProjectCalendar().getProjectPlanningDateRange();
			DateRange totalDateRange = totalBudgetCostsTimePeriodCostsMap.getRolledUpDateRange(projectPlanningDateRange);
			getWriter().writeStartElement(baseObjectSchema.getObjectName() + TIME_PERIOD_COSTS);

			getWriter().writeStartElement(TIME_PERIOD_COSTS);
			getWriter().writeElement(CALCULATED_START_DATE, totalDateRange.getStartDate().toIsoDateString());
			getWriter().writeElement(CALCULATED_END_DATE, totalDateRange.getEndDate().toIsoDateString());
			getWriter().writeElement(CALCULATED_TOTAL_BUDGET_COST, totalCostValue.toString());

			writeResourceIds(CALCULATED_WHO, totalBudgetCost.getWorkUnitsRefSetForType(ProjectResourceSchema.getObjectType()));
			writeOptionalTotalCost(CALCULATED_EXPENSE_TOTAL, totalBudgetCost.getTotalExpense());

			writeOptionalTotalCost(CALCULATED_WORK_UNITS_TOTAL, totalBudgetCost.getTotalWorkUnits());			

			TimePeriodCostsMap expenseAssignmentTimePeriodCostsMap = baseObject.getExpenseAssignmentsTimePeriodCostsMap();
			TimePeriodCostsMap resourceAssignmentTimePeriodCostsMap = baseObject.getResourceAssignmentsTimePeriodCostsMap();
			new ExpenseTimePeriodCostsWriter2(getWriter()).writeTimePeriodCosts(expenseAssignmentTimePeriodCostsMap.getDateUnitTimePeriodCostsMap());
			new WorkUnitsTimePeriodCostsWriter2(getWriter()).writeTimePeriodCosts(resourceAssignmentTimePeriodCostsMap.getDateUnitTimePeriodCostsMap());

			getWriter().writeEndElement(TIME_PERIOD_COSTS);
			getWriter().writeEndElement(baseObjectSchema.getObjectName() + TIME_PERIOD_COSTS);
		}
	}
	
	private void writeResourceIds(String elementName, Set<ORef> resourceRefs) throws Exception
	{
		if(resourceRefs.isEmpty())
			return;
		
		getWriter().writeStartElement(elementName);
		for(ORef resourceRef : resourceRefs)
		{
			getWriter().writeElement("", RESOURCE_ID, resourceRef.getObjectId().toString());
		}
		getWriter().writeEndElement(elementName);
	}
	
	private void writeOptionalTotalCost(final String totalCostElementName, final OptionalDouble totalCost) throws Exception
	{
		if (totalCost.hasValue())
		{
			getWriter().writeElement(totalCostElementName, totalCost.toString());
		}
	}
}
