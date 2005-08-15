/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JDialog;

import org.conservationmeasures.eam.diagram.nodes.Node;
import org.martus.swing.UiButton;
import org.martus.swing.UiTextField;
import org.martus.swing.UiVBox;
import org.martus.swing.Utilities;

public class NodePropertiesDialog extends JDialog implements ActionListener
{
	public NodePropertiesDialog(Frame parent, String title, Node nodeToEdit)
			throws HeadlessException
	{
		super(parent, title);
		UiVBox bigBox = new UiVBox();
		bigBox.add(createTextField(nodeToEdit.getText()));
		bigBox.add(createButtonBar());

		Container contents = getContentPane();
		contents.add(bigBox);
		pack();
		setLocation(nodeToEdit.getLocation());
		setResizable(true);
		setModal(true);
	}
	
	private UiTextField createTextField(String initialText)
	{
		textField = new UiTextField(initialText);
		textField.requestFocus(true);
		textField.selectAll();
		return textField;
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
	
	public String getText()
	{
		return textField.getText();
	}

	boolean result;
	UiTextField textField;
	UiButton okButton;
	UiButton cancelButton;
}
