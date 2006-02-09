/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.icons;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.renderers.MultilineNodeRenderer;
import org.conservationmeasures.eam.diagram.renderers.RectangleRenderer;
import org.conservationmeasures.eam.objects.ThreatPriority;
import org.conservationmeasures.eam.project.Project;

public class ThreatPriorityIcon extends EamIcon 
{
	public ThreatPriorityIcon(ThreatPriority priorityToUse)
	{
		priority = priorityToUse;
	}
	
	MultilineNodeRenderer getRenderer() 
	{
		return new RectangleRenderer();
	}

	Color getIconColor() 
	{
		return Project.getPriorityColor(priority);
	}
	
	private ThreatPriority priority;
}

