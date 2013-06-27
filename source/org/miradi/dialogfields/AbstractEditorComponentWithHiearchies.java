/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.dialogfields;

import java.util.Vector;

import javax.swing.Box;
import javax.swing.JComponent;

import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

abstract public class AbstractEditorComponentWithHiearchies extends QuestionBasedEditorComponent
{
	public AbstractEditorComponentWithHiearchies(ChoiceQuestion questionToUse)
	{
		super(questionToUse);
	}
	
	@Override
	protected void addComponentToRowPanel(MiradiPanel mainRowsPanel, JComponent leftColumnComponent, ChoiceItem rootChoiceItem)
	{
		mainRowsPanel.add(getSafeIconLabel(rootChoiceItem.getIcon()));
		rootChoiceItem.setSelectable(isRootChoiceItemSelectable());
		mainRowsPanel.add(createLeftColumnComponent(rootChoiceItem));
		try
		{
			final int horizontalIndent = 0;
			addRowComponents(mainRowsPanel, rootChoiceItem, horizontalIndent);
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
		
		mainRowsPanel.add(createDescriptionComponent(rootChoiceItem));
	}
	
	private void addRowComponents(MiradiPanel mainRowsPanel, ChoiceItem parentChoiceItem, int horizontalIndent) throws Exception
	{
		horizontalIndent += INDENT_PER_LEVEL;
		Vector<ChoiceItem> children = parentChoiceItem.getChildren();
		for(ChoiceItem childChoiceItem : children)
		{
			Box box = createHorizontalBoxWithIndents(horizontalIndent);
			box.add(createLeftColumnComponent(childChoiceItem));
			mainRowsPanel.add(box);
			addRowComponents(mainRowsPanel, childChoiceItem, horizontalIndent);
		}
	}
	
	@Override
	protected int calculateColumnCount()
	{
		return 1;
	}

	private Box createHorizontalBoxWithIndents(int indentCount)
	{
		Box box = Box.createHorizontalBox();
		for (int index = 0; index < indentCount; ++index)
		{
			box.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		}
		
		return box;
	}
	
	abstract protected boolean isRootChoiceItemSelectable();
	
	private static final int INDENT_PER_LEVEL = 5;
}
