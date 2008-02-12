/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram.cells;

import java.awt.Color;

import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.IntermediateResult;


public class DiagramIntermediateResultCell extends FactorCell
{
	public DiagramIntermediateResultCell(IntermediateResult cmFactor, DiagramFactor diagramFactorToUse)
	{
		super(cmFactor, diagramFactorToUse);
	}

	public Color getColor()
	{
		return EAM.getMainWindow().getColorPreference(AppPreferences.TAG_COLOR_INTERMEDIATE_RESULT);
	}
}
