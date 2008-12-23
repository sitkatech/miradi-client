/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import javax.swing.JTable;

public interface TableWithRowHeightManagement
{
	public void setMultiTableRowHeightController(MultiTableRowHeightController controller);
	public void setRowHeight(int newHeight);
	public void setRowHeight(int row, int newHeight);
	public int getRowHeight();
	public int getRowHeight(int row);
	public int getPreferredRowHeight(int row);
	public boolean allowUserToSetRowHeight();
	public void ensureSelectedRowVisible(); 
	public JTable asTable();
	public int getRowCount();
	public void setVariableRowHeight();
	public void updateAutomaticRowHeights();
	public boolean shouldSaveRowHeight();
}