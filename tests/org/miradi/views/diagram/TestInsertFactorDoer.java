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

import org.martus.util.TestCaseEnhanced;

public class TestInsertFactorDoer extends TestCaseEnhanced
{
	public TestInsertFactorDoer(String name)
	{
		super(name);
	}

	public void testNothing()
	{
		
	}

	// TODO: If we are going to test doers like this, we 
	// really need to have a true main window with real views
	// and everything. None of this half-way stuff.
	// Alternative: Delete this test class.
//	public void testDoIt() throws Exception
//	{
//		ProjectForTesting project = new ProjectForTesting(getName());
//		OurMainWindow mainWindow = new OurMainWindow(project);
//
//		try
//		{
//			launchPropertiesEditor(mainWindow, project);
//			verifyCenterLocation(mainWindow, project);
//			verifyMouseLocation(mainWindow, project);
//			verifyNewTargetNodeLocation(mainWindow, project);
//			verifyNonTargetWithSelectedNode(mainWindow, project);
//		}
//		finally
//		{
//			project.close();
//		}
//	}
//
//	private void launchPropertiesEditor(OurMainWindow mainWindow, ProjectForTesting project) throws Exception, CommandFailedException
//	{
//		Point at = project.getSnapped(new Point(25,167));
//		InsertInterventionWithFakePropertiesEditing inserter = createInserter(mainWindow);
//		inserter.setLocation(at);
//		inserter.doIt();
//
//		FactorId id = project.getStrategyPool().getAllStrategies()[0].getFactorId();
//		FactorCell cell = project.getDiagramModel().getFactorCellByWrappedId(id);
//		assertEquals("didn't set location?", inserter.getLocation(), cell.getDiagramFactor().getLocation());
//		assertEquals("didn't set name?", inserter.getInitialText(), cell.getUnderlyingObject().getLabel());
//		assertTrue("select node was called?", inserter.wasSelectNodeCalled());
//
//		assertTrue("didn't invoke editor?", inserter.wasPropertiesEditorLaunched);
//	}
//
//	private void verifyCenterLocation(OurMainWindow mainWindow, Project project) throws Exception
//	{
//		InsertInterventionWithFakePropertiesEditing inserter = createInserter(mainWindow);
//
//		Point center = inserter.getCenterLocation(new Rectangle(0, 0, DIAGRAM_WIDTH, DIAGRAM_HEIGHT));
//		Point validCenter = new Point(DIAGRAM_WIDTH / 2, DIAGRAM_HEIGHT / 2);
//
//		assertEquals("didn't center?", center, validCenter);
//	}
//	
//	private void verifyMouseLocation(OurMainWindow mainWindow, Project project) throws Exception
//	{
//		Point somePoint = project.getSnapped(new Point(37, 70));
//		InsertInterventionWithFakePropertiesEditing inserter = createInserter(mainWindow);
//		inserter.setLocation(somePoint);
//
//		Point samePoint = inserter.getLocation();
//
//		assertEquals("is same point?", somePoint, samePoint);
//	}
//
//	private void verifyNewTargetNodeLocation(OurMainWindow mainWindow, ProjectForTesting project) throws Exception
//	{
//		InsertInterventionWithFakePropertiesEditing inserter = createInserter(mainWindow);
//
//		FactorCell dNode1 = project.createFactorCell(ObjectType.TARGET);
//
//		final Rectangle visibleRect = new Rectangle(0, 0, DIAGRAM_WIDTH, DIAGRAM_HEIGHT);
//		DiagramFactor diagramFactor = dNode1.getDiagramFactor();
//		Point newNodeLocation1 = inserter.getTargetLocation(project.getDiagramModel(), diagramFactor, visibleRect);
//		dNode1.setLocation(newNodeLocation1);
//		
//		int node1BoudsWidth = (int)dNode1.getBounds().getWidth();
//		int x = DIAGRAM_WIDTH - node1BoudsWidth - InsertFactorDoer.TARGET_RIGHT_SPACING;
//		int y = InsertFactorDoer.TARGET_TOP_LOCATION;
//		Point firstPoint = new Point(x, y);
//		assertEquals("first target location wrong?", firstPoint, newNodeLocation1);
//
//		FactorCell dNode2 = project.createFactorCell(ObjectType.TARGET);	
//
//		y = InsertFactorDoer.TARGET_TOP_LOCATION + (int)dNode1.getRectangle().getHeight() + InsertFactorDoer.TARGET_BETWEEN_SPACING;
//
//		Point secondPoint  = new Point(x, y);
//		Point newNodeLocation2 = inserter.getTargetLocation(project.getDiagramModel(), dNode2.getDiagramFactor(), visibleRect);
//		assertEquals("second target location wrong?", secondPoint, newNodeLocation2);
//	}
//	
//	private void verifyNonTargetWithSelectedNode(OurMainWindow mainWindow, ProjectForTesting project) throws Exception
//	{
//		Point someFirstPoint = project.getSnapped(400, 500);
//		Point someSecondPoint = project.getSnapped(0, 500);
//		InsertInterventionWithFakePropertiesEditing inserter = createInserter(mainWindow);
//		
//		FactorCell diagramNode = project.createFactorCell(ObjectType.CAUSE);
//		int x = (int)diagramNode.getBounds().getWidth() / 2 + InsertFactorDoer.DEFAULT_MOVE;
//		Point someThirdPoint = project.getSnapped(x , 500);
//
//		checkNodeLocation(project, inserter, diagramNode, someFirstPoint);
//		checkNodeLocation(project, inserter, diagramNode, someSecondPoint);
//		checkNodeLocation(project, inserter, diagramNode, someThirdPoint);
//	}
//
//	private void checkNodeLocation(Project project, InsertInterventionWithFakePropertiesEditing inserter, FactorCell diagramNode, Point somePoint)
//	{
//		diagramNode.setLocation(somePoint);
//		int nodeWidth = (int)diagramNode.getBounds().getWidth();
//		FactorCell[] selectedNodes = new FactorCell[1];
//		selectedNodes[0] = diagramNode;
//		Point movedLocation = inserter.getLocationSelectedNonTargetNode(selectedNodes, nodeWidth);
//		int snappedX = project.getSnapped(somePoint).x;
//
//		Point shouldBeLocation = new Point(Math.max(0, snappedX - nodeWidth - InsertFactorDoer.DEFAULT_MOVE), project.getSnapped(somePoint).y);
//		assertEquals("node is in bounds", shouldBeLocation, movedLocation);
//	}
//	
//	private InsertInterventionWithFakePropertiesEditing createInserter(OurMainWindow mainWindow) throws Exception
//	{
//		InsertInterventionWithFakePropertiesEditing inserter = new InsertInterventionWithFakePropertiesEditing();
//		inserter.setMainWindow(mainWindow);
//		inserter.setView(new DiagramView(mainWindow));
//		return inserter;
//	}
//	
//	static class InsertInterventionWithFakePropertiesEditing extends InsertStrategyDoer
//	{
//		protected void selectNewFactor(FactorId idToUse)
//		{
//			nodeSelectCalled = true;
//		}
//		
//		boolean wasSelectNodeCalled()
//		{
//			return nodeSelectCalled;
//		}
//
//		void launchPropertiesEditor(FactorId id) throws Exception, CommandFailedException
//		{
//			wasPropertiesEditorLaunched = true;
//		}
//
//		public boolean wasPropertiesEditorLaunched;
//		private boolean nodeSelectCalled;
//	}
//
//	static class OurMainWindow extends MainWindow
//	{
//		public OurMainWindow(Project projectToUse) throws IOException
//		{
//			super(projectToUse);
//			actions = new Actions(this);
//			diagramComponent = new DiagramComponent(this);
//			diagramComponent.setBounds(new Rectangle(0, 0, DIAGRAM_WIDTH, DIAGRAM_HEIGHT));
//			DiagramModel model = new DiagramModel(projectToUse);
//			diagramComponent.setModel(model);
//		}
//
//		public DiagramComponent getDiagramComponent()
//		{
//			return diagramComponent;
//		}
//
//		private DiagramComponent diagramComponent;
//	}
//	
//	static final int DIAGRAM_WIDTH = 1000;
//	static final int DIAGRAM_HEIGHT = 1000;
//	
}
