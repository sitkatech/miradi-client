/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.martus.util.TestCaseEnhanced;

public class TestInsertNode extends TestCaseEnhanced
{
	public TestInsertNode(String name)
	{
		super(name);
	}

	public void testDoIt() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		OurMainWindow mainWindow = new OurMainWindow(project);

		try
		{
			launchPropertiesEditor(mainWindow, project);
			verifyCenterLocation(mainWindow, project);
			verifyMouseLocation(mainWindow, project);
			verifyNewTargetNodeLocation(mainWindow, project);
			verifyNonTargetWithSelectedNode(mainWindow, project);
		}
		finally
		{
			project.close();
		}
	}

	private void launchPropertiesEditor(OurMainWindow mainWindow, ProjectForTesting project) throws Exception, CommandFailedException
	{
		Point at = project.getSnapped(new Point(25,167));
		InsertInterventionWithFakePropertiesEditing inserter = new InsertInterventionWithFakePropertiesEditing();
		inserter.setMainWindow(mainWindow);
		inserter.setView(new DiagramView(mainWindow));
		inserter.setLocation(at);
		inserter.doIt();

		ModelNodeId id = project.getNodePool().getModelNodeIds()[0];
		DiagramNode node = project.getDiagramModel().getNodeById(id);
		assertEquals("didn't set location?", inserter.getLocation(), node.getLocation());
		assertEquals("didn't set name?", inserter.getInitialText(), node.getLabel());

		assertTrue("didn't invoke editor?", inserter.wasPropertiesEditorLaunched);
	}

	private void verifyCenterLocation(OurMainWindow mainWindow, Project project) throws Exception
	{
		InsertInterventionWithFakePropertiesEditing inserter = new InsertInterventionWithFakePropertiesEditing();
		inserter.setMainWindow(mainWindow);
		inserter.setView(new DiagramView(mainWindow));

		Point center = inserter.getCenterLocation(new Rectangle(0, 0, DIAGRAM_WIDTH, DIAGRAM_HEIGHT));
		Point validCenter = new Point(DIAGRAM_WIDTH / 2, DIAGRAM_HEIGHT / 2);

		assertEquals("didn't center?", center, validCenter);
	}

	private void verifyNewTargetNodeLocation(OurMainWindow mainWindow, Project project) throws Exception
	{
		final int SAME_HEIGHT_WIDTH = DIAGRAM_HEIGHT;
		InsertInterventionWithFakePropertiesEditing inserter = new InsertInterventionWithFakePropertiesEditing();
		inserter.setMainWindow(mainWindow);
		inserter.setView(new DiagramView(mainWindow));

		DiagramNode dNode1 = getDiagramNode(project);

		final Rectangle visibleRect = new Rectangle(0, 0, SAME_HEIGHT_WIDTH, SAME_HEIGHT_WIDTH);
		Point newNodeLocation1 = inserter.getTargetLocation(dNode1, visibleRect);
		dNode1.setLocation(newNodeLocation1);

		int x = SAME_HEIGHT_WIDTH - (int)dNode1.getBounds().getWidth() - InsertNode.TARGET_RIGHT_SPACING;
		int y = InsertNode.TARGET_TOP_LOCATION;
		Point firstPoint = new Point(x, y);
		assertEquals("is first target Location?", firstPoint, newNodeLocation1);

		DiagramNode dNode2 = getDiagramNode(project);	

		y = (int)dNode1.getRectangle().getHeight() + InsertNode.TARGET_TOP_LOCATION + InsertNode.TARGET_BETWEEN_SPACING;

		Point secondPoint  = new Point(x, y);
		Point newNodeLocation2 = inserter.getTargetLocation(dNode2, visibleRect);
		assertEquals("is second target Location?", secondPoint, newNodeLocation2);
	}

	private DiagramNode getDiagramNode(Project project) throws Exception
	{
		CreateModelNodeParameter modelNodeParameter2 = new CreateModelNodeParameter(new NodeTypeTarget());
		ModelNodeId nodeId2 = (ModelNodeId)project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, modelNodeParameter2);
		DiagramNodeId  dModelId2 = project.addNodeToDiagram(nodeId2);
		DiagramNode dNode2 = project.getDiagramModel().getNodeById(dModelId2);
		return dNode2;
	}

	private void verifyMouseLocation(OurMainWindow mainWindow, Project project) throws Exception
	{
		Point somePoint = project.getSnapped(new Point(37, 70));
		InsertInterventionWithFakePropertiesEditing inserter = new InsertInterventionWithFakePropertiesEditing();
		inserter.setMainWindow(mainWindow);
		inserter.setView(new DiagramView(mainWindow));
		inserter.setLocation(somePoint);

		Point samePoint = inserter.getLocation();

		assertEquals("is same point?", somePoint, samePoint);
	}

	private void verifyNonTargetWithSelectedNode(OurMainWindow mainWindow, Project project) throws Exception
	{
		Point someFirstPoint = project.getSnapped(new Point(400, 500));
		Point someSecondPoint = project.getSnapped(new Point(200, 500));
		InsertInterventionWithFakePropertiesEditing inserter = new InsertInterventionWithFakePropertiesEditing();
		inserter.setMainWindow(mainWindow);
		inserter.setView(new DiagramView(mainWindow));

		DiagramNode diagramNode = getDiagramNode(project);

		checkNodeLocation(project, inserter, diagramNode, someFirstPoint);
		checkNodeLocation(project, inserter, diagramNode, someSecondPoint);
	}

	private void checkNodeLocation(Project project, InsertInterventionWithFakePropertiesEditing inserter, DiagramNode diagramNode, Point somePoint)
	{
		diagramNode.setLocation(somePoint);
		int nodeWidth = (int)diagramNode.getBounds().getWidth();
		DiagramNode[] selectedNodes = new DiagramNode[1];
		selectedNodes[0] = diagramNode;
		Point movedLocation = inserter.getLocationSelectedNonTargetNode(selectedNodes, nodeWidth);
		int snappedX = project.getSnapped(somePoint).x;

		Point shouldBeLocation = new Point(Math.max(0, snappedX - nodeWidth - InsertNode.DEFAULT_MOVE), project.getSnapped(somePoint).y);
		assertEquals("is node in bounds", shouldBeLocation, movedLocation);
	}

	static class InsertInterventionWithFakePropertiesEditing extends InsertIntervention
	{
		void launchPropertiesEditor(DiagramNodeId id) throws Exception, CommandFailedException
		{
			wasPropertiesEditorLaunched = true;
		}

		public boolean wasPropertiesEditorLaunched; 
	}

	static class OurMainWindow extends MainWindow
	{
		public OurMainWindow(Project projectToUse) throws IOException
		{
			super(projectToUse);
			actions = new Actions(this);
			diagramComponent = new DiagramComponent(this);
			diagramComponent.setBounds(new Rectangle(0, 0, DIAGRAM_WIDTH, DIAGRAM_HEIGHT));
		}

		public DiagramComponent getDiagramComponent()
		{
			return diagramComponent;
		}

		private DiagramComponent diagramComponent;
	}
	
	static final int DIAGRAM_WIDTH = 1000;
	static final int DIAGRAM_HEIGHT = 1000;
	
}
