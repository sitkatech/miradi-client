/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Point;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.utils.PointList;
import org.jgraph.graph.GraphLayoutCache;

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
