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
import java.util.Vector;

import javax.swing.JComponent;

import org.miradi.main.AppPreferences;

public class SelectableRow
{
	public SelectableRow(Vector<JComponent> selectableComponentsToUse, AbstractLongDescriptionProvider descriptionProviderToUse)
	{
		selectableComponents = selectableComponentsToUse;
		descriptionProvider = descriptionProviderToUse;
	}
	
	public void addMouseListener(MouseListener listener)
	{
		for (int index = 0; index < selectableComponents.size(); ++index)
		{
			selectableComponents.get(index).addMouseListener(listener);
		}		
	}
	
	public AbstractLongDescriptionProvider getDescriptionProvider()
	{
		return descriptionProvider;
	}
	
	public void selectRow() throws Exception
	{
		isSelected = true;
		for (int index = 0; index < selectableComponents.size(); ++index)
		{
			setSelectionBorder(selectableComponents.get(index));
		}
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
		for (int index = 0; index < selectableComponents.size(); ++index)
		{
			setBackgroundColor(selectableComponents.get(index), AppPreferences.getDataPanelBackgroundColor());
		}
	}
	
	private void setBackgroundColor(JComponent component, Color backgroundColor)
	{
		component.setOpaque(true);
		component.setBackground(backgroundColor);
	}

	@Override
	public int hashCode()
	{
		return selectableComponents.hashCode();
	}

	@Override
	public boolean equals(Object rawObjet)
	{
		if(!(rawObjet instanceof SelectableRow))
			return false;

		SelectableRow selectableRow = (SelectableRow) rawObjet;

		return selectableRow.selectableComponents.equals(selectableComponents);
	}

	private Vector<JComponent> selectableComponents;
	private boolean isSelected;
	private AbstractLongDescriptionProvider descriptionProvider;
}
