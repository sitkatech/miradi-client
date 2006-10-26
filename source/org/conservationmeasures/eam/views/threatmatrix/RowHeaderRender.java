/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.BorderFactory;
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
    protected Border noFocusBorder1, focusBorder;


    public RowHeaderRenderer()
    {
        setOpaque(true);
        setBorder(noFocusBorder1);
        setWrapStyleWord(true);
        setLineWrap(true);
    }

    public void updateUI()
    {
        super.updateUI();
        Border cell = UIManager.getBorder("TableHeader.cellBorder");
        Border focus = UIManager.getBorder("Table.focusCellHighlightBorder");

        focusBorder = new BorderUIResource.CompoundBorderUIResource(cell, focus);
 
        Insets i = focus.getBorderInsets(this);

        noFocusBorder1 = new BorderUIResource.CompoundBorderUIResource
             (cell, BorderFactory.createEmptyBorder(i.top, i.left, i.bottom, i.right));
    }

    public Component getListCellRendererComponent(JList list, Object value, 
        int index, boolean selected, boolean focused) 
    {
        if (list != null)
        {
            if (selected)
            {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
            else
            {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            setFont(list.getFont());

            setEnabled(list.isEnabled());

            
        }
        else
        {
            setBackground(UIManager.getColor("TableHeader.background"));
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setFont(UIManager.getFont("TableHeader.font"));
            setEnabled(true);
        }

        if (focused)
            setBorder(focusBorder);
        else
            setBorder(noFocusBorder1);

        setText((String)value);
        return this;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                       boolean selected, boolean focused, int row, int column)
    {
        if (table != null)
        {
            if (selected)
            {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }
            else
            {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            setFont(table.getFont());

            setEnabled(table.isEnabled());
        }
        else
        {
            setBackground(UIManager.getColor("TableHeader.background"));
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setFont(UIManager.getFont("TableHeader.font"));
            setEnabled(true);
        }
        
        if (focused)
            setBorder(focusBorder);
        else
            setBorder(noFocusBorder1);

        setText((String)value);
        return this;
    }
}

