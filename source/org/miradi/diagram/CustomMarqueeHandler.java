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
package org.miradi.diagram;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.objects.DiagramLink;
import org.miradi.utils.PointList;

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
	
	public void mouseReleased(MouseEvent arg0)
	{
		super.mouseReleased(arg0);
		diagram.setMarquee(false);
	}

	public void handleMarqueeEvent(MouseEvent e, JGraph graph, Rectangle2D bounds)
	{
		super.handleMarqueeEvent(e, graph, bounds);
		selectAllBendPointsInBouds();
	}

	private void selectAllBendPointsInBouds()
	{
		PersistentDiagramModel model = diagram.getDiagramModel();
		DiagramLink[] allLinks = model.getAllDiagramLinksAsArray();
		
		for (int i = 0 ; i < allLinks.length; ++i)
		{
			selectBendPointsAndLinksInBounds(model, allLinks[i]);
		}
	}

	private void selectBendPointsAndLinksInBounds(PersistentDiagramModel model, DiagramLink diagramLink)
	{
		LinkCell linkCell = model.getDiagramFactorLink(diagramLink);
		Rectangle2D.Double scaledBounds = diagram.getScaledBounds(linkCell);
		if (! marqueeBounds.intersects(scaledBounds))
				return;
		
		PointList bendPoints = diagramLink.getBendPoints();
		for (int i = 0; i < bendPoints.size(); ++i)
		{
			Point bendPoint = bendPoints.get(i);
			Point2D.Double scaledBendPoint = diagram.getScaledPoint(bendPoint);
			if (marqueeBounds.contains(scaledBendPoint))
			{
				diagram.addSelectionCell(linkCell);
				linkCell.getBendPointSelectionHelper().addToSelectionIndexList(i);
			}
		}
	}

	private DiagramComponent diagram;
}
