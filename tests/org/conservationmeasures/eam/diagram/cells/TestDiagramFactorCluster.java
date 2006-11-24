/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorCluster;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objects.FactorCluster;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramFactorCluster extends EAMTestCase
{
	public TestDiagramFactorCluster(String name)
	{
		super(name);
	}

	public void testBuildCommandsToClear() throws Exception
	{
		FactorCluster cmCluster = new FactorCluster(new FactorId(94));
		IdList memberIds = new IdList(new FactorId[] {new FactorId(2), new FactorId(27), new FactorId(4), new FactorId(25)});
		cmCluster.setData(FactorCluster.TAG_MEMBER_IDS, memberIds.toString());

		DiagramFactorId nodeId = new DiagramFactorId(44);
		DiagramFactorCluster cluster = new DiagramFactorCluster(nodeId, cmCluster);
		Command[] commands = cluster.buildCommandsToClear();
		CommandSetObjectData memberClearing = (CommandSetObjectData)commands[0];
		assertEquals(new IdList().toString(), memberClearing.getDataValue());
	}
}
