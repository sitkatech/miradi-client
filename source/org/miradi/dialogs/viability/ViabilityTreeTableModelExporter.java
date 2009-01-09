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

import org.miradi.dialogs.planning.upperPanel.TreeTableModelExporter;
import org.miradi.icons.ColoredIcon;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;

public class ViabilityTreeTableModelExporter extends TreeTableModelExporter
{
	public ViabilityTreeTableModelExporter(Project projectToUse, ViabilityTreeModel modelToUse) throws Exception
	{
		super(projectToUse, modelToUse);
	}

	@Override
	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		Object valueAt = getModel().getValueAt(row, column);
		if (valueAt == null)
			return new EmptyChoiceItem();
		
		if (((ViabilityTreeModel)getModel()).isChoiceItemColumn(getModel().getColumnTag(column)))
		{
			ChoiceItem choiceItem = (ChoiceItem) valueAt;
			ColoredIcon rowRatingIcon = new ColoredIcon(choiceItem.getColor());
			choiceItem.setIcon(rowRatingIcon);
						
			return choiceItem;	
		}
		
		return new EmptyChoiceItem();
	}
}
