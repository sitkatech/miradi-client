/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.threatmatrix;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.miradi.ids.BaseId;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objects.RatingCriterion;
import org.miradi.objects.ValueOption;
import org.miradi.project.SimpleThreatRatingFramework;
import org.miradi.project.ThreatRatingBundle;
import org.miradi.utils.Utility;

class CustomTableCellRenderer extends JComponent implements TableCellRenderer
{
	public CustomTableCellRenderer(ThreatGridPanel threatGridPanelToUse)
	{
		threatGridPanel = threatGridPanelToUse;
		font = new Font(null,Font.BOLD,12);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		bundle = null;
		valueOption = (ValueOption)value;
		renderingRow = row;
		renderingCol = column;
		setBorders(table, row, column);
		
		return this;
	}

	
	private void setBorders(JTable table, int row, int column)
	{
		if (isOverallRatingCell(table, row, column))
			setBorder(BorderFactory.createMatteBorder(5,5,1,1,Color.DARK_GRAY));
		else if (isSummaryRowCell(table, row, column))
			setBorder(BorderFactory.createMatteBorder(5,1,1,1,Color.DARK_GRAY));
		else if (isSummaryColumnCell(table, row, column))
			setBorder(BorderFactory.createMatteBorder(1,5,1,1,Color.DARK_GRAY));
		else 
			setBorderForNormalCell(row, column);
	}


	private void setBorderForNormalCell(int row, int column)
	{
		try 
		{
			int indirectColumn = threatGridPanel.getThreatMatrixTable().convertColumnIndexToModel(column);
			//TODO: should not set selection bundle here as part of setBoarder...either that or this mehtod is miss named
			bundle = getThreatTableModel().getBundle(row, indirectColumn);
			
			if(bundle != null && threatGridPanel.getSelectedBundle()!= null)
			{
				if(threatGridPanel.getSelectedBundle().equals(bundle))
				{
					setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
					return;
				}
			}

			setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1));
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
	}

	private ThreatMatrixTableModel getThreatTableModel()
	{
		return (ThreatMatrixTableModel)threatGridPanel.getThreatMatrixTable().getModel();
	}
	
	private AppPreferences getAppPreferences()
	{
		//TODO: shold be a better way to get preferences, instead of this , or going in via the view which we can not use here do to image print
		return EAM.getMainWindow().getAppPreferences();
	}

	private SimpleThreatRatingFramework getThreatRatingFramework()
	{
		return threatGridPanel.getProject().getSimpleThreatRatingFramework();
	}

	private boolean isOverallRatingCell(JTable table, int row, int column)
	{
		return(row==maxIndex(table.getRowCount())) && 
				(column==maxIndex(table.getColumnCount()));
	}
	
	private boolean isSummaryRowCell(JTable table, int row, int column)
	{
		return( row==maxIndex(table.getRowCount()));
	}
	
	private boolean isSummaryColumnCell(JTable table, int row, int column)
	{
		return (column==maxIndex(table.getColumnCount()));
	}
	
	public int maxIndex(int arraySize) 
	{
		return arraySize-1;
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintBorder(g);
		int height = getHeight();

		boolean isCellRatingVisible = getAppPreferences().getBoolean(AppPreferences.TAG_CELL_RATINGS_VISIBLE);
		if (isCellRatingVisible &&  (bundle!=null))
		{
			drawMainCellBody(g, INNER_CELL_RATING_BOX_WIDTH, height);
			drawRatingBoxes(g, height);
		}
		else
			drawMainCellBody(g, 0, height);
	}


	private void drawMainCellBody(Graphics g, int offset, int height)
	{
		int width = getWidth() - offset;
		drawSolidRect(g, offset, 0, width, height, valueOption.getColor());
		drawMainBobyCellText(g, offset, width, height);
	}

	private void drawMainBobyCellText(Graphics g, int offset, int width, int height)
	{
		String label = valueOption.getLabel();
		g.setFont(font);
		int textHeight = g.getFontMetrics().getAscent();
		int textWidth = g.getFontMetrics().stringWidth(label);
		g.setColor(Color.BLACK);
		g.drawString(label, (width-textWidth)/2 + offset, (height-textHeight)/2 + textHeight);
	}

	private void drawRatingBoxes(Graphics g, int height)
	{
		RatingCriterion[] criterionItems = getThreatRatingFramework().getCriteria();
		int boxHeight = height/criterionItems.length;
		for (int i=0; i<criterionItems.length; ++i)
			drawRatingBox(g, 0, i*boxHeight, INNER_CELL_RATING_BOX_WIDTH, boxHeight, criterionItems[i]);
	}

	private void drawRatingBox(Graphics g, int xpos, int ypos, int width, int height, RatingCriterion criterionItem)
	{
		BaseId valueId = bundle.getValueId(criterionItem.getId());
		ValueOption option = getThreatRatingFramework().getValueOption(valueId);
		drawSolidRect(g, xpos+1, ypos, width, height, option.getColor());
		drawLineRect(g, xpos+1, ypos, width, height, Color.BLACK);
		
		Font letterFont = g.getFont().deriveFont(10.0f).deriveFont(Font.BOLD);
		g.setFont(letterFont);
		String letter = option.getLabel().substring(0,1);
		Utility.drawStringCentered((Graphics2D)g, letter, new Rectangle(xpos+1, ypos, width, height));
	}
	
	private void drawSolidRect(Graphics g, int xpos, int ypos, int width, int height, Color colorToUse)
	{
		g.setColor(colorToUse);
		g.fillRect(xpos, ypos, width, height);
	}
	
	private void drawLineRect(Graphics g, int xpos, int ypos, int width, int height, Color colorToUse)
	{
		g.setColor(colorToUse);
		g.drawRect(xpos, ypos, width, height);
	}
	
	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue)
	{
		//  Do nothing, as recommended in the javadocs for DefaultTableCellRenderer
	}

	public void revalidate()
	{
		//  Do nothing, as recommended in the javadocs for DefaultTableCellRenderer
	}

	public void validate()
	{
		//  Do nothing, as recommended in the javadocs for DefaultTableCellRenderer
	}

	public void repaint()
	{
		//  Do nothing, as recommended in the javadocs for DefaultTableCellRenderer
	}

	private static final int INNER_CELL_RATING_BOX_WIDTH = 20;
	ThreatGridPanel threatGridPanel;
	ThreatRatingBundle bundle;
	ValueOption valueOption;
	Font font;
	int renderingRow;
	int renderingCol;
}