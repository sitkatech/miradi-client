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
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.miradi.diagram.cells.FactorCell;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdAssigner;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.Factor;
import org.miradi.project.ProjectForTesting;
import org.miradi.views.diagram.LinkCreator;

public class TestDiagramModel extends EAMTestCase
{
	public TestDiagramModel(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		model = project.getDiagramModel();
		idAssigner = new IdAssigner();
	}

	public void tearDown() throws Exception
	{
		project.close();
		project = null;

		super.tearDown();
	}
	
	public void testRecursivelyGetNonOverlappingFactorPoint() throws Exception
	{
		DiagramFactor cause1 = project.createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		Point point1 = new Point(15, 15);
		cause1.setLocation(point1);
		
		DiagramFactor cause2 = project.createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		Point point2 = new Point(30, 30);
		cause2.setLocation(point2);
		
		DiagramFactor cause3 = project.createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		Point point3 = new Point(60, 60);
		cause3.setLocation(point3);
		
		Point nonOverlappingPoint = model.recursivelyGetNonOverlappingFactorPoint(point1);
		assertEquals("wrong point?", new Point(45, 45), nonOverlappingPoint);
	}
	
	public void testGetNodesInChain() throws Exception
	{
		int[] linkagePairs = getLinkagePairs();
		SampleDiagramBuilder.buildNodeGrid(project, 7, linkagePairs);
		int[][] expectedNodesInChain = getExpectedNodesInChain();
		
		for(int threatIndex = 0; threatIndex < expectedNodesInChain.length; ++threatIndex)
		{
			int[] expectedChainNodeIds = expectedNodesInChain[threatIndex];
			String label = Integer.toString(expectedChainNodeIds[0]);
			DiagramFactor diagramFactor = findDiagramFactor(label);
			
			FactorSet gotChainNodes = model.getNodesInChain(diagramFactor);
			FactorSet foundNodes = findNodes(expectedChainNodeIds);
			assertEquals("wrong direct threat chain nodes for " + expectedChainNodeIds[0] + "?", foundNodes, gotChainNodes);
		}
	}
	
	private Factor findFactor(String label)
	{
		DiagramFactor[] diagramFactors = project.getAllDiagramFactors();
		for (int i = 0; i < diagramFactors.length; ++i)
		{
			ORef factorRef = diagramFactors[i].getWrappedORef();
			Factor factor = (Factor) project.findObject(factorRef);
			if (factor.getLabel().equals(label))
				return factor;
		}
		
		return null;
	}

	private DiagramFactor findDiagramFactor(String label)
	{
		DiagramFactor[] diagramFactors = project.getAllDiagramFactors();
		for (int i = 0; i < diagramFactors.length; ++i)
		{
			ORef factorRef = diagramFactors[i].getWrappedORef();
			Factor factor = (Factor) project.findObject(factorRef);
			if (factor.getLabel().equals(label))
				return diagramFactors[i];
		}
		
		return null;
	}

	private int[][] getExpectedNodesInChain()
	{
		int[][] expectedNodesInChain = {
				{ 31,  11, 12, 13, 21, 22, 41 },
				{ 32,  13, 23, 41 },
				{ 33,  13, 23, 42, 43 },
				{ 34,  24, 25, 26, 35, 36, 44 },
				{ 35,  25, 26, 34, 36, 45 },
				// 36 is actually an INDIRECT FACTOR!
		};
		return expectedNodesInChain;
		
	}
	
	private int[] getLinkagePairs()
	{
		int[] linkagePairs = {
				11, 21,		21, 31, 	31, 41,
				12, 21, 	22, 31, 	32, 41,
				13, 22, 	23, 32, 	33, 42,
				13, 23, 	23, 33, 	33, 43,
				24, 34, 	34, 44,
				25, 35, 	35, 45,
				26, 36, 	36, 35, 	35, 34,
		};
		return linkagePairs;
	}
	
	
	private int[] getLoopLinkagePairs()
	{
		int[] linkagePairs = {
				11, 21,		21, 31, 	31, 41,		41, 11,  	31,11
		};
		return linkagePairs;
	}
	
	private int[][] getExpectedNodesInLoopChain()
	{
		int[][] expectedNodesInLoopChain = {
				{ 11, 21, 31, 41,}
		};
		return expectedNodesInLoopChain;
	}
	
	public void testLoopChainIds() throws Exception
	{
		int[] linkagePairs = getLoopLinkagePairs();
		SampleDiagramBuilder.buildNodeGrid(project, 1, linkagePairs);
		int[][] expectedNodesInChain = getExpectedNodesInLoopChain();

		for(int nodeIndex = 0; nodeIndex < expectedNodesInChain.length; ++nodeIndex)
		{
			int[] expectedChainNodeIds = expectedNodesInChain[nodeIndex];
			for (int i=0; i<expectedChainNodeIds.length; ++i) 
			{
				String label = Integer.toString(expectedChainNodeIds[i]);
				DiagramFactor diagramFactor = findDiagramFactor(label);
				
				FactorSet gotUpChainNodes = model.getAllUpstreamNodes(diagramFactor);
				Set gotLabels1 = getLabelSet(gotUpChainNodes);
				assertEquals("wrong upstream chain nodes for " + expectedChainNodeIds[i] + "?", toSet(expectedChainNodeIds), gotLabels1);
				
				FactorSet gotDownChainNodes = model.getAllDownstreamNodes(diagramFactor);
				Set gotLabels2 = getLabelSet(gotDownChainNodes);
				assertEquals("wrong downstream chain nodes for " + expectedChainNodeIds[i] + "?", toSet(expectedChainNodeIds), gotLabels2);
			}
		}
	}
	
	private Set toSet(int[] ints)
	{
		HashSet set = new HashSet();
		for (int i = 0; i < ints.length; ++i)
		{
			set.add(Integer.toString(ints[i]));
		}
		
		return set;
	}
	
	private Set getLabelSet(FactorSet gotChainNodes)
	{
		Factor[] factors = gotChainNodes.toFactorArray();
		HashSet set = new HashSet();
		for (int i = 0; i < factors.length; ++i)
		{
			set.add(factors[i].getLabel());
		}
		
		return set;
	}

	public FactorSet findNodes(int[] values)
	{
		FactorSet result = new FactorSet();
		for(int i = 0; i < values.length; ++i)
		{	
			String valueAsString = Integer.toString(values[i]);
			result.attemptToAdd(findFactor(valueAsString));
		}
		
		return result;
	}

	public void testIsNode() throws Exception
	{
		FactorCell factor = project.createFactorCell(ObjectType.CAUSE);
		FactorCell target = project.createFactorCell(ObjectType.TARGET);
		assertTrue("factor isn't a node?", factor.isFactor());
		assertTrue("target isn't a node?", target.isFactor());
	}
	
	public void testCounts()throws Exception
	{
		FactorCell factor = project.createFactorCell(ObjectType.CAUSE);
		FactorCell target = project.createFactorCell(ObjectType.TARGET);
		createLinkage(new FactorLinkId(BaseId.INVALID.asInt()), factor.getDiagramFactor(), target.getDiagramFactor());
		assertEquals(2, model.getFactorCount());
		assertEquals(1, model.getFactorLinkCount());
	}
	
	public void testHasLinkage() throws Exception
	{
		DiagramFactor diagramFactor = project.createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		model.removeDiagramFactor(diagramFactor.getRef());
		DiagramFactor cause = project.createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		DiagramFactor target = project.createDiagramFactorAndAddToDiagram(ObjectType.TARGET);
		assertFalse("already linked?", model.areDiagramFactorsLinked(cause.getRef(), target.getRef()));
		createLinkage(new FactorLinkId(BaseId.INVALID.asInt()), cause, target);
		
		assertTrue("not linked?", model.areDiagramFactorsLinked(cause.getRef(), target.getRef()));
		assertTrue("reverse link not detected?", model.areDiagramFactorsLinked(target.getRef(), cause.getRef()));
	}
	
	public void testGetLinkages() throws Exception
	{
		FactorCell factor1 = project.createFactorCell(ObjectType.CAUSE);
		FactorCell factor2 = project.createFactorCell(ObjectType.CAUSE);
		FactorCell target = project.createFactorCell(ObjectType.TARGET);
		DiagramLink linkage1 = createLinkage(takeNextLinkageId(), factor1.getDiagramFactor(), target.getDiagramFactor());
		DiagramLink linkage2 = createLinkage(takeNextLinkageId(), factor2.getDiagramFactor(), target.getDiagramFactor());
		Set found = model.getFactorLinks(target);
		assertEquals("Didn't see both links?", 2, found.size());
		assertTrue("missed first?", found.contains(model.findLinkCell(linkage1)));
		assertTrue("missed second?", found.contains(model.findLinkCell(linkage2)));
	}

	private FactorLinkId takeNextLinkageId()
	{
		return new FactorLinkId(idAssigner.takeNextId().asInt());
	}
	
	
	public void testCreateNode() throws Exception
	{
		project.createFactorCell(ObjectType.TARGET);		
		DiagramFactor diagramFactor = project.createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		project.createFactorCell(ObjectType.TARGET);
		FactorCell lastCreated = project.createFactorCell(ObjectType.TARGET);		
		model.removeDiagramFactor(diagramFactor.getRef());
		FactorCell nodeAfterUndo = project.createFactorCell(ObjectType.TARGET);
		
		assertTrue("reused an id?", nodeAfterUndo.getDiagramFactorId().asInt() > lastCreated.getDiagramFactorId().asInt());
	}

	public void testGetAllNodes() throws Exception
	{
		DiagramFactor node1 = project.createDiagramFactorAndAddToDiagram(ObjectType.TARGET);
		DiagramFactor node2 = project.createDiagramFactorAndAddToDiagram(ObjectType.TARGET);		
		createLinkage(new FactorLinkId(BaseId.INVALID.asInt()), node1, node2);
		DiagramFactor node3 = project.createDiagramFactorAndAddToDiagram(ObjectType.TARGET);		
		
		DiagramFactor[] diagramFactors = project.getAllDiagramFactors();
		assertEquals(3, diagramFactors.length);
		assertContains(node1, diagramFactors);
		assertContains(node2, diagramFactors);
		assertContains(node3, diagramFactors);
	}
	
	public void testGetAllLinkages() throws Exception
	{
		FactorCell node1 = project.createFactorCell(ObjectType.TARGET);		
		FactorCell node2 = project.createFactorCell(ObjectType.TARGET);		
		DiagramLink link1 = createLinkage(takeNextLinkageId(), node1.getDiagramFactor(), node2.getDiagramFactor());
		FactorCell node3 = project.createFactorCell(ObjectType.TARGET);		
		DiagramLink link2 = createLinkage(takeNextLinkageId(), node1.getDiagramFactor(), node3.getDiagramFactor());
		
		Vector linkages = model.getAllDiagramFactorLinks();
		assertEquals(2, linkages.size());
		assertContains(link1, linkages);
		assertContains(link2, linkages);
	}
	
	public void testFillFrom() throws Exception
	{
		FactorCell node1 = project.createFactorCell(ObjectType.TARGET);		
		FactorCell node2 = project.createFactorCell(ObjectType.TARGET);		
		DiagramLink link1 = createLinkage(takeNextLinkageId(), node1.getDiagramFactor(), node2.getDiagramFactor());

		DiagramModel copy = new DiagramModel(project);
		copy.fillFrom(model.getDiagramObject());
		
		assertNotNull("missing node1?", copy.getFactorCellByRef(node1.getDiagramFactorRef()));
		assertNotNull("missing node2?", copy.getFactorCellByRef(node2.getDiagramFactorRef()));
		assertNotNull("missing link?", copy.getDiagramLinkByRef(link1.getRef()));
	}
	
	public void testActionsFiring() throws Exception
	{
		TestTableModel testModel = new TestTableModel();
		testModel.addListener(model);
		DiagramFactor diagramFactor = project.createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		
		assertEquals("test model wasn't notified of add action?", 1, testModel.nodeAdded);
		assertEquals("node changed already called?", 0, testModel.nodeChanged);
		assertEquals("node deleted already called?", 0, testModel.nodeDeleted);
		assertEquals("node add did a node move notify?", 0, testModel.nodeMoved);
		assertEquals("added already called?", 0, testModel.linkAdded);
		assertEquals("linkage deleted already called?", 0, testModel.linkDeleted);
		
		model.updateDiagramFactor(diagramFactor.getRef());
		assertEquals("update did a node add notify?", 1, testModel.nodeAdded);
		assertEquals("update didn't do a node changed notify?", 1, testModel.nodeChanged);
		assertEquals("test model fired a delete action for a modify?", 0, testModel.nodeDeleted);
		assertEquals("update did a node move notify?", 0, testModel.nodeMoved);
		assertEquals("update did a link add notify?", 0, testModel.linkAdded);
		assertEquals("update did a linkdeleted notify?", 0, testModel.linkDeleted);
		
		model.removeDiagramFactor(diagramFactor.getRef());
		assertEquals("delete node didn't notify?", 1, testModel.nodeDeleted);
		assertEquals("delete node triggered a node add notify?", 1, testModel.nodeAdded);
		assertEquals("test model modify action called for delete?", 1, testModel.nodeChanged);
		assertEquals("node delete did a node move notify?", 0, testModel.nodeMoved);
		assertEquals("delete node did a link add notify?", 0, testModel.linkAdded);
		assertEquals("delete node did a link delete notify?", 0, testModel.linkDeleted);

		FactorCell node2 = project.createFactorCell(ObjectType.TARGET);
		FactorCell node3 = project.createFactorCell(ObjectType.TARGET);
		DiagramLink link1 = createLinkage(new FactorLinkId(BaseId.INVALID.asInt()), node2.getDiagramFactor(), node3.getDiagramFactor());
		assertEquals("didn't do more node add notify's?", 3, testModel.nodeAdded);
		assertEquals("add link did a node delete notify?", 1, testModel.nodeDeleted);
		assertEquals("add link did a node change notify?", 1, testModel.nodeChanged);
		assertEquals("link add did a node move notify?", 0, testModel.nodeMoved);
		assertEquals("test model linkageAdded action not called for addition of a link?", 1, testModel.linkAdded);
		assertEquals("add link did a delete link notify?", 0, testModel.linkDeleted);

		model.deleteDiagramFactorLink(link1);
		assertEquals("link delete did a node add notify?", 3, testModel.nodeAdded);
		assertEquals("link delete did a node delete notify?", 1, testModel.nodeDeleted);
		assertEquals("link delete did a node change notify?", 1, testModel.nodeChanged);
		assertEquals("link delete did a node move notify?", 0, testModel.nodeMoved);
		assertEquals("link delete did a link add notify?", 1, testModel.linkAdded);
		assertEquals("test model linkDeleted action not called?",1, testModel.linkDeleted);
		
		ORefList diagramFactorRefs = new ORefList();
		diagramFactorRefs.add(node2.getDiagramFactorRef());
		diagramFactorRefs.add(node3.getDiagramFactorRef());
		model.factorsWereMoved(diagramFactorRefs);
		assertEquals("move nodes did a node add notify?", 3, testModel.nodeAdded);
		assertEquals("move nodes did a node delete notify?", 1, testModel.nodeDeleted);
		assertEquals("move nodes did a node change notify?", 1, testModel.nodeChanged);
		assertEquals("node move didn't do all node move notify's?", 2, testModel.nodeMoved);
		assertEquals("move nodes did a link add notify?", 1, testModel.linkAdded);
		assertEquals("move nodes did a link delete notify?", 1, testModel.linkDeleted);
		
	}
	
	private DiagramLink createLinkage(FactorLinkId id, DiagramFactor fromDiagramFactor, DiagramFactor toDiagramFactor) throws Exception
	{
		LinkCreator linkCreator = new LinkCreator(project);
		ORef factorLinkRef = linkCreator.createFactorLinkAndAddToDiagramUsingCommands(project.getDiagramModel(), fromDiagramFactor, toDiagramFactor);
		DiagramLink diagramLink = project.getDiagramModel().getDiagramLinkByWrappedRef(factorLinkRef);
		
		return diagramLink;
	}
	
	static class TestTableModel implements DiagramModelListener
	{
		
		public void addListener(DiagramModel model)
		{
			model.addDiagramModelListener(this);
		}

		public void factorAdded(DiagramModelEvent event) 
		{
			nodeAdded++;
		}

		public void factorDeleted(DiagramModelEvent event) 
		{
			nodeDeleted++;
		}

		public void factorChanged(DiagramModelEvent event) 
		{
			nodeChanged++;
		}
		
		public void factorMoved(DiagramModelEvent event) 
		{
			nodeMoved++;
		}

		public void linkAdded(DiagramModelEvent event) 
		{
			linkAdded++;
		}

		public void linkDeleted(DiagramModelEvent event) 
		{
			linkDeleted++;
		}

		int nodeAdded = 0;
		int nodeDeleted = 0;
		int nodeChanged = 0;
		int linkAdded = 0;
		int linkDeleted = 0;
		int nodeMoved = 0;
	}

	ProjectForTesting project;
	DiagramModel model;
	IdAssigner idAssigner;
}
