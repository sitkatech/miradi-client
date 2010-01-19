/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.dialogfields;

import javax.swing.JComponent;

import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class ReadOnlyCodeListField extends ObjectDataInputField
{
	public ReadOnlyCodeListField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, projectToUse.getMetadata().getRef(), tagToUse);
		
		codeListComponent = new ReadOnlyCodeListComponent(questionToUse.getChoices(), 1);
		
		setDefaultFieldBorder();
	}
	
	@Override
	public JComponent getComponent()
	{
		return codeListComponent;
	}

	@Override
	public String getText()
	{
		return codeListComponent.getText();
	}

	@Override
	public void setText(String newValue)
	{
		codeListComponent.setText(newValue);
	}
	
	private ReadOnlyCodeListComponent codeListComponent;
}
