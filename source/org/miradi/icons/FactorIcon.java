/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.icons;

import java.awt.Color;

import org.miradi.diagram.renderers.FactorRenderer;
import org.miradi.objects.Factor;

public class FactorIcon extends AbstractShapeIcon
{
	public FactorIcon(Factor factorToUse)
	{
		factor = factorToUse;
	}
	
	private AbstractShapeIcon findCorrespondingIcon()
	{
		if (factor.isStrategy())
			return new StrategyIcon();
		if (factor.isTarget())
			return new TargetIcon();
		if (factor.isContributingFactor())
			return new ContributingFactorIcon();
		if (factor.isDirectThreat())
			return new DirectThreatIcon();
		
		return null;
	}

	Color getIconColor()
	{
		return findCorrespondingIcon().getIconColor();
	}

	FactorRenderer getRenderer()
	{
		return findCorrespondingIcon().getRenderer();
	}

	AbstractShapeIcon icon;
	Factor factor;
}
