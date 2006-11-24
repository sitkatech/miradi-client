/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.DiagramConstants;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Cause;

public class DiagramCause extends DiagramFactor
{
	public DiagramCause(DiagramFactorId idToUse, Cause cmFactor)
	{
		super(idToUse, cmFactor);
	}

	public Color getColor()
	{
		if(isContributingFactor())
			return EAM.mainWindow.getColorPreference(AppPreferences.TAG_COLOR_INDIRECT_FACTOR);
		if(isDirectThreat())
			return EAM.mainWindow.getColorPreference(AppPreferences.TAG_COLOR_DIRECT_THREAT);
		if(isStress())
			return DiagramConstants.COLOR_STRESS;
		
		throw new RuntimeException("Unknown factor type: " + getNodeType().getClass());
	}


}
