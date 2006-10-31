/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objects.ConceptualModelCluster;
import org.jgraph.graph.GraphConstants;

public class DiagramCluster extends DiagramNode
{
	public DiagramCluster(DiagramNodeId idToUse, ConceptualModelCluster cmGroup)
	{
		super(idToUse, cmGroup);
		GraphConstants.setGroupOpaque(getAttributes(), true);
	}

	public Color getColor()
	{
		return DiagramConstants.COLOR_CLUSTER;
	}

	public Rectangle getAnnotationsRect()
	{
		return getAnnotationsRect(0);
	}

	public Command[] buildCommandsToClear()
	{
		Vector commands = new Vector();
		commands.add(new CommandSetObjectData(getWrappedType(), getWrappedId(), ConceptualModelCluster.TAG_MEMBER_IDS, new IdList().toString()));
		commands.addAll(Arrays.asList(super.buildCommandsToClear()));
		return (Command[])commands.toArray(new Command[0]);
	}

}
