package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
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
		original.setData(ConceptualModelLinkage.TAG_STRESS_LABEL, "What stress!");
		JSONObject json = original.toJson();
		ConceptualModelLinkage gotBack = new ConceptualModelLinkage(json);
		assertEquals("wrong id?", original.getId(), gotBack.getId());
		assertEquals("wrong from?", original.getFromNodeId(), gotBack.getFromNodeId());
		assertEquals("wrong to?", original.getToNodeId(), gotBack.getToNodeId());
		assertEquals("wrong stress label?", original.getStressLabel(), gotBack.getStressLabel());
	}
	
	public void testStressLabel() throws Exception
	{
		String sampleStressLabel = "A Stress";
		ConceptualModelLinkage linkage = new ConceptualModelLinkage(id, nodeAId, nodeBId);
		assertEquals("Didn't default to blank stress?", "", linkage.getStressLabel());
		linkage.setData(ConceptualModelLinkage.TAG_STRESS_LABEL, sampleStressLabel);
		assertEquals("Didn't set stress?", sampleStressLabel, linkage.getStressLabel());
		assertEquals("getData didn't work?", sampleStressLabel, linkage.getData(ConceptualModelLinkage.TAG_STRESS_LABEL));
		
	}
	
	static final BaseId id = new BaseId(1);
	static final ModelNodeId nodeAId = new ModelNodeId(2);
	static final ModelNodeId nodeBId = new ModelNodeId(3);

}
