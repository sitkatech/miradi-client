/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
/**
 * 
 */
package org.miradi.dialogs.viability;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.miradi.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.miradi.dialogs.tablerenderers.TableCellRendererForObjects;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.icons.GoalIcon;
import org.miradi.objects.Goal;
import org.miradi.objects.Measurement;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.TrendQuestion;

class MeasurementValueRenderer extends TableCellRendererForObjects
{
	public MeasurementValueRenderer(TargetViabilityTreeTable providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		question = new StatusQuestion();
	}
	
	public void setColumnTag(String columnTagToUse)
	{
		columnTag = columnTagToUse;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JLabel renderer = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		Color color = getBackground();
		renderer.setIcon(null);
		if(value != null && !value.equals(""))
		{
			ChoiceItem choice = question.findChoiceByCode(columnTag);
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
		TargetViabilityTreeTable treeTable = (TargetViabilityTreeTable)getObjectProvider();
		TreeTableNode node = treeTable.getNodeForRow(row);
		if (node.getType() == Goal.getObjectType())
			return new GoalIcon();
		
		if (node.getType() != Measurement.getObjectType())
			return null;
		
		String trendData = node.getObject().getData(Measurement.TAG_TREND);
		return getTrendIcon(trendData);
	}
	
	public Icon getTrendIcon(String measurementTrendCode)
	{
		TrendQuestion trendQuestion = new TrendQuestion();
		ChoiceItem findChoiceByCode = trendQuestion.findChoiceByCode(measurementTrendCode);
		
		return findChoiceByCode.getIcon();
	}

	private String columnTag;
	private StatusQuestion question;
}