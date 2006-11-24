/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Strategy;

public class DiagramStrategy extends DiagramNode
{
	public DiagramStrategy(DiagramNodeId idToUse, Strategy cmIntervention)
	{
		super(idToUse, cmIntervention);
	}

	public Color getColor()
	{
		if(isStatusDraft())
			return DiagramConstants.COLOR_DRAFT_INTERVENTION;
		
		return EAM.mainWindow.getColorPreference(AppPreferences.TAG_COLOR_INTERVENTION);
	}

}
