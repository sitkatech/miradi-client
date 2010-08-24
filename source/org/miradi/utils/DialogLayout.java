/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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
package org.miradi.utils;

import java.awt.Component;
import java.awt.Container;
import java.util.Arrays;

import javax.swing.Spring;
import javax.swing.SpringLayout;

/*
 * This class helps set up a simple 2-column dialog, where
 * all the labels line up (left aligned right now), and all 
 * the values in the second column line up.
 * 
 * Construct the layout manager, tell the container to use 
 * this layout, add all the items to the container, and then 
 * invoke the layout's adjustSizes method.
 *  
 */
public class DialogLayout extends SpringLayout
{
	public void adjustSizes(Container container)
	{
		// Could be passed as parameters later
		int columns = 2;
		int rows = container.getComponentCount() / columns;
		Spring columnPadding = Spring.constant(5);
		Spring rowPadding = Spring.constant(5);
		
		// figure out tallest item in each row, and 
		// widest item in each column
		Spring[] heightOfRow = new Spring[rows];
		Spring[] widthOfColumn = new Spring[columns];
		Arrays.fill(heightOfRow, Spring.constant(0));
		Arrays.fill(widthOfColumn, Spring.constant(0));
		for(int row = 0; row < rows; ++row)
		{
			for(int col = 0; col < columns; ++col)
			{
				Component component = container.getComponent(row * columns + col);
				SpringLayout.Constraints constraints = getConstraints(component);
				heightOfRow[row] = Spring.max(heightOfRow[row], constraints.getHeight());
				widthOfColumn[col] = Spring.max(widthOfColumn[col], constraints.getWidth());
			}
		}

		// set the x, y, width, and height of each item
		Spring startOfRow = Spring.constant(0);
		Spring startOfColumn = Spring.constant(0);
		for(int row = 0; row < rows; ++row)
		{
			startOfColumn = Spring.constant(0);
			for(int col = 0; col < columns; ++col)
			{
				Component component = container.getComponent(row * columns + col);
				SpringLayout.Constraints constraints = getConstraints(component);
				constraints.setX(startOfColumn);
				constraints.setY(startOfRow);
				constraints.setWidth(widthOfColumn[col]);
				constraints.setHeight(heightOfRow[row]);
				startOfColumn = Spring.sum(startOfColumn, widthOfColumn[col]);
				startOfColumn = Spring.sum(startOfColumn, columnPadding);
			}
			startOfRow = Spring.sum(startOfRow, heightOfRow[row]);
			startOfRow = Spring.sum(startOfRow, rowPadding);
		}

		// make the parent large enough to hold it all
		SpringLayout.Constraints containerConstraints = getConstraints(container);
		containerConstraints.setConstraint(SpringLayout.SOUTH, startOfRow);
		containerConstraints.setConstraint(SpringLayout.EAST, startOfColumn);
	}
}
