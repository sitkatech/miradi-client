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

import java.util.Vector;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceItem;

public interface TableExporter
{

	public int convertToModelColumn(int tableColumn);

	public String getStyleTagAt(int row, int column);

	public String getSafeValue(Object object);

	public int getDepth(int row, int tableColumn);

	public String getTextAt(int row, int tableColumn);

	public ChoiceItem getChoiceItemAt(int row, int tableColumn);

	public String getColumnName(int tableColumn);
	
	public String getAboveColumnHeaderText(int tableColumn);
	
	public String getColumnGroupName(int modelColumn);

	public int getMaxDepthCount();

	public int getColumnCount();

	public int getRowCount();

	public int getRowType(int row);

	public BaseObject getBaseObjectForRow(int row);

	//TODO these two methods were created to export details of tree or table.
	// we currently dont export details of tree or tables, and these methods might
	// need to be removed
	public Vector<Integer> getAllTypes();

	public ORefList getAllRefs(int objectType);

}