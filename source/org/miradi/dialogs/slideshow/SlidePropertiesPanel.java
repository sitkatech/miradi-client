/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.slideshow;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Slide;
import org.miradi.project.Project;

public class SlidePropertiesPanel extends ObjectDataInputPanel
{
	public SlidePropertiesPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.SLIDE, idToEdit);
		addField(createStringField(Slide.getObjectType(), Slide.TAG_LABEL));
		addField(createReadonlyTextField(Slide.getObjectType(), Slide.PSEUDO_TAG_DIAGRAM_OBJECT_LABEL));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Slide Properties");
	}
}
