/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.questions.ChoiceQuestion;

public class RefListComponent extends AbstractListComponent
{
	public RefListComponent(ChoiceQuestion questionToUse, int columnCount, ListSelectionListener listener)
	{
		super(questionToUse, columnCount, listener);
	}

	public String getText()
	{
		return null;
	}

	public void setText(String codesToUse)
	{
	}
}
