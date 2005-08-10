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
		Node nodeA = new Node(Node.TYPE_THREAT);
		Node nodeB = new Node(Node.TYPE_THREAT);
		Linkage linkage = new Linkage(nodeA, nodeB);
		new LinkageData(linkage);

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
