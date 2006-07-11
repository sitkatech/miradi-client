/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.objects.ConceptualModelCluster;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramCluster extends EAMTestCase
{
	public TestDiagramCluster(String name)
	{
		super(name);
	}

	public void testBuildCommandsToClear() throws Exception
	{
		ConceptualModelCluster cmCluster = new ConceptualModelCluster(94);
		IdList memberIds = new IdList(new int[] {2, 27, 4, 25});
		cmCluster.setData(ConceptualModelCluster.TAG_MEMBER_IDS, memberIds.toString());

		DiagramCluster cluster = new DiagramCluster(cmCluster);
		Command[] commands = cluster.buildCommandsToClear();
		CommandSetObjectData memberClearing = (CommandSetObjectData)commands[0];
		assertEquals(new IdList().toString(), memberClearing.getDataValue());
	}
}
