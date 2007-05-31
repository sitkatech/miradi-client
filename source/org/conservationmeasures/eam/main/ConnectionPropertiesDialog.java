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
import javax.swing.JPanel;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.dialogs.DiagramPanel;
import org.conservationmeasures.eam.dialogs.EAMDialog;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.martus.swing.UiButton;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiVBox;
import org.martus.swing.Utilities;

import com.jhlabs.awt.BasicGridLayout;

public class ConnectionPropertiesDialog extends EAMDialog implements ActionListener
{
	public ConnectionPropertiesDialog(MainWindow parent, DiagramPanel panelToUse) throws HeadlessException
	{
		super(parent, EAM.text("Title|Link Properties"));
		
		diagramPanel = panelToUse;
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
	
	private JPanel createFromToBox()
	{
		linkFromList = createChoices(FactorLink.FROM);
		linkToList = createChoices(FactorLink.TO);
		DiagramComponent diagram = diagramPanel.getdiagramComponent();

		FactorCell firstSelected = diagram.getSelectedFactor(0);
		if(firstSelected != null)
			linkFromList.setSelectedItem(new FactorDropDownItem(firstSelected.getUnderlyingObject(), firstSelected.getDiagramFactor()));
		
		FactorCell secondSelected = diagram.getSelectedFactor(1);
		if(secondSelected != null)
			linkToList.setSelectedItem(new FactorDropDownItem(secondSelected.getUnderlyingObject(), secondSelected.getDiagramFactor()));

		JPanel vbox = new JPanel(new BasicGridLayout(2,1));
		
		Box box = Box.createHorizontalBox();
		Component[] components = {linkFromList, new PanelTitleLabel(EAM.text("Label| affects ")), linkToList};
		Utilities.addComponentsRespectingOrientation(box, components);
		vbox.add(box);

		if (firstSelected==null || secondSelected==null)
			vbox.add(new PanelTitleLabel(EAM.text("  HINT: You can quickly add links by selecting the first factor, " +
			"holding Ctrl while selecting the second factor, and then hitting the add link button.  ")));
			
		return vbox;
	}
	
	private UiComboBox createChoices(int linkFromTo)
	{
		UiComboBox comboBox = new UiComboBox();
		comboBox.addItem(EAM.text("Label|--Select One---"));
		
		DiagramModel model = diagramPanel.getDiagramModel();
		DiagramFactor[] allDiagramFactors = model.getAllDiagramFactorsAsArray();
		DiagramFactor[] filteredDiagramFactors = getFilteredDiagramFactors(allDiagramFactors);
		Factor[] factors = convertToFactorList(model.getProject(), filteredDiagramFactors);
		
		Vector dropDownItems = new Vector();
		for(int i = 0; i < factors.length; ++i)
		{
			Factor factor = factors[i];
			dropDownItems.add(new FactorDropDownItem(factor, filteredDiagramFactors[i]));
		}
		
		return addItemsToComboBoxAndSort(comboBox, dropDownItems);
	}

	private DiagramFactor[] getFilteredDiagramFactors(DiagramFactor[] allDiagramFactors)
	{
		Vector filterdDiagramFactors = new Vector();
		for (int i = 0; i < allDiagramFactors.length; ++i)
		{
			if (allDiagramFactors[i].getWrappedType() != ObjectType.TEXT_BOX)
				filterdDiagramFactors.add(allDiagramFactors[i]);
		}
			
		return (DiagramFactor[]) filterdDiagramFactors.toArray(new DiagramFactor[0]);
	}

	private UiComboBox addItemsToComboBoxAndSort(UiComboBox comboBox, Vector dropDownItems)
	{
		FactorDropDownItem[] items = (FactorDropDownItem[]) dropDownItems.toArray(new FactorDropDownItem[0]);
		Arrays.sort(items, new IgnoreCaseStringComparator());
		for (int i = 0; i < items.length; i++)
		{
			comboBox.addItem(items[i]);
		}
		
		return comboBox;
	}

	private Factor[] convertToFactorList(Project project, DiagramFactor[] allDiagramFactors)
	{
		Factor[] factors = new Factor[allDiagramFactors.length];
		for (int i = 0; i < allDiagramFactors.length; i++)
		{
			factors[i] = (Factor) project.findObject(allDiagramFactors[i].getWrappedORef());
		}
		return factors;
	}
	
	static class FactorDropDownItem
	{

		public FactorDropDownItem(Factor factorToUse, DiagramFactor diagramFactorToUse)
		{
			factor = factorToUse;
			diagramFactor = diagramFactorToUse;
		}
		
		public DiagramFactor getDiagramFactor()
		{
			return diagramFactor;
		}
		
		public Factor getFactor()
		{
			return factor;
		}
		
		public boolean equals(Object rawOther)
		{
			if (! (rawOther instanceof FactorDropDownItem))
				return false;
			
			FactorDropDownItem other = (FactorDropDownItem) rawOther;
			if (! other.getDiagramFactor().getId().equals(diagramFactor.getId()))
				return false;
			
			BaseId otherFactorId = other.getFactor().getId();
			BaseId factorId = factor.getId();
			if (! otherFactorId.equals(factorId))
				return false;
			
			return true;
		}
		
		public String toString()
		{
			return factor.getLabel();
		}
		
		private Factor factor;
		private DiagramFactor diagramFactor;
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
		FactorDropDownItem item = (FactorDropDownItem)linkFromList.getSelectedItem();
		return item.getDiagramFactor();
	}
	
	public DiagramFactor getTo()
	{
		FactorDropDownItem item = (FactorDropDownItem)linkToList.getSelectedItem();
		return item.getDiagramFactor();
	}
	
	DiagramPanel diagramPanel;
	boolean result;
	UiComboBox linkFromList;
	UiComboBox linkToList;
	UiButton okButton;
	UiButton cancelButton;
}
