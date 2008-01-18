/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.ThreatReductionResult;

public class DiagramThreatReductionResultCell extends FactorCell
{
	public DiagramThreatReductionResultCell(ThreatReductionResult threatReduction, DiagramFactor diagramFactorToUse)
	{
		super(threatReduction, diagramFactorToUse);
	}

	public Color getColor()
	{
		return EAM.getMainWindow().getColorPreference(AppPreferences.TAG_COLOR_THREAT_REDUCTION_RESULT);
	}
}
