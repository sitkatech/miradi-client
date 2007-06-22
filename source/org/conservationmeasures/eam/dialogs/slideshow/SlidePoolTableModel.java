/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.slideshow;

import org.conservationmeasures.eam.dialogs.ObjectPoolTableModel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Slide;
import org.conservationmeasures.eam.project.Project;

public class SlidePoolTableModel extends ObjectPoolTableModel
{
	public SlidePoolTableModel(Project project)
	{
		super(project, ObjectType.SLIDE, COLUMN_TAGS);
	}

	private static final String[] COLUMN_TAGS = new String[] {
		Slide.TAG_LABEL,
	};
}
