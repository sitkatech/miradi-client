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

package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.project.ProjectTotalCalculator;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.utils.OptionalDouble;

public class ProjectResourceWorkUnitsTableModel extends AbstractWorkUnitsTableModel
{
	public ProjectResourceWorkUnitsTableModel(Project projectToUse,	RowColumnBaseObjectProvider providerToUse, String treeModelIdentifierAsTagToUse) throws Exception
	{
		super(projectToUse, providerToUse, treeModelIdentifierAsTagToUse);
	}
	
	@Override
	protected OptionalDouble getOptionalDoubleData(BaseObject baseObject, DateUnit dateUnit) throws Exception
	{
		ProjectTotalCalculator projectTotalCalculator = getProject().getProjectTotalCalculator();
		TimePeriodCostsMap totalProject = projectTotalCalculator.calculateProjectTotals();
		TimePeriodCosts timePeriodCosts = totalProject.calculateTimePeriodCosts(dateUnit);
		timePeriodCosts.filterProjectResources(new ORefSet(baseObject));
		
		return calculateValue(timePeriodCosts);
	}

	@Override
	protected CommandSetObjectData createAppendAssignmentCommand(BaseObject baseObjectForRowColumn, ORef assignmentRef)	throws Exception
	{
		throw new RuntimeException(EAM.text("Project Resource Work Units Table is not editbale."));
	}

	@Override
	protected boolean isEditableModel()
	{
		return false;
	}
	
	@Override
	public String getColumnGroupCode(int modelColumn)
	{
		return CustomPlanningColumnsQuestion.META_PROJECT_RESOURCE_WORK_UNITS_COLUMN_CODE;
	}

	@Override
	public String getUniqueTableModelIdentifier()
	{
		return getTreeModelIdentifierAsTag() + "." + UNIQUE_TABLE_MODEL_IDENTIFIER;
	}
	
	private static final String UNIQUE_TABLE_MODEL_IDENTIFIER = "ProjectResourceWorkUnitsTableModel";
}
