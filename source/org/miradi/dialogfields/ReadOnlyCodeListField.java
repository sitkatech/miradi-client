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

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.Translation;

public class ReadOnlyCodeListField extends ObjectDataInputField
{
	public ReadOnlyCodeListField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, projectToUse.getMetadata().getRef(), tagToUse);
		
		componentPanel = new OneColumnPanel();		
		codeListComponent = new ReadOnlyCodeListComponent(questionToUse.getChoices());
		countLabel = new PanelTitleLabel();
		componentPanel.add(countLabel);
		componentPanel.add(codeListComponent);
		
		setDefaultFieldBorder();
	}
	
	@Override
	public JComponent getComponent()
	{
		return componentPanel;
	}

	@Override
	public String getText()
	{
		return codeListComponent.getText();
	}

	@Override
	public void setText(String newValue)
	{
		try
		{
			codeListComponent.setText(newValue);
			CodeList codeList = new CodeList(codeListComponent.getText());
			String fieldLabel = Translation.fieldLabel(getObjectType(), getTag());
			countLabel.setText(fieldLabel + ": " + codeList.size());
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
	}
	
	private ReadOnlyCodeListComponent codeListComponent;
	private OneColumnPanel componentPanel;
	private PanelTitleLabel countLabel;
}
