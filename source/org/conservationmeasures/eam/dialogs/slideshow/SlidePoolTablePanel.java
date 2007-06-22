/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.slideshow;

import org.conservationmeasures.eam.actions.ActionCreateSlide;
import org.conservationmeasures.eam.actions.ActionDeleteSlide;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.ObjectTablePanelWithCreateAndDelete;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class SlidePoolTablePanel extends ObjectTablePanelWithCreateAndDelete
{
	public SlidePoolTablePanel(Project project, Actions actions)
	{
		super(project, ObjectType.SLIDE, 
			new SlidePoolTable(new SlidePoolTableModel(project)),
			actions, buttons);
	}
	
	
	static Class[] buttons = new Class[] {
		ActionCreateSlide.class,
		ActionDeleteSlide.class,
	};
}
