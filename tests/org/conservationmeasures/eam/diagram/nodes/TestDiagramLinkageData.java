package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.testall.EAMTestCase;
import org.json.JSONObject;

public class TestDiagramLinkageData extends EAMTestCase
{
	public TestDiagramLinkageData(String name) 
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		int id = 1;
		LinkageData linkageData = createSampleLinkageData(id);
		assertEquals("Id not the same?", id, linkageData.getId());
		assertEquals("From Node Ids don't match", nodeBId, linkageData.getFromNodeId());
		assertEquals("To Node Ids don't match", nodeAId, linkageData.getToNodeId());
	}

	public void testToJson() throws Exception
	{
		int id = 1;
		LinkageData original = createSampleLinkageData(id);
		JSONObject json = original.toJson();
		LinkageData gotBack = new LinkageData(json);
		assertEquals("wrong id?", original.getId(), gotBack.getId());
		assertEquals("wrong from?", original.getFromNodeId(), gotBack.getFromNodeId());
		assertEquals("wrong to?", original.getToNodeId(), gotBack.getToNodeId());
	}
	
	private LinkageData createSampleLinkageData(int id) throws Exception
	{
		ConceptualModelIntervention cmIntervention = new ConceptualModelIntervention();
		ConceptualModelTarget cmTarget = new ConceptualModelTarget();

		nodeAId = 1;
		DiagramNode nodeA = DiagramNode.wrapConceptualModelObject(cmIntervention);
		nodeA.setId(nodeAId);
		
		nodeBId = 2;
		DiagramNode nodeB = DiagramNode.wrapConceptualModelObject(cmTarget);
		nodeB.setId(nodeBId);
		
		DiagramLinkage linkage = new DiagramLinkage(nodeB, nodeA);
		linkage.setId(id);
		LinkageData linkageData = new LinkageData(linkage);
		return linkageData;
	}
	
	private int nodeAId;
	private int nodeBId;

}
