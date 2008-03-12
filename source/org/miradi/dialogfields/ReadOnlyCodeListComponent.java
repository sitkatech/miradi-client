/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import javax.swing.JLabel;

import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.utils.CodeList;

import com.jhlabs.awt.BasicGridLayout;

public class ReadOnlyCodeListComponent extends MiradiPanel
{
	public ReadOnlyCodeListComponent(ChoiceItem[] choiceItemsToUse, int columnCount)
	{
		setLayout(new BasicGridLayout(0, columnCount));
		choiceItems = choiceItemsToUse;
		for (int i = 0; i < choiceItems.length; ++i)
		{
			add(new JLabel(choiceItems[i].getLabel()));
		}
		
		setBackground(EAM.READONLY_BACKGROUND_COLOR);
		setForeground(EAM.READONLY_FOREGROUND_COLOR);
	}

	public String getText()
	{
		CodeList codeList = new CodeList();
		for (int choiceIndex = 0; choiceIndex < choiceItems.length; ++choiceIndex )
		{
			codeList.add(choiceItems[choiceIndex].getCode());
		}
		
		return codeList.toString();
	}
	
	public void setText(String codesToUse)
	{
		removeAll();
		try
		{
			CodeList codeList = new CodeList(codesToUse);
			for (int choiceIndex = 0; choiceIndex < choiceItems.length; ++choiceIndex)
			{
				if (codeList.contains(choiceItems[choiceIndex].getCode()))
				{
					add(new JLabel(choiceItems[choiceIndex].getLabel()));
				}
			}
		}
		catch(Exception e)
		{
			EAM.errorDialog(EAM.text("Internal Error"));
			EAM.logException(e);
		}
		
		if (getTopLevelAncestor() != null)
			getTopLevelAncestor().validate();
	}
	
	protected ChoiceItem choiceItems[];
}
