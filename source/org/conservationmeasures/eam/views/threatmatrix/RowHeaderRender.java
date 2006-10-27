/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.table.TableCellRenderer;


class RowHeaderRenderer
	extends JTextArea 
    implements ListCellRenderer , TableCellRenderer
{

    public RowHeaderRenderer()
    {
        setOpaque(true);
        setBorder(noFocusBorder);
        setWrapStyleWord(true);
        setLineWrap(true);
    }

    public void updateUI()
    {
        super.updateUI();
        Border cellBorder = UIManager.getBorder("TableHeader.cellBorder");
        Border foucsCellHighlightBorder = UIManager.getBorder("Table.focusCellHighlightBorder");

        focusBorder = new BorderUIResource.CompoundBorderUIResource(cellBorder, foucsCellHighlightBorder);
 
        Insets insets = foucsCellHighlightBorder.getBorderInsets(this);

        noFocusBorder = new BorderUIResource.CompoundBorderUIResource
             (cellBorder, BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
    }

    public Component getListCellRendererComponent(JList list, Object value, 
        int index, boolean selected, boolean focused) 
    {
        Color selBackground = list.getSelectionBackground();
        Color selForeground = list.getSelectionForeground();
        setupCellRendereComponent(list, value, selected, focused, selBackground, selForeground);
        return this;
    }


    public Component getTableCellRendererComponent(JTable table, Object value,
                       boolean selected, boolean focused, int row, int column)
    {
        Color selBackground = table.getSelectionBackground();
        Color selForeground = table.getSelectionForeground();
        setupCellRendereComponent(table, value, selected, focused, selBackground, selForeground);
        return this;
    }
    
    
	private void setupCellRendereComponent(JComponent component, Object value, boolean selected, boolean focused, Color selectionBackground ,  Color selectionForeground)
	{
        if (selected)
        {
            setBackground(selectionBackground);
            setForeground(selectionForeground);
        }
        else
        {
            setBackground(component.getBackground());
            setForeground(component.getForeground());
        }

        setFont(component.getFont());

        setEnabled(component.isEnabled());

        if (focused)
            setBorder(focusBorder);
        else
            setBorder(noFocusBorder);

        setText((String)value);
	}

    
    protected Border noFocusBorder;
    protected Border focusBorder;


}

