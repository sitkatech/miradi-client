/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;

import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.jgraph.graph.DefaultGraphCell;

public class EAMGraphCell extends DefaultGraphCell
{
	public EAMGraphCell()
	{
	}

	public boolean isFactor()
	{
		return false;
	}
	
	public boolean isProjectScope()
	{
		return false;
	}
	
	public boolean isFactorLink()
	{
		return false;
	}
	
	public DiagramLink getDiagramLink()
	{
		return null;
	}
	
	public DiagramFactor getDiagramFactor()
	{
		return null;
	}
	
	public Color getColor()
	{
		return Color.BLACK;
	}
	
}
