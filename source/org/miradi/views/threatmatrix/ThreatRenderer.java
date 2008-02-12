/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.threatmatrix;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.miradi.icons.ThreatPriorityIcon;
import org.miradi.objects.ValueOption;

class ThreatRenderer extends DefaultListCellRenderer
{
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
	{
		Component cell = super.getListCellRendererComponent(list, value, index, isSelected,	cellHasFocus);
		ValueOption thisOption = (ValueOption)value;
		setIcon(new ThreatPriorityIcon(thisOption));
		return cell;
	}
}