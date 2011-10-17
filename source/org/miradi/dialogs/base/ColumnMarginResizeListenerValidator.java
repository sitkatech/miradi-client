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
package org.miradi.dialogs.base;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

public class ColumnMarginResizeListenerValidator implements TableColumnModelListener
{
	public ColumnMarginResizeListenerValidator(JComponent compnentToUse) 
	{
		componentToValidate = compnentToUse;
	}

	public void columnAdded(TableColumnModelEvent e)
	{
		// NOTE: We only care about margin changed (column resize) events
	}

	public void columnMarginChanged(ChangeEvent e)
	{
		resizeTablesToExactlyFitAllColumns();
	}

	public void columnMoved(TableColumnModelEvent e)
	{
		// NOTE: We only care about margin changed (column resize) events
	}

	public void columnRemoved(TableColumnModelEvent e)
	{
		// NOTE: We only care about margin changed (column resize) events
	}

	public void columnSelectionChanged(ListSelectionEvent e)
	{
		// NOTE: We only care about margin changed (column resize) events
	}
	
	private void resizeTablesToExactlyFitAllColumns() 
	{
		Container topLevel = componentToValidate.getTopLevelAncestor();
		if(topLevel == null)
			topLevel = componentToValidate;
		topLevel.validate();
	}
	
	private JComponent componentToValidate;
}
