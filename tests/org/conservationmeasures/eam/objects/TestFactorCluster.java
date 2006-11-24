/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestFactorCluster extends ObjectTestCase
{
	public TestFactorCluster(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		FactorId id = new FactorId(5);
		FactorCluster group = new FactorCluster(id);
		assertEquals("wrong id?", id, group.getId());
		
		IdList members = new IdList();
		members.add(3);
		members.add(91);
		group.setData(FactorCluster.TAG_MEMBER_IDS, members.toString());
		assertEquals("didn't save members?", members.toString(), group.getData(FactorCluster.TAG_MEMBER_IDS));
		
		FactorCluster got = (FactorCluster)EAMBaseObject.createFromJson(group.getType(), group.toJson());
		assertEquals("didn't jsonize id?", group.getId(), got.getId());
		assertEquals("didn't jsonize members?", group.getMemberIds(), got.getMemberIds());
	}
	
	public void testFields() throws Exception
	{
		CreateModelNodeParameter extraInfo = new CreateModelNodeParameter(Factor.TYPE_CLUSTER);
		verifyFields(ObjectType.MODEL_NODE, extraInfo);
	}
}
