/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fieldComponents;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;

import org.conservationmeasures.eam.icons.RatingIcon;
import org.conservationmeasures.eam.questions.ChoiceItem;

public class ChoiceItemComboBox extends PanelComboBox
{
	public ChoiceItemComboBox(ChoiceItem[] items)
	{
		super(items);
		setRenderer(new ChoiceItemRenderer());
	}

	static class ChoiceItemRenderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
		{
			Component cell = super.getListCellRendererComponent(list, value, index, isSelected,	cellHasFocus);
			ChoiceItem thisOption = (ChoiceItem)value;
			if (value!=null)
			{
				Icon icon = thisOption.getIcon();
				if(icon == null)
					icon = new RatingIcon(thisOption); 
				setIcon(icon);
			}
			return cell;
		}
	}
}
