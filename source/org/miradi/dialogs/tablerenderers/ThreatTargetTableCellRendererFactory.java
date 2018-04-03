/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import java.awt.*;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.miradi.dialogs.threatrating.upperPanel.TargetThreatLinkTable;
import org.miradi.dialogs.threatrating.upperPanel.TargetThreatLinkTableModel;
import org.miradi.icons.BundleIcon;
import org.miradi.icons.ColoredIcon;
import org.miradi.main.AppPreferences;
import org.miradi.objecthelpers.ThreatTargetVirtualLinkHelper;
import org.miradi.objects.Cause;
import org.miradi.objects.Target;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ThreatRatingQuestion;

public class ThreatTargetTableCellRendererFactory extends ChoiceItemTableCellRendererFactory
{
	public ThreatTargetTableCellRendererFactory(AppPreferences preferences,	RowColumnBaseObjectProvider providerToUse, FontForObjectProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		
		stressBasedIcon = new ColoredIcon();
		simpleIcon = new BundleIcon(preferences);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JLabel renderer = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);

		int modelColumn = table.convertColumnIndexToModel(tableColumn);

		TargetThreatLinkTableModel model = getModel(table);
		Cause threat = getThreat(table, row);
		Target target = getTarget(table, modelColumn);

		if(!isSelected)
			renderer.setBackground(getBackgroundColor(model, threat, target, value));

		String labelText = getText(model, threat, target, value);
		renderer.setText(labelText);

		Icon configuredIcon = getConfiguredIcon(model, threat, target, table, row, getChoiceItem(value));
		renderer.setIcon(configuredIcon);

		return renderer;
	}

	private Color getBackgroundColor(TargetThreatLinkTableModel model, Cause threat, Target target, Object value)
	{
		if (ThreatTargetVirtualLinkHelper.isThreatRatingNotApplicable(model.getProject(), threat.getRef(), target.getRef()))
			return ThreatRatingQuestion.NOT_APPLICABLE_COLOR;
		else
			return getBackgroundColor(getChoiceItem(value));
	}

	private String getText(TargetThreatLinkTableModel model, Cause threat, Target target, Object value)
	{
		if (ThreatTargetVirtualLinkHelper.isThreatRatingNotApplicable(model.getProject(), threat.getRef(), target.getRef()))
			return ThreatRatingQuestion.NOT_APPLICABLE;
		else
			return getLabelText(value);
	}

	private TargetThreatLinkTableModel getModel(JTable table)
	{
		TargetThreatLinkTable targetThreatLinkTable = (TargetThreatLinkTable) table;
		return targetThreatLinkTable.getTargetThreatLinkTableModel();
	}

	private Cause getThreat(JTable table, int row)
	{
		TargetThreatLinkTable targetThreatLinkTable = (TargetThreatLinkTable) table;
		TargetThreatLinkTableModel model = targetThreatLinkTable.getTargetThreatLinkTableModel();
		return (Cause) model.getDirectThreat(row);
	}

	private Target getTarget(JTable table, int modelColumn)
	{
		TargetThreatLinkTable targetThreatLinkTable = (TargetThreatLinkTable) table;
		TargetThreatLinkTableModel model = targetThreatLinkTable.getTargetThreatLinkTableModel();
		return model.getTarget(modelColumn);
	}

	private Icon getConfiguredIcon(TargetThreatLinkTableModel model, Cause threat, Target target, JTable table, int row, ChoiceItem choice)
	{
		if(!ThreatTargetVirtualLinkHelper.canSupportThreatRatings(model.getProject(), threat, target.getRef()))
			return null;

		if(model.getProject().isStressBaseMode())
		{
			if (ThreatTargetVirtualLinkHelper.isThreatRatingNotApplicable(model.getProject(), threat.getRef(), target.getRef()))
			{
				stressBasedIcon.setColor(ThreatRatingQuestion.NOT_APPLICABLE_COLOR);
				return stressBasedIcon;
			}

			if (choice == null)
				return null;

			stressBasedIcon.setColor(choice.getColor());
			return stressBasedIcon;
		}

		simpleIcon.setThreatTarget(threat, target);
		simpleIcon.setRowHeight(table.getRowHeight(row));

		return simpleIcon;
	}
	
	private BundleIcon simpleIcon;
	private ColoredIcon stressBasedIcon;
}
