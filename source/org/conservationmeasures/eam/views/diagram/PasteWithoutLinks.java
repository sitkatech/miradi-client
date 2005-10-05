/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.TransferableEamList;

public class PasteWithoutLinks extends Paste 
{
	public void pasteCellsIntoProject(TransferableEamList list) throws CommandFailedException 
	{
		getProject().pasteNodesOnlyIntoProject(list, getLocation());
	}
}
