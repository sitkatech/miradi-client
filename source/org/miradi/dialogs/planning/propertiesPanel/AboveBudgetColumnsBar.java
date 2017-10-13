/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogs.planning.TableWithExpandableColumnsInterface;
import org.miradi.dialogs.tablerenderers.BasicTableCellEditorOrRendererFactory;
import org.miradi.main.AppPreferences;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Vector;

public class AboveBudgetColumnsBar extends AbstractFixedHeightDirectlyAboveTreeTablePanel implements AdjustmentListener
{
	public AboveBudgetColumnsBar(Project projectToUse, TableWithExpandableColumnsInterface tableToSitAbove)
	{
		project = projectToUse;
		table = tableToSitAbove;
	}
	
	public void setTableScrollPane(JScrollPane tableScrollPaneToUse)
	{
		tableScrollPane = tableScrollPaneToUse;
		tableScrollPane.getHorizontalScrollBar().addAdjustmentListener(this);
	}

	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		validate();
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(getBackground());
		g.fillRect(getX(), getY(), getWidth(), getHeight());
		DateUnit forever = new DateUnit();
		drawColumnGroupHeader(g, findColumnGroupBounds(WorkPlanColumnConfigurationQuestion.getAllPossibleWorkUnitsColumnGroups()), getWorkUnitsAboveColumnLabelLocal(), AppPreferences.getWorkUnitsBackgroundColor(forever));
		drawColumnGroupHeader(g, findColumnGroupBounds(WorkPlanColumnConfigurationQuestion.getAllPossibleExpensesColumnGroups()), getExpensesAboveColumnLabelLocal(), AppPreferences.getExpenseAmountBackgroundColor(forever));
		drawColumnGroupHeader(g, findColumnGroupBounds(WorkPlanColumnConfigurationQuestion.getAllPossibleBudgetTotalsColumnGroups()), getBudgetTotalsAboveColumnLabel(), AppPreferences.getBudgetDetailsBackgroundColor(forever));
	}

	protected String getWorkUnitsAboveColumnLabelLocal()
	{
		return getWorkUnitsAboveColumnLabel();
	}

	protected String getExpensesAboveColumnLabelLocal()
	{
		return getExpensesAboveColumnLabel();
	}

	public static String getWorkUnitsAboveColumnLabel()
	{
		return getChoiceLabel(WorkPlanColumnConfigurationQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE);
	}

	public static String getExpensesAboveColumnLabel()
	{
		return getChoiceLabel(WorkPlanColumnConfigurationQuestion.META_EXPENSE_ASSIGNMENT_COLUMN_CODE);
	}

	public static String getBudgetTotalsAboveColumnLabel()
	{
		return getChoiceLabel(WorkPlanColumnConfigurationQuestion.META_BUDGET_DETAIL_COLUMN_CODE);
	}
	
	protected static String getChoiceLabel(String metaResourceAssignmentColumnCode)
	{
		ChoiceQuestion question = StaticQuestionManager.getQuestion(WorkPlanColumnConfigurationQuestion.class);
		ChoiceItem choiceItem = question.findChoiceByCode(metaResourceAssignmentColumnCode);
		
		return choiceItem.getLabel();
	}

	protected Project getProject()
	{
		return project;
	}

	protected void drawColumnGroupHeader(Graphics g, Rectangle groupHeaderArea, String text, Color backgroundColor)
	{
		if(groupHeaderArea == null)
			return;
		
		g.setColor(backgroundColor);
		g.fillRect(groupHeaderArea.x, groupHeaderArea.y, groupHeaderArea.width, groupHeaderArea.height);
		g.setColor(BasicTableCellEditorOrRendererFactory.getCellBorderColor());
		g.drawRect(groupHeaderArea.x, groupHeaderArea.y, groupHeaderArea.width, groupHeaderArea.height);
		g.setColor(Color.BLACK);

		Shape oldClip = g.getClip();
		try
		{
			g.clipRect(groupHeaderArea.x, groupHeaderArea.y, groupHeaderArea.width, groupHeaderArea.height);
			Graphics2D g2 = (Graphics2D) g;
			Rectangle fontBounds = g.getFont().getStringBounds(text, g2.getFontRenderContext()).getBounds();
			int textX = groupHeaderArea.x + groupHeaderArea.width/2 - fontBounds.width/2;
			int textY = groupHeaderArea.y + groupHeaderArea.height/2 + fontBounds.height/2 - ARBITRARY_MARGIN;
			g.drawString(text, textX, textY);
		}
		finally
		{
			g.setClip(oldClip);
		}
	}

	protected Rectangle findColumnGroupBounds(Vector<String> columnGroups)
	{
		int startColumn = -1;
		int startX = 0;
		for(int tableColumn = 0; tableColumn < table.getColumnCount(); ++tableColumn)
		{
			if(columnGroups.contains(table.getColumnGroupCode(tableColumn)))
			{
				startColumn = tableColumn;
				break;
			}
			startX += table.getColumnWidth(tableColumn);
		}
		
		if(startColumn < 0)
			return null;

		Rectangle rect = new Rectangle(new Point(startX, getY()), new Dimension(0, getHeight()));
		while(startColumn < table.getColumnCount() && columnGroups.contains(table.getColumnGroupCode(startColumn)))
		{
			int thisColumnWidth = table.getColumnWidth(startColumn);
			rect.width += thisColumnWidth;
			++startColumn;
		}
		
		rect.x -= tableScrollPane.getHorizontalScrollBar().getValue();
		return rect;
	}

	private Project project;
	private TableWithExpandableColumnsInterface table;
	private JScrollPane tableScrollPane;
}
