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


class TargetRowHeaderRenderer
	extends JTextArea 
    implements TableCellRenderer
{

    public TargetRowHeaderRenderer()
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
             (cellBorder, BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
	}


    public Component getTableCellRendererComponent(JTable table, Object value,
                       boolean selected, boolean focused, int row, int column)
    {
        Color selectionBackground = table.getSelectionBackground();
        Color selectionForeground = table.getSelectionForeground();
        setupCellRendererComponent(table, value, selected, focused, selectionBackground, selectionForeground);
        return this;
    }
    
    
	private void setupCellRendererComponent(JComponent component, Object value, boolean selected, boolean focused, Color selectionBackground ,  Color selectionForeground)
	{
        setBackground(component.getBackground());
        setForeground(component.getForeground());
        setBorder(noFocusBorder);

        setOpaque(false);
        setText(value.toString());
		setFont(new Font(null,Font.BOLD,12));
        setSize(150,Short.MAX_VALUE);
        setPreferredSize(new Dimension(150,getPreferredSize().height));
	}

    
    protected Border noFocusBorder;
}

