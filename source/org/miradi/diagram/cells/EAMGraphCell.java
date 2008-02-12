/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram.cells;

import java.awt.Color;

import org.jgraph.graph.DefaultGraphCell;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;

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
