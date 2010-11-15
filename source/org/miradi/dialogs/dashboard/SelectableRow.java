/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.dashboard;

import java.awt.Color;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import org.miradi.main.AppPreferences;

public class SelectableRow
{
	public SelectableRow(JComponent leftSideToUse, JComponent rightSideToUse, StaticLongDescriptionProvider descriptionProviderToUse)
	{
		leftSide = leftSideToUse;
		rightSide = rightSideToUse;
		descriptionProvider = descriptionProviderToUse;
	}
	
	public void addMouseListener(MouseListener listener)
	{
		leftSide.addMouseListener(listener);
		rightSide.addMouseListener(listener);
	}
	
	public StaticLongDescriptionProvider getDescriptionProvider()
	{
		return descriptionProvider;
	}
	
	public void selectRow() throws Exception
	{
		isSelected = true;
		setSelectionBorder(leftSide);
		setSelectionBorder(rightSide);
	}
	
	private void unSelect()
	{
		isSelected = false;
	}

	public boolean isSelected()
	{
		return isSelected;
	}

	private void setSelectionBorder(JComponent component)
	{
		setBackgroundColor(component, AppPreferences.getWizardBackgroundColor());
	}

	public void clearSelection()
	{
		unSelect();
		setBackgroundColor(leftSide, AppPreferences.getDataPanelBackgroundColor());
		setBackgroundColor(rightSide, AppPreferences.getDataPanelBackgroundColor());
	}
	
	private void setBackgroundColor(JComponent component, Color backgroundColor)
	{
		component.setOpaque(true);
		component.setBackground(backgroundColor);
	}

	@Override
	public int hashCode()
	{
		return leftSide.hashCode() + rightSide.hashCode();
	}

	@Override
	public boolean equals(Object rawObjet)
	{
		if(!(rawObjet instanceof SelectableRow))
			return false;

		SelectableRow selectableRow = (SelectableRow) rawObjet;
		if(!selectableRow.leftSide.equals(leftSide))
			return false;

		return selectableRow.rightSide.equals(rightSide);
	}
	
	private JComponent leftSide;
	private JComponent rightSide;
	private boolean isSelected;
	private StaticLongDescriptionProvider descriptionProvider;
}
