/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.miradi.icons.RatingIcon;
import org.miradi.ids.BaseId;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class ObjectRaitingChoiceField extends ObjectChoiceField
{
	public ObjectRaitingChoiceField(Project projectToUse, int objectType, BaseId objectId, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectType, objectId, tagToUse, questionToUse);
		combo.setRenderer(new RatingChoiceRenderer());
	}

	class RatingChoiceRenderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
		{
			Component cell = super.getListCellRendererComponent(list, value, index, isSelected,	cellHasFocus);
			ChoiceItem thisOption = (ChoiceItem)value;
			if (value!=null)
				setIcon(RatingIcon.createFromChoice(thisOption));
			return cell;
		}
	}

}
