package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class TestFactorLink extends EAMTestCase
{
	public TestFactorLink(String name) 
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		FactorLink linkageData = new FactorLink(id, nodeAId, nodeBId);
		assertEquals("Id not the same?", id, linkageData.getId());
		assertEquals("From Node Ids don't match", nodeAId, linkageData.getFromFactorId());
		assertEquals("To Node Ids don't match", nodeBId, linkageData.getToFactorId());
	}

	public void testToJson() throws Exception
	{
		FactorLink original = new FactorLink(id, nodeAId, nodeBId);
		original.setData(FactorLink.TAG_STRESS_LABEL, "What stress!");
		EnhancedJsonObject json = original.toJson();
		FactorLink gotBack = (FactorLink)BaseObject.createFromJson(original.getType(), json);
		assertEquals("wrong id?", original.getId(), gotBack.getId());
		assertEquals("wrong from?", original.getFromFactorId(), gotBack.getFromFactorId());
		assertEquals("wrong to?", original.getToFactorId(), gotBack.getToFactorId());
		assertEquals("wrong stress label?", original.getStressLabel(), gotBack.getStressLabel());
	}
	
	public void testStressLabel() throws Exception
	{
		String sampleStressLabel = "A Stress";
		FactorLink linkage = new FactorLink(id, nodeAId, nodeBId);
		assertEquals("Didn't default to blank stress?", "", linkage.getStressLabel());
		linkage.setData(FactorLink.TAG_STRESS_LABEL, sampleStressLabel);
		assertEquals("Didn't set stress?", sampleStressLabel, linkage.getStressLabel());
		assertEquals("getData didn't work?", sampleStressLabel, linkage.getData(FactorLink.TAG_STRESS_LABEL));
		
	}
	
	public void testExtraInfo() throws Exception
	{
		FactorLink linkage = new FactorLink(new FactorLinkId(BaseId.INVALID.asInt()), nodeAId, nodeBId);

		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(nodeAId, nodeBId);
		CreateFactorLinkParameter gotExtraInfo = (CreateFactorLinkParameter)linkage.getCreationExtraInfo();
		assertEquals(extraInfo.getFromId(), gotExtraInfo.getFromId());
		assertEquals(extraInfo.getToId(), gotExtraInfo.getToId());
	}

	static final FactorLinkId id = new FactorLinkId(1);
	static final FactorId nodeAId = new FactorId(2);
	static final FactorId nodeBId = new FactorId(3);

}
