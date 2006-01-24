/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.project.LinkagePool;
import org.conservationmeasures.eam.project.NodePool;

public class DiagramModelForTesting extends DiagramModel
{
	public DiagramModelForTesting()
	{
		super(new NodePool(), new LinkagePool());
	}
	
	public NodePool getNodePool()
	{
		return nodePool;
	}
	
	public LinkagePool getLinkagePool()
	{
		return linkagePool;
	}
}
