/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.dialogs.base.EAMDialog;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.martus.swing.UiButton;
import org.martus.swing.UiList;
import org.martus.swing.UiVBox;
import org.martus.swing.Utilities;

import com.jhlabs.awt.Alignment;
import com.jhlabs.awt.GridLayoutPlus;

public class LinkCreateDialog extends EAMDialog implements ActionListener
{
	public LinkCreateDialog(MainWindow parent, DiagramPanel panelToUse) throws HeadlessException
	{
		super(parent, EAM.text("Title|Link Create Dialog"));
		
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

		GridLayoutPlus layout = new GridLayoutPlus(2,1);
		layout.setFill(Alignment.FILL_NONE);
		layout.setAlignment(Alignment.CENTER);
		JPanel vbox = new JPanel(layout);
		
		FastScrollPane fromScroller = createScroller(linkFromList);
		FastScrollPane toScroller = createScroller(linkToList);
		
		Box box = Box.createHorizontalBox();
		Component[] components = {
				fromScroller, 
				Box.createHorizontalStrut(20), 
				new PanelTitleLabel(EAM.text("Label|--- affects -->")), 
				Box.createHorizontalStrut(20), 
				toScroller,
		};
		Utilities.addComponentsRespectingOrientation(box, components);
		vbox.add(box);

		FactorCell firstSelected = diagram.getSelectedFactor(0);
		FactorCell secondSelected = diagram.getSelectedFactor(1);
		if (firstSelected==null || secondSelected==null)
		{
			vbox.add(new PanelTitleLabel(EAM.text("<html>" +
					"<br>" +
					"<em><strong>HINT:</strong> You can quickly add links by selecting both factors in the diagram before hitting the Create Link button. <br>" +
					"On most systems, to select the second factor, hold down Ctrl while clicking the second factor in the diagram.<br>" +
					"")));
		}
			
		if(firstSelected != null)
			linkFromList.setSelectedValue(new FactorDropDownItem(firstSelected.getUnderlyingObject(), firstSelected.getDiagramFactor()), true);
		
		if(secondSelected != null)
			linkToList.setSelectedValue(new FactorDropDownItem(secondSelected.getUnderlyingObject(), secondSelected.getDiagramFactor()), true);

		return vbox;
	}
	
	private FastScrollPane createScroller(UiList listToWrap)
	{
		Dimension baseDimension = listToWrap.getPreferredScrollableViewportSize();
		final int ARBITRARY_REASONABLE_WIDTH = 300;
		final int ARBITRARY_REASONABLE_HEIGHT = 400;
		int width = Math.min(ARBITRARY_REASONABLE_WIDTH, baseDimension.width);
		int height = Math.min(ARBITRARY_REASONABLE_HEIGHT, baseDimension.height);
		FastScrollPane scroller = new FastScrollPane(listToWrap);
		scroller.getViewport().setPreferredSize(new Dimension(width, height));
		return scroller;
	}
	
	private UiList createChoices(int linkFromTo)
	{
		
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

		Collections.sort(dropDownItems, new IgnoreCaseStringComparator());
		UiList list = new UiList(dropDownItems);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return list;
	}

	private DiagramFactor[] getFilteredDiagramFactors(DiagramFactor[] allDiagramFactors)
	{
		Vector filterdDiagramFactors = new Vector();
		for (int i = 0; i < allDiagramFactors.length; ++i)
		{
			int wrappedType = allDiagramFactors[i].getWrappedType();
			if (wrappedType != ObjectType.TEXT_BOX)
				filterdDiagramFactors.add(allDiagramFactors[i]);
		}
			
		return (DiagramFactor[]) filterdDiagramFactors.toArray(new DiagramFactor[0]);
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
		okButton = new PanelButton(EAM.text("Button|OK"));
		okButton.addActionListener(this);
		getRootPane().setDefaultButton(okButton);
		cancelButton = new PanelButton(EAM.text("Button|Cancel"));
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
			if(linkFromList.getSelectedIndex() < 0 || linkToList.getSelectedIndex() < 0)
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
		FactorDropDownItem item = (FactorDropDownItem)linkFromList.getSelectedValue();
		return item.getDiagramFactor();
	}
	
	public DiagramFactor getTo()
	{
		FactorDropDownItem item = (FactorDropDownItem)linkToList.getSelectedValue();
		return item.getDiagramFactor();
	}
	
	DiagramPanel diagramPanel;
	boolean result;
	UiList linkFromList;
	UiList linkToList;
	UiButton okButton;
	UiButton cancelButton;
}
