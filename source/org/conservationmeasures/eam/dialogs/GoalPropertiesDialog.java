package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Goal;

public class GoalPropertiesDialog extends ObjectPropertiesDialog
{

	public GoalPropertiesDialog(MainWindow parentToUse, EAMObject goalToEdit) throws Exception
	{
		super(parentToUse, goalToEdit);
		setTitle(EAM.text("Title|Goal Properties"));
		initializeFields(tags);
	}
	
	DialogField createDialogField(String tag) throws Exception
	{
		return super.createDialogField(tag);
	}
	
	static final String[] tags = new String[] {
		Goal.TAG_SHORT_LABEL, 
		Goal.TAG_LABEL, 
		Goal.TAG_FULL_TEXT
	};


}
