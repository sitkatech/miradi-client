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
package org.miradi.dialogs.tablerenderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.miradi.dialogs.threatrating.upperPanel.TargetThreatLinkTable;
import org.miradi.dialogs.threatrating.upperPanel.TargetThreatLinkTableModel;
import org.miradi.icons.BundleIcon;
import org.miradi.icons.ColoredIcon;
import org.miradi.main.AppPreferences;
import org.miradi.objects.Cause;
import org.miradi.objects.Target;
import org.miradi.questions.ChoiceItem;

public class ThreatTargetTableCellRendererFactory extends ChoiceItemTableCellRendererFactory
{
	public ThreatTargetTableCellRendererFactory(AppPreferences preferences,	RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		
		defaultBackgroundColor = Color.WHITE;
		stressBasedIcon = new ColoredIcon();
		simpleIcon = new BundleIcon(preferences);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JLabel renderer = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		String labelText = getLabelText(value);

		if(!isSelected)
			renderer.setBackground(getBackgroundColor(getChoiceItem(value)));

		renderer.setText(labelText);

		int modelColumn = table.convertColumnIndexToModel(tableColumn);
		Icon configuredIcon = getConfiguredIcon(table, row, modelColumn, getChoiceItem(value));
		renderer.setIcon(configuredIcon);
		return renderer;
	}

	protected Color getBackgroundColor(Object value)
	{
		if(value == null)
			return Color.GRAY.brighter();
		
		ChoiceItem choice = getChoiceItem(value);
		if(choice == null || choice.getColor() == null)
			return defaultBackgroundColor;
		
		return choice.getColor();
	}
		
	protected Icon getConfiguredIcon(JTable table, int row, int modelColumn, ChoiceItem choice)
	{
		TargetThreatLinkTable targetThreatLinkTable = (TargetThreatLinkTable) table;
		TargetThreatLinkTableModel model = targetThreatLinkTable.getTargetThreatLinkTableModel();
		if(model.getProject().isStressBaseMode())
		{
			stressBasedIcon.setColor(choice.getColor());
			return stressBasedIcon;
		}
		
		Cause threat = (Cause)model.getDirectThreat(row);
		Target target = model.getTarget(modelColumn);
		if (threat != null && target != null)
		{
			simpleIcon.setThreatTarget(threat, target);
			return simpleIcon;
		}
		
		return null;
	}
	
	private BundleIcon simpleIcon;
	private ColoredIcon stressBasedIcon;
	private Color defaultBackgroundColor;
}
