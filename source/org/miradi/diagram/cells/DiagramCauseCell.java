/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram.cells;

import java.awt.Color;

import org.miradi.diagram.DiagramConstants;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;

public class DiagramCauseCell extends FactorCell
{
	public DiagramCauseCell(Cause cmFactor, DiagramFactor diagramFactorToUse)
	{
		super(cmFactor, diagramFactorToUse);
	}

	public Color getColor()
	{
		if(isContributingFactor())
			return EAM.getMainWindow().getColorPreference(AppPreferences.TAG_COLOR_CONTRIBUTING_FACTOR);
		if(isDirectThreat())
			return EAM.getMainWindow().getColorPreference(AppPreferences.TAG_COLOR_DIRECT_THREAT);
		if(isStress())
			return DiagramConstants.COLOR_STRESS;
		
		throw new RuntimeException("Unknown factor type: " + getClass());
	}


}
