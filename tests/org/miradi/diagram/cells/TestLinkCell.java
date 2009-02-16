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
package org.miradi.diagram.cells;

import java.awt.Point;

import org.jgraph.graph.GraphLayoutCache;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.PersistentDiagramModel;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.main.EAM;
import org.miradi.main.EAMTestCase;
import org.miradi.main.MainWindow;
import org.miradi.objects.DiagramLink;
import org.miradi.project.ProjectForTesting;
import org.miradi.utils.PointList;

public class TestLinkCell extends EAMTestCase
{
	public TestLinkCell(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
	}

	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}
	
	public void testGetNewBendPointList() throws Exception
	{
		PersistentDiagramModel model = project.getDiagramModel();
		EAM.setMainWindow(new MainWindow(project));
		DiagramComponent diagramComponent = new DiagramComponent(EAM.getMainWindow(), model);
		GraphLayoutCache cache = project.getDiagramModel().getGraphLayoutCache();
		diagramComponent.setGraphLayoutCache(cache);
		
		Point bendPoint1 = new Point(3, 3);
		LinkCell linkCell = project.createLinkCell();
		
		DiagramLink diagramLink = linkCell.getDiagramLink();
		assertEquals("has bend point?", 0, diagramLink.getBendPoints().size());
		PointList bendPoints = linkCell.getNewBendPointList(model, cache, bendPoint1);
		assertEquals("bend point not added?", 1, bendPoints.size());
	}
	
	ProjectForTesting project;

}
