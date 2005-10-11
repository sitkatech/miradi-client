package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramLinkageData extends EAMTestCase
{
	public TestDiagramLinkageData(String name) 
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		int nodeAId = 1;
		DiagramNode nodeA = new DiagramNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		nodeA.setId(nodeAId);
		
		int nodeBId = 2;
		DiagramNode nodeB = new DiagramNode(DiagramNode.TYPE_TARGET);
		nodeB.setId(nodeBId);
		
		DiagramLinkage linkage = new DiagramLinkage(nodeB, nodeA);
		int id = 1;
		linkage.setId(id);
		LinkageData linkageData = new LinkageData(linkage);
		assertEquals("Id not the same?", id, linkageData.getId());
		assertEquals("From Node Ids don't match", nodeBId, linkageData.getFromNodeId());
		assertEquals("To Node Ids don't match", nodeAId, linkageData.getToNodeId());
	}
	
}
