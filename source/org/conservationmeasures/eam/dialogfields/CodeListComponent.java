/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import javax.swing.JCheckBox;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.utils.CodeList;

public class CodeListComponent extends AbstractListComponent
{
	public CodeListComponent(ChoiceQuestion questionToUse, int columnCount, ListSelectionListener listener)
	{
		super(questionToUse, columnCount, listener);
		
		codesToDisable = new CodeList();
	}
	
	public String getText()
	{
		CodeList codes = new CodeList();
		for (int checkBoxIndex = 0; checkBoxIndex<checkBoxes.length; ++checkBoxIndex )
		{
			JCheckBox checkBox = checkBoxes[checkBoxIndex];
			if (checkBox.isSelected())
			{
				ChoiceItem choiceItem = choiceItems[checkBoxIndex];
				codes.add(choiceItem.getCode());
			}
		}
		setSameToolTipForAllCheckBoxes();
		
		return codes.toString();
	}
	
	public void setText(String codesToUse)
	{
		skipNotice=true;
		try
		{
			CodeList codes = new CodeList(codesToUse);

			for (int choiceIndex = 0; choiceIndex<choiceItems.length; ++choiceIndex)
			{
				checkBoxes[choiceIndex].setSelected(false);
				ChoiceItem choiceItem = choiceItems[choiceIndex];
				boolean isChecked  = codes.contains(choiceItem.getCode());
				checkBoxes[choiceIndex].setSelected(isChecked);
			}
			
			setSameToolTipForAllCheckBoxes();
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
	
	private void setSameToolTipForAllCheckBoxes()
	{
		String partialToolTip = ""; 
		for (int choiceIndex = 0; (choiceIndex < choiceItems.length && choiceIndex < 6); ++choiceIndex)
		{
			ChoiceItem choiceItem = choiceItems[choiceIndex];
			if (checkBoxes[choiceIndex].isSelected())
				partialToolTip += choiceItem.getLabel() + "<BR>";
		}
		
		if (partialToolTip.length()== 0 )
			return;
		
		String toolTip = ("<HTML>" + partialToolTip + "...more</HTML>");
		for (int choiceIndex = 0; choiceIndex < choiceItems.length; ++choiceIndex)
		{
			checkBoxes[choiceIndex].setToolTipText(toolTip);
		}
	}

	public void setEnabled(boolean isValidObject)
	{
		super.setEnabled(isValidObject);
		disableCheckBoxes();
	}

	private void disableCheckBoxes()
	{
		for (int choiceIndex = 0; choiceIndex<checkBoxes.length; ++choiceIndex)
		{
			ChoiceItem choiceItem = choiceItems[choiceIndex];
			if (codesToDisable.contains(choiceItem.getCode()))
				checkBoxes[choiceIndex].setEnabled(false);
		}
	}

	public void setDisabledCodes(CodeList codesToDiableToUse)
	{
		codesToDisable = codesToDiableToUse;
	}
	
	private CodeList codesToDisable;
}
