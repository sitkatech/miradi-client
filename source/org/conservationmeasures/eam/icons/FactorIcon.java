/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.icons;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.renderers.FactorRenderer;
import org.conservationmeasures.eam.objects.Factor;

public class FactorIcon extends EamIcon
{
	public FactorIcon(Factor factorToUse)
	{
		factor = factorToUse;
	}
	
	private EamIcon findCorrespondingIcon()
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

	EamIcon icon;
	Factor factor;
}
