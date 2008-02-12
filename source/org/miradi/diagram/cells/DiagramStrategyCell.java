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
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Strategy;

public class DiagramStrategyCell extends FactorCell
{
	public DiagramStrategyCell(Strategy strategyToWrap, DiagramFactor diagramFactor)
	{
		super(strategyToWrap, diagramFactor);
	}

	public Color getColor()
	{
		if(isStatusDraft())
			return DiagramConstants.COLOR_DRAFT_STRATEGY;
		
		return EAM.getMainWindow().getColorPreference(AppPreferences.TAG_COLOR_STRATEGY);
	}

}
