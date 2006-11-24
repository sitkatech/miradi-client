/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objects.FactorCluster;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramCluster extends EAMTestCase
{
	public TestDiagramCluster(String name)
	{
		super(name);
	}

	public void testBuildCommandsToClear() throws Exception
	{
		FactorCluster cmCluster = new FactorCluster(new ModelNodeId(94));
		IdList memberIds = new IdList(new ModelNodeId[] {new ModelNodeId(2), new ModelNodeId(27), new ModelNodeId(4), new ModelNodeId(25)});
		cmCluster.setData(FactorCluster.TAG_MEMBER_IDS, memberIds.toString());

		DiagramNodeId nodeId = new DiagramNodeId(44);
		DiagramCluster cluster = new DiagramCluster(nodeId, cmCluster);
		Command[] commands = cluster.buildCommandsToClear();
		CommandSetObjectData memberClearing = (CommandSetObjectData)commands[0];
		assertEquals(new IdList().toString(), memberClearing.getDataValue());
	}
}
