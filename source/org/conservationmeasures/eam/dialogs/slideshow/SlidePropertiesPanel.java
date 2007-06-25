/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.slideshow;

import org.conservationmeasures.eam.dialogs.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Slide;
import org.conservationmeasures.eam.project.Project;

public class SlidePropertiesPanel extends ObjectDataInputPanel
{
	public SlidePropertiesPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.SLIDE, idToEdit);
		addField(createStringField(Slide.getObjectType(), Slide.TAG_LABEL));
		addField(createStringField(Slide.getObjectType(), Slide.PSEUDO_TAG_DIAGRAM_OBJECT_LABEL));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Slide Properties");
	}
}
