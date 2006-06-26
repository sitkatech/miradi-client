/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestConceptualModelCluster extends EAMTestCase
{
	public TestConceptualModelCluster(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		int id = 5;
		ConceptualModelCluster group = new ConceptualModelCluster(id);
		assertEquals("wrong id?", id, group.getId());
		
		IdList members = new IdList();
		members.add(3);
		members.add(91);
		group.setData(ConceptualModelCluster.TAG_MEMBER_IDS, members.toString());
		assertEquals("didn't save members?", members.toString(), group.getData(ConceptualModelCluster.TAG_MEMBER_IDS));
		
		ConceptualModelCluster got = new ConceptualModelCluster(group.toJson());
		assertEquals("didn't jsonize id?", group.getId(), got.getId());
		assertEquals("didn't jsonize members?", group.getMemberIds(), got.getMemberIds());
	}
}
