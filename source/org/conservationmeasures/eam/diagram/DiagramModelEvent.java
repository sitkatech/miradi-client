/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.util.EventObject;

import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.objects.DiagramLink;


public class DiagramModelEvent extends EventObject 
{
	public DiagramModelEvent(Object source, EAMGraphCell cellToUse) 
	{
		super(source);
		cell = cellToUse;
	}
	
	public FactorCell getFactorCell()
	{
		return (FactorCell)cell;
	}
	
	public DiagramLink getDiagramLink()
	{
		return cell.getDiagramLink();
	}
	
	private EAMGraphCell cell;
}
