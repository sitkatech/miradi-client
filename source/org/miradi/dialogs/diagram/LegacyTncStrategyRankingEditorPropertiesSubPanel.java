/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;
import org.miradi.utils.ObjectsActionButton;

public class LegacyTncStrategyRankingEditorPropertiesSubPanel extends ObjectDataInputPanel
{
	public LegacyTncStrategyRankingEditorPropertiesSubPanel(Project project, ORef strategyRef, Actions actions)
	{
		super(project, strategyRef);
		
		if (hasLegacyTncRankings(strategyRef))
			addLegacyTncRankings(actions);
		
		updateFieldsFromProject();
	}

	private boolean hasLegacyTncRankings(ORef strategyRef)
	{
		if (strategyRef.isInvalid())
			return false;
		
		Strategy strategy = Strategy.find(getProject(), strategyRef);
		String legacyTncRanking = strategy.getData(Strategy.TAG_LEGACY_TNC_STRATEGY_RANKING);
		
		return legacyTncRanking.length() > 0;
	}

	private void addLegacyTncRankings(Actions actions)
	{
		ObjectsActionButton viewButton = createObjectsActionButton(actions.getObjectsAction(ActionViewLegacyTncStrategtyRanking.class), getPicker());
		ObjectsActionButton deleteButton = createObjectsActionButton(actions.getObjectsAction(ActionDeleteLegacyTncStrategyRanking.class), getPicker());
		
		addButtons(EAM.text("Legacy TNC Ratings"), new  PanelButton[]{viewButton, deleteButton} );
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Legacy TNC Strategy Editor Ranking Properties");
	}
}
