/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.main.EAM;

public class ActionInsertThreat extends DiagramAction
{
	public ActionInsertThreat(DiagramComponent diagramComponentToUse, Point location)
	{
		super(diagramComponentToUse, getLabel());
		createThreatAt = location;
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Threat");
	}

	public void actionPerformed(ActionEvent event)
	{
		DiagramModel model = getDiagramComponent().getDiagramModel();
		model.createThreatNode(createThreatAt, EAM.text("Label|New Threat"));
	}

	Point createThreatAt;
}
