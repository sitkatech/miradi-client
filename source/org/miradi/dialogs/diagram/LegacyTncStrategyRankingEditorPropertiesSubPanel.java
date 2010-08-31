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
package org.miradi.dialogs.diagram;

import org.miradi.actions.ActionDeleteLegacyTncStrategyRanking;
import org.miradi.actions.ActionViewLegacyTncStrategtyRanking;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;
import org.miradi.utils.ObjectsActionButton;

public class LegacyTncStrategyRankingEditorPropertiesSubPanel extends ObjectDataInputPanel
{
	public LegacyTncStrategyRankingEditorPropertiesSubPanel(Project project, ORef strategyRef, Actions actionsToUse)
	{
		super(project, strategyRef);
		
		actions = actionsToUse;
	
		buttonsPanel = new MiradiPanel();
		buttonsPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		ObjectsActionButton viewButton = createObjectsActionButton(actions.getObjectsAction(ActionViewLegacyTncStrategtyRanking.class), getPicker());
		ObjectsActionButton deleteButton = createObjectsActionButton(actions.getObjectsAction(ActionDeleteLegacyTncStrategyRanking.class), getPicker());

		buttonsPanel.add(viewButton);
		buttonsPanel.add(deleteButton);

		rebuildThisPropertiesPanel(strategyRef);
		
		updateFieldsFromProject();
	}

	@Override
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
	
		ORefList refList = new ORefList(orefsToUse);
		ORef strategyRef = refList.getRefForType(Strategy.getObjectType());
		rebuildThisPropertiesPanel(strategyRef);
	}
	
	private void rebuildThisPropertiesPanel(ORef strategyRef)
	{
		removeAll();
		if (hasLegacyTncRankings(strategyRef))
		{
			add(buttonsPanel);
		}			
	}

	private boolean hasLegacyTncRankings(ORef strategyRef)
	{
		if (strategyRef.isInvalid())
			return false;
		
		if (!Strategy.is(strategyRef))
			return false;
		
		Strategy strategy = Strategy.find(getProject(), strategyRef);
		String legacyTncRanking = strategy.getData(Strategy.TAG_LEGACY_TNC_STRATEGY_RANKING);
		
		return legacyTncRanking.length() > 0;
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Legacy TNC Strategy Editor Ranking Properties");
	}
	
	private MiradiPanel buttonsPanel;
	private Actions actions;
}
