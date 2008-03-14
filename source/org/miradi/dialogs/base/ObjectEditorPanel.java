/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.base;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.GenericObjectChoiceQuestion;

public class ObjectEditorPanel extends ObjectDataInputPanel
{
	public ObjectEditorPanel(Project projectToUse, ORef orefToUse, String tagToUse, int type)
	{
		super(projectToUse, orefToUse);

		addField(createOverridenObjectListField(tagToUse, new GenericObjectChoiceQuestion(projectToUse, type)));
		
		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Editor");
	}
}
