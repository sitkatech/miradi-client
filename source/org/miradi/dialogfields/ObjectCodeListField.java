/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
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
		
		PanelButton selectButton = new PanelButton(EAM.text("Select..."));
		selectButton.addActionListener(new SelectButtonHandler());
		OneColumnPanel buttonPanel = new OneColumnPanel();
		buttonPanel.setBackground(EAM.READONLY_BACKGROUND_COLOR);
		buttonPanel.setForeground(EAM.READONLY_FOREGROUND_COLOR);
		buttonPanel.add(selectButton);
		component.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		component.add(buttonPanel, BorderLayout.AFTER_LINE_ENDS);
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
		component.validate();
	}
	
	public class SelectButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			CodeListEditorPanel codeListPanel = new CodeListEditorPanel(getProject(), getORef(), getTag(), question, 1);
			ModalDialogWithClose dialog = new ModalDialogWithClose(EAM.getMainWindow(), codeListPanel, EAM.text("Selection Dialog"));
			Utilities.centerDlg(dialog);
			dialog.setVisible(true);	
		}
	}
	
	private ChoiceQuestion question;
	private MiradiPanel component;
	private ReadOnlyCodeListComponent codeListComponent;
}
