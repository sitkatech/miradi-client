/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jhlabs.awt.BasicGridLayout;

public class DialogGridPanel extends JPanel
{
	public DialogGridPanel()
	{
		super(new BasicGridLayout(DEFAULT_ROWS, TWO_COLUMNS));
		setBorder(new EmptyBorder(5, 5, 5, 5));
	}
	
	static final int DEFAULT_ROWS = 1;
	static final int TWO_COLUMNS = 2;
}
