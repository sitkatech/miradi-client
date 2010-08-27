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
