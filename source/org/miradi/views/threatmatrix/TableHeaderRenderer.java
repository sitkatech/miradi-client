/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.threatmatrix;

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


class TableHeaderRenderer extends JTextArea implements TableCellRenderer
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
		//TODO: for now this code allows the MAC to be tuned independently 
		// until a general solution can be found for all  
		if (!UIManager.getBorder("TableHeader.cellBorder").getClass().getName().startsWith("javax"))
		{
			cellBorder =  BorderFactory.createRaisedBevelBorder();
		}
        Border focusCellHighlightBorder = UIManager.getBorder("Table.focusCellHighlightBorder");
        createCellNotSelectionBorder(cellBorder, focusCellHighlightBorder);
	}


	private void createCellNotSelectionBorder(Border cellBorder, Border focusCellHighlightBorder)
	{
		Insets insets = focusCellHighlightBorder.getBorderInsets(this);
		noFocusBorder = new BorderUIResource.CompoundBorderUIResource
             (cellBorder, BorderFactory.createEmptyBorder(insets.top, SMALL_MARGIN, insets.bottom, insets.right));
	}


    public Component getTableCellRendererComponent(JTable table, Object value,
                       boolean selected, boolean focused, int row, int column)
    {
        setupCellRendererComponent(table, value);
        return this;
    }
    
    
	private void setupCellRendererComponent(JComponent component, Object value)
	{
        setForeground(component.getForeground());
        setBorder(noFocusBorder);

        setOpaque(true);
        setText(value.toString());
		setFont(new Font(null,Font.BOLD,12));
		int height = calculatePerferredHeight();
        setPreferredSize(new Dimension(150,height));
        
        //TODO: background should be set to current background not default
        if (overRideColor != null)
        	setBackground(overRideColor);
        else
        	setBackground(UIDEFAULT_TABLEHEADER_COLOR);
	}


	private int calculatePerferredHeight()
	{
		return Math.max(getPreferredSize().height, ABOUT_THREE_TEXT_LINES);
	}
	
	static public void SetOverRideColor(Color color)
	{
		overRideColor = color;
	}

	private static final int SMALL_MARGIN = 5;
    private static final int ABOUT_THREE_TEXT_LINES = 55;
	private Border noFocusBorder;
	static private Color overRideColor;
	static private Color UIDEFAULT_TABLEHEADER_COLOR = UIManager.getDefaults().getColor("TableHeader.background");
}

