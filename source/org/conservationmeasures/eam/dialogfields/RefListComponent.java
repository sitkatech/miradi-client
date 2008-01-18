/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import javax.swing.JCheckBox;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ChoiceQuestion;

public class RefListComponent extends AbstractListComponent
{
	public RefListComponent(ChoiceQuestion questionToUse, int columnCount, ListSelectionListener listener)
	{
		super(questionToUse, columnCount, listener);
	}

	public String getText()
	{
		ORefList refList = new ORefList();
		for (int checkBoxIndex = 0; checkBoxIndex<checkBoxes.length; ++checkBoxIndex )
		{
			JCheckBox checkBox = checkBoxes[checkBoxIndex];
			if (checkBox.isSelected())
			{
				ChoiceItem choiceItem = choiceItems[checkBoxIndex];
				refList.add(ORef.createFromString(choiceItem.getCode()));
			}
		}
		
		return refList.toString();
	}
	
	public void setText(String codesToUse)
	{
		skipNotice=true;
		try
		{
			ORefList refs = new ORefList(codesToUse);

			for (int choiceIndex = 0; choiceIndex<choiceItems.length; ++choiceIndex)
			{
				checkBoxes[choiceIndex].setSelected(false);
				ChoiceItem choiceItem = choiceItems[choiceIndex];
				String code = choiceItem.getCode();
				ORef ref = ORef.createFromString(code);
				boolean isChecked  = refs.contains(ref);
				checkBoxes[choiceIndex].setSelected(isChecked);
			}
		}
		catch(Exception e)
		{
			EAM.errorDialog(EAM.text("Internal Error"));
			EAM.logException(e);
		}
		finally
		{
			skipNotice=false;
		}
	}
}
