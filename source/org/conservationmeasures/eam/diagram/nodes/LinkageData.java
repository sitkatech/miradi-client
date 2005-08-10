/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

public class LinkageData 
{
	public LinkageData(EAMGraphCell cell) throws Exception
	{
		if(!cell.isLinkage())
			throw new Exception("EAMGraphCell not a Linkage");
	}

}
