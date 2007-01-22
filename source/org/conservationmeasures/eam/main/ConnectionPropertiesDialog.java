/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.awt.Component;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.Box;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.dialogs.EAMDialog;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.martus.swing.UiButton;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;
import org.martus.swing.UiVBox;
import org.martus.swing.Utilities;

public class ConnectionPropertiesDialog extends EAMDialog implements ActionListener
{
	public ConnectionPropertiesDialog(MainWindow parent) throws HeadlessException
	{
		super(parent, EAM.text("Title|Link Properties"));
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
		linkFromList = createChoices(FactorLink.FROM);
		linkToList = createChoices(FactorLink.TO);
		DiagramComponent diagram = mainWindow.getDiagramComponent();
		DiagramFactor firstSelected = diagram.getSelectedFactor(0);
		if(firstSelected != null)
			linkFromList.setSelectedItem(firstSelected);
		DiagramFactor secondSelected = diagram.getSelectedFactor(1);
		if(secondSelected != null)
			linkToList.setSelectedItem(secondSelected);
		Box box = Box.createHorizontalBox();
		Component[] components = {linkFromList, new UiLabel(EAM.text("Label|affects")), linkToList};
		Utilities.addComponentsRespectingOrientation(box, components);
		return box;
	}
	
	private UiComboBox createChoices(int linkFromTo)
	{
		boolean acceptStrategies = (linkFromTo == FactorLink.FROM);
		boolean acceptTargets = (linkFromTo == FactorLink.TO);
		
		DiagramModel model = mainWindow.getProject().getDiagramModel();
		UiComboBox comboBox = new UiComboBox();
		comboBox.addItem(EAM.text("Label|--Select One---"));
		
		Vector vectorOfFactors = model.getAllDiagramFactors();
		DiagramFactor[] diagramFactors = (DiagramFactor[])vectorOfFactors.toArray(new DiagramFactor[0]);
		Arrays.sort(diagramFactors, new IgnoreCaseStringComparator());
		for(int i=0; i < diagramFactors.length; ++i)
		{
			if(( acceptStrategies || !diagramFactors[i].isStrategy()) && 
				(acceptTargets || !diagramFactors[i].isTarget()))
				comboBox.addItem(diagramFactors[i]);
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
			if(linkFromList.getSelectedIndex() == 0 || linkToList.getSelectedIndex() == 0)
			{
				String title = EAM.text("Incomplete Link");
				String body = EAM.text("You must select one item in each of the two lists");
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
	
	public DiagramFactor getFrom()
	{
		return (DiagramFactor)linkFromList.getSelectedItem();
	}
	
	public DiagramFactor getTo()
	{
		return (DiagramFactor)linkToList.getSelectedItem();
	}
	
	MainWindow mainWindow;
	boolean result;
	UiComboBox linkFromList;
	UiComboBox linkToList;
	UiButton okButton;
	UiButton cancelButton;
}
