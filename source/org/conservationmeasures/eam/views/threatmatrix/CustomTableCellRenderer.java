/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.RatingCriterion;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

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
		selected =  hasFocus;
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
	
	private ThreatRatingFramework getThreatRatingFramework()
	{
		return threatGridPanel.getThreatMatrixView().getThreatRatingFramework();
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

		boolean optionSelected = false;
		if (selected && optionSelected &&  (bundle!=null))
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
		drawRect(g, offset, 0, width, height, valueOption.getColor());
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

	//TODO: it is possible that this should be a loop in case more ratings are added
	private void drawRatingBoxes(Graphics g, int height)
	{
		RatingCriterion[] criterionItems = getThreatRatingFramework().getCriteria();
		drawRatingBox(g, 0, 0, INNER_CELL_RATING_BOX_WIDTH, height/3, criterionItems[0]);
		drawRatingBox(g, 0, height/3, INNER_CELL_RATING_BOX_WIDTH, height/3, criterionItems[1]);
		drawRatingBox(g, 0, 2*(height/3), INNER_CELL_RATING_BOX_WIDTH, height/3, criterionItems[2]);
	}

	private void drawRatingBox(Graphics g, int xpos, int ypos, int width, int height, RatingCriterion criterionItem)
	{
		BaseId valueId = bundle.getValueId(criterionItem.getId());
		ValueOption option = getThreatRatingFramework().getValueOption(valueId);
		drawRect(g, xpos, ypos, width, height, option.getColor());
		g.setColor(Color.BLACK);
		g.drawRect(xpos, ypos, width, height);
	}

	private void drawRect(Graphics g, int xpos, int ypos, int width, int height, Color colorToUse)
	{
		g.setColor(colorToUse);
		g.fillRect(xpos, ypos, width, height);
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

	private static final int INNER_CELL_RATING_BOX_WIDTH = 10;
	boolean selected;
	ThreatGridPanel threatGridPanel;
	ThreatRatingBundle bundle;
	ValueOption valueOption;
	Font font;
	int renderingRow;
	int renderingCol;
}