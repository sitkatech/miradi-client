/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.ratings.ResourceRoleQuestion;
import org.conservationmeasures.eam.utils.CodeList;

import com.jhlabs.awt.BasicGridLayout;

public class CodeListComponent extends JPanel implements ItemListener
{
	public CodeListComponent(ResourceRoleQuestion questionToUse, ListSelectionListener listener)
	{
		super(new BasicGridLayout(0,3));
		listSelectionListener = listener;
		ChoiceItem[] choices = questionToUse.getChoices();
		choiceItems = new ChoiceItem[choices.length];
		checkBoxes = new JCheckBox[choices.length];
		
		for (int i=0; i<choices.length; ++i)
		{
			JCheckBox checkBox = new JCheckBox(choices[i].getLabel());
			checkBox.addItemListener(this);
			choiceItems[i] = choices[i];
			checkBoxes[i] = checkBox;
			add(checkBox);
		}
	}
	
	public void itemStateChanged(ItemEvent e) 
	{
	    if (e.getStateChange() == ItemEvent.SELECTED || 
	    	e.getStateChange() == ItemEvent.DESELECTED)
	    {
	    	valueChanged();
	    }
	}
	
	public void valueChanged()
	{
		if (!skipNotice)
		{
			ListSelectionEvent event = new ListSelectionEvent("DUMMY EVENT",0,0, false);
			listSelectionListener.valueChanged(event);
		}
	}
	
	public String getText()
	{
		CodeList codes = new CodeList();
		for (int codeIndex = 0; codeIndex<checkBoxes.length; ++codeIndex )
		{
			JCheckBox checkBox = checkBoxes[codeIndex];
			if (checkBox.isSelected())
			{
				ChoiceItem choiceItem = choiceItems[codeIndex];
				codes.add(choiceItem.getCode());
			}
		}
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
					if (codes.contains(choiceItem.getCode()))
						checkBoxes[choiceIndex].setSelected(true);
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
	
	public void setEnable(boolean isValidObject)
	{
		//FIXME: For some reason this code does not disable the check boxes even when isValidObject=false
		super.setEnabled(isValidObject);
	}
	

	private JCheckBox checkBoxes[];
	private ChoiceItem choiceItems[];
	private ListSelectionListener listSelectionListener;
	private boolean skipNotice;
}
