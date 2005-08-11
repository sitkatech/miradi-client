package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestLinkageData extends EAMTestCase
{
	public TestLinkageData(String name) 
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		int nodeAId = 1;
		Node nodeA = new Node(Node.TYPE_THREAT);
		nodeA.setId(nodeAId);
		
		int nodeBId = 2;
		Node nodeB = new Node(Node.TYPE_GOAL);
		nodeB.setId(nodeBId);
		
		Linkage linkage = new Linkage(nodeB, nodeA);
		int id = 1;
		linkage.setId(id);
		LinkageData linkageData = new LinkageData(linkage);
		assertEquals("Id not the same?", id, linkageData.getId());
		assertEquals("From Node Ids don't match", nodeBId, linkageData.getFromNodeId());
		assertEquals("To Node Ids don't match", nodeAId, linkageData.getToNodeId());
		
		try
		{
			new LinkageData(nodeA);
			fail("Should have thrown since this is not a linkage");
		}
		catch (Exception expectedException)
		{
		}
	}
	
}
