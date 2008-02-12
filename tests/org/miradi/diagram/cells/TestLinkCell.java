/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram.cells;

import java.awt.Point;

import org.jgraph.graph.GraphLayoutCache;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
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
		DiagramModel model = project.getDiagramModel();
		EAM.setMainWindow(new MainWindow(project));
		DiagramComponent diagramComponent = new DiagramComponent(EAM.getMainWindow());
		diagramComponent.setModel(model);
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
