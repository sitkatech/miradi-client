package org.conservationmeasures.eam.diagram.objects;

import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.json.JSONObject;

public class TestConceptualModelLinkage extends EAMTestCase
{
	public TestConceptualModelLinkage(String name) 
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		ConceptualModelLinkage linkageData = new ConceptualModelLinkage(id, nodeAId, nodeBId);
		assertEquals("Id not the same?", id, linkageData.getId());
		assertEquals("From Node Ids don't match", nodeAId, linkageData.getFromNodeId());
		assertEquals("To Node Ids don't match", nodeBId, linkageData.getToNodeId());
	}

	public void testToJson() throws Exception
	{
		ConceptualModelLinkage original = new ConceptualModelLinkage(id, nodeAId, nodeBId);
		JSONObject json = original.toJson();
		ConceptualModelLinkage gotBack = new ConceptualModelLinkage(json);
		assertEquals("wrong id?", original.getId(), gotBack.getId());
		assertEquals("wrong from?", original.getFromNodeId(), gotBack.getFromNodeId());
		assertEquals("wrong to?", original.getToNodeId(), gotBack.getToNodeId());
	}
	
	static final int id = 1;
	static final int nodeAId = 2;
	static final int nodeBId = 3;

}
