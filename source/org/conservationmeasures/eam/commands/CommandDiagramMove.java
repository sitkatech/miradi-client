/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

public class CommandDiagramMove extends Command
{
	public CommandDiagramMove(int deltaX, int deltaY)
	{
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
	
	public String toString()
	{
		return "DIAGRAM-MOVE:" + deltaX + "," + deltaY;
	}

	int deltaX;
	int deltaY;
}
