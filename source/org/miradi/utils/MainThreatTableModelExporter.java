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

import java.util.Vector;

import javax.swing.Icon;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.views.threatmatrix.AbstractThreatTargetTableModel;

public class MainThreatTableModelExporter extends AbstractTableExporter
{
	public MainThreatTableModelExporter(AbstractThreatTargetTableModel mainThreatTableModelToUse)
	{
		mainThreatTableModel = mainThreatTableModelToUse;
	}
	
	@Override
	public ORefList getAllRefs(int objectType)
	{
		throw new RuntimeException("getAllRefs has not been implemented yet");
	}

	@Override
	public Vector<Integer> getAllTypes()
	{
		throw new RuntimeException("getAllRefs has not been implemented yet");
	}

	@Override
	public BaseObject getBaseObjectForRow(int row)
	{
		return mainThreatTableModel.getDirectThreat(row);
	}

	@Override
	public int getColumnCount()
	{
		return mainThreatTableModel.getColumnCount();
	}

	@Override
	public int getDepth(int row)
	{
		return 0;
	}

	@Override
	public String getHeaderFor(int column)
	{
		return mainThreatTableModel.getColumnName(column);
	}

	@Override
	public Icon getIconAt(int row, int column)
	{
		return null;
	}

	@Override
	public int getMaxDepthCount()
	{
		return 0;
	}

	@Override
	public int getRowCount()
	{
		return mainThreatTableModel.getRowCount();
	}

	@Override
	public int getRowType(int row)
	{
		return getBaseObjectForRow(row).getType();
	}

	@Override
	public String getTextAt(int row, int column)
	{
		return getSafeValue(mainThreatTableModel.getValueAt(row, column));
	}
	
	private AbstractThreatTargetTableModel mainThreatTableModel;
}
