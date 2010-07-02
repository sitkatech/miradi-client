/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.planning.upperPanel;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.planning.propertiesPanel.AbstractWorkUnitsTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FundingSource;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.OptionalDouble;
import org.miradi.views.workplan.WorkPlanView;

public class RollupReportsWorkUnitsModel extends AbstractWorkUnitsTableModel
{
	public RollupReportsWorkUnitsModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse,	String treeModelIdentifierAsTagToUse) throws Exception
	{
		super(projectToUse, providerToUse, treeModelIdentifierAsTagToUse);
	}
	
	@Override
	protected OptionalDouble getOptionalDoubleAt(int row, int column)
	{
		try	
		{
			ViewData workPlanViewData = getProject().getViewData(WorkPlanView.getViewName());
			CodeList rollupReportsTypes = workPlanViewData.getBudgetRollupReportLevelTypes();
			DateUnit dateUnit = getDateUnit(column);
			TimePeriodCosts timePeriodCosts = getProjectTotalTimePeriodCostFor(dateUnit);
			ORefList objectHierarchy = getProvider().getObjectHiearchy(row, column);
			for (int index = 0; index < objectHierarchy.size(); ++index)
			{
				BaseObject baseObject = BaseObject.find(getProject(), objectHierarchy.get(index));
				if (ProjectResource.is(baseObject) && rollupReportsTypes.containsInt(baseObject.getType()))
					timePeriodCosts.filterProjectResources(new ORefSet(baseObject));
					
				if (FundingSource.is(baseObject) && rollupReportsTypes.containsInt(baseObject.getType()))
					timePeriodCosts.filterFundingSourcesWorkUnits(new ORefSet(baseObject));
				
				if (AccountingCode.is(baseObject) && rollupReportsTypes.containsInt(baseObject.getType()))
					timePeriodCosts.filterAccountingCodeWorkUnits(new ORefSet(baseObject));
			}
			
			return calculateValue(timePeriodCosts);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	
		return new OptionalDouble();
	}

	@Override
	protected CommandSetObjectData createAppendAssignmentCommand(BaseObject baseObjectForRowColumn, ORef assignmentRef)	throws Exception
	{
		throw new RuntimeException(EAM.text("Project Resource Work Units Table is not editbale."));
	}
	
	@Override
	public String getColumnGroupCode(int modelColumn)
	{
		return CustomPlanningColumnsQuestion.META_ROLLUP_REPORTS_WORK_UNITS_COLUMN_CODE;
	}

	@Override
	protected boolean isEditableModel()
	{
		return false;
	}

	@Override
	public String getUniqueTableModelIdentifier()
	{
		return getTreeModelIdentifierAsTag() + "." + UNIQUE_TABLE_MODEL_IDENTIFIER;
	}
	
	private static final String UNIQUE_TABLE_MODEL_IDENTIFIER = "RollupReportsWorkUnitsModel";
}
