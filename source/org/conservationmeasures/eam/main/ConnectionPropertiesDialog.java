/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.Component;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JDialog;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.martus.swing.UiButton;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;
import org.martus.swing.UiVBox;
import org.martus.swing.Utilities;

public class ConnectionPropertiesDialog extends JDialog implements ActionListener
{
	public ConnectionPropertiesDialog(MainWindow parent) throws HeadlessException
	{
		super(parent, EAM.text("Title|Connection Properties"));
		mainWindow = parent;
		UiVBox bigBox = new UiVBox();
		bigBox.add(createFromToBox());
		bigBox.addSpace();
		bigBox.add(createButtonBar());

		Container contents = getContentPane();
		contents.add(bigBox);
		Utilities.centerDlg(this);
		setResizable(true);
		setModal(true);
		
	}
	
	private Box createFromToBox()
	{
		Box box = Box.createHorizontalBox();
		from = createChoices();
		to = createChoices();
		Component[] components = {from, new UiLabel(EAM.text("Label|affects")), to};
		Utilities.addComponentsRespectingOrientation(box, components);
		return box;
	}
	
	private UiComboBox createChoices()
	{
		DiagramModel model = mainWindow.getProject().getDiagramModel();
		UiComboBox comboBox = new UiComboBox();
		comboBox.addItem(EAM.text("Label|[Select a node]"));
		Object[] all = DiagramModel.getAll(model);
		for(int i=0; i < all.length; ++i)
		{
			if(all[i] instanceof DiagramNode)
			{
				comboBox.addItem(all[i]);
			}
		}
		return comboBox;
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
		{
			if(from.getSelectedIndex() == 0 || to.getSelectedIndex() == 0)
			{
				String title = EAM.text("Must Choose a Node");
				String body = EAM.text("You must choose a node in each of the two lists");
				EAM.okDialog(title, new String[] {body});
				toFront();
				return;
			}
			result = true;
		}
		dispose();
	}
	
	public boolean getResult()
	{
		return result;
	}
	
	public DiagramNode getFrom()
	{
		return (DiagramNode)from.getSelectedItem();
	}
	
	public DiagramNode getTo()
	{
		return (DiagramNode)to.getSelectedItem();
	}
	
	MainWindow mainWindow;
	boolean result;
	UiComboBox from;
	UiComboBox to;
	UiButton okButton;
	UiButton cancelButton;
}
