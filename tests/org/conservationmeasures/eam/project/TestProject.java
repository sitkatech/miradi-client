/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactor;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandDiagramRemoveFactor;
import org.conservationmeasures.eam.commands.CommandSetFactorSize;
import org.conservationmeasures.eam.commands.CommandSwitchView;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorLink;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.exceptions.AlreadyInThatViewException;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.DirectThreatSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.LinkagePool;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.diagram.InsertConnection;
import org.martus.util.DirectoryUtils;

public class TestProject extends EAMTestCase
{
	public TestProject(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		project = new ProjectForTesting(getName());
		idAssigner = project.getNodeIdAssigner();
		chainManager = new ChainManager(project);
		super.setUp();
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}
	
	public void testGetTaskResources() throws Exception
	{
		BaseId taskId = project.createObject(ObjectType.TASK);
		Task task = (Task)project.findObject(ObjectType.TASK, taskId);
		assertEquals("Not empty for zero resources?", 0, project.getTaskResources(task).length);
		BaseId resourceId1 = project.createObject(ObjectType.PROJECT_RESOURCE);
		ProjectResource resource1 = (ProjectResource)project.findObject(ObjectType.PROJECT_RESOURCE, resourceId1);
		resource1.setLabel("One");
		BaseId resourceId2 = project.createObject(ObjectType.PROJECT_RESOURCE);
		ProjectResource resource2 = (ProjectResource)project.findObject(ObjectType.PROJECT_RESOURCE, resourceId2);
		resource2.setLabel("Two");
		IdList resourceIds = new IdList();
		resourceIds.add(resourceId1);
		resourceIds.add(resourceId2);
		task.setData(Task.TAG_RESOURCE_IDS, resourceIds.toString());
		assertEquals("wrong length", 2, project.getTaskResources(task).length);
		assertEquals("wrong first", resourceId1, project.getTaskResources(task)[0].getId());
		assertEquals("wrong second", resourceId2, project.getTaskResources(task)[1].getId());
	}
	
	public void testForOnlyOneAnnotationIdAssigner() throws Exception
	{
		IdAssigner original = project.getAnnotationIdAssigner();
		project.getProjectInfo().fillFrom(project.getProjectInfo().toJson());
		assertTrue("Constructed new annotation id assigner?", original == project.getAnnotationIdAssigner());
	}
	
	public void testUndoRedoSaveInfoAndDiagram() throws Exception
	{
		FactorId factorId = project.createNode(Factor.TYPE_CAUSE);
		CommandDiagramAddFactor cmd = new CommandDiagramAddFactor(new DiagramFactorId(BaseId.INVALID.asInt()), factorId);
		project.executeCommand(cmd);
		DiagramModel model = new DiagramModel(project);
		project.getDatabase().readDiagram(model);
		assertEquals("not one node?", 1, model.getAllNodes().size());
		
		project.undo();
		project.getDatabase().readDiagram(model);
		assertEquals("node not removed?", 0, model.getAllNodes().size());

		project.redo();
		project.getDatabase().readDiagram(model);
		assertEquals("node not re-added?", 1, model.getAllNodes().size());
	}
	
	public void testGetViewData() throws Exception
	{
		String viewName1 = DiagramView.getViewName();
		ViewData diagramData = project.getViewData(viewName1);
		assertEquals("gave back wrong data?", viewName1, diagramData.getLabel());
		ViewData repeat = project.getViewData(viewName1);
		assertEquals("didn't return same data?", diagramData.getId(), repeat.getId());
		
		assertNotNull("didn't create arbitrary data?", project.getViewData("iweflijjfliej"));
	}
	
	public void testCreateAndDeleteModelLinkage() throws Exception
	{
		FactorId threatId = (FactorId)project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, new CreateFactorParameter(new FactorTypeCause()));
		FactorId targetId = (FactorId)project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, new CreateFactorParameter(new FactorTypeTarget()));
		Cause factor = (Cause)project.findNode(threatId);
		assertFalse("already direct threat?", factor.isDirectThreat());
		CreateFactorLinkParameter parameter = new CreateFactorLinkParameter(threatId, targetId);
		BaseId createdId = project.createObject(ObjectType.MODEL_LINKAGE, BaseId.INVALID, parameter);
		BaseId linkageId = createdId;
		assertTrue("didn't become direct threat?", factor.isDirectThreat());
		project.deleteObject(ObjectType.MODEL_LINKAGE, linkageId);
		assertFalse("still a direct threat?", factor.isDirectThreat());
	}
	
	public void testGetSnapped() throws Exception
	{
		Point zeroZero = new Point(0, 0);
		assertEquals("moved zero zero?", zeroZero, project.getSnapped(zeroZero));
		
		Point defaultPlus = new Point(Project.DEFAULT_GRID_SIZE, Project.DEFAULT_GRID_SIZE);
		assertEquals("moved default plus?", defaultPlus, project.getSnapped(defaultPlus));
		
		Point defaultMinus = new Point(-Project.DEFAULT_GRID_SIZE, -Project.DEFAULT_GRID_SIZE);
		assertEquals("moved default minus?", defaultMinus, project.getSnapped(defaultMinus));

		Point oneThirdPlus = new Point(Project.DEFAULT_GRID_SIZE/3, Project.DEFAULT_GRID_SIZE/3);
		assertEquals("didn't snap one third?", zeroZero, project.getSnapped(oneThirdPlus));
		
		Point oneThirdMinus = new Point(-Project.DEFAULT_GRID_SIZE/3, -Project.DEFAULT_GRID_SIZE/3);
		assertEquals("didn't snap minus one third?", zeroZero, project.getSnapped(oneThirdMinus));
		
		Point twoThirdsPlus = new Point(2*Project.DEFAULT_GRID_SIZE/3, 2*Project.DEFAULT_GRID_SIZE/3);
		assertEquals("didn't snap two thirds?", defaultPlus, project.getSnapped(twoThirdsPlus));
		
		Point twoThirdsMinus = new Point(-2*Project.DEFAULT_GRID_SIZE/3, -2*Project.DEFAULT_GRID_SIZE/3);
		assertEquals("didn't snap minus two thirds?", defaultMinus, project.getSnapped(twoThirdsMinus));
		
		
	}
	
	public void testIsValidProjectFilename() throws Exception
	{
		assertTrue("AlphaNumericDotDashSpace", Project.isValidProjectFilename("AZaz09.- "));
		assertFalse("allowed really long name?", Project.isValidProjectFilename("1234567890123456789012345678901234567890"));
		assertFalse("Other Punct", Project.isValidProjectFilename("$"));
		final char ACCENT_A_LOWER = 0xE1;
		assertTrue("Foreign", Project.isValidProjectFilename(new String(new char[] {ACCENT_A_LOWER})));
	}
	
	public void testViewChanges() throws Exception
	{
		assertEquals("didn't start in summary view?", Project.SUMMARY_VIEW_NAME, project.getCurrentView());
		String destination = Project.MAP_VIEW_NAME;
		CommandSwitchView toMap = new CommandSwitchView(destination);
		project.executeCommand(toMap);
		assertEquals("didn't update project?", destination, project.getCurrentView());
		try
		{
			project.executeCommand(toMap);
			fail("should have thrown for switch to current view");
		}
		catch(AlreadyInThatViewException ignoreExpected)
		{
		}
		
		Command illegalView = new CommandSwitchView("Not a legal view name");
		try
		{
			project.executeCommand(illegalView);
			fail("should have thrown for switch to bogus view name");
		}
		catch(CommandFailedException ignoreExpected)
		{
		}
	}

	public void testGetAllSelectedCellsWithLinkages() throws Exception
	{
		DiagramFactor node1 = createNode(Factor.TYPE_TARGET);
		DiagramFactor node2 =  createNode(Factor.TYPE_INTERVENTION);
		DiagramFactor node3 =  createNode(Factor.TYPE_CAUSE);
		
		DiagramFactorLink linkage1 = createLinkage(idAssigner.takeNextId(), node1.getWrappedId(), node2.getWrappedId());
		DiagramFactorLink linkage2 = createLinkage(idAssigner.takeNextId(), node1.getWrappedId(), node3.getWrappedId());
		
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

	public void testGetAllSelectedNodes() throws Exception
	{
		DiagramFactor node1 = createNode(Factor.TYPE_TARGET);
		DiagramFactor node2 =  createNode(Factor.TYPE_INTERVENTION);
		
		DiagramFactorLink linkage1 = createLinkage(idAssigner.takeNextId(), node1.getWrappedId(), node2.getWrappedId());
		
		EAMGraphCell[] selectedCells = {linkage1};
		EAMGraphCell[] selectedItems = project.getOnlySelectedNodes(selectedCells);
		assertEquals(0, selectedItems.length);
		
		selectedCells[0] = node2;
		selectedItems = project.getOnlySelectedNodes(selectedCells);
		assertEquals(1, selectedItems.length);
		assertEquals(node2, selectedItems[0]);
		
		EAMGraphCell[] selectedCellsTwo = {node2,linkage1,node1};
		selectedItems = project.getOnlySelectedNodes(selectedCellsTwo);
		assertEquals(2, selectedItems.length);
	}
	
	public void TestPasteNodesAndLinksIntoProject() throws Exception
	{
		DiagramModel model = project.getDiagramModel();

		DiagramFactor node1 = createNode(Factor.TYPE_TARGET);
		DiagramFactor node2 =  createNode(Factor.TYPE_INTERVENTION);
		DiagramFactor node3 =  createNode(Factor.TYPE_CAUSE);
		
		createLinkage(idAssigner.takeNextId(), node1.getWrappedId(), node2.getWrappedId());
		createLinkage(idAssigner.takeNextId(), node1.getWrappedId(), node3.getWrappedId());
		
		Vector cellVector = project.getAllSelectedCellsWithLinkages(new EAMGraphCell[]{node1});
		Object[] selectedCells = cellVector.toArray(new EAMGraphCell[0]);
		TransferableEamList transferableList = new TransferableEamList(project.getFilename(), selectedCells);
		assertEquals(3, model.getAllNodes().size());
		assertEquals(2, model.getLinkages(node1).size());
		assertEquals(1, model.getLinkages(node2).size());
		assertEquals(1, model.getLinkages(node3).size());
		
		new NodeCommandHelper(project).pasteNodesAndLinksIntoProject(transferableList, new Point(5,5));
		Vector nodes = model.getAllNodes();
		assertEquals(4, nodes.size());
		assertEquals(4, model.getAllLinkages().size());
		for(int i = 0; i < nodes.size(); ++i)
		{
			assertEquals(2, model.getLinkages((DiagramFactor)nodes.get(i)).size());
		}
		
		//Test when a pasted item has linkages to a previously deleted node
		model.deleteNode(node2);
		new NodeCommandHelper(project).pasteNodesAndLinksIntoProject(transferableList, new Point(5,5));
		assertEquals(2, model.getLinkages(node1).size());
		assertEquals(3, model.getLinkages(node3).size());
	}

	public void testDiagramMoveOnly() throws Exception
	{
		DiagramFactor node1 = createNode(Factor.TYPE_TARGET);
		node1.setPreviousLocation(new Point(0,0));
		node1.setLocation(new Point(0,0));
		node1.setPreviousSize(node1.getSize());

		DiagramFactorId[] noNodesMoved = new DiagramFactorId[1];
		noNodesMoved[0] = node1.getDiagramNodeId();
	
		new NodeMoveHandler(project).nodesWereMovedOrResized(0, 0, noNodesMoved);
		try
		{
			project.getLastCommand();
			fail("Should have thrown a null pointer since command should not have been recorded for a node which wasn't moved");
		}
		catch(Exception expected)
		{
		}
		
		
		int deltaX = 55;
		int deltaY = 88;
		node1.setLocation(new Point(deltaX,deltaY));
		
		DiagramFactor node2 =  createNode(Factor.TYPE_CAUSE);
		node2.setPreviousLocation(new Point(10,10));
		node2.setLocation(new Point(20,30));
		
		DiagramFactorId[] ids = new DiagramFactorId[2];
		ids[0] = node1.getDiagramNodeId();
		ids[1] = node2.getDiagramNodeId();
		
		
		new NodeMoveHandler(project).nodesWereMovedOrResized(deltaX, deltaY, ids);
		project.getLastCommand(); //End Transaction
		CommandDiagramMove commandDiagramMoveRecorded = (CommandDiagramMove)project.getLastCommand();
		assertEquals(deltaX, commandDiagramMoveRecorded.getDeltaX());
		assertEquals(deltaY, commandDiagramMoveRecorded.getDeltaY());
		assertEquals(node1.getDiagramNodeId(), commandDiagramMoveRecorded.getIds()[0]);
		assertEquals(node2.getDiagramNodeId(), commandDiagramMoveRecorded.getIds()[1]);
		project.getLastCommand(); //begin Transaction
		
	}
	
	public void testResizeNodesOnly() throws Exception
	{
		DiagramFactor node1 =  createNode(Factor.TYPE_INTERVENTION);
		node1.setSize(new Dimension(5,10));
		node1.setPreviousSize((new Dimension(55, 80)));
		node1.setPreviousLocation(new Point(0,0));
		node1.setLocation(new Point(0,0));

		DiagramFactor node2 =  createNode(Factor.TYPE_CAUSE);
		node2.setSize(new Dimension(15,15));
		node2.setPreviousSize((new Dimension(52, 33)));
		node2.setPreviousLocation(new Point(0,0));
		node2.setLocation(new Point(0,0));
		
		DiagramFactorId[] ids = new DiagramFactorId[2];
		ids[0] = node1.getDiagramNodeId();
		ids[1] = node2.getDiagramNodeId();
		
		new NodeMoveHandler(project).nodesWereMovedOrResized(0, 0, ids);
		project.getLastCommand(); //End Transaction
		CommandSetFactorSize commandSetNodeSize2Recorded = (CommandSetFactorSize)project.getLastCommand();
		assertEquals(node2.getSize(), commandSetNodeSize2Recorded.getCurrentSize());
		assertEquals(node2.getPreviousSize(), commandSetNodeSize2Recorded.getPreviousSize());
		CommandSetFactorSize commandSetNodeSize1Recorded = (CommandSetFactorSize)project.getLastCommand();
		assertEquals(node1.getSize(), commandSetNodeSize1Recorded.getCurrentSize());
		assertEquals(node1.getPreviousSize(), commandSetNodeSize1Recorded.getPreviousSize());
		project.getLastCommand(); //begin Transaction
	}

	public void testResizeAndMoveNodes() throws Exception
	{
		int x = 5;
		int y = 10;
		int deltaX = 20;
		int deltaY = 30;
		Dimension position1 = new Dimension(45, 65);
		Dimension position2 = new Dimension(95, 88);
		
		
		DiagramFactor nodeResizedAndMoved =  createNode(Factor.TYPE_INTERVENTION);
		nodeResizedAndMoved.setSize(position1);
		nodeResizedAndMoved.setPreviousSize(position2);
		nodeResizedAndMoved.setPreviousLocation(new Point(x,y));
		nodeResizedAndMoved.setLocation(new Point(x+deltaX, y+deltaY));

		DiagramFactor nodeMovedOnly =  createNode(Factor.TYPE_CAUSE);
		nodeMovedOnly.setSize(position1);
		nodeMovedOnly.setPreviousSize(position1);
		nodeMovedOnly.setPreviousLocation(new Point(x,y));
		nodeMovedOnly.setLocation(new Point(x+deltaX, y+deltaY));
		
		DiagramFactor nodeResizedOnly = createNode(Factor.TYPE_CAUSE);
		nodeResizedOnly.setSize(position1);
		nodeResizedOnly.setPreviousSize(position2);
		nodeResizedOnly.setPreviousLocation(new Point(x,y));
		nodeResizedOnly.setLocation(new Point(x,y));

		DiagramFactor nodeNotMovedOrResized =  createNode(Factor.TYPE_CAUSE);
		nodeNotMovedOrResized.setSize(position1);
		nodeNotMovedOrResized.setPreviousSize(position1);
		nodeNotMovedOrResized.setPreviousLocation(new Point(x,y));
		nodeNotMovedOrResized.setLocation(new Point(x,y));

		
		DiagramFactorId[] ids = new DiagramFactorId[4];
		ids[0] = nodeResizedAndMoved.getDiagramNodeId();
		ids[1] = nodeMovedOnly.getDiagramNodeId();
		ids[2] = nodeResizedOnly.getDiagramNodeId();
		ids[3] = nodeNotMovedOrResized.getDiagramNodeId();

		new NodeMoveHandler(project).nodesWereMovedOrResized(deltaX, deltaY, ids);
		
		project.getLastCommand(); //End Transaction
		
		CommandDiagramMove commandDiagramMoveRecorded = (CommandDiagramMove)project.getLastCommand();
		assertEquals(deltaX, commandDiagramMoveRecorded.getDeltaX());
		assertEquals(deltaY, commandDiagramMoveRecorded.getDeltaY());
		assertEquals(nodeResizedAndMoved.getDiagramNodeId(), commandDiagramMoveRecorded.getIds()[0]);
		assertEquals(nodeMovedOnly.getDiagramNodeId(), commandDiagramMoveRecorded.getIds()[1]);

		CommandSetFactorSize commandNodeResizedOnlyRecorded = (CommandSetFactorSize)project.getLastCommand();
		assertEquals(nodeResizedOnly.getSize(), commandNodeResizedOnlyRecorded.getCurrentSize());
		assertEquals(nodeResizedOnly.getPreviousSize(), commandNodeResizedOnlyRecorded.getPreviousSize());
		
		CommandSetFactorSize commandNodeResizedAndMovedRecorded = (CommandSetFactorSize)project.getLastCommand();
		assertEquals(nodeResizedAndMoved.getSize(), commandNodeResizedAndMovedRecorded.getCurrentSize());
		assertEquals(nodeResizedAndMoved.getPreviousSize(), commandNodeResizedAndMovedRecorded.getPreviousSize());
		
		project.getLastCommand(); //begin Transaction
	}

	public void testPasteNodesOnlyIntoProject() throws Exception
	{
		DiagramModel model = project.getDiagramModel();

		DiagramFactor node1 = createNode(Factor.TYPE_TARGET);
		DiagramFactor node2 = createNode(Factor.TYPE_INTERVENTION);
		DiagramFactor node3 = createNode(Factor.TYPE_CAUSE);
		
		createLinkage(idAssigner.takeNextId(), node1.getWrappedId(), node2.getWrappedId());
		createLinkage(idAssigner.takeNextId(), node1.getWrappedId(), node3.getWrappedId());
		
		Vector cellVector = project.getAllSelectedCellsWithLinkages(new EAMGraphCell[]{node1});
		Object[] selectedCells = cellVector.toArray(new EAMGraphCell[0]);
		TransferableEamList transferableList = new TransferableEamList(project.getFilename(), selectedCells);
		assertEquals(3, model.getAllNodes().size());
		assertEquals(2, model.getLinkages(node1).size());
		assertEquals(1, model.getLinkages(node2).size());
		assertEquals(1, model.getLinkages(node3).size());
		
		new NodeCommandHelper(project).pasteNodesOnlyIntoProject(transferableList, new Point(5,5));
		Vector nodes = model.getAllNodes();
		assertEquals(4, nodes.size());
		assertEquals(2, model.getAllLinkages().size());
	}
	
	public void testCutAndPaste() throws Exception
	{
		DiagramModel model = project.getDiagramModel();

		assertEquals("objects already in the pool?", 0, project.getNodePool().size());
		assertEquals("nodes  already in the diagram?", 0, model.getAllNodes().size());

		DiagramFactor node1 = createNode(Factor.TYPE_TARGET);

		Object[] selectedCells = new DiagramFactor[] {node1};
		TransferableEamList transferableList = new TransferableEamList(project.getFilename(), selectedCells);
		DiagramFactorId idToDelete = node1.getDiagramNodeId();
		project.removeNodeFromDiagram(idToDelete);
		project.deleteObject(ObjectType.MODEL_NODE, node1.getWrappedId());
		
		assertEquals("objects still in the pool?", 0, project.getNodePool().size());
		assertEquals("nodes  still in the diagram?", 0, model.getAllNodes().size());

		Point pastePoint = new Point(5,5);
		new NodeCommandHelper(project).pasteNodesAndLinksIntoProject(transferableList, pastePoint);
		Vector nodes = model.getAllNodes();
		assertEquals(1, nodes.size());
		DiagramFactor pastedNode = (DiagramFactor)nodes.get(0);
		assertEquals("didn't paste correct size?", node1.getSize(), pastedNode.getSize());
		assertNotEquals("didn't change id?", node1.getDiagramNodeId(), pastedNode.getDiagramNodeId());
		assertEquals("didn't snap?", project.getSnapped(pastePoint), pastedNode.getLocation());

		assertEquals("not just one object in the pool?", 1, project.getNodePool().size());
	}

	public void testCloseClearsCurrentView() throws Exception
	{
		assertEquals("not starting on summary view?", Project.SUMMARY_VIEW_NAME, project.getCurrentView());
		String sampleViewName = Project.MAP_VIEW_NAME;
		project.switchToView(sampleViewName);
		assertEquals("didn't switch?", sampleViewName, project.getCurrentView());
		project.close();
		assertEquals("didn't reset view?", Project.NO_PROJECT_VIEW_NAME, project.getCurrentView());
	}
	
	public void testExecuteCommandWritesDiagram() throws Exception
	{
		FactorId modelNodeId = project.createNode(Factor.TYPE_CAUSE);
		CommandDiagramAddFactor cmd = new CommandDiagramAddFactor(new DiagramFactorId(BaseId.INVALID.asInt()), modelNodeId);
		project.executeCommand(cmd);
		DiagramModel copyOfModel = new DiagramModel(project);
		project.getDatabase().readDiagram(copyOfModel);
		assertEquals("didn't read back our one node?", 1, copyOfModel.getAllNodes().size());
	}
	
	public void testNodesDoNotGetWritten() throws Exception
	{
		ProjectServerForTesting database = project.getTestDatabase();
		
		FactorId targetId = project.createNode(Factor.TYPE_TARGET);
		FactorId factorId = project.createNode(Factor.TYPE_CAUSE);
		int existingCalls = database.callsToWriteObject;
		
		CommandDiagramAddFactor targetCommand = new CommandDiagramAddFactor(new DiagramFactorId(BaseId.INVALID.asInt()), targetId);
		project.executeCommand(targetCommand);
		assertEquals(existingCalls, database.callsToWriteObject);
		DiagramFactor target = project.getDiagramModel().getNodeById(targetId);
		
		CommandDiagramAddFactor factorCommand = new CommandDiagramAddFactor(new DiagramFactorId(BaseId.INVALID.asInt()), factorId);
		project.executeCommand(factorCommand);
		assertEquals(0 + existingCalls, database.callsToWriteObject);
		DiagramFactor factor = project.getDiagramModel().getNodeById(factorId);
		
		// undo the AddNode
		project.undo();
		assertEquals(0 + existingCalls, database.callsToWriteObject);
		
		// redo the AddNode
		project.redo();
		assertEquals(0 + existingCalls, database.callsToWriteObject);

		project.executeCommand(new CommandDiagramMove(9, 9, new DiagramFactorId[] {target.getDiagramNodeId(), factor.getDiagramNodeId()} ));
		assertEquals(0 + existingCalls, database.callsToWriteObject);
		
		Dimension oldDimension = factor.getSize();
		project.executeCommand(new CommandSetFactorSize(factor.getDiagramNodeId(), new Dimension(50, 75), oldDimension));
		assertEquals(0 + existingCalls, database.callsToWriteObject);
		
	}
	
	public void testInsertDuplicateNodes() throws Exception
	{
		FactorId gotId = project.createNode(Factor.TYPE_CAUSE);
		try
		{
			project.createNodeAndAddToDiagram(Factor.TYPE_CAUSE, gotId);
			fail("Should have thrown for inserting a duplicate id");
		}
		catch(RuntimeException ignoreExpected)
		{
		}
		
		CommandDiagramAddFactor cmd = new CommandDiagramAddFactor(new DiagramFactorId(BaseId.INVALID.asInt()), gotId);
		project.executeCommand(cmd);
		try
		{
			EAM.setLogToString();
			cmd.execute(project);
			fail("Should have thrown for replaying an insert over an existing node");
		}
		catch(CommandFailedException ignoreExpected)
		{
		}
		finally
		{
			EAM.setLogToConsole();
		}
		
	}
	
	public void testLinkagePool() throws Exception
	{
		DiagramFactor nodeA = createNode(new FactorTypeCause());
		DiagramFactor nodeB = createNode(new FactorTypeTarget());
		FactorId idA = nodeA.getWrappedId();
		FactorId idB = nodeB.getWrappedId();
		CreateFactorLinkParameter parameter = new CreateFactorLinkParameter(idA, idB);
		FactorLinkId createdId = (FactorLinkId)project.createObject(ObjectType.MODEL_LINKAGE, idAssigner.takeNextId(), parameter);
		FactorLinkId linkageId = createdId;
		LinkagePool linkagePool = project.getLinkagePool();
		assertEquals("not in pool?", 1, linkagePool.size());
		FactorLink cmLinkage = linkagePool.find(linkageId);
		assertEquals("wrong from?", idA, cmLinkage.getFromNodeId());
		assertEquals("wrong to?", idB, cmLinkage.getToNodeId());
		assertTrue("not linked?", project.isLinked(nodeA.getWrappedId(), nodeB.getWrappedId()));
		
		project.deleteObject(ObjectType.MODEL_LINKAGE, linkageId);
		assertEquals("Didn't remove from pool?", 0, linkagePool.size());
		assertFalse("still linked?", project.isLinked(nodeA.getWrappedId(), nodeB.getWrappedId()));
	}

	public void testFindNodesThatUseThisObjective() throws Exception
	{
		DiagramFactor nodeA = createNode(new FactorTypeTarget());
		DiagramFactor nodeB = createNode(new FactorTypeTarget());
		createNode(new FactorTypeTarget());
		
		BaseId objectiveId1 = project.createObject(ObjectType.OBJECTIVE);
		BaseId objectiveId2 = project.createObject(ObjectType.OBJECTIVE);

		IdList objectiveId = new IdList();
		objectiveId.add(objectiveId1);
		
		nodeA.getUnderlyingObject().setObjectives(objectiveId);
		nodeB.getUnderlyingObject().setObjectives(objectiveId);
		
		FactorSet foundNodes = chainManager.findNodesThatUseThisObjective(objectiveId1);
				
		assertEquals("didn't find both nodes?", 2, foundNodes.size());
		assertContains("missing nodeA? ", nodeA.getUnderlyingObject(), foundNodes);
		assertContains("missing nodeB?", nodeB.getUnderlyingObject(), foundNodes);

		FactorSet noNodes = chainManager.findNodesThatUseThisObjective(objectiveId2);
		
		assertEquals("found a node?", 0, noNodes.size());
		
	}
	
	public void testFindAllNodesRelatedToThisObjective() throws Exception
	{
		DiagramFactor nodeIndirectFactor = createNode(new FactorTypeCause());
		DiagramFactor nodeDirectThreat = createNode(new FactorTypeCause());
		
		BaseId objectiveId1 = project.createObject(ObjectType.OBJECTIVE);
		
		IdList objectiveId = new IdList();
		objectiveId.add(objectiveId1);

		nodeIndirectFactor.getUnderlyingObject().setObjectives(objectiveId);
		
		createLinkage(BaseId.INVALID, nodeIndirectFactor.getWrappedId(), nodeDirectThreat.getWrappedId());
		
		FactorSet foundNodes = chainManager.findAllNodesRelatedToThisObjective(objectiveId1);
		
		assertEquals("didn't find anything?", 2, foundNodes.size());
		assertContains("missing direct threat?", nodeDirectThreat.getUnderlyingObject(), foundNodes);
		assertContains("missing contributing factor?", nodeIndirectFactor.getUnderlyingObject(), foundNodes);
		
		
	}
	
	public void testFindNodesThatUseThisIndicator() throws Exception
	{
		
		DiagramFactor nodeA = createNode(new FactorTypeTarget());
		DiagramFactor nodeB = createNode(new FactorTypeTarget());
		createNode(new FactorTypeTarget());
		
		IndicatorId indicatorId1 = (IndicatorId)project.createObject(ObjectType.INDICATOR);
		IndicatorId indicatorId2 = (IndicatorId)project.createObject(ObjectType.INDICATOR);
		
		IdList indicators1 = new IdList();
		indicators1.add(indicatorId1);
		nodeA.getUnderlyingObject().setIndicators(indicators1);
		nodeB.getUnderlyingObject().setIndicators(indicators1);
		
		FactorSet foundNodes = chainManager.findNodesThatUseThisIndicator(indicatorId1);
				
		assertEquals("didn't find both nodes?", 2, foundNodes.size());
		assertContains("missing nodeA? ", nodeA.getUnderlyingObject(), foundNodes);
		assertContains("missing nodeB?", nodeB.getUnderlyingObject(), foundNodes);

		FactorSet noNodes = chainManager.findNodesThatUseThisIndicator(indicatorId2);
		
		assertEquals("found a node?", 0, noNodes.size());
	}
	
	public void testFindAllNodesRelatedToThisIndicator() throws Exception
	{
		DiagramFactor nodeIndirectFactor = createNode(new FactorTypeCause());
		DiagramFactor nodeDirectThreat = createNode(new FactorTypeCause());
		
		IndicatorId indicatorId1 = (IndicatorId)project.createObject(ObjectType.INDICATOR);
		IdList indicators1 = new IdList();
		indicators1.add(indicatorId1);
		nodeIndirectFactor.getUnderlyingObject().setIndicators(indicators1);
		
		createLinkage(BaseId.INVALID, nodeIndirectFactor.getWrappedId(), nodeDirectThreat.getWrappedId());
		
		FactorSet foundNodes = chainManager.findAllNodesRelatedToThisIndicator(indicatorId1);
		
		assertEquals("didn't find anything?", 2, foundNodes.size());
		assertContains("missing direct threat?", nodeDirectThreat.getUnderlyingObject(), foundNodes);
		assertContains("missing contributing factor?", nodeIndirectFactor.getUnderlyingObject(), foundNodes);
		
		
	}
	
	public void testDirectThreatSet() throws Exception
	{
		DiagramFactor nodeIndirectFactor = createNode(new FactorTypeCause());
		DiagramFactor nodeDirectThreatA = createNode(new FactorTypeCause());	
		((Cause)nodeDirectThreatA.getUnderlyingObject()).increaseTargetCount();
		DiagramFactor nodeDirectThreatB = createNode(new FactorTypeCause());
		((Cause)nodeDirectThreatB.getUnderlyingObject()).increaseTargetCount();
		
		FactorSet allNodes = new FactorSet();
		allNodes.attemptToAdd(nodeIndirectFactor.getUnderlyingObject());
		allNodes.attemptToAdd(nodeDirectThreatA.getUnderlyingObject());
		allNodes.attemptToAdd(nodeDirectThreatB.getUnderlyingObject());	
		
		DirectThreatSet foundNodes = new DirectThreatSet(allNodes);
		
		assertEquals("didn't find both nodes?", 2, foundNodes.size());
		assertContains("missing nodeDirectThreatA? ", nodeDirectThreatA.getUnderlyingObject(), foundNodes);
		assertContains("missing nodeDirectThreatB?", nodeDirectThreatB.getUnderlyingObject(), foundNodes);
	}
	
	public void testOpenProject() throws Exception
	{
		FactorId factorId;
		File tempDir = createTempDirectory();
		Project diskProject = new Project();
		diskProject.createOrOpen(tempDir);
		try
		{
			factorId = createNodeAndAddToDiagram(diskProject, Factor.TYPE_CAUSE, BaseId.INVALID);
			FactorId targetId = createNodeAndAddToDiagram(diskProject, Factor.TYPE_TARGET, BaseId.INVALID);
			
			InsertConnection.createModelLinkageAndAddToDiagramUsingCommands(diskProject, factorId, targetId);
			
			CreateFactorParameter parameter = new CreateFactorParameter(Factor.TYPE_INTERVENTION);
			FactorId interventionId = (FactorId)diskProject.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, parameter);
			DiagramFactorId diagramNodeId = diskProject.addNodeToDiagram(interventionId);

			CommandDiagramRemoveFactor cmdDelete = new CommandDiagramRemoveFactor(diagramNodeId);
			diskProject.executeCommand(cmdDelete);
			
			diskProject.deleteObject(ObjectType.MODEL_NODE, interventionId);
		}
		finally
		{
			diskProject.close();
		}
		
		Project loadedProject = new Project(new ProjectServer());
		loadedProject.createOrOpen(tempDir);
		try
		{
			assertEquals("didn't read node pool?", 2, loadedProject.getNodePool().size());
			assertEquals("didn't read link pool?", 1, loadedProject.getLinkagePool().size());
			assertEquals("didn't populate diagram?", 2, loadedProject.getDiagramModel().getNodeCount());
			assertEquals("didn't preserve next node id?", diskProject.getNodeIdAssigner().takeNextId(), loadedProject.getNodeIdAssigner().takeNextId());
			BaseId expectedAnnotationId = diskProject.getAnnotationIdAssigner().takeNextId();
			assertEquals("didn't preserve next annotation id?", expectedAnnotationId, loadedProject.getAnnotationIdAssigner().takeNextId());
			Cause factor = (Cause)loadedProject.findNode(factorId);
			assertTrue("didn't update factor target count?", factor.isDirectThreat());
		}
		finally
		{
			loadedProject.close();
			DirectoryUtils.deleteEntireDirectoryTree(tempDir);
		}
		
		int highestAnnotationIdBeforeClearing = diskProject.getAnnotationIdAssigner().getHighestAssignedId();
		
		File emptyDir = createTempDirectory();
		diskProject.createOrOpen(emptyDir);
		try
		{
			assertEquals("didn't clear node pool?", 0, diskProject.getNodePool().size());
			assertEquals("didn't clear link pool?", 0, diskProject.getLinkagePool().size());
			assertEquals("didn't clear diagram?", 0, diskProject.getDiagramModel().getNodeCount());
			assertEquals("didn't clear next node id?", new BaseId(0), diskProject.getNodeIdAssigner().takeNextId());
			assertTrue("didn't clear next annotation id?", diskProject.getAnnotationIdAssigner().getHighestAssignedId() < highestAnnotationIdBeforeClearing);
		}
		finally
		{
			diskProject.close();
			DirectoryUtils.deleteEntireDirectoryTree(emptyDir);
		}
		
	}
	
	public void testCreateNewProject() throws Exception
	{
		File tempDir = createTempDirectory();
		Project newProject = new Project(new ProjectServer());
		newProject.createOrOpen(tempDir);
		
		try
		{
			assertEquals("default criterion not created?", 3, newProject.getThreatRatingFramework().getCriteria().length);
			assertEquals("default valueoptions not created?", 5, newProject.getThreatRatingFramework().getValueOptions().length);
		}
		finally
		{
			newProject.close();
			DirectoryUtils.deleteEntireDirectoryTree(tempDir);
		}
	}
	
	private DiagramFactor createNode(FactorType nodeType) throws Exception
	{
		FactorId insertedId = project.createNodeAndAddToDiagram(nodeType, BaseId.INVALID);
		return project.getDiagramModel().getNodeById(insertedId);
	}
	
	private DiagramFactorLink createLinkage(BaseId id, FactorId fromId, FactorId toId) throws Exception
	{
		CreateFactorLinkParameter parameter = new CreateFactorLinkParameter(fromId, toId);
		FactorLinkId createdId = (FactorLinkId)project.createObject(ObjectType.MODEL_LINKAGE, id, parameter);
		DiagramFactorLinkId diagramLinkageId = project.addLinkageToDiagram(createdId);
		return project.getDiagramModel().getLinkageById(diagramLinkageId);
	}

	public FactorId createNodeAndAddToDiagram(Project projectToUse, FactorType nodeType, BaseId id) throws Exception
	{
		CreateFactorParameter parameter = new CreateFactorParameter(nodeType);
		FactorId nodeId = (FactorId)projectToUse.createObject(ObjectType.MODEL_NODE, id, parameter);
		projectToUse.addNodeToDiagram(nodeId);
		return nodeId;
	}


	ProjectForTesting project;
	IdAssigner idAssigner;
	ChainManager chainManager;
}
