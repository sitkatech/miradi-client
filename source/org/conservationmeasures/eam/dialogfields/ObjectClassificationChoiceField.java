/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.martus.swing.UiComboBox;

public class ObjectClassificationChoiceField extends ObjectChoiceField
{
	public ObjectClassificationChoiceField(Project projectToUse, int objectType, BaseId objectId, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectType, objectId, questionToUse);
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
