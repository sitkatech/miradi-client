/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JDialog;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.UiButton;
import org.martus.swing.UiCheckBox;
import org.martus.swing.UiVBox;
import org.martus.swing.Utilities;

public class LayerDialog extends JDialog implements ActionListener
{
	public LayerDialog(Frame parent, LayerManager initialValues) throws HeadlessException
	{
		super(parent, EAM.text("Title|View Layers"));
		
		interventionCheckBox = new UiCheckBox(EAM.text("Label|Show Interventions"));
		factorCheckBox = new UiCheckBox(EAM.text("Label|Show Indirect Factors"));
		threatCheckBox = new UiCheckBox(EAM.text("Label|Show Direct Threats"));
		stressCheckBox = new UiCheckBox(EAM.text("Label|Show Stresses"));
		targetCheckBox = new UiCheckBox(EAM.text("Label|Show Targets"));
		
		interventionCheckBox.setSelected(initialValues.isTypeVisible(DiagramNode.TYPE_INTERVENTION));
		factorCheckBox.setSelected(initialValues.isTypeVisible(DiagramNode.TYPE_INDIRECT_FACTOR));
		threatCheckBox.setSelected(initialValues.isTypeVisible(DiagramNode.TYPE_DIRECT_THREAT));
		stressCheckBox.setSelected(initialValues.isTypeVisible(DiagramNode.TYPE_STRESS));
		targetCheckBox.setSelected(initialValues.isTypeVisible(DiagramNode.TYPE_TARGET));
		
		UiVBox bigBox = new UiVBox();
		bigBox.add(createLayerOptions());
		bigBox.add(createButtonBar());
		
		Container contents = getContentPane();
		contents.add(bigBox);
		pack();
		setResizable(true);
		setModal(true);
	}
	
	private Component createLayerOptions()
	{
		UiVBox options = new UiVBox();
		options.add(interventionCheckBox);
		options.add(factorCheckBox);
		options.add(threatCheckBox);
		options.add(stressCheckBox);
		options.add(targetCheckBox);
		return options;
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
	
	public void updateLayerManager(LayerManager managerToUpdate)
	{
		managerToUpdate.setVisibility(DiagramNode.TYPE_INTERVENTION, interventionCheckBox.isSelected());
		managerToUpdate.setVisibility(DiagramNode.TYPE_INDIRECT_FACTOR, factorCheckBox.isSelected());
		managerToUpdate.setVisibility(DiagramNode.TYPE_DIRECT_THREAT, threatCheckBox.isSelected());
		managerToUpdate.setVisibility(DiagramNode.TYPE_STRESS, stressCheckBox.isSelected());
		managerToUpdate.setVisibility(DiagramNode.TYPE_TARGET, targetCheckBox.isSelected());
	}
	
	boolean result;
	UiButton okButton;
	UiButton cancelButton;

	UiCheckBox interventionCheckBox;
	UiCheckBox factorCheckBox;
	UiCheckBox threatCheckBox;
	UiCheckBox stressCheckBox;
	UiCheckBox targetCheckBox;
}
