/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.indicator;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.StatusQuestion;

public class SimpleViabilityFieldsPanel extends ObjectDataInputPanel
{
	public SimpleViabilityFieldsPanel(Project projectToUse, ORef refToUse) throws Exception
	{
		super(projectToUse, refToUse);			
		
		addField(createRatingChoiceField(Target.TAG_TARGET_STATUS, new StatusQuestion()));		
		addField(createStringField(Target.TAG_CURRENT_STATUS_JUSTIFICATION));
		
		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Simple Viability");
	}
}
