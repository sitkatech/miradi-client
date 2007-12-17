/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.DiagramConstants;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.GroupBox;

public class DiagramGroupBoxCell extends FactorCell
{
	public DiagramGroupBoxCell(GroupBox groupBox, DiagramFactor diagramFactorToUse)
	{
		super(groupBox, diagramFactorToUse);
	}

	public Color getColor()
	{
		return DiagramConstants.GROUP_BOX_COLOR;
	}
}
