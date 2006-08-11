/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;
import java.awt.Rectangle;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;

public class DiagramIntervention extends DiagramNode
{
	public DiagramIntervention(ConceptualModelIntervention cmIntervention)
	{
		super(cmIntervention);
	}

	public Color getColor()
	{
		if(isStatusDraft())
			return DiagramConstants.COLOR_DRAFT_INTERVENTION;
		
		return EAM.mainWindow.getColorPreference(AppPreferences.TAG_COLOR_INTERVENTION);
	}

	public Rectangle getAnnotationsRect()
	{
		return getAnnotationsRect(getObjectives().size());
	}

}
