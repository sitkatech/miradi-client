/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;


class ThreatRowHeaderRenderer
	extends JTextArea 
    implements ListCellRenderer , TableCellRenderer
{

    public ThreatRowHeaderRenderer()
    {
    	setWrapStyleWord(true);
        setLineWrap(true);
    }

    public void updateUI()
    {
        super.updateUI();
    }

    public Component getListCellRendererComponent(JList list, Object value, 
        int index, boolean selected, boolean focused) 
    {
        setupCellRendererComponent(list, value, selected, focused);
        return this;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                       boolean selected, boolean focused, int row, int column)
    {
        setupCellRendererComponent(table, value, selected, focused);
        return this;
    }
    
    
	private void setupCellRendererComponent(JComponent component, Object value, boolean selected, boolean focused)
	{
        setOpaque(false);
        setText(value.toString());
		setFont(new Font(null,Font.BOLD,12));
		setSize(60,60);
	}

}

