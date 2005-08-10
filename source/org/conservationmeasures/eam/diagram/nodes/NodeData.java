/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

public class NodeData 
{
	public NodeData(EAMGraphCell cell) throws Exception
	{
		if(!cell.isNode())
			throw new Exception("EAMGraphCell not a Node");
	}
}
