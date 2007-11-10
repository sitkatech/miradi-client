/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
/**
 * 
 */
package org.conservationmeasures.eam.dialogs.viability;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.conservationmeasures.eam.dialogs.fieldComponents.TreeNodeForRowProvider;
import org.conservationmeasures.eam.dialogs.treetables.TableCellRendererWithColor;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.questions.TrendQuestion;

class MeasurementValueRenderer extends TableCellRendererWithColor
{
	public MeasurementValueRenderer(TreeNodeForRowProvider providerToUse)
	{
		super(providerToUse);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JLabel renderer = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		String columnTag = nodeProvider.getColumnTag(tableColumn);
		Color color = getBackgroundColor(columnTag);
		renderer.setIcon(null);
		if(value != null && !value.equals(""))
		{
			ChoiceItem choice = new StatusQuestion("").findChoiceByCode(columnTag);
			color = choice.getColor();
			renderer.setIcon(getCellIcon(row, choice));
		}
		renderer.setBackground(color);
		if (isSelected)
			setBackground(table.getSelectionBackground());
		
		return renderer;
	}

	public Icon getCellIcon(int row, ChoiceItem choice)
	{
		TreeTableNode node = nodeProvider.getNodeForRow(row);
		if (node.getType() == Goal.getObjectType())
			return new GoalIcon();
		
		if (node.getType() != Measurement.getObjectType())
			return null;
		
		String trendData = node.getObject().getData(Measurement.TAG_TREND);
		return getTrendIcon(trendData);
	}
	
	public Icon getTrendIcon(String measurementTrendCode)
	{
		TrendQuestion trendQuestion = new TrendQuestion(Measurement.TAG_TREND);
		ChoiceItem findChoiceByCode = trendQuestion.findChoiceByCode(measurementTrendCode);
		
		return findChoiceByCode.getIcon();
	}

}