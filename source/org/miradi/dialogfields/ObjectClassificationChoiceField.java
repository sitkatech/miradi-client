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
package org.miradi.dialogfields;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import org.martus.swing.UiComboBox;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class ObjectClassificationChoiceField extends ObjectChoiceField
{
	public ObjectClassificationChoiceField(Project projectToUse, int objectType, BaseId objectId, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectType, objectId, tagToUse, questionToUse);
		combo.addFocusListener(new ClassificationFocusHandler());
	}

	
	public void saveSelection()
	{
		ChoiceItem taxonomyItem = getTaxonomyItem(combo);
		if(taxonomyItem != null)
		{
			super.saveSelection();
		}
	}
	
	class ClassificationFocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent e)
		{
		}

		public void focusLost(FocusEvent e)
		{
			ChoiceItem taxonomyItem = (ChoiceItem) ((UiComboBox)e.getSource()).getSelectedItem();
			if (!taxonomyItem.isSelectable())
			{
				String text = EAM.substitute(EAM.text("Please choose a specific classification, instead of a category like:\n'%s'"), taxonomyItem.getLabel());
				EAM.errorDialog(text);
			}
		}
	}
	
	private ChoiceItem getTaxonomyItem(UiComboBox comboBox)
	{
		ChoiceItem taxonomyItem = (ChoiceItem) comboBox.getSelectedItem();
		
		if (!taxonomyItem.isSelectable()) 
			return null;
		
		return taxonomyItem;
	}

}
