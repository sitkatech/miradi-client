/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.layout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class OneRowPanel extends JPanel
{
	public OneRowPanel()
	{
		super(new OneRowGridLayout());
	}
	
	public void setMargins(int margin)
	{
		getGridLayout().setMargins(margin);
	}
	
	public void setVerticalMargin(int margin)
	{
		getGridLayout().setVerticalMargin(margin);
	}

	public void setGaps(int gap)
	{
		getGridLayout().setGaps(gap);
	}
	
	public void setAlignmentRight()
	{
		add(new JLabel(" "), 0);
		getGridLayout().setColWeight(0, 1);
	}

	public OneRowGridLayout getGridLayout()
	{
		return (OneRowGridLayout)getLayout();
	}
}
