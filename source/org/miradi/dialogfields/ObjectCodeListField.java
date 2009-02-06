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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.CodeListEditorPanel;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.ids.BaseId;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class ObjectCodeListField extends ObjectDataInputField
{
	public ObjectCodeListField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, ChoiceQuestion questionToUse, int columnCount)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse);
		
		question = questionToUse;
		component = new MiradiPanel(new BorderLayout());
		codeListComponent = new ReadOnlyCodeListComponent(questionToUse.getChoices(), columnCount);
		component.add(codeListComponent, BorderLayout.CENTER);
		
		selectButton = new PanelButton(EAM.text("Select..."));

		selectButton.addActionListener(new SelectButtonHandler());
		OneColumnPanel buttonPanel = new OneColumnPanel();
		buttonPanel.setBackground(EAM.READONLY_BACKGROUND_COLOR);
		buttonPanel.setForeground(EAM.READONLY_FOREGROUND_COLOR);
		buttonPanel.add(selectButton);
		setDefaultFieldBorder();
		component.add(buttonPanel, BorderLayout.AFTER_LINE_ENDS);
	}
	
	@Override
	public void updateEditableState()
	{
		super.updateEditableState();
		
		selectButton.setEnabled(isValidObject());			
	}
	
	@Override
	public JComponent getComponent()
	{
		return component;
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
	
	public class SelectButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			CodeListEditorPanel codeListPanel = new CodeListEditorPanel(getProject(), getORef(), getTag(), question, 1);
			ModalDialogWithClose dialog = new ModalDialogWithClose(EAM.getMainWindow(), EAM.text("Selection Dialog"));
			dialog.setScrollableMainPanel(codeListPanel);
			Utilities.centerDlg(dialog);
			dialog.setVisible(true);	
		}
	}
	
	private ChoiceQuestion question;
	private MiradiPanel component;
	private ReadOnlyCodeListComponent codeListComponent;
	private PanelButton selectButton;
}
