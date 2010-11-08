/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.dialogfields.editors.QuestionWithDescriptionEditorPanel;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.base.OneFieldObjectDataInputPanel;
import org.miradi.dialogs.dashboard.QuestionBasedLeftSideEditorComponent;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.questions.ChoiceQuestion;

//FIXME urgent - this class has been duplicated from EditableCodeListField
public class CodeListPopupWithDescriptionPanelField extends	AbstractEditableCodeListField
{
	public CodeListPopupWithDescriptionPanelField(MainWindow mainWindowToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(mainWindowToUse.getProject(), refToUse, tagToUse, questionToUse, 1);
		
		mainWindow = mainWindowToUse;
		question = questionToUse;
		component = new MiradiPanel(new BorderLayout());
		codeListComponent = new ReadOnlyCodeListComponent(questionToUse.getChoices(), 1);
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
			try
			{
				QuestionBasedLeftSideEditorComponent leftComponent = new QuestionBasedLeftSideEditorComponent(mainWindow, question);
				ComponentWrapperObjectDataInputField field = new ComponentWrapperObjectDataInputField(getProject(), getORef(), getTag(), leftComponent);
				OneFieldObjectDataInputPanel leftPanel = new OneFieldObjectDataInputPanel(getProject(), getORef(), getTag(), field);
				QuestionWithDescriptionEditorPanel editorPanel = new QuestionWithDescriptionEditorPanel(mainWindow, question, leftPanel);
				ModalDialogWithClose dialog = new ModalDialogWithClose(EAM.getMainWindow(), EAM.text("Selection Dialog"));
				dialog.setScrollableMainPanel(editorPanel);
				Utilities.centerDlg(dialog);
				dialog.setVisible(true);
			}
			catch (Exception e)
			{
				EAM.logException(e);
				EAM.unexpectedErrorDialog(e);
			}
		}
	}

	private MainWindow mainWindow;
	private ChoiceQuestion question;
	private MiradiPanel component;
	private ReadOnlyCodeListComponent codeListComponent;
	private PanelButton selectButton;
}
