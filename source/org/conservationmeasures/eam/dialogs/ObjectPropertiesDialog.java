/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JDialog;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DialogGridPanel;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;
import org.martus.swing.Utilities;

abstract public class ObjectPropertiesDialog extends JDialog
{
	ObjectPropertiesDialog(MainWindow parentToUse, EAMObject objectToEdit)
	{
		super(parentToUse);
		objectType = objectToEdit.getType();
		objectId = objectToEdit.getId();
		
		setModal(true);
	}
	
	void initializeFields(String[] tags)
	{
		DialogGridPanel grid = new DialogGridPanel();
		fields = new DialogField[tags.length];
		for(int field = 0; field < fields.length; ++field)
		{
			fields[field] = createDialogField(tags[field]);
			grid.add(new UiLabel(fields[field].getLabel()));
			grid.add(fields[field].getComponent());
		}

		Container contents = getContentPane();
		contents.setLayout(new BorderLayout());
		contents.add(grid, BorderLayout.CENTER);
		contents.add(createButtonBar(), BorderLayout.AFTER_LAST_LINE);
		Utilities.centerDlg(this);
		pack();
	}

	DialogField createDialogField(String tag)
	{
		String label = EAM.text("Label|" + tag);
		String value = getProject().getObjectData(objectType, objectId, tag);
		DialogField dialogField = new StringDialogField(tag, label, value);
		return dialogField;
	}

	private Project getProject()
	{
		return ((MainWindow)getParent()).getProject();
	}

	private Component createButtonBar()
	{
		okButton = new UiButton(EAM.text("Button|OK"));
		okButton.addActionListener(new OkHandler());
		getRootPane().setDefaultButton(okButton);
		cancelButton = new UiButton(EAM.text("Button|Cancel"));
		cancelButton.addActionListener(new CancelHandler());

		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), okButton, cancelButton};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}
	
	class CancelHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			dispose();
		}
		
	}
	
	class OkHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				getProject().executeCommand(new CommandBeginTransaction());
				for(int field = 0; field < fields.length; ++field)
				{
					CommandSetObjectData cmd = new CommandSetObjectData(objectType, objectId, fields[field].getTag(), fields[field].getText());
					getProject().executeCommand(cmd);
				}
				getProject().executeCommand(new CommandEndTransaction());
				dispose();
			}
			catch (CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("Unexpected error prevented this operation");
			}
		}
	}

	int objectType;
	int objectId;
	DialogField[] fields;
	UiButton okButton;
	UiButton cancelButton;
}

