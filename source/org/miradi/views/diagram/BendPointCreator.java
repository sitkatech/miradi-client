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
package org.miradi.views.diagram;

import java.awt.Point;

import org.jgraph.graph.GraphLayoutCache;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.objects.DiagramLink;
import org.miradi.project.Project;
import org.miradi.utils.PointList;

public class BendPointCreator
{
	public BendPointCreator(DiagramComponent diagramToUse)
	{
		diagram = diagramToUse;
		project = diagram.getProject();
		model = diagram.getDiagramModel();
	}
	
	//TODO write test for this method
	public void createBendPoint(Point insertionLocation, DiagramLink selectedLink) throws Exception
	{
		LinkCell selectedLinkCell = model.getDiagramFactorLink(selectedLink);
		Point insertPoint = selectedLinkCell.getNewBendPointLocation(model, getCache(), insertionLocation);
		insertBendPointForLink(selectedLinkCell, insertPoint);
	}
	
	public void insertBendPointForLink(LinkCell linkCell, Point insertPoint) throws Exception
	{
		DiagramLink selectedLink = linkCell.getDiagramLink();
		PointList bendPoints = selectedLink.getBendPoints();
		Point snapped = project.getSnapped(insertPoint);
		if (bendPoints.contains(snapped))
			return;

		PointList newListWithBendPoint = linkCell.getNewBendPointList(model, getCache(), snapped);
		
		CommandSetObjectData setBendPointsCommand = CommandSetObjectData.createNewPointList(selectedLink, DiagramLink.TAG_BEND_POINTS, newListWithBendPoint);
		project.executeCommand(setBendPointsCommand);
					
		diagram.addSelectionCell(linkCell);
		linkCell.getBendPointSelectionHelper().addToSelection(snapped);
	}
	
	private GraphLayoutCache getCache()
	{
		return diagram.getGraphLayoutCache();
	}
	
	private DiagramComponent diagram;
	private DiagramModel model;
	private Project project;
}
