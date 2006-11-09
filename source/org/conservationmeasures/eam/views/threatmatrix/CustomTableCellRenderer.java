/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.ThreatRatingBundle;

class CustomTableCellRenderer extends DefaultTableCellRenderer
{
	public CustomTableCellRenderer(ThreatGirdPanel threatGridPanelToUse)
	{
		threatGridPanel = threatGridPanelToUse;
		setHorizontalAlignment(CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component cell = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		cell.setBackground( ((ValueOption)value).getColor() );
		cell.setFont(new Font(null,Font.BOLD,12));
		cell.setForeground(Color.BLACK); 
		
		setBoarders(table, row, column);
		
		return cell;
	}

	private void setBoarders(JTable table, int row, int column)
	{
		
		if (isOverallRatingCell(table, row, column))
			setBorder(BorderFactory.createMatteBorder(5,5,1,1,Color.DARK_GRAY));
		else
		if (isSummaryRowCell(table, row, column))
			setBorder(BorderFactory.createMatteBorder(5,1,1,1,Color.DARK_GRAY));
		else 
		if (isSummaryColumnCell(table, row, column))
				setBorder(BorderFactory.createMatteBorder(1,5,1,1,Color.DARK_GRAY));
		else 
			setBorderForCell(row, column);
	}


	private void setBorderForCell(int row, int column)
	{
		try 
		{
			int indirectColumn = threatGridPanel.getThreatMatrixTable().getColumnModel().getColumn(column).getModelIndex();
			ThreatRatingBundle bundle = 
				((NonEditableThreatMatrixTableModel)threatGridPanel.getThreatMatrixTable().getModel()).getBundle(row, indirectColumn);
			if(bundle != null)
				if(threatGridPanel.getSelectedBundle().equals(bundle))
				{
					setBorder(BorderFactory.createCompoundBorder(BorderFactory
							.createLineBorder(Color.BLUE, 3), getBorder()));
					return;
				}
		}
		catch (Exception e)
		{
		}
		
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY,1),getBorder()));
		return;
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
	ThreatGirdPanel threatGridPanel;
}