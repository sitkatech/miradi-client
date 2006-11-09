/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.table.TableCellRenderer;


class TableHeaderRenderer
	extends JTextArea 
    implements TableCellRenderer
{

	public TableHeaderRenderer()
    {
        setBorder(noFocusBorder);
        setWrapStyleWord(true);
        setLineWrap(true);
    }

    public void updateUI()
    {
        super.updateUI();
        createCellSelectionBorders();
    }

	private void createCellSelectionBorders()
	{
		Border cellBorder = UIManager.getBorder("TableHeader.cellBorder");
        Border focusCellHighlightBorder = UIManager.getBorder("Table.focusCellHighlightBorder");
        
        createCellNotSelectionBorder(cellBorder, focusCellHighlightBorder);
	}


	private void createCellNotSelectionBorder(Border cellBorder, Border focusCellHighlightBorder)
	{
		Insets insets = focusCellHighlightBorder.getBorderInsets(this);
		noFocusBorder = new BorderUIResource.CompoundBorderUIResource
             (cellBorder, BorderFactory.createEmptyBorder(insets.top, EIGTH_OF_INCH, insets.bottom, insets.right));
	}


    public Component getTableCellRendererComponent(JTable table, Object value,
                       boolean selected, boolean focused, int row, int column)
    {
        Color selectionBackground = table.getSelectionBackground();
        Color selectionForeground = table.getSelectionForeground();
        setupCellRendererComponent(table, value, selectionBackground, selectionForeground);
        return this;
    }
    
    
	private void setupCellRendererComponent(JComponent component, Object value,Color selectionBackground ,  Color selectionForeground)
	{
        setBackground(component.getBackground());
        setForeground(component.getForeground());
        setBorder(noFocusBorder);

        setOpaque(false);
        setText(value.toString());
		setFont(new Font(null,Font.BOLD,12));
		int height = (getPreferredSize().height<THREE_QUATERS_INCH) ? THREE_QUATERS_INCH: getPreferredSize().height;
        setPreferredSize(new Dimension(150,height));
	}

	private static final int EIGTH_OF_INCH = 5;
    private static final int THREE_QUATERS_INCH = 55;
	private Border noFocusBorder;
}

