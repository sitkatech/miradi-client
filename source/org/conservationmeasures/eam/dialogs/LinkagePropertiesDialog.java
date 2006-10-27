/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;

import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextField;
import org.martus.swing.UiVBox;
import org.martus.swing.Utilities;

public class LinkagePropertiesDialog extends EAMDialog implements ActionListener
{
	public LinkagePropertiesDialog(MainWindow parent, Project project, EAMGraphCell scope)
	{
		super(parent, EAM.text("Title|Connection Properties"));
		
		UiVBox bigBox = new UiVBox();
		bigBox.add(createTextField());
		bigBox.add(createButtonBar());

		Container contents = getContentPane();
		contents.add(bigBox);
		pack();
		setResizable(true);
		setModal(true);
	}
	
	private Component createTextField()
	{
		UiLabel textLabel = new UiLabel(EAM.text("Label|Stress"));
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
	UiButton cancelButton;
}
