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
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JComponent;

abstract public class SelectableRow
{
	public SelectableRow(Vector<JComponent> selectableComponentsToUse, AbstractLongDescriptionProvider descriptionProviderToUse)
	{
		selectableComponents = selectableComponentsToUse;
		descriptionProvider = descriptionProviderToUse;
		foregroundColorsOfComponents = new HashMap<JComponent, Color>();
		backgroundColorsOfComponents = new HashMap<JComponent, Color>();
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
		if(isSelected())
			return;
		
		foregroundColorsOfComponents.clear();
		backgroundColorsOfComponents.clear();
		Color fg = getSelectedForegroundColor();
		Color bg = getSelectedBackgroundColor();
		for (int index = 0; index < selectableComponents.size(); ++index)
		{
			JComponent component = selectableComponents.get(index);
			foregroundColorsOfComponents.put(component, component.getForeground());
			backgroundColorsOfComponents.put(component, component.getBackground());
			setColors(component, fg, bg);
		}
		isSelected = true;
	}
	
	public void clearSelection()
	{
		if(!isSelected())
			return;
		
		for (int index = 0; index < selectableComponents.size(); ++index)
		{
			JComponent component = selectableComponents.get(index);
			Color fg = foregroundColorsOfComponents.get(component);
			Color bg = backgroundColorsOfComponents.get(component);
			setColors(component, fg, bg);
		}
		isSelected = false;
	}

	abstract protected Color getSelectedForegroundColor();
	abstract protected Color getSelectedBackgroundColor();
	
	private void setColors(JComponent component, Color foregroundColor, Color backgroundColor)
	{
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
	private HashMap<JComponent, Color> foregroundColorsOfComponents;
	private HashMap<JComponent, Color> backgroundColorsOfComponents;
}
