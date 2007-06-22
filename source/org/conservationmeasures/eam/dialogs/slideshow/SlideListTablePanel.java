/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.slideshow;

import org.conservationmeasures.eam.actions.ActionCreateSlide;
import org.conservationmeasures.eam.actions.ActionDeleteSlide;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.ObjectListTablePanel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class SlideListTablePanel extends ObjectListTablePanel
{
	public SlideListTablePanel(Project project, Actions actions, ORef oref)
	{
		super(project, ObjectType.SLIDE, new SlideListTableModel(project, oref), actions, buttons);
	}
	
	static Class[] buttons = new Class[] {
		ActionCreateSlide.class,
		ActionDeleteSlide.class,
	};
}
