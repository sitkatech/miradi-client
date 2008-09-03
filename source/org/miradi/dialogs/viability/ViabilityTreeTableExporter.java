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
package org.miradi.dialogs.viability;

import javax.swing.Icon;

import org.miradi.questions.ChoiceItem;
import org.miradi.utils.TreeTableExporter;

public class ViabilityTreeTableExporter extends TreeTableExporter
{
	public ViabilityTreeTableExporter(TargetViabilityTreeTable viabilityTreeTable)
	{
		super(viabilityTreeTable);
	}
	
	@Override
	public Icon getIconAt(int row, int column)
	{
		Object valueAt = getViabilityTreeTable().getValueAt(row, column);
		if (valueAt == null)
			return null;
		
		if (getViabilityTreeTable().isChoiceItemCell(row, column))
		{
			ChoiceItem choiceItem = (ChoiceItem) valueAt;
			return choiceItem.getIcon();	
		}
		
		return super.getIconAt(row, column);
	}
	
	private TargetViabilityTreeTable getViabilityTreeTable()
	{
		return (TargetViabilityTreeTable) getTreeTable();
	}
}
