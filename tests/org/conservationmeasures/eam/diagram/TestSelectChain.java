/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.util.HashSet;
import java.util.Set;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class TestSelectChain  extends EAMTestCase
{
	public TestSelectChain(String name)
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
		super.tearDown();
	}

	
	private Factor findFactor(String label)
	{
		DiagramFactor[] diagramFactors = project.getAllDiagramFactors();
		for (int i = 0; i < diagramFactors.length; ++i)
		{
			FactorId factorId = diagramFactors[i].getWrappedId();
			Factor factor = (Factor) project.findObject(ObjectType.FACTOR, factorId);
			if (factor.getLabel().equals(label))
				return factor;
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
	
	
	//TODO: to selectChainBasedOnLinkSelection new code to be done
	public void testSelectChain() throws Exception
	{
		int[] linkagePairs = getLinkagePairs();
		SampleDiagramBuilder.buildNodeGrid(project, 7, linkagePairs);
		int[][] expectedNodesInChain = getExpectedNodesInChain();
				
		for(int threatIndex = 0; threatIndex < expectedNodesInChain.length; ++threatIndex)
		{
			int[] expectedChainNodeIds = expectedNodesInChain[threatIndex];
			String label = Integer.toString(expectedChainNodeIds[0]);
			Factor cmNode = findFactor(label);
			FactorSet gotChainNodes = model.getDirectThreatChainNodes(cmNode);
			Set gotLabels = getLabelSet(gotChainNodes);
			assertEquals("wrong direct threat chain nodes for " + expectedChainNodeIds[0] + "?", toSet(expectedChainNodeIds), gotLabels);
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
		Factor[] factors = gotChainNodes.toNodeArray();
		HashSet set = new HashSet();
		for (int i = 0; i < factors.length; ++i)
		{
			set.add(factors[i].getLabel());
		}
		
		return set;
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