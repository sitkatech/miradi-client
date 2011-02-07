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

import java.util.HashMap;

import org.miradi.project.Project;
import org.miradi.questions.ActionTreeConfigurationQuestion;
import org.miradi.utils.CodeList;

public class ActionPlanMultiRowColumnProvider extends AbstractPlanningTreeRowColumnProvider
{
	public ActionPlanMultiRowColumnProvider(Project projectToUse)
	{
		super(projectToUse);
	
		createMap();
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
		return getSubViewProvider().shouldIncludeResultsChain();
	}

	public boolean shouldIncludeConceptualModelPage() throws Exception
	{
		return getSubViewProvider().shouldIncludeConceptualModelPage();
	}
	
	@Override
	public boolean doObjectivesContainStrategies() throws Exception
	{
		return getSubViewProvider().doObjectivesContainStrategies();
	}
	
	private AbstractPlanningTreeRowColumnProvider getSubViewProvider() throws Exception
	{
		String actionTreeConfigurationCode = getProject().getCurrentViewData().getTreeConfigurationChoice();
		
		return codeToProviderMap.get(actionTreeConfigurationCode);
	}
	
	private void createMap()
	{
		codeToProviderMap = new HashMap<String, AbstractPlanningTreeRowColumnProvider>();
		codeToProviderMap.put(ActionTreeConfigurationQuestion.OBJECTIVES_CONTAIN_STRATEGIES_CODE, new ActionPlanSubViewObjectiveBasedRowColumnProvider(getProject()));
		codeToProviderMap.put(ActionTreeConfigurationQuestion.STRATEGIES_CONTAIN_OBJECTIVES_CODE, new ActionPlanSubViewStrategyBasedRowColumnProvider(getProject()));
	}
	
	private HashMap<String, AbstractPlanningTreeRowColumnProvider> codeToProviderMap;
}
