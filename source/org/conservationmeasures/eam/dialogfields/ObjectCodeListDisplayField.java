/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;

public class ObjectCodeListDisplayField extends ObjectCodeListField
{
	public ObjectCodeListDisplayField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, ChoiceQuestion questionToUse, int columnCount)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, questionToUse);
	}
	
	public void updateEditableState()
	{
		codeListEditor.setEnabled(false);
	}
}
