/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.base;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class CodeListEditorPanel extends ObjectDataInputPanel
{
	public CodeListEditorPanel(Project projectToUse, ORef orefToUse, String tagToUse, ChoiceQuestion question, int columnCount)
	{
		super(projectToUse, orefToUse);
		
		addField(createMultiCodeField(orefToUse.getObjectType(), tagToUse, question, columnCount));
		
		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Editor");
	}
}
