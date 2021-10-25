/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogs.planning;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;

abstract public class AbstractPlanningTreeRowColumnProvider extends AbstractPlanningViewRowColumnProvider implements PlanningTreeRowColumnProvider
{
	public AbstractPlanningTreeRowColumnProvider(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public boolean shouldPutTargetsAtTopLevelOfTree() throws Exception
	{
		return false;
	}
	
	public String getWorkPlanBudgetMode() throws Exception
	{
		return WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE;
	}

	public String getDiagramFilter() throws Exception
	{
		return "";
	}

	protected PlanningTreeRowColumnProvider getCurrentCustomization() throws Exception
	{
		ViewData viewData = getCurrentViewData();
		ORef customizationRef = viewData.getORef(ViewData.TAG_TREE_CONFIGURATION_REF);
		if(customizationRef.isInvalid())
			return null;
		PlanningTreeRowColumnProvider customization = (PlanningTreeRowColumnProvider)viewData.getProject().findObject(customizationRef);
		return customization;
	}
}
