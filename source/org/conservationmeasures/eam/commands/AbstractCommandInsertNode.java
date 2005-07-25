/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.diagram.nodes.Node;


abstract public class AbstractCommandInsertNode extends Command
{
	public AbstractCommandInsertNode()
	{
		setId(Node.INVALID_ID);
	}
	
	public void setId(int newId)
	{
		insertedId = newId;
	}

	public int getId()
	{
		return insertedId;
	}

	protected int insertedId;
}
