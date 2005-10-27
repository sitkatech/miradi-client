package org.conservationmeasures.eam.main;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JDialog;

import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextField;
import org.martus.swing.UiVBox;
import org.martus.swing.Utilities;

public class ProjectScopePropertiesDialog extends JDialog implements
		ActionListener
{
	public ProjectScopePropertiesDialog(Frame parent, Project project, EAMGraphCell scope)
	{
		super(parent, EAM.text("Title|Project Scope Properties"));
		
		UiVBox bigBox = new UiVBox();
		bigBox.add(createTextField());
		bigBox.add(createButtonBar());

		Container contents = getContentPane();
		contents.add(bigBox);
		pack();
/////////		setLocation(node.getLocation());
		setResizable(true);
		setModal(true);
	}
	
	private Component createTextField()
	{
		UiLabel textLabel = new UiLabel(EAM.text("Label|Project Vision"));
		textField = new UiTextField(40);
		textField.requestFocus(true);
		textField.selectAll();

		Box labelBar = Box.createHorizontalBox();
		Component[] components = new Component[] {textLabel, new UiLabel(" "), textField, Box.createHorizontalGlue()};
		Utilities.addComponentsRespectingOrientation(labelBar, components);
		return labelBar;
	}
	

	private Box createButtonBar()
	{
		okButton = new UiButton(EAM.text("Button|OK"));
		okButton.addActionListener(this);
		getRootPane().setDefaultButton(okButton);
		cancelButton = new UiButton(EAM.text("Button|Cancel"));
		cancelButton.addActionListener(this);

		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), okButton, cancelButton};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}

	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == okButton)
			result = true;
		dispose();
	}
	
	public boolean getResult()
	{
		return result;
	}
	
	public void setText(String text)
	{
		textField.setText(text);
	}
	
	public String getText()
	{
		return textField.getText();
	}
	
	boolean result;
	UiTextField textField;
	UiButton okButton;
	UiButton cancelButton;}
