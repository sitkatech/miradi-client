/* 
RCopyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.dialogs.base;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;

import org.martus.swing.Utilities;
import org.miradi.dialogfields.AbstractReadonlyChoiceComponent;
import org.miradi.dialogfields.DataField;
import org.miradi.dialogfields.ReadonlyPanelAndPopupEditorProvider;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceQuestion;

abstract public class ReadonlyPanelWithPopupEditor extends MiradiPanel
{
	public ReadonlyPanelWithPopupEditor(ReadonlyPanelAndPopupEditorProvider readonlyAndEditorProviderToUse, String popupEditorDialogTitleToUse, ChoiceQuestion choiceQuestion)
	{
		super(new BorderLayout());
		
		popupEditorDialogTitle = popupEditorDialogTitleToUse;
		readonlyWithEditorProvider = readonlyAndEditorProviderToUse;
		readOnlyCodeListComponent = readonlyWithEditorProvider.createReadOnlyComponent(choiceQuestion, 1);
		add(readOnlyCodeListComponent, BorderLayout.CENTER);
		setBackground(EAM.READONLY_BACKGROUND_COLOR);
		selectButton = new PanelButton(EAM.text("Select..."));
		selectButton.addActionListener(new SelectButtonHandler());
		OneColumnPanel buttonPanel = new OneColumnPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		buttonPanel.setBackground(EAM.READONLY_BACKGROUND_COLOR);
		buttonPanel.setForeground(EAM.READONLY_FOREGROUND_COLOR);
		buttonPanel.add(selectButton);
		setBorder(DataField.createLineBorderWithMargin());
		add(buttonPanel, BorderLayout.AFTER_LINE_ENDS);
	}
	
	public void setText(String newValue)
	{
		readOnlyCodeListComponent.setText(newValue);
	}
	
	public String getText()
	{
		return readOnlyCodeListComponent.getText();
	}
	
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		
		selectButton.setEnabled(enabled);
	}

	public class SelectButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				DisposablePanel editorPanel = readonlyWithEditorProvider.createEditorPanel();
				ModalDialogWithClose dialog = new ModalDialogWithClose(EAM.getMainWindow(), popupEditorDialogTitle);
				addDialogMainPanel(dialog, editorPanel);
				dialog.becomeActive();
				setFixedDialogWidth(dialog);
				Utilities.centerDlg(dialog);
				dialog.setVisible(true);
			}
			catch (Exception e)
			{
				EAM.alertUserOfNonFatalException(e);
			}
		}

		private void setFixedDialogWidth(ModalDialogWithClose dialog)
		{
			final int ARBITRARY_DIALOG_WIDTH = 700;
			Dimension size = dialog.getSize();
			size.setSize(ARBITRARY_DIALOG_WIDTH, size.getHeight());
			dialog.setSize(size);
		}
	}
	
	abstract protected void addDialogMainPanel(ModalDialogWithClose dialog, DisposablePanel editorPanel);
	
	private PanelButton selectButton;
	private AbstractReadonlyChoiceComponent readOnlyCodeListComponent;
	private ReadonlyPanelAndPopupEditorProvider readonlyWithEditorProvider;
	private String popupEditorDialogTitle;
}
