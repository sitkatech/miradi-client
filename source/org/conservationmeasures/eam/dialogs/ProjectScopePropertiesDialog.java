package org.conservationmeasures.eam.dialogs;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.JDialog;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextField;
import org.martus.swing.UiVBox;
import org.martus.swing.Utilities;

public class ProjectScopePropertiesDialog extends JDialog implements ActionListener
{
	public ProjectScopePropertiesDialog(MainWindow parent, Project project, EAMGraphCell scope)
	{
		super(parent, EAM.text("Title|Project Scope Properties"));
		mainWindow = parent;
		
		UiVBox bigBox = new UiVBox();
		bigBox.add(createTextField());

		Container contents = getContentPane();
		contents.add(bigBox);
		pack();
/////////		setLocation(node.getLocation());
		setResizable(true);
	}
	
	private Component createTextField()
	{
		UiLabel textLabel = new UiLabel(EAM.text("Label|Project Vision"));
		textField = new UiTextField(40);
		textField.requestFocus(true);
		textField.selectAll();
		textField.addFocusListener(new LabelChangeHandler());
		textField.addActionListener(this);

		Box labelBar = Box.createHorizontalBox();
		Component[] components = new Component[] {textLabel, new UiLabel(" "), textField, Box.createHorizontalGlue()};
		Utilities.addComponentsRespectingOrientation(labelBar, components);
		return labelBar;
	}
	

	public void setText(String text)
	{
		textField.setText(text);
	}
	
	public String getText()
	{
		return textField.getText();
	}
	
	class LabelChangeHandler implements FocusListener
	{
		public void focusGained(FocusEvent e)
		{
		}

		public void focusLost(FocusEvent event)
		{
			saveLabel();
		}
		
	}

	public void saveLabel()
	{
		try
		{
			int type = ObjectType.PROJECT_METADATA;
			BaseId metadataId = getProject().getMetadata().getId();
			String tag = ProjectMetadata.TAG_PROJECT_VISION;
			String newVision = getText();
			
			if(newVision.equals(getProject().getMetadata().getProjectVision()))
				return;
			
			CommandSetObjectData cmd = new CommandSetObjectData(type, metadataId, tag, newVision);
			getProject().executeCommand(cmd);
		}
		catch (CommandFailedException e)
		{
			e.printStackTrace();
			EAM.errorDialog("Unexpected error saving Project Vision");
		}
	}

	private Project getProject()
	{
		return mainWindow.getProject();
	}

	public void actionPerformed(ActionEvent e)
	{
		saveLabel();
	}

	MainWindow mainWindow;
	UiTextField textField;
}
