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
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.Color;

import org.miradi.dialogs.base.ChoiceItemTableModel;
import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.utils.OptionalDouble;

abstract public class PlanningViewAbstractTreeTableSyncedTableModel extends EditableObjectTableModel implements ChoiceItemTableModel
{
	public PlanningViewAbstractTreeTableSyncedTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse) throws Exception
	{
		super(projectToUse);
		
		project = projectToUse;
		objectProvider = providerToUse;
		resourceRefsFilter = new ORefSet();
	}
	
	public int getRowCount()
	{
		return getRowColumnObjectProvider().getRowCount();
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getRowColumnObjectProvider().getBaseObjectForRowColumn(row, column);
	}
	
	@Override
	public void setObjectRefs(ORefList hierarchyToSelectedRef)
	{
	}
	
	@Override
	public int getProportionShares(int row)
	{
		return getRowColumnObjectProvider().getProportionShares(row);
	}

	@Override
	public boolean areBudgetValuesAllocated(int row)
	{
		return getRowColumnObjectProvider().areBudgetValuesAllocated(row);
	}
	
	public boolean isCurrencyColumn(int column)
	{
		return false;
	}
	
	@Override
	public boolean isChoiceItemColumn(int column)
	{
		return false;
	}
	
	public boolean isProgressColumn(int column)
	{
		return false;
	}
	
	public boolean isDateUnitColumn(int column)
	{
		return false;
	}
	
	public boolean isPlannedWhenColumn(int modelColumn)
	{
		return false;
	}
	
	public boolean isAssignedWhenColumn(int modelColumn)
	{
		return false;
	}

	public boolean isFormattedColumn(int modelColumn)
	{
		return false;
	}
	
	@Override
	public String getColumnGroupCode(int modelColumn)
	{
		return getColumnTag(modelColumn);
	}
	
	public String getTagForCell(int objectType, int modelColumn)
	{
		return getColumnGroupCode(modelColumn);
	}
	
	public Class getCellQuestion(int row, int modelColumn)
	{
		return null;
	}
	
	public boolean isColumnCollapsible(int modelColumn)
	{
		return false;
	}

	public boolean isColumnExpandable(int modelColumn)
	{
		return false;
	}
	
	public DateUnit getDateUnit(int modelColumn)
	{
		throw new RuntimeException("This method should be overridden by subclass.");
	}

	public void respondToExpandOrCollapseColumnEvent(int modelColumnIndex) throws Exception
	{
	}
	
	public boolean isFullTimeEmployeeFractionAvailable(int row, int modelColumn)
	{
		return false;
	}
	
	public void updateFullTimeEmployeeDaysPerYearFraction(int row, int modelColumn, double percent) throws Exception
	{
	}
	
	public OptionalDouble getCellFraction(int row, int modelColumn) throws Exception
	{
		return new OptionalDouble();
	}
	
	protected ORefSet getResourcesFilter()
	{
		return resourceRefsFilter;
	}

	public void setResourcesFilter(ORefSet resourceRefFiltersToUse)
	{
		resourceRefsFilter = resourceRefFiltersToUse;
	}
	
	public TimePeriodCosts calculateTimePeriodPlannedCosts(BaseObject baseObject, DateUnit dateUnit, String workPlanBudgetMode) throws Exception
	{
		return calculateTimePeriodPlannedCostsMap(baseObject, workPlanBudgetMode).calculateTimePeriodCosts(dateUnit);
	}

	public TimePeriodCostsMap calculateTimePeriodPlannedCostsMap(BaseObject baseObject, String workPlanBudgetMode) throws Exception
	{
		if (ProjectMetadata.is(baseObject))
			return getProject().getTimePeriodCostsMapsCache().calculateProjectPlannedTotals(workPlanBudgetMode);

		if (ConceptualModelDiagram.is(baseObject.getRef()) || ResultsChainDiagram.is(baseObject.getRef()))
			return getProject().getTimePeriodCostsMapsCache().calculateDiagramObjectPlannedTotals((DiagramObject) baseObject, workPlanBudgetMode);

		if (Assignment.is(baseObject))
			return new TimePeriodCostsMap();

		return getTotalTimePeriodPlannedCostsMap(baseObject);
	}

	protected TimePeriodCostsMap getTotalTimePeriodPlannedCostsMap(BaseObject baseObject) throws Exception
	{
		return getProject().getTimePeriodCostsMapsCache().getTotalTimePeriodPlannedCostsMap(baseObject);
	}

	public TimePeriodCosts calculateTimePeriodAssignedCosts(BaseObject baseObject, DateUnit dateUnit, String workPlanBudgetMode) throws Exception
	{
		return calculateTimePeriodAssignedCostsMap(baseObject, workPlanBudgetMode).calculateTimePeriodCosts(dateUnit);
	}

	public TimePeriodCostsMap calculateTimePeriodAssignedCostsMap(BaseObject baseObject, String workPlanBudgetMode) throws Exception
	{
		if (ProjectMetadata.is(baseObject))
			return getProject().getTimePeriodCostsMapsCache().calculateProjectAssignedTotals(workPlanBudgetMode);

		if (ConceptualModelDiagram.is(baseObject.getRef()) || ResultsChainDiagram.is(baseObject.getRef()))
			return getProject().getTimePeriodCostsMapsCache().calculateDiagramObjectAssignedTotals((DiagramObject) baseObject, workPlanBudgetMode);

		return getTotalTimePeriodAssignedCostsMap(baseObject);
	}

	protected TimePeriodCostsMap getTotalTimePeriodAssignedCostsMap(BaseObject baseObject) throws Exception
	{
		return getProject().getTimePeriodCostsMapsCache().getTotalTimePeriodAssignedCostsMap(baseObject);
	}

	protected RowColumnBaseObjectProvider getRowColumnObjectProvider()
	{
		return objectProvider;
	}
	
	abstract public Color getCellBackgroundColor(int column);

	protected Project project;
	private RowColumnBaseObjectProvider objectProvider;
	protected ORefSet resourceRefsFilter;
}
