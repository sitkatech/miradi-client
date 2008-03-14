/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.base.ObjectRefListEditorPanel;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

public class ObjectEditableObjectListField extends ObjectDataInputField
{
	public ObjectEditableObjectListField(Project projectToUse, ORef refToUse, String tagToUse)
	{
		super(projectToUse, refToUse, tagToUse);
		
		component = new MiradiPanel(new BorderLayout());
		objectListComponent = new ObjectReadonlyObjectList(projectToUse, refToUse, tagToUse);
		component.add(objectListComponent.getComponent(), BorderLayout.CENTER);
		
		PanelButton selectButton = new PanelButton(EAM.text("Edit..."));
		selectButton.addActionListener(new EditButtonHandler());
		OneColumnPanel buttonPanel = new OneColumnPanel();
		buttonPanel.setBackground(EAM.READONLY_BACKGROUND_COLOR);
		buttonPanel.setForeground(EAM.READONLY_FOREGROUND_COLOR);
		buttonPanel.add(selectButton);
		setDefaultFieldBorder();
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
		return objectListComponent.getText();
	}

	@Override
	public void setText(String newValue)
	{
		objectListComponent.setText(newValue);
	}
	
	public class EditButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			BaseObject foundObject = getProject().findObject(getORef());
			int annotationType = foundObject.getAnnotationType(getTag());
			ObjectRefListEditorPanel codeListPanel = new ObjectRefListEditorPanel(getProject(), getORef(), getTag(), annotationType);
			
			ModalDialogWithClose dialog = new ModalDialogWithClose(EAM.getMainWindow(), codeListPanel, EAM.text("Edit Dialog"));
			Utilities.centerDlg(dialog);
			dialog.setVisible(true);	
		}
	}
	
	private MiradiPanel component;
	private ObjectReadonlyObjectList objectListComponent;
}
