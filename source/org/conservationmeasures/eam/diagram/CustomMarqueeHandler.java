/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;

public class CustomMarqueeHandler extends BasicMarqueeHandler
{
	public CustomMarqueeHandler(DiagramComponent diagramToUse)
	{
		diagram = diagramToUse;
	}
	
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		diagram.setMarquee(true);
	}

	public void handleMarqueeEvent(MouseEvent e, JGraph graph, Rectangle2D bounds)
	{
		super.handleMarqueeEvent(e, graph, bounds);
		selectBendPointInBounds();
	}

	private void selectBendPointInBounds()
	{

		Object[] selectedCells = diagram.getSelectionCells();
		for (int i = 0; i < selectedCells.length; ++i)
		{
			EAMGraphCell rawCell = (EAMGraphCell) selectedCells[i];
			if (rawCell.isFactorLink())
			{
				LinkCell linkCell = (LinkCell) rawCell;
				//FIXME dont select all but only those bend points withiin bounds (must select the link first)
				linkCell.getBendPointSelectionHelper().selectAll();
			}
		}
	}
	
	private DiagramComponent diagram;
}
