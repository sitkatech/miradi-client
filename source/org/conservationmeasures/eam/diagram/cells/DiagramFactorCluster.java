/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;
import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objects.FactorCluster;
import org.jgraph.graph.GraphConstants;

public class DiagramFactorCluster extends DiagramNode
{
	public DiagramFactorCluster(DiagramNodeId idToUse, FactorCluster cmGroup)
	{
		super(idToUse, cmGroup);
		GraphConstants.setGroupOpaque(getAttributes(), true);
	}

	public Color getColor()
	{
		return DiagramConstants.COLOR_CLUSTER;
	}

	public Command[] buildCommandsToClear()
	{
		Vector commands = new Vector();
		commands.add(new CommandSetObjectData(getWrappedType(), getWrappedId(), FactorCluster.TAG_MEMBER_IDS, new IdList().toString()));
		commands.addAll(Arrays.asList(super.buildCommandsToClear()));
		return (Command[])commands.toArray(new Command[0]);
	}

}
