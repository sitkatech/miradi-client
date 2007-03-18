/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import javax.swing.table.DefaultTableModel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;

public class ViabilityRatingsTableField extends ObjectStringMapTableField
{

	public ViabilityRatingsTableField(Project projectToUse, int objectType, BaseId objectId, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectType, objectId, questionToUse);
		model = (DefaultTableModel)table.getModel();
		model.insertRow(1, new String[]{});
		question = questionToUse;
	}

	private void setIconRow()
	{
		//TODO: set iconic row display based on summary fields.
		Object[] data = new String[] {"aa","b","c","aa"};
		model.setValueAt(data[0], 1, 0);
		model.setValueAt(data[1], 1, 1);
		model.setValueAt(data[2], 1, 2);
		model.setValueAt(data[3], 1, 3);
	}
	

	public void setIconRowObject(ORef oref)
	{
		if (oref==null)
			clearIconRow();
		else
			setIconRow();
	}
	
	private void clearIconRow()
	{
		model.setValueAt("", 1, 0);
		model.setValueAt("", 1, 1);
		model.setValueAt("", 1, 2);
		model.setValueAt("", 1, 3);
	}
	
	ChoiceQuestion question;
	DefaultTableModel model;

}
