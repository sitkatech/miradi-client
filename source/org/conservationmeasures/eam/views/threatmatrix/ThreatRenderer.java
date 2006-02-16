/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.conservationmeasures.eam.icons.ThreatPriorityIcon;
import org.conservationmeasures.eam.objects.RatingValueOption;

class ThreatRenderer extends DefaultListCellRenderer
{
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
	{
		Component cell = super.getListCellRendererComponent(list, value, index, isSelected,	cellHasFocus);
		RatingValueOption thisOption = (RatingValueOption)value;
		setIcon(new ThreatPriorityIcon(thisOption));
		return cell;
	}
}