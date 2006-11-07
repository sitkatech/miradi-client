/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;


class ThreatRowHeaderRenderer
	extends JTextArea 
    implements TableCellRenderer
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

    public Component getTableCellRendererComponent(JTable table, Object value,
                       boolean selected, boolean focused, int row, int column)
    {
        setupCellRendererComponent(value);
        return this;
    }
    
    
	private void setupCellRendererComponent(Object value)
	{
        setOpaque(false);
        setText(value.toString());
		setFont(new Font(null,Font.BOLD,12));
	}

}

