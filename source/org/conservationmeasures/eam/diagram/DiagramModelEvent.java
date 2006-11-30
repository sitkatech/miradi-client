/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

import java.util.EventObject;

import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.objects.DiagramFactorLink;


public class DiagramModelEvent extends EventObject 
{
	public DiagramModelEvent(Object source, EAMGraphCell cellToUse) 
	{
		super(source);
		cell = cellToUse;
	}
	
	public DiagramFactor getDiagramFactor()
	{
		return (DiagramFactor)cell;
	}
	
	public DiagramFactorLink getDiagramFactorLink()
	{
		return cell.getDiagramFactorLink();
	}
	
	private EAMGraphCell cell;
}
