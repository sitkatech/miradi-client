/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.planning;

import org.miradi.project.Project;
import org.miradi.questions.ActionTreeConfigurationQuestion;
import org.miradi.utils.CodeList;

//FIXME urgent - this class is still under construction
public class ActionPlanMultiRowColumnProvider extends AbstractPlanningTreeRowColumnProvider
{
	public ActionPlanMultiRowColumnProvider(Project projectToUse)
	{
		super(projectToUse);
		
		subView1 = new ActionPlanSubViewObjectiveBasedRowColumnProvider(getProject());
		subView2 = new ActionPlanSubViewStrategyBasedRowColumnProvider(getProject());
	}

	public CodeList getColumnCodesToShow() throws Exception
	{
		return getSubViewProvider().getColumnCodesToShow();
	}

	public CodeList getRowCodesToShow() throws Exception
	{
		return getSubViewProvider().getRowCodesToShow();
	}
	
	public boolean shouldIncludeResultsChain() throws Exception
	{
		return true;
	}

	public boolean shouldIncludeConceptualModelPage() throws Exception
	{
		return true;
	}
	
	@Override
	public boolean doObjectivesContainStrategies() throws Exception
	{
		return getSubViewProvider().doObjectivesContainStrategies();
	}
	
	private AbstractPlanningTreeRowColumnProvider getSubViewProvider() throws Exception
	{
		String actionTreeConfigurationCode = getProject().getCurrentViewData().getTreeConfigurationChoice();
		if (actionTreeConfigurationCode.equals(ActionTreeConfigurationQuestion.OBJECTIVES_CONTAIN_STRATEGIES_CODE))
			return subView1;

			else if (actionTreeConfigurationCode.equals(ActionTreeConfigurationQuestion.STRATEGIES_CONTAIN_OBJECTIVES_CODE))
			return subView2;
		
		throw new RuntimeException("Could not find a matching sub view row column provider");
	}
	
	private AbstractPlanningTreeRowColumnProvider subView1;
	private AbstractPlanningTreeRowColumnProvider subView2;
}
