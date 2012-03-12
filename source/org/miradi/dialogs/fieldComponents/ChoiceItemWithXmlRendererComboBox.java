/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.fieldComponents;

import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;

import org.miradi.icons.RatingIcon;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

//FIXME this is duplicate of ChoiceItemComboBox with the exception of decoding for xml vs html
public class ChoiceItemWithXmlRendererComboBox extends AbstractChoiceItemComboBox
{
	public ChoiceItemWithXmlRendererComboBox(ChoiceQuestion question)
	{
		this(question.getChoices());
	}

	public ChoiceItemWithXmlRendererComboBox(Vector<ChoiceItem> items)
	{
		this(items.toArray(new ChoiceItem[0]));
	}
	
	public ChoiceItemWithXmlRendererComboBox(ChoiceItem[] items)
	{
		super(items);
		
		setRenderer(createListCellRenderer());
	}

	protected ChoiceItemListCellRenderer createListCellRenderer()
	{
		return new ChoiceItemListCellRenderer();
	}
	
	private class ChoiceItemListCellRenderer extends DefaultListCellRenderer
	{
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
		{
			Component cell = super.getListCellRendererComponent(list, value, index, isSelected,	cellHasFocus);
			ChoiceItem choiceItem = (ChoiceItem) value;
			setText(choiceItem.getXmlLabel());
			if (value!=null)
			{
				Icon icon = getOrCreateIcon((ChoiceItem)value); 
				setIcon(icon);
			}
			return cell;
		}

		private Icon getOrCreateIcon(ChoiceItem thisOption)
		{
			Icon icon = thisOption.getIcon();
			if(icon != null)
				return icon;
			
			Color color = thisOption.getColor();
			if(color != null)
				return new RatingIcon(thisOption);
			
			return null;
		}		
	}
}
