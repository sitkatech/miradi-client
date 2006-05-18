/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.icons;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.nodes.DiagramConstants;
import org.conservationmeasures.eam.diagram.renderers.IconHexagonRenderer;
import org.conservationmeasures.eam.diagram.renderers.MultilineNodeRenderer;

public class DraftInterventionIcon extends EamIcon
{
	MultilineNodeRenderer getRenderer()
	{
		return new IconHexagonRenderer(true);
	}
	
	Color getIconColor()
	{
		return DiagramConstants.COLOR_DRAFT_INTERVENTION;
	}
}
