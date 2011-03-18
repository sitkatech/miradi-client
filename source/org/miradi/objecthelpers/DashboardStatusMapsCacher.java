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

package org.miradi.objecthelpers;

import java.util.Collection;
import java.util.Vector;

import org.miradi.dialogs.dashboard.DashboardRowDefinition;
import org.miradi.dialogs.dashboard.DashboardRowDefinitionManager;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.questions.OpenStandardsDynamicProgressStatusQuestion;
import org.miradi.utils.CodeList;

public class DashboardStatusMapsCacher implements CommandExecutedListener
{
	public DashboardStatusMapsCacher(Project projectToUse) throws Exception
	{
		project = projectToUse;
	}
	
	public void enable()
	{
		getProject().addCommandExecutedListener(this);
	}
	
	public void disable()
	{
		getProject().removeCommandExecutedListener(this);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			invalidateAllCachedMaps();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void invalidateAllCachedMaps() throws Exception
	{
		cachedEffectiveStatusMap = null;
		cachedCalculatedStatusMap = null;
	}

	private Dashboard getDashboardSingletonObject()
	{
		ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType()); 
		return Dashboard.find(getProject(), dashboardRef);
	}

	public StringChoiceMap calculateEffectiveMap() throws Exception
	{
		if (cachedEffectiveStatusMap == null)
			rebuildCachedMaps();
		
		return cachedEffectiveStatusMap;
	}
	
	public StringChoiceMap calculateStatusMap() throws Exception
	{
		if (cachedCalculatedStatusMap == null)
			rebuildCachedMaps();
		
		return cachedCalculatedStatusMap;
	}
	
	private void rebuildCachedMaps() throws Exception
	{
		cachedEffectiveStatusMap = new StringChoiceMap();
		cachedCalculatedStatusMap = new StringChoiceMap();
		CodeList allThirdLevelCodes = getDashboardRowDefinitionManager().getThirdLevelCodes();
		for (int index = 0; index < allThirdLevelCodes.size(); ++index)
		{
			String thirdLevelCode = allThirdLevelCodes.get(index);
			Vector<DashboardRowDefinition> rowDefinitions = getDashboardRowDefinitionManager().getRowDefinitions(thirdLevelCode);
			
			String effectiveCode = getDashboardSingletonObject().getProgressChoiceMap().get(thirdLevelCode);
			String calculatedCode = computeStatusCodeFromStatistics(rowDefinitions);
			if (effectiveCode.equals(OpenStandardsDynamicProgressStatusQuestion.NOT_SPECIFIED_CODE))
				effectiveCode = calculatedCode;
			
			cachedEffectiveStatusMap.put(thirdLevelCode, effectiveCode);
			cachedCalculatedStatusMap.put(thirdLevelCode, calculatedCode);
		}
	}
	
	private String computeStatusCodeFromStatistics(Vector<DashboardRowDefinition> rowDefinitions)
	{
		Vector<String> pseudoValues = new Vector<String>();
		for (DashboardRowDefinition rowDefinition: rowDefinitions)
		{
			Vector<String> pseudoTags = rowDefinition.getPseudoTags();
			for (String pseudoTag: pseudoTags)
			{
				String pseudoDataValue = getDashboardSingletonObject().getPseudoData(pseudoTag);
				pseudoValues.add(pseudoDataValue);
			}
		}
		
		return getStatusCode(pseudoValues);
	}
	
	private String getStatusCode(Collection<String> rawDataValues)
	{
		if (rawDataValues.isEmpty())
			return OpenStandardsDynamicProgressStatusQuestion.NOT_STARTED_CODE;
		
		int valuesWithDataCount = 0;
		for (String rawData : rawDataValues)
		{
			if (rawData.length() > 0 && !rawData.equals("0"))
				++valuesWithDataCount;
		}
		
		if (valuesWithDataCount == 0)
			return OpenStandardsDynamicProgressStatusQuestion.NOT_STARTED_CODE;
			
		return OpenStandardsDynamicProgressStatusQuestion.IN_PROGRESS_CODE;
	}
	
	private DashboardRowDefinitionManager getDashboardRowDefinitionManager()
	{
		return getDashboardSingletonObject().getDashboardRowDefinitionManager();
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
	private StringChoiceMap cachedEffectiveStatusMap;
	private StringChoiceMap cachedCalculatedStatusMap;
}
