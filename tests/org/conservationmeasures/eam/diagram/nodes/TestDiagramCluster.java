/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objects.ConceptualModelCluster;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramCluster extends EAMTestCase
{
	public TestDiagramCluster(String name)
	{
		super(name);
	}

	public void testBuildCommandsToClear() throws Exception
	{
		ConceptualModelCluster cmCluster = new ConceptualModelCluster(new BaseId(94));
		IdList memberIds = new IdList(new BaseId[] {new BaseId(2), new BaseId(27), new BaseId(4), new BaseId(25)});
		cmCluster.setData(ConceptualModelCluster.TAG_MEMBER_IDS, memberIds.toString());

		DiagramCluster cluster = new DiagramCluster(cmCluster);
		Command[] commands = cluster.buildCommandsToClear();
		CommandSetObjectData memberClearing = (CommandSetObjectData)commands[0];
		assertEquals(new IdList().toString(), memberClearing.getDataValue());
	}
}
