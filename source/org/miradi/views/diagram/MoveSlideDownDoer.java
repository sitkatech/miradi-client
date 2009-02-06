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
package org.miradi.views.diagram;

import javax.swing.JTable;

public class MoveSlideDownDoer extends MoveSlideDoer
{
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
		if (getPicker()==null)
			return false;
		
		int rowCount = ((JTable)getPicker()).getRowCount();
		int selectedRow = ((JTable)getPicker()).getSelectedRow();
		
		if ((selectedRow < 0) || (selectedRow == rowCount-1))
			return false;
		
		return true;
	}

	public int getDirection()
	{
		return 1;
	}
}