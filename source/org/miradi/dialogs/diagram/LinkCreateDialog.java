/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.diagram;

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

import org.martus.swing.UiButton;
import org.martus.swing.UiList;
import org.martus.swing.UiVBox;
import org.martus.swing.Utilities;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.dialogs.base.DialogWithButtonBar;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;
import org.miradi.utils.IgnoreCaseStringComparator;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.views.diagram.LinkCreator;

import com.jhlabs.awt.Alignment;
import com.jhlabs.awt.GridLayoutPlus;

public class LinkCreateDialog extends DialogWithButtonBar implements ActionListener
{
	public LinkCreateDialog(MainWindow parent, DiagramPanel panelToUse) throws HeadlessException
	{
		super(parent);
		setTitle(EAM.text("Title|Link Create Dialog"));
		
		diagramPanel = panelToUse;
		UiVBox bigBox = new UiVBox();
		bigBox.add(createFromToBox());

		Container contents = getContentPane();
		contents.add(new MiradiScrollPane(bigBox));
		setButtons(getButtonComponents());
		Utilities.centerDlg(this);
		setResizable(true);
		setModal(true);
		
	}
	
	private JPanel createFromToBox()
	{
		linkFromList = createChoices(FactorLink.FROM);
		linkToList = createChoices(FactorLink.TO);
		DiagramComponent diagram = diagramPanel.getCurrentDiagramComponent();

		GridLayoutPlus layout = new GridLayoutPlus(2,1);
		layout.setFill(Alignment.FILL_NONE);
		layout.setAlignment(Alignment.CENTER);
		JPanel vbox = new JPanel(layout);
		
		MiradiScrollPane fromScroller = createScroller(linkFromList);
		MiradiScrollPane toScroller = createScroller(linkToList);
		
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
			linkFromList.setSelectedValue(new FactorDropDownItem(firstSelected.getWrappedFactor(), firstSelected.getDiagramFactor()), true);
		
		if(secondSelected != null)
			linkToList.setSelectedValue(new FactorDropDownItem(secondSelected.getWrappedFactor(), secondSelected.getDiagramFactor()), true);

		return vbox;
	}
	
	private MiradiScrollPane createScroller(UiList listToWrap)
	{
		Dimension baseDimension = listToWrap.getPreferredScrollableViewportSize();
		final int ARBITRARY_REASONABLE_WIDTH = 300;
		final int ARBITRARY_REASONABLE_HEIGHT = 400;
		int width = Math.min(ARBITRARY_REASONABLE_WIDTH, baseDimension.width);
		int height = Math.min(ARBITRARY_REASONABLE_HEIGHT, baseDimension.height);
		MiradiScrollPane scroller = new MiradiScrollPane(listToWrap);
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
			if (LinkCreator.isValidLinkableType(wrappedType))
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

	private Vector<Component> getButtonComponents()
	{
		okButton = new PanelButton(EAM.text("Button|OK"));
		okButton.addActionListener(this);
		getRootPane().setDefaultButton(okButton);
		cancelButton = new PanelButton(EAM.text("Button|Cancel"));
		cancelButton.addActionListener(this);

		Vector<Component> buttons = new Vector<Component>();
		buttons.add(Box.createHorizontalGlue());
		buttons.add(okButton);
		buttons.add(Box.createHorizontalStrut(10));
		buttons.add(cancelButton);
		return buttons;
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
	
	private DiagramPanel diagramPanel;
	private boolean result;
	private UiList linkFromList;
	private UiList linkToList;
	private UiButton okButton;
	private UiButton cancelButton;
}
