/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.main.EAM;

public class ActionInsertThreat extends DiagramAction
{
	public ActionInsertThreat(DiagramComponent diagramComponentToUse)
	{
		super(diagramComponentToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Threat");
	}

	public void actionPerformed(ActionEvent arg0)
	{
		// TODO Auto-generated method stub

	}

}
