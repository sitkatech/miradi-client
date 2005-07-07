/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

public class CommandDiagramSelectCells extends Command
{
	public CommandDiagramSelectCells(int[] selectedCellIds)
	{
		ids = selectedCellIds;
	}
	
	public String toString()
	{
		String result = "DIAGRAM-SELECT:";
		for(int i=0; i < ids.length; ++i)
			result += ids[i] + ",";
		return result;
	}

	int[] ids;
}
