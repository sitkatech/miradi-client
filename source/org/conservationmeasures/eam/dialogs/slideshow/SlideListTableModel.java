/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.slideshow;

import org.conservationmeasures.eam.dialogs.ObjectListTableModel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Slide;
import org.conservationmeasures.eam.objects.SlideShow;
import org.conservationmeasures.eam.project.Project;

public class SlideListTableModel extends ObjectListTableModel
{
	public SlideListTableModel(Project project, ORef containingOref)
	{
		super(project, containingOref.getObjectType(), containingOref.getObjectId(), SlideShow.TAG_SLIDE_REFS, Slide.getObjectType(), COLUMN_TAGS);
	}

	private static final String[] COLUMN_TAGS = new String[] {
		Slide.TAG_LABEL,
	};
}
