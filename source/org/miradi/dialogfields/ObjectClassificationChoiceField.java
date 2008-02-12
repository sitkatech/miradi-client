/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
				EAM.errorDialog("(" + EAM.text(taxonomyItem.getLabel() + ")\n Please choose a specific classification not a category"));
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
