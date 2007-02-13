/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiComboBox;

public class ObjectClassificationChoiceField extends ObjectChoiceField
{
	public ObjectClassificationChoiceField(Project projectToUse, int objectType, BaseId objectId, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectType, objectId, questionToUse);
		combo.addActionListener(new ClassificationChangeHandler());
		combo.addFocusListener(new ClassificationFocusHandler());
	}

	class ClassificationChangeHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			UiComboBox comboBox = (UiComboBox)event.getSource();
			ChoiceItem taxonomyItem = getTaxonomyItem(comboBox);
			actionSaveTaxonomySelection(comboBox, taxonomyItem);
		}
	}
	
	private void actionSaveTaxonomySelection(UiComboBox thisComboBox, ChoiceItem taxonomyItem)
	{
		if(taxonomyItem != null)
		{
			setNeedsSave();
			saveIfNeeded();
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
