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
package org.miradi.dialogs.viability;

import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.utils.TreeTableExporter;

public class ViabilityTreeTableExporter extends TreeTableExporter
{
	public ViabilityTreeTableExporter(TargetViabilityTreeTable viabilityTreeTable) throws Exception
	{
		super(viabilityTreeTable);
	}
	
	//FIXME urgent - test this case to make sure that its working properly,  this 
	//method is complex
	@Override
	public ChoiceItem getModelChoiceItemAt(int row, int modelColumn)
	{
		Object valueAt = getViabilityTreeTable().getModel().getValueAt(row, modelColumn);
		if (valueAt == null)
			return new EmptyChoiceItem();

		if (getViabilityTreeTable().isChoiceItemCell(row, modelColumn))
			return (ChoiceItem) valueAt;
		
		return super.getModelChoiceItemAt(row, modelColumn);
	}
	
	private TargetViabilityTreeTable getViabilityTreeTable()
	{
		return (TargetViabilityTreeTable) getTreeTable();
	}
}
