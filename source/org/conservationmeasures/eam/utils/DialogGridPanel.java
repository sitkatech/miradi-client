/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
