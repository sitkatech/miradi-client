/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import java.awt.Font;
import java.util.Vector;

import javax.swing.Box;

import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.dashboard.LeftSidePanelWithSelectableRows;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.MainWindow;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceItemWithChildren;
import org.miradi.questions.ChoiceQuestion;

//FIXME urgent this class is under construction
public class QuestionEditorWithHierarchichalRows extends LeftSidePanelWithSelectableRows
{
	public QuestionEditorWithHierarchichalRows(MainWindow mainWindowToUse, ChoiceQuestion questionToUse)
	{
		super(mainWindowToUse, questionToUse);
	}
	
	@Override
	protected MiradiPanel createRowPanel(ChoiceItem choiceItem)
	{
		MiradiPanel miradiPanel = new MiradiPanel(new OneColumnGridLayout());
		ChoiceItemWithChildren castedChoiceItem = (ChoiceItemWithChildren) choiceItem;
		miradiPanel.add(createHeaderTitleLabel(castedChoiceItem.getLeftLabel()));
		miradiPanel.add(createHeaderTitleLabel(castedChoiceItem.getRightLabel()));
		
		Vector<ChoiceItem> children = castedChoiceItem.getChildren();
		for(ChoiceItem childChoiceItem : children)
		{
			Box box = Box.createHorizontalBox();
			box.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
			box.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
			ChoiceItemWithChildren thisChoiceItem = (ChoiceItemWithChildren) childChoiceItem;
			box.add(new PanelTitleLabel(thisChoiceItem.getLeftLabel()));
			box.add(new PanelTitleLabel(thisChoiceItem.getRightLabel()));
			
			miradiPanel.add(box);
		}
		
		return miradiPanel;
	}
	
	private PanelTitleLabel createHeaderTitleLabel(String labelToUse)
	{
		PanelTitleLabel leftLabel = new PanelTitleLabel(labelToUse);
		Font font = leftLabel.getFont();
		font = font.deriveFont(Font.BOLD);
		font = font.deriveFont((float)(font.getSize() * 1.5));
		leftLabel.setFont(font);
		
		return leftLabel;
	}
	
	@Override
	protected String getMainDescriptionFileName()
	{
		return "dashboard/1.html";
	}
	
	private static final int INDENT_PER_LEVEL = 25;
}
