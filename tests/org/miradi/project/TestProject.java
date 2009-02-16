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
package org.miradi.project;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.HashSet;

import org.martus.util.DirectoryUtils;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.PersistentDiagramModel;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.exceptions.NothingToUndoException;
import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdAssigner;
import org.miradi.ids.IdList;
import org.miradi.ids.IndicatorId;
import org.miradi.main.EAMTestCase;
import org.miradi.main.TransferableMiradiList;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.DirectThreatSet;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.FactorLinkPool;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TextBox;
import org.miradi.objects.ViewData;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.views.diagram.DiagramCopyPaster;
import org.miradi.views.diagram.DiagramPaster;
import org.miradi.views.diagram.DiagramView;
import org.miradi.views.diagram.LinkCreator;

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
		project = null;
	}
	
	public void testForOnlyOneAnnotationIdAssigner() throws Exception
	{
		IdAssigner original = project.getAnnotationIdAssigner();
		project.getProjectInfo().fillFrom(project.getProjectInfo().toJson());
		assertTrue("Constructed new annotation id assigner?", original == project.getAnnotationIdAssigner());
	}
	
	public void testEmptyTransaction() throws Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandEndTransaction());
		try
		{
			project.undo();
			fail("Empty transaction should not be undoable");
		}
		catch(NothingToUndoException ignoreExpected)
		{
		}
	}
	
	public void testUndoRedoSaveInfoAndDiagram() throws Exception
	{
		project.createAndAddFactorToDiagram(ObjectType.CAUSE);
		
		//undo add diagramFactor
		project.undo();
		
		//undo create diagramFactor
		project.undo();
		
		assertEquals("node not removed?", 0, project.getTestingDiagramObject().getAllDiagramFactorIds().size());

		project.redo();
		
		project.redo();
		assertEquals("node not re-added?", 1, project.getTestingDiagramObject().getAllDiagramFactorIds().size());
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
		DiagramFactor threat = project.createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		DiagramFactor target = project.createDiagramFactorAndAddToDiagram(ObjectType.TARGET);
		Cause factor = Cause.find(project, threat.getWrappedORef());
		assertFalse("already direct threat?", factor.isDirectThreat());
		CreateFactorLinkParameter parameter = new CreateFactorLinkParameter(threat.getWrappedORef(), target.getWrappedORef());
		BaseId createdId = project.createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, parameter);
		BaseId linkageId = createdId;
		assertTrue("didn't become direct threat?", factor.isDirectThreat());
		BaseObject object = project.findObject(new ORef(ObjectType.FACTOR_LINK, linkageId));
		project.deleteObject(object);
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

	public void testGetSnappedSize() throws Exception
	{		
		Dimension zeroByZero = new Dimension(0, 0);
		assertEquals("moved zero zero?", zeroByZero, project.getSnapped(zeroByZero));
		
		Dimension defaultPlus = new Dimension(Project.DEFAULT_GRID_SIZE, Project.DEFAULT_GRID_SIZE);
		assertEquals("moved default plus?", defaultPlus, project.getSnapped(defaultPlus));
		
		Dimension defaultMinus = new Dimension(-Project.DEFAULT_GRID_SIZE, -Project.DEFAULT_GRID_SIZE);
		assertEquals("moved default minus?", defaultMinus, project.getSnapped(defaultMinus));

		Dimension oneThirdPlus = new Dimension(Project.DEFAULT_GRID_SIZE/3, Project.DEFAULT_GRID_SIZE/3);
		assertEquals("didn't snap one third?", zeroByZero, project.getSnapped(oneThirdPlus));
		
		Dimension oneThirdMinus = new Dimension(-Project.DEFAULT_GRID_SIZE/3, -Project.DEFAULT_GRID_SIZE/3);
		assertEquals("didn't snap minus one third?", zeroByZero, project.getSnapped(oneThirdMinus));
		
		Dimension twoThirdsPlus = new Dimension(2*Project.DEFAULT_GRID_SIZE/3, 2*Project.DEFAULT_GRID_SIZE/3);
		assertEquals("didn't snap two thirds?", defaultPlus, project.getSnapped(twoThirdsPlus));
		
		Dimension twoThirdsMinus = new Dimension(-2*Project.DEFAULT_GRID_SIZE/3, -2*Project.DEFAULT_GRID_SIZE/3);
		assertEquals("didn't snap minus two thirds?", defaultMinus, project.getSnapped(twoThirdsMinus));		
	}
	
	public void testForceNonZeroEvenSnap()
	{
		verifyEvenSnap(30, 0);
		verifyEvenSnap(30, 15);
		verifyEvenSnap(30, 29);
		verifyEvenSnap(30, 40);
		verifyEvenSnap(60, 45);
	}
	
	private void verifyEvenSnap(int expectedValue, int valueToEvenSnap)
	{
		assertEquals("value was not snapped to even size?", expectedValue, getProject().forceNonZeroEvenSnap(valueToEvenSnap));
	}

	public void testIsValidProjectFilename() throws Exception
	{
		assertTrue("AlphaNumericDotDashSpace", Project.isValidProjectFilename("AZaz09_"));
		assertFalse("allowed really long name?", Project.isValidProjectFilename("1234567890123456789012345678901234567890"));
		assertFalse("Other Punct", Project.isValidProjectFilename("$"));
		final char ACCENT_A_LOWER = 0xE1;
		assertTrue("Foreign", Project.isValidProjectFilename(new String(new char[] {ACCENT_A_LOWER})));
		assertFalse("Empty", Project.isValidProjectFilename(""));
	}
	
	public void testMakeProjectFilenameLegal() throws Exception
	{
		assertEquals("didn't fix empty?", "-", Project.makeProjectFilenameLegal(""));
		String longest = "12345678901234567890123456789012";
		assertEquals("didn't fix long?", longest, Project.makeProjectFilenameLegal(longest + longest));
		String allGood = "abc_123. -";
		assertEquals("Ruined a good name?", allGood, Project.makeProjectFilenameLegal(allGood));
		String bad = "`~!@#$%^&*()=+[]\\{}|;':\',<>/?. -";
		assertEquals("Didn't fix bad?", "-----------------------------. -", Project.makeProjectFilenameLegal(bad));
	}
	
	public void testGetAllSelectedCellsWithLinkages() throws Exception
	{
		FactorCell node1 = project.createFactorCell(ObjectType.TARGET);
		FactorCell node2 =  project.createFactorCell(ObjectType.STRATEGY);
		FactorCell node3 =  project.createFactorCell(ObjectType.CAUSE);
		
		DiagramLink linkage1 = createLinkage(idAssigner.takeNextId(), node1.getWrappedFactorRef(), node2.getWrappedFactorRef());
		DiagramLink linkage2 = createLinkage(idAssigner.takeNextId(), node1.getWrappedFactorRef(), node3.getWrappedFactorRef());
		
		LinkCell cell1 = project.getDiagramModel().findLinkCell(linkage1);
		LinkCell cell2 = project.getDiagramModel().findLinkCell(linkage2);
		EAMGraphCell[] selectedCells = {cell1};
		PersistentDiagramModel model = project.getDiagramModel();
		HashSet<EAMGraphCell> selectedItems = model.getAllSelectedCellsWithRelatedLinkages(selectedCells);
		assertEquals(1, selectedItems.size());
		assertContains(cell1, selectedItems);
		
		selectedCells[0] = node2;
		selectedItems = model.getAllSelectedCellsWithRelatedLinkages(selectedCells);
		assertEquals(2, selectedItems.size());
		assertContains(node2, selectedItems);
		assertContains(cell1, selectedItems);
		
		selectedCells[0] = node1;
		selectedItems = model.getAllSelectedCellsWithRelatedLinkages(selectedCells);
		assertEquals(3, selectedItems.size());
		assertContains(node1, selectedItems);
		assertContains(cell1, selectedItems);
		assertContains(cell2, selectedItems);
	}

	public void testGetAllSelectedNodes() throws Exception
	{
		FactorCell node1 = project.createFactorCell(ObjectType.TARGET);
		FactorCell node2 =  project.createFactorCell(ObjectType.STRATEGY);
		
		DiagramLink linkage1 = createLinkage(idAssigner.takeNextId(), node1.getWrappedFactorRef(), node2.getWrappedFactorRef());
		
		LinkCell cell1 = project.getDiagramModel().findLinkCell(linkage1);

		EAMGraphCell[] selectedCells = {cell1};
		EAMGraphCell[] selectedItems = DiagramPanel.getOnlySelectedFactorCells(selectedCells);
		assertEquals(0, selectedItems.length);
		
		selectedCells[0] = node2;
		selectedItems = DiagramPanel.getOnlySelectedFactorCells(selectedCells);
		assertEquals(1, selectedItems.length);
		assertEquals(node2, selectedItems[0]);
		
		EAMGraphCell[] selectedCellsTwo = {node2, cell1, node1};
		selectedItems = DiagramPanel.getOnlySelectedFactorCells(selectedCellsTwo);
		assertEquals(2, selectedItems.length);
	}
	
	public void testPasteNodesAndLinksIntoProject() throws Exception
	{
		PersistentDiagramModel model = project.getDiagramModel();

		FactorCell node1 = project.createFactorCell(ObjectType.TARGET);
		DiagramFactor diagramFactor1 = project.createDiagramFactorAndAddToDiagram(ObjectType.STRATEGY);
		FactorCell node3 =  project.createFactorCell(ObjectType.CAUSE);
		
		createLinkage(idAssigner.takeNextId(), node1.getWrappedFactorRef(), diagramFactor1.getWrappedORef());
		createLinkage(idAssigner.takeNextId(), node1.getWrappedFactorRef(), node3.getWrappedFactorRef());
		
		HashSet<EAMGraphCell> cellVector = model.getAllSelectedCellsWithRelatedLinkages(new EAMGraphCell[]{node1});
		Object[] selectedCells = cellVector.toArray(new EAMGraphCell[0]);
		ORef diagramObjectRef = project.getTestingDiagramObject().getRef();
		TransferableMiradiList transferableList = new TransferableMiradiList(project, diagramObjectRef);
		transferableList.storeData(selectedCells);
		assertEquals(3, project.getAllDiagramFactorIds().length);
		assertEquals(2, model.getFactorLinks(node1).size());
		assertEquals(1, model.getFactorLinksSize(diagramFactor1.getRef()));
		assertEquals(1, model.getFactorLinks(node3).size());
		
		new DiagramCopyPaster(null, project.getDiagramModel(), transferableList).pasteFactorsAndLinks(new Point(5,5));
		DiagramFactorId[] diagramFactorIds = project.getAllDiagramFactorIds();
		assertEquals(4, diagramFactorIds.length);
		assertEquals(4, model.getAllDiagramFactorLinks().size());
		for(int i = 0; i < diagramFactorIds.length; ++i)
		{
			assertEquals(2, project.getToAndFromLinks(diagramFactorIds[i]).length);
		}
		
		//Test when a pasted item has linkages to a previously deleted node
		model.removeDiagramFactor(diagramFactor1.getRef());
		new DiagramCopyPaster(null, project.getDiagramModel(), transferableList).pasteFactorsAndLinks(new Point(5,5));
		assertEquals(2, model.getFactorLinks(node1).size());
		assertEquals(3, model.getFactorLinks(node3).size());
	}

	public void testDiagramMoveOnly() throws Exception
	{
		FactorCell node1 = project.createFactorCell(ObjectType.TARGET);
		
		// reset command stack to empty
		assertNotNull(project.getLastCommand());
		assertNotNull(project.getLastCommand());
		assertNotNull(project.getLastCommand());
		
		node1.setPreviousLocation(new Point(0,0));
		node1.setLocation(new Point(0,0));
		node1.setPreviousSize(node1.getSize());

		ORefList noNodesMoved = new ORefList();
		noNodesMoved.add(node1.getDiagramFactorRef());
	
		project.recordCommand(new CommandBeginTransaction());
		new FactorMoveHandler(project, project.getDiagramModel()).factorsWereMovedOrResized(noNodesMoved);
		project.recordCommand(new CommandEndTransaction());
		
		//begin transaction
		project.getLastCommand();
		
		//end trasaction
		project.getLastCommand();
		
		
		try
		{
			project.getLastCommand();
			fail("Should have thrown a null pointer since command should not have been recorded for a node which wasn't moved");
		}
		catch(Exception expected)
		{
		}
		
		Point deltaLocation = new Point(55, 88);
		node1.setLocation(deltaLocation);
		
		FactorCell node2 =  project.createFactorCell(ObjectType.CAUSE);
		node2.setPreviousLocation(new Point(10,10));
		Point node2Location = new Point(20,30);
		node2.setLocation(node2Location);
		
		ORefList diagramFactorRefs = new ORefList();
		diagramFactorRefs.add(node1.getDiagramFactorRef());
		diagramFactorRefs.add(node2.getDiagramFactorRef());
		
		project.recordCommand(new CommandBeginTransaction());
		new FactorMoveHandler(project, project.getDiagramModel()).factorsWereMovedOrResized(diagramFactorRefs);
		project.recordCommand(new CommandEndTransaction());
		
		project.getLastCommand(); //End Transaction
		
		CommandSetObjectData commandDiagramMove1 = (CommandSetObjectData)project.getLastCommand();
		assertEquals(node2.getDiagramFactorId(), commandDiagramMove1.getObjectId());
		Point point1 = EnhancedJsonObject.convertToPoint(commandDiagramMove1.getDataValue());
		assertEquals(point1, project.getSnapped(node2Location));
		
		CommandSetObjectData commandDiagramMove2 = (CommandSetObjectData)project.getLastCommand();
		assertEquals(node1.getDiagramFactorId(), commandDiagramMove2.getObjectId());
		Point point2 = EnhancedJsonObject.convertToPoint(commandDiagramMove2.getDataValue());
		assertEquals(point2, project.getSnapped(deltaLocation));
		
		project.getLastCommand(); //begin Transaction
	}
	
	public void testResizeNodesOnly() throws Exception
	{
		FactorCell node1 =  project.createFactorCell(ObjectType.STRATEGY);
		DiagramFactor diagramFactor1 = node1.getDiagramFactor();
		node1.setSize(new Dimension(5,10));
		node1.setPreviousSize((new Dimension(55, 80)));
		node1.setPreviousLocation(new Point(0,0));
		node1.setLocation(new Point(0,0));
		diagramFactor1.setSize(node1.getPreviousSize());
		diagramFactor1.setLocation(node1.getPreviousLocation());

		FactorCell node2 =  project.createFactorCell(ObjectType.CAUSE);
		DiagramFactor diagramFactor2 = node2.getDiagramFactor();
		node2.setSize(new Dimension(15,15));
		node2.setPreviousSize((new Dimension(52, 33)));
		node2.setPreviousLocation(new Point(0,0));
		node2.setLocation(new Point(0,0));
		diagramFactor2.setSize(node2.getPreviousSize());
		diagramFactor2.setLocation(node2.getPreviousLocation());

		
		ORefList diagramFactorRefs = new ORefList();
		diagramFactorRefs.add(node1.getDiagramFactorRef());
		diagramFactorRefs.add(node2.getDiagramFactorRef());
		
		project.recordCommand(new CommandBeginTransaction());
		new FactorMoveHandler(project, project.getDiagramModel()).factorsWereMovedOrResized(diagramFactorRefs);
		project.recordCommand(new CommandEndTransaction());
		
		project.getLastCommand(); //End Transaction
		
		CommandSetObjectData commandSetNodeSize2 = (CommandSetObjectData)project.getLastCommand();
		DiagramFactor diagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, node2.getDiagramFactorId()));
		String foundSize =  EnhancedJsonObject.convertFromDimension(diagramFactor.getSize());
		assertEquals(foundSize, commandSetNodeSize2.getDataValue());
		
		String node2PreviousSize = EnhancedJsonObject.convertFromDimension(node2.getPreviousSize());
		assertEquals(node2PreviousSize, commandSetNodeSize2.getPreviousDataValue());
		
		CommandSetObjectData commandSetNodeSize1 = (CommandSetObjectData)project.getLastCommand();
		String size = EnhancedJsonObject.convertFromDimension(node1.getSize());
		assertEquals(size, commandSetNodeSize1.getDataValue());
		String previousSize = EnhancedJsonObject.convertFromDimension(node1.getPreviousSize());
		assertEquals(previousSize, commandSetNodeSize1.getPreviousDataValue());
		
		project.getLastCommand(); //begin Transaction
	}

	public void testResizeAndMoveNodes() throws Exception
	{
		int x = 5;
		int y = 10;
		int deltaX = 20;
		int deltaY = 30;
		//Note: the expceted sizes are expected to be in even incr of the DefaultGridSize.
		//TODO: test code shuold be converted to use the DefaultGridSize
		Dimension position1 = new Dimension(30, 60);
		Dimension position2 = new Dimension(60, 90);
		
		
		FactorCell nodeResizedAndMoved =  project.createFactorCell(ObjectType.STRATEGY);
		nodeResizedAndMoved.setSize(position1);
		nodeResizedAndMoved.setPreviousSize(position2);
		nodeResizedAndMoved.setPreviousLocation(new Point(x,y));
		nodeResizedAndMoved.setLocation(new Point(x+deltaX, y+deltaY));
		DiagramFactor diagramFactorResizedAndMoved = nodeResizedAndMoved.getDiagramFactor();
		diagramFactorResizedAndMoved.setLocation(nodeResizedAndMoved.getPreviousLocation());
		diagramFactorResizedAndMoved.setSize(nodeResizedAndMoved.getPreviousSize());

		FactorCell nodeMovedOnly =  project.createFactorCell(ObjectType.CAUSE);
		nodeMovedOnly.setSize(position1);
		nodeMovedOnly.setPreviousSize(position1);
		nodeMovedOnly.setPreviousLocation(new Point(x,y));
		nodeMovedOnly.setLocation(new Point(x+deltaX, y+deltaY));
		DiagramFactor diagramFactorMovedOnly = nodeMovedOnly.getDiagramFactor();
		diagramFactorMovedOnly.setLocation(nodeMovedOnly.getPreviousLocation());
		diagramFactorMovedOnly.setSize(nodeMovedOnly.getPreviousSize());
		
		FactorCell nodeResizedOnly = project.createFactorCell(ObjectType.CAUSE);
		nodeResizedOnly.setSize(position1);
		nodeResizedOnly.setPreviousSize(position2);
		nodeResizedOnly.setPreviousLocation(new Point(x,y));
		nodeResizedOnly.setLocation(new Point(x,y));
		DiagramFactor diagramFactorResizedOnly = nodeResizedOnly.getDiagramFactor();
		diagramFactorResizedOnly.setLocation(nodeResizedOnly.getPreviousLocation());
		diagramFactorResizedOnly.setSize(nodeResizedOnly.getPreviousSize());
		
		FactorCell nodeNotMovedOrResized =  project.createFactorCell(ObjectType.CAUSE);
		nodeNotMovedOrResized.setSize(position1);
		nodeNotMovedOrResized.setPreviousSize(position1);
		nodeNotMovedOrResized.setPreviousLocation(new Point(x,y));
		nodeNotMovedOrResized.setLocation(new Point(x,y));
		DiagramFactor diagramFactorNotMovedOrResized = nodeNotMovedOrResized.getDiagramFactor();
		diagramFactorNotMovedOrResized.setLocation(nodeNotMovedOrResized.getPreviousLocation());
		diagramFactorNotMovedOrResized.setSize(nodeNotMovedOrResized.getPreviousSize());
		
		ORefList diagramFactorRefs = new ORefList();
		diagramFactorRefs.add(nodeResizedAndMoved.getDiagramFactorRef());
		diagramFactorRefs.add(nodeMovedOnly.getDiagramFactorRef());
		diagramFactorRefs.add(nodeResizedOnly.getDiagramFactorRef());
		diagramFactorRefs.add(nodeNotMovedOrResized.getDiagramFactorRef());

		project.recordCommand(new CommandBeginTransaction());
		new FactorMoveHandler(project, project.getDiagramModel()).factorsWereMovedOrResized(diagramFactorRefs);
		project.recordCommand(new CommandEndTransaction());
		
		project.getLastCommand(); //End Transaction
		
		CommandSetObjectData commandNodeResizedOnly = (CommandSetObjectData)project.getLastCommand();
		String nodeResizeOnlySize = EnhancedJsonObject.convertFromDimension(nodeResizedOnly.getSize());
		assertEquals(nodeResizeOnlySize, commandNodeResizedOnly.getDataValue());
		
		String nodeResizeOnlyPreviousSize = EnhancedJsonObject.convertFromDimension(nodeResizedOnly.getPreviousSize()); 
		assertEquals(nodeResizeOnlyPreviousSize, commandNodeResizedOnly.getPreviousDataValue());
		
		
		CommandSetObjectData commandDiagramMoveRecorded = (CommandSetObjectData)project.getLastCommand();
		Point deltaPoint = EnhancedJsonObject.convertToPoint(commandDiagramMoveRecorded.getDataValue());
		assertEquals(diagramFactorMovedOnly.getLocation(), deltaPoint);
		assertEquals(diagramFactorMovedOnly.getDiagramFactorId(), commandDiagramMoveRecorded.getObjectId());
		
		CommandSetObjectData commandNodeResizedAndMovedRecorded = (CommandSetObjectData)project.getLastCommand();
		String nodeResizedAndMovedSize = EnhancedJsonObject.convertFromDimension(nodeResizedAndMoved.getSize());
		assertEquals(nodeResizedAndMovedSize, commandNodeResizedAndMovedRecorded.getDataValue());
		
		String nodeResizedAndMovedPreviousSize = EnhancedJsonObject.convertFromDimension(nodeResizedAndMoved.getPreviousSize());
		assertEquals(nodeResizedAndMovedPreviousSize, commandNodeResizedAndMovedRecorded.getPreviousDataValue());
		
		project.getLastCommand(); //begin Transaction
	}

	public void testPasteNodesOnlyIntoProject() throws Exception
	{
		PersistentDiagramModel model = project.getDiagramModel();

		FactorCell node1 = project.createFactorCell(ObjectType.TARGET);
		FactorCell node2 = project.createFactorCell(ObjectType.STRATEGY);
		FactorCell node3 = project.createFactorCell(ObjectType.CAUSE);
		
		createLinkage(idAssigner.takeNextId(), node1.getWrappedFactorRef(), node2.getWrappedFactorRef());
		createLinkage(idAssigner.takeNextId(), node1.getWrappedFactorRef(), node3.getWrappedFactorRef());
		
		HashSet<EAMGraphCell> cellVector = model.getAllSelectedCellsWithRelatedLinkages(new EAMGraphCell[]{node1});
		Object[] selectedCells = cellVector.toArray(new EAMGraphCell[0]);
		ORef diagramObjectRef = project.getTestingDiagramObject().getRef();
		TransferableMiradiList transferableList = new TransferableMiradiList(project, diagramObjectRef);
		transferableList.storeData(selectedCells);
		assertEquals(3, project.getAllDiagramFactorIds().length);
		assertEquals(2, model.getFactorLinks(node1).size());
		assertEquals(1, model.getFactorLinks(node2).size());
		assertEquals(1, model.getFactorLinks(node3).size());
		
		new DiagramCopyPaster(null, project.getDiagramModel(), transferableList).pasteFactors(new Point(5,5));
		DiagramFactorId[] diagramFactorIds = project.getAllDiagramFactorIds();
		assertEquals(4, diagramFactorIds.length);
		assertEquals(2, model.getAllDiagramFactorLinks().size());
	}
	
	public void testCutAndPaste() throws Exception
	{
		assertEquals("objects already in the cause pool?", 0, project.getCausePool().size());
		assertEquals("objects already in the strategy pool?", 0, project.getStrategyPool().size());
		assertEquals("objects already in the target pool?", 0, project.getTargetPool().size());
		assertEquals("nodes  already in the diagram?", 0, project.getAllDiagramFactorIds().length);

		FactorCell node1 = project.createFactorCell(ObjectType.TARGET);
		Object[] selectedCells = new FactorCell[] {node1};
		ORef diagramObjectRef = project.getTestingDiagramObject().getRef();
		TransferableMiradiList transferableList = new TransferableMiradiList(project, diagramObjectRef);
		transferableList.storeData(selectedCells);
		
		DiagramFactorId idToDelete = node1.getDiagramFactorId();
		DiagramObject diagramObject = project.getTestingDiagramObject();
		CommandSetObjectData removeFactor = CommandSetObjectData.createRemoveIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, idToDelete);
		project.executeCommand(removeFactor);

		BaseObject target = project.findObject(new ORef(ObjectType.TARGET, node1.getWrappedId()));
		project.deleteObject(target);
		project.deleteObject(node1.getDiagramFactor());
		
		assertEquals("objects still in the target pool?", 0, project.getTargetPool().size());
		assertEquals("nodes  still in the diagram?", 0, project.getAllDiagramFactorIds().length);

		Point pastePoint = new Point(5,5);
		DiagramPaster diagramPaster = new DiagramCopyPaster(null, project.getDiagramModel(), transferableList);
		diagramPaster.pasteFactorsAndLinks(pastePoint);
		DiagramFactor diagramFactors[] = project.getAllDiagramFactors();
		assertEquals(1, diagramFactors.length);
		DiagramFactor pastedNode = diagramFactors[0];
		assertEquals("didn't paste correct size?", node1.getSize(), pastedNode.getSize());
		assertNotEquals("didn't change id?", node1.getDiagramFactorId(), pastedNode.getDiagramFactorId());
		assertEquals("didn't snap?", project.getSnapped(pastePoint), pastedNode.getLocation());

		assertEquals("not just one object in the target pool?", 1, project.getTargetPool().size());
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
		ORef factorRef = project.createFactorAndReturnRef(ObjectType.CAUSE);
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorRef);
		CommandCreateObject createDiagramFactorCommand = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		project.executeCommand(createDiagramFactorCommand);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactorCommand.getCreatedId();
		DiagramObject diagramObject = project.getTestingDiagramObject();
		CommandSetObjectData addDiagramFactor = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorId);
		project.executeCommand(addDiagramFactor);
		project.closeAndReopen();
		assertEquals("didn't read back our one node?", 1, project.getAllDiagramFactorIds().length);
	}
	
	public void testNodesDoNotGetWritten() throws Exception
	{
		ProjectServerForTesting database = project.getTestDatabase();
		
		ORef targetRef = project.createFactorAndReturnRef(ObjectType.TARGET);
		ORef factorRef = project.createFactorAndReturnRef(ObjectType.CAUSE);
		int existingCalls = database.callsToWriteObject;
		
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(targetRef);
		CommandCreateObject createDiagramFactor = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		project.executeCommand(createDiagramFactor);
		assertEquals(1 + existingCalls, database.callsToWriteObject);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactor.getCreatedId();
		DiagramObject diagramObject = project.getTestingDiagramObject();
		CommandSetObjectData addDiagramFactor = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorId);
		project.executeCommand(addDiagramFactor);
		assertEquals(2 + existingCalls, database.callsToWriteObject);
		
		CreateDiagramFactorParameter extraDiagramFactorInfo2 = new CreateDiagramFactorParameter(factorRef);
		CommandCreateObject createDiagramFactor2 = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo2);
		project.executeCommand(createDiagramFactor2);
		assertEquals(3 + existingCalls, database.callsToWriteObject);
		
		DiagramFactorId diagramFactorId2 = (DiagramFactorId) createDiagramFactor2.getCreatedId();
		CommandSetObjectData addDiagramFactor2 = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorId2);
		project.executeCommand(addDiagramFactor2);
		assertEquals(4 + existingCalls, database.callsToWriteObject);
		FactorCell factor = project.getDiagramModel().getFactorCellByWrappedRef(factorRef);
		
		// undo the AddNode
		project.undo();
		assertEquals(5 + existingCalls, database.callsToWriteObject);
		
		// undo the create diagram factor
		project.undo();
		assertEquals(5 + existingCalls, database.callsToWriteObject);
		
		// redo the created diagram factor
		project.redo();
		assertEquals(6 + existingCalls, database.callsToWriteObject);
		
		// redo the add diagram factor
		project.redo();
		assertEquals(7 + existingCalls, database.callsToWriteObject);

		String previousLocation = EnhancedJsonObject.convertFromPoint(new Point(5, 5));
		String newLocation = EnhancedJsonObject.convertFromPoint(new Point(9, 9));
		CommandSetObjectData moveDiagramFactor = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, factor.getDiagramFactorId(), DiagramFactor.TAG_LOCATION, newLocation);
		moveDiagramFactor.setPreviousDataValue(previousLocation);
		project.executeCommand(moveDiagramFactor);
		assertEquals(8 + existingCalls, database.callsToWriteObject);
		
		String newDimension = EnhancedJsonObject.convertFromDimension(new Dimension(50, 75));
		project.executeCommand(new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, factor.getDiagramFactorId(), DiagramFactor.TAG_SIZE, newDimension));
		assertEquals(9 + existingCalls, database.callsToWriteObject);
	}	
	
	public void testLinkagePool() throws Exception
	{
		FactorCell nodeA = project.createFactorCell(ObjectType.CAUSE);
		FactorCell nodeB = project.createFactorCell(ObjectType.TARGET);
		ORef refA = nodeA.getWrappedFactorRef();
		ORef refB = nodeB.getWrappedFactorRef();
		CreateFactorLinkParameter parameter = new CreateFactorLinkParameter(nodeA.getWrappedFactorRef(), nodeB.getWrappedFactorRef());
		FactorLinkId createdId = (FactorLinkId)project.createObject(ObjectType.FACTOR_LINK, idAssigner.takeNextId(), parameter);
		FactorLinkId linkageId = createdId;
		FactorLinkPool linkagePool = project.getFactorLinkPool();
		assertEquals("not in pool?", 1, linkagePool.size());
		FactorLink cmLinkage = linkagePool.find(linkageId);
		assertEquals("wrong from?", refA, cmLinkage.getFromFactorRef());
		assertEquals("wrong to?", refB, cmLinkage.getToFactorRef());
		assertTrue("not linked?", project.areLinked(nodeA.getWrappedFactorRef(), nodeB.getWrappedFactorRef()));
		
		project.deleteObject(cmLinkage);
		assertEquals("Didn't remove from pool?", 0, linkagePool.size());
		assertFalse("still linked?", project.areLinked(nodeA.getWrappedFactorRef(), nodeB.getWrappedFactorRef()));
	}

	public void testFindAllNodesRelatedToThisObjective() throws Exception
	{
		FactorCell nodeContributingFactor = project.createFactorCell(ObjectType.CAUSE);
		FactorCell nodeDirectThreat = project.createFactorCell(ObjectType.CAUSE);
		
		BaseId objectiveId1 = project.createObjectAndReturnId(ObjectType.OBJECTIVE);
		
		IdList objectiveId = new IdList(Objective.getObjectType());
		objectiveId.add(objectiveId1);

		nodeContributingFactor.getUnderlyingObject().setObjectives(objectiveId);
		
		createLinkage(BaseId.INVALID, nodeContributingFactor.getWrappedFactorRef(), nodeDirectThreat.getWrappedFactorRef());
		
		FactorSet foundNodes = chainManager.findAllFactorsRelatedToThisObjective(objectiveId1);
		
		assertEquals("didn't find anything?", 2, foundNodes.size());
		assertContains("missing direct threat?", nodeDirectThreat.getUnderlyingObject(), foundNodes);
		assertContains("missing contributing factor?", nodeContributingFactor.getUnderlyingObject(), foundNodes);
		
		
	}
	
	public void testFindAllNodesRelatedToThisIndicator() throws Exception
	{
		FactorCell nodeContributingFactor = project.createFactorCell(ObjectType.CAUSE);
		FactorCell nodeDirectThreat = project.createFactorCell(ObjectType.CAUSE);
		
		IndicatorId indicatorId1 = (IndicatorId)project.createObjectAndReturnId(ObjectType.INDICATOR);
		IdList indicators1 = new IdList(Indicator.getObjectType());
		indicators1.add(indicatorId1);
		nodeContributingFactor.getUnderlyingObject().setIndicators(indicators1);
		
		createLinkage(BaseId.INVALID, nodeContributingFactor.getWrappedFactorRef(), nodeDirectThreat.getWrappedFactorRef());
		
		FactorSet foundNodes = chainManager.findAllFactorsRelatedToThisIndicator(indicatorId1);
		
		assertEquals("didn't find anything?", 2, foundNodes.size());
		assertContains("missing direct threat?", nodeDirectThreat.getUnderlyingObject(), foundNodes);
		assertContains("missing contributing factor?", nodeContributingFactor.getUnderlyingObject(), foundNodes);
		
		
	}
	
	public void testDirectThreatSet() throws Exception
	{
		FactorCell nodeContributingFactor = project.createFactorCell(ObjectType.CAUSE);
		FactorCell nodeDirectThreatA = project.createFactorCell(ObjectType.CAUSE);	
		
		FactorCell target = project.createFactorCell(ObjectType.TARGET);
		CreateFactorLinkParameter parameter1 = new CreateFactorLinkParameter(nodeDirectThreatA.getWrappedFactorRef(), target.getWrappedFactorRef());
		project.createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, parameter1);
		
		FactorCell nodeDirectThreatB = project.createFactorCell(ObjectType.CAUSE);
		CreateFactorLinkParameter parameter2 = new CreateFactorLinkParameter(nodeDirectThreatB.getWrappedFactorRef(), target.getWrappedFactorRef());
		project.createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, parameter2);
		
		FactorSet allNodes = new FactorSet();
		allNodes.attemptToAdd(nodeContributingFactor.getUnderlyingObject());
		allNodes.attemptToAdd(nodeDirectThreatA.getUnderlyingObject());
		allNodes.attemptToAdd(nodeDirectThreatB.getUnderlyingObject());	
		
		DirectThreatSet foundNodes = new DirectThreatSet(allNodes);
		
		assertEquals("didn't find both nodes?", 2, foundNodes.size());
		assertContains("missing nodeDirectThreatA? ", nodeDirectThreatA.getUnderlyingObject(), foundNodes);
		assertContains("missing nodeDirectThreatB?", nodeDirectThreatB.getUnderlyingObject(), foundNodes);
	}
	  
	public void testOpenProject() throws Exception
	{
		int memorizedHighestId = -1;
		
		ORef factorRef;
		File tempDir = createTempDirectory();
		Project diskProject = new Project();
		diskProject.createOrOpen(tempDir);
		
		ORef diagramObjectRef = diskProject.createObject(ConceptualModelDiagram.getObjectType());
		DiagramObject diagramObject = ConceptualModelDiagram.find(diskProject, diagramObjectRef);
		try
		{
			DiagramFactor cause = createNodeAndAddToDiagram(diskProject, diagramObject, ObjectType.CAUSE);
			factorRef = cause.getWrappedORef();
			DiagramFactor target = createNodeAndAddToDiagram(diskProject, diagramObject, ObjectType.TARGET);
			
			LinkCreator linkCreator = new LinkCreator(diskProject);
			linkCreator.createFactorLinkAndAddToDiagramUsingCommands(diagramObject, cause, target);
			
			FactorId interventionId = (FactorId)diskProject.createObjectAndReturnId(ObjectType.STRATEGY);
			Factor object = (Factor) diskProject.findObject(new ORef(ObjectType.STRATEGY, interventionId));
			diskProject.deleteObject(object);
	
			DiagramFactor diagramFactor = createNodeAndAddToDiagram(diskProject, diagramObject, ObjectType.CAUSE);
			deleteNodeAndRemoveFromDiagram(diagramObject, diagramFactor);
			
			createNodeAndAddToDiagram(diskProject, diagramObject, TextBox.getObjectType());
		}
		finally
		{
			memorizedHighestId = diskProject.getAnnotationIdAssigner().getHighestAssignedId();
			diskProject.close();
		}
		
		Project loadedProject = new Project();
		loadedProject.createOrOpen(tempDir);
		try
		{
			assertEquals("didn't read cause pool?", 1, loadedProject.getCausePool().size());
			assertEquals("didn't read strategy pool?", 0, loadedProject.getStrategyPool().size());
			assertEquals("didn't read target pool?", 1, loadedProject.getTargetPool().size());
			assertEquals("didn't read text box pool?", 1, loadedProject.getTextBoxPool().size());
			
			ORef conceptualModelRef = loadedProject.getConceptualModelDiagramPool().getORefList().getRefForType(ConceptualModelDiagram.getObjectType());
			ConceptualModelDiagram conceptualModel = ConceptualModelDiagram.find(loadedProject, conceptualModelRef);
			assertEquals("didn't read link pool?", 1, loadedProject.getFactorLinkPool().size());
			assertEquals("didn't populate diagram?", 3, conceptualModel.getAllDiagramFactorRefs().size());
			assertEquals("didn't preserve next node id?", memorizedHighestId, loadedProject.getNodeIdAssigner().getHighestAssignedId());
			assertEquals("didn't preserve next annotation id?", memorizedHighestId, loadedProject.getAnnotationIdAssigner().getHighestAssignedId());
			Cause factor = Cause.find(loadedProject, factorRef);
			assertTrue("didn't update factor target count?", factor.isDirectThreat());
		}
		finally
		{
			loadedProject.close();
			DirectoryUtils.deleteEntireDirectoryTree(tempDir);
		}
		
		assertEquals("didn't clear node cause pool?", 0, diskProject.getCausePool().size());
		assertEquals("didn't clear node strategy pool?", 0, diskProject.getStrategyPool().size());
		assertEquals("didn't clear node target pool?", 0, diskProject.getTargetPool().size());
		assertEquals("didn't clear conceptual model diagram pool?", 0, diskProject.getConceptualModelDiagramPool().size());
		assertEquals("didn't clear link pool?", 0, diskProject.getFactorLinkPool().size());
		assertTrue("didn't clear next annotation id?", diskProject.getAnnotationIdAssigner().getHighestAssignedId() < memorizedHighestId);
	}
	
	private void deleteNodeAndRemoveFromDiagram(DiagramObject diagramObject, DiagramFactor diagramFactor) throws Exception
	{
		FactorDeleteHelper factorHelper = FactorDeleteHelper.createFactorDeleteHelperForNonSelectedFactors(diagramObject);
		factorHelper.deleteFactorAndDiagramFactor(diagramFactor);
	}
	
	public void atestCreateNewProject() throws Exception
	{
		File tempDir = createTempDirectory();
		Project newProject = new Project();
		newProject.createOrOpen(tempDir);
		
		try
		{
			assertEquals("default criterion not created?", 3, newProject.getSimpleThreatRatingFramework().getCriteria().length);
			assertEquals("default valueoptions not created?", 5, newProject.getSimpleThreatRatingFramework().getValueOptions().length);
			assertEquals("default currency not set?", "$", newProject.getMetadata().getData(ProjectMetadata.TAG_CURRENCY_SYMBOL));
		}
		finally
		{
			newProject.close();
			DirectoryUtils.deleteEntireDirectoryTree(tempDir);
		}
	}
	
	private DiagramLink createLinkage(BaseId id, ORef fromFactorRef, ORef toFactorRef) throws Exception
	{
		DiagramFactor fromDiagramFactor = project.getDiagramModel().getFactorCellByWrappedRef(fromFactorRef).getDiagramFactor();
		DiagramFactor toDiagramFactor = project.getDiagramModel().getFactorCellByWrappedRef(toFactorRef).getDiagramFactor();
		CreateFactorLinkParameter parameter = new CreateFactorLinkParameter(fromDiagramFactor.getWrappedORef(), toDiagramFactor.getWrappedORef());
		FactorLinkId createdId = (FactorLinkId)project.createObject(ObjectType.FACTOR_LINK, id, parameter);
		
		CreateDiagramFactorLinkParameter extraInfo = new CreateDiagramFactorLinkParameter(createdId, fromDiagramFactor.getDiagramFactorId(), toDiagramFactor.getDiagramFactorId());
		ORef diagramLinkRef = project.createObject(DiagramLink.getObjectType(), extraInfo);
		
		DiagramObject diagramObject = project.getTestingDiagramObject();
		CommandSetObjectData addLink = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkRef.getObjectId());
		project.executeCommand(addLink);

		return project.getDiagramModel().getDiagramLinkByRef(diagramLinkRef);
	}

	public DiagramFactor createNodeAndAddToDiagram(Project projectToUse, DiagramObject diagramObject, int nodeType) throws Exception
	{
		FactorCommandHelper commandHelper = new FactorCommandHelper(projectToUse, diagramObject);
		CommandCreateObject createCommand = commandHelper.createFactorAndDiagramFactor(nodeType);
		DiagramFactorId diagramFactorId = (DiagramFactorId) createCommand.getCreatedId();
		DiagramFactor diagramFactor = (DiagramFactor) projectToUse.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorId);
		
		return diagramFactor;
	}
	
	public void testEnsureAllDiagramFactorsAreVisible() throws Exception
	{
		verifyOnScreenLocation(new Point(50, 50), new Point(50, 50));
		verifyOnScreenLocation(new Point(0, 0), new Point(0, 0));
		verifyOnScreenLocation(new Point(0, 0), new Point(-10, 0));
		verifyOnScreenLocation(new Point(0, 0), new Point(0, -10));
		verifyOnScreenLocation(new Point(10, 0), new Point(10, 0));
		verifyOnScreenLocation(new Point(0, 10), new Point(0, 10));
	}

	private void verifyOnScreenLocation(Point expectedPoint, Point initialPoint) throws Exception
	{
		DiagramFactor onScreenDiagramFactor = project.createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		CommandSetObjectData setOnScreenLocation = new CommandSetObjectData(onScreenDiagramFactor.getRef(), DiagramFactor.TAG_LOCATION, EnhancedJsonObject.convertFromPoint(initialPoint));
		getProject().executeCommand(setOnScreenLocation);
		
		project.ensureAllDiagramFactorsAreVisible();
		DiagramFactor alreadyOnScreenDiagramFactor = (DiagramFactor) project.findObject(onScreenDiagramFactor.getRef());
		assertEquals("did not move off screen diagram factor on screen?", expectedPoint, alreadyOnScreenDiagramFactor.getLocation());
	}

	public ProjectForTesting getProject()
	{
		return project;
	}
	
	private ProjectForTesting project;
	private IdAssigner idAssigner;
	private ChainManager chainManager;
}
