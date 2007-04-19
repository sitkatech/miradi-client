/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.DiagramConstants;
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
		return DiagramConstants.COLOR_THREAT_REDUCTION_RESULT;
	}
}
