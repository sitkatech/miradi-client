/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Point;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSwitchView;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.AlreadyInThatViewException;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.main.ViewChangeListener;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.conservationmeasures.eam.views.NoProjectView;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.interview.InterviewView;

public class TestProject extends EAMTestCase
{
	public TestProject(String name)
	{
		super(name);
	}
	
	public void testIsValidName() throws Exception
	{
		assertTrue("AlphaNumericDotDashSpace", Project.isValidProjectName("AZaz09.- "));
		assertFalse("Other Punct", Project.isValidProjectName("$"));
		final char ACCENT_A_LOWER = 0xE1;
		assertTrue("Foreign", Project.isValidProjectName(new String(new char[] {ACCENT_A_LOWER})));
	}
	
	public void testData() throws Exception
	{
		Project project = new ProjectForTesting(getName());
		assertEquals("bad fieldname has data?", "", project.getDataValue("lisjefijef"));
		
		String fieldName = "sample field name";
		String fieldData = "sample field data";
		project.setDataValue(fieldName, fieldData);
		assertEquals("Didn't set data?", fieldData, project.getDataValue(fieldName));
		
		project.close();
		assertEquals("Didn't clear data?", "", project.getDataValue(fieldName));
		
	}
	
	public void testViewChanges() throws Exception
	{
		class SampleViewChangeListener implements ViewChangeListener
		{
			public void switchToView(String viewName)
			{
				if(viewName.equals(InterviewView.getViewName()))
					++interviewViewCount;
				else
					throw new RuntimeException("Unknown view: " + viewName);
			}

			int interviewViewCount;
		}
		
		Project project = new ProjectForTesting(getName());
		SampleViewChangeListener listener = new SampleViewChangeListener();
		project.addViewChangeListener(listener);
		Command toInterview = new CommandSwitchView(InterviewView.getViewName());
		project.executeCommand(toInterview);
		assertEquals("didn't notify listener of view switch?", 1, listener.interviewViewCount);
		try
		{
			project.executeCommand(toInterview);
			fail("Can't switch to current view");
		}
		catch(AlreadyInThatViewException ignoreExpected)
		{
		}
	}

	public void testGetAllSelectedCellsWithLinkages() throws Exception
	{
		Project project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();

		DiagramNode node1 = model.createNode(DiagramNode.TYPE_TARGET);
		DiagramNode node2 =  model.createNode(DiagramNode.TYPE_INTERVENTION);
		DiagramNode node3 =  model.createNode(DiagramNode.TYPE_FACTOR);
		
		DiagramLinkage linkage1 = model.createLinkage(DiagramNode.INVALID_ID, node1.getId(), node2.getId());
		DiagramLinkage linkage2 = model.createLinkage(DiagramNode.INVALID_ID, node1.getId(), node3.getId());
		
		EAMGraphCell[] selectedCells = {linkage1};
		Vector selectedItems = project.getAllSelectedCellsWithLinkages(selectedCells);
		assertEquals(1, selectedItems.size());
		assertContains(linkage1, selectedItems);
		
		selectedCells[0] = node2;
		selectedItems = project.getAllSelectedCellsWithLinkages(selectedCells);
		assertEquals(2, selectedItems.size());
		assertContains(node2, selectedItems);
		assertContains(linkage1, selectedItems);
		
		selectedCells[0] = node1;
		selectedItems = project.getAllSelectedCellsWithLinkages(selectedCells);
		assertEquals(3, selectedItems.size());
		assertContains(node1, selectedItems);
		assertContains(linkage1, selectedItems);
		assertContains(linkage2, selectedItems);
	}
	
	public void TestPasteNodesAndLinksIntoProject() throws Exception
	{
		Project project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();

		DiagramNode node1 = model.createNode(DiagramNode.TYPE_TARGET);
		DiagramNode node2 =  model.createNode(DiagramNode.TYPE_INTERVENTION);
		DiagramNode node3 =  model.createNode(DiagramNode.TYPE_FACTOR);
		
		model.createLinkage(DiagramNode.INVALID_ID, node1.getId(), node2.getId());
		model.createLinkage(DiagramNode.INVALID_ID, node1.getId(), node3.getId());
		
		Vector cellVector = project.getAllSelectedCellsWithLinkages(new EAMGraphCell[]{node1});
		Object[] selectedCells = cellVector.toArray(new EAMGraphCell[0]);
		TransferableEamList transferableList = new TransferableEamList(selectedCells);
		assertEquals(3, model.getAllNodes().size());
		assertEquals(2, model.getLinkages(node1).size());
		assertEquals(1, model.getLinkages(node2).size());
		assertEquals(1, model.getLinkages(node3).size());
		
		project.pasteNodesAndLinksIntoProject(transferableList, new Point(5,5));
		Vector nodes = model.getAllNodes();
		assertEquals(4, nodes.size());
		assertEquals(4, model.getAllLinkages().size());
		for(int i = 0; i < nodes.size(); ++i)
		{
			assertEquals(2, model.getLinkages((DiagramNode)nodes.get(i)).size());
		}
		
		//Test when a pasted item has linkages to a previously deleted node
		model.deleteNode(node2);
		project.pasteNodesAndLinksIntoProject(transferableList, new Point(5,5));
		assertEquals(2, model.getLinkages(node1).size());
		assertEquals(3, model.getLinkages(node3).size());
	}

	public void testPasteNodesOnlyIntoProject() throws Exception
	{
		Project project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();

		DiagramNode node1 = model.createNode(DiagramNode.TYPE_TARGET);
		DiagramNode node2 =  model.createNode(DiagramNode.TYPE_INTERVENTION);
		DiagramNode node3 =  model.createNode(DiagramNode.TYPE_FACTOR);
		
		model.createLinkage(DiagramNode.INVALID_ID, node1.getId(), node2.getId());
		model.createLinkage(DiagramNode.INVALID_ID, node1.getId(), node3.getId());
		
		Vector cellVector = project.getAllSelectedCellsWithLinkages(new EAMGraphCell[]{node1});
		Object[] selectedCells = cellVector.toArray(new EAMGraphCell[0]);
		TransferableEamList transferableList = new TransferableEamList(selectedCells);
		assertEquals(3, model.getAllNodes().size());
		assertEquals(2, model.getLinkages(node1).size());
		assertEquals(1, model.getLinkages(node2).size());
		assertEquals(1, model.getLinkages(node3).size());
		
		project.pasteNodesOnlyIntoProject(transferableList, new Point(5,5));
		Vector nodes = model.getAllNodes();
		assertEquals(4, nodes.size());
		assertEquals(2, model.getAllLinkages().size());
	}

	public void testCloseClearsCurrentView() throws Exception
	{
		Project project = new ProjectForTesting(getName());
		assertEquals("not starting on diagram view?", DiagramView.getViewName(), project.getCurrentView());
		String sampleViewName = "blah blah";
		project.switchToView(sampleViewName);
		assertEquals("didn't switch?", sampleViewName, project.getCurrentView());
		project.close();
		assertEquals("didn't reset view?", NoProjectView.getViewName(), project.getCurrentView());
	}
}
