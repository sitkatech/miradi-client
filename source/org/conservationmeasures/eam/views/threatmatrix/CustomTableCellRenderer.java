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
		bundle = getBundle(row, column);
		valueOption = (ValueOption)value;
		renderingRow = row;
		renderingCol = column;
		
		setBorders(table, row, column);
		
		return this;
	}

	private ThreatRatingBundle getBundle(int row, int column)
	{
		try
		{
			int indirectColumn = threatGridPanel.getThreatMatrixTable().convertColumnIndexToModel(column);
			return getThreatTableModel().getBundle(row, indirectColumn);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return null;
		}
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
		
		int widthForRatingBoxes = 0;
		int height = getHeight();

		boolean optionSelected = false;
		if ( optionSelected &&  (bundle!=null))
		{
			widthForRatingBoxes = 10;
			drawMainCellBody(g, widthForRatingBoxes, height);
			drawRatingBoxes(g, widthForRatingBoxes, height);
		}
		else
			drawMainCellBody(g, widthForRatingBoxes, height);
	}

	private void drawMainCellBody(Graphics g, int widthForRatingBoxes, int height)
	{
		// ***do not adjust for summary cells
		int width = getWidth() - widthForRatingBoxes;
		drawRect(g, widthForRatingBoxes, 0, width, height, valueOption.getColor());
	
		String label = valueOption.getLabel();
		g.setFont(font);
		int textHeight = g.getFontMetrics().getAscent();
		int textWidth = g.getFontMetrics().stringWidth(label);
		g.setColor(Color.BLACK);
		g.drawString(label, (width-textWidth)/2 + widthForRatingBoxes, (height-textHeight)/2 + textHeight);
	}

	//TODO: it is possible that this should be a loop in case more ratings are added
	private void drawRatingBoxes(Graphics g, int width, int height)
	{
		RatingCriterion[] criterionItems = getThreatRatingFramework().getCriteria();
		drawRatingBox(g, 0, 0, width, height/3, criterionItems[0]);
		drawRatingBox(g, height/3, 0, width, height/3, criterionItems[1]);
		drawRatingBox(g, 2*(height/3), 0, width, height/3, criterionItems[2]);
	}

	private void drawRatingBox(Graphics g, int xpos, int ypos, int width, int height, RatingCriterion criterionItem)
	{
		BaseId valueId = bundle.getValueId(criterionItem.getId());
		ValueOption option = getThreatRatingFramework().getValueOption(valueId);
		drawRect(g, ypos, xpos, width, height, option.getColor());
		g.setColor(Color.BLACK);
		g.drawRect(ypos, xpos, width, height);
	}

	private void drawRect(Graphics g,  int ypos,int xpos, int width, int height, Color colorToUse)
	{
		g.setColor(colorToUse);
		g.fillRect(ypos, xpos, width, height);
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


	ThreatGridPanel threatGridPanel;
	ThreatRatingBundle bundle;
	ValueOption valueOption;
	Font font;
	int renderingRow;
	int renderingCol;
}