/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.xml.xmpz2;

import java.util.Set;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.utils.DateRange;
import org.miradi.utils.OptionalDouble;

public class BaseObjectExporter implements Xmpz2XmlConstants
{
	public BaseObjectExporter(final Xmpz2XmlWriter writerToUse, final int objectTypeToUse)
	{
		writer = writerToUse;
		objectType = objectTypeToUse;
	}
	
	public void writeBaseObjectDataSchemaElement(final BaseObject baseObject) throws Exception
	{
		BaseObjectSchema baseObjectSchema = baseObject.getSchema();
		writeStartElement(baseObject);
		writeFields(baseObject, baseObjectSchema);
		getWriter().writeObjectElementEnd(baseObjectSchema);
	}

	protected void writeStartElement(final BaseObject baseObject) throws Exception
	{
		getWriter().writeObjectStartElementWithAttribute(baseObject, ID, baseObject.getId().toString());
	}
	
	protected void writeFields(final BaseObject baseObject,	BaseObjectSchema baseObjectSchema) throws Exception
	{
		for(AbstractFieldSchema fieldSchema : baseObjectSchema)
		{
			if (doesFieldRequireSpecialHandling(fieldSchema.getTag()))
				continue;
			
			if (shouldOmitField(fieldSchema.getTag()))
				continue;
			
			writeField(baseObject, fieldSchema);
		}
	}
	
	protected boolean shouldOmitField(String tag)
	{
		return false;
	}

	private void writeField(final BaseObject baseObject, final AbstractFieldSchema fieldSchema) throws Exception
	{
		getWriter().writeFieldElement(baseObject, fieldSchema);
	}

	protected boolean doesFieldRequireSpecialHandling(final String tag)
	{
		return false;
	}
	
	public String getExporterContainerName(final int objectTypeToUse)
	{
		final String internalObjectTypeName = getProject().getObjectManager().getInternalObjectTypeName(objectTypeToUse);
		
		return internalObjectTypeName;
	}

	protected Xmpz2XmlWriter getWriter()
	{
		return writer;
	}
	
	protected Project getProject()
	{
		return getWriter().getProject();
	}
	
	protected String getDiagramFactorWrappedFactorTypeName(Factor wrappedFactor)
	{
		if (Target.is(wrappedFactor))
			return wrappedFactor.getSchema().getXmpz2ElementName();
		
		if (Cause.is(wrappedFactor))
			return wrappedFactor.getSchema().getXmpz2ElementName();
		
		if (HumanWelfareTarget.is(wrappedFactor))
			return HUMAN_WELFARE_TARGET;
		
		return wrappedFactor.getTypeName();
	}
	
    protected void writeOptionalCalculatedTimePeriodCosts(BaseObject baseObject, BaseObjectSchema baseObjectSchema) throws Exception
    {
        // we need to check if any of these optional values should be exported before we write out the 2 container elements that surround them
        final TimePeriodCostsMap totalBudgetCostsTimePeriodCostsMap = baseObject.getTotalTimePeriodCostsMap();
        final TimePeriodCosts totalBudgetCost = totalBudgetCostsTimePeriodCostsMap.calculateTotalBudgetCost();
        final DateRange projectPlanningDateRange = getProject().getProjectCalendar().getProjectPlanningDateRange();
        final DateRange totalDateRange = totalBudgetCostsTimePeriodCostsMap.getRolledUpDateRange(projectPlanningDateRange);
        final OptionalDouble totalCostValue = totalBudgetCost.calculateTotalCost(getProject());
        final OptionalDouble calculatedWorkCostTotal = totalBudgetCost.calculateResourcesTotalCost(getProject());
        final Set<ORef> calculatedWho = totalBudgetCost.getWorkUnitsRefSetForType(ProjectResourceSchema.getObjectType());
        final OptionalDouble calculatedExpenseTotal = totalBudgetCost.getTotalExpense();
        final OptionalDouble calculatedWorkUnits = totalBudgetCost.getTotalWorkUnits();
        final TimePeriodCostsMap expenseAssignmentTimePeriodCostsMap = baseObject.getExpenseAssignmentsTimePeriodCostsMap();
        final TimePeriodCostsMap resourceAssignmentTimePeriodCostsMap = baseObject.getResourceAssignmentsTimePeriodCostsMap();
        final boolean haveExpenseTotalCost = expenseAssignmentTimePeriodCostsMap.calculateTotalBudgetCost(getProject()).hasValue();
        final boolean haveResourceTotalCost = resourceAssignmentTimePeriodCostsMap.calculateTotalBudgetCost(getProject()).hasValue();

        boolean haveValueToWrite = totalDateRange != null
                                || totalCostValue.hasValue()
                                || calculatedWorkCostTotal.hasValue()
                                || !calculatedWho.isEmpty()
                                || calculatedExpenseTotal.hasValue()
                                || calculatedWorkUnits.hasValue()
                                || haveExpenseTotalCost
                                || haveResourceTotalCost;

        if(haveValueToWrite)
        {
            getWriter().writeStartElement(baseObjectSchema.getObjectName() + TIME_PERIOD_COSTS);
            getWriter().writeStartElement(TIME_PERIOD_COSTS);

            if(totalDateRange != null)
            {
                getWriter().writeElement(CALCULATED_START_DATE, totalDateRange.getStartDate().toIsoDateString());
                getWriter().writeElement(CALCULATED_END_DATE, totalDateRange.getEndDate().toIsoDateString());
            }
            writeOptionalCost(CALCULATED_TOTAL_BUDGET_COST, totalCostValue);
            getWriter().writeElement(CALCULATED_WORK_COST_TOTAL, calculatedWorkCostTotal);
            writeResourceIds(CALCULATED_WHO, calculatedWho);
            writeOptionalCost(CALCULATED_EXPENSE_TOTAL, calculatedExpenseTotal);
            writeOptionalCost(CALCULATED_WORK_UNITS_TOTAL, calculatedWorkUnits);
            if (haveExpenseTotalCost)
            {
                new ExpenseTimePeriodCostsWriter2(getWriter()).writeTimePeriodCosts(expenseAssignmentTimePeriodCostsMap.getDateUnitTimePeriodCostsMap());
            }
            if (haveResourceTotalCost)
            {
                new WorkUnitsTimePeriodCostsWriter2(getWriter()).writeTimePeriodCosts(resourceAssignmentTimePeriodCostsMap.getDateUnitTimePeriodCostsMap());
            }

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
	
	private void writeOptionalCost(final String totalCostElementName, final OptionalDouble totalCost) throws Exception
	{
		if (totalCost.hasValue())
		{
			getWriter().writeElement(totalCostElementName, totalCost.toString());
		}
	}
	
	public int getObjectType()
	{
		return objectType;
	}

	private Xmpz2XmlWriter writer;
	private int objectType;
}
