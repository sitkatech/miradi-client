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

abstract public class SelectableRow
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
	
	public boolean isSelected()
	{
		return isSelected;
	}

	public void selectRow() throws Exception
	{
		updateSelection(true);
	}
	
	public void clearSelection()
	{
		updateSelection(false);
	}

	private void updateSelection(boolean shouldSelect)
	{
		Color fg = (shouldSelect ? getSelectedForegroundColor() : getUnselectedForegroundColor());
		Color bg = (shouldSelect ? getSelectedBackgroundColor() : getUnselectedBackgroundColor());
		isSelected = shouldSelect;
		for (int index = 0; index < selectableComponents.size(); ++index)
		{
			setColors(selectableComponents.get(index), fg, bg);
		}
	}

	abstract protected Color getSelectedForegroundColor();
	abstract protected Color getUnselectedForegroundColor();
	abstract protected Color getSelectedBackgroundColor();
	abstract protected Color getUnselectedBackgroundColor();
	
	private void setColors(JComponent component, Color foregroundColor, Color backgroundColor)
	{
		component.setOpaque(true);
		component.setForeground(foregroundColor);
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
