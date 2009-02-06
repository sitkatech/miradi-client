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
