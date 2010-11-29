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

package org.miradi.dialogs.base;

import java.awt.Font;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JComponent;

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnGridLayout;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.DynamicChoiceWithRootChoiceItem;

abstract public class AbstractOpenStandardsQuestionPanel extends AbstractObjectDataInputPanel
{
	public AbstractOpenStandardsQuestionPanel(Project projectToUse, DynamicChoiceWithRootChoiceItem questionToUse) throws Exception
	{
		super(projectToUse, ORef.createInvalidWithType(Dashboard.getObjectType()));
		
		setLayout(new TwoColumnGridLayout());
		question = questionToUse;
		
		addFirstLevelRow(question.getHeaderChoiceItem());
		addSecondLevelRows(question.getHeaderChoiceItem().getChildren());
	}

	private void addFirstLevelRow(ChoiceItem choiceItem) throws Exception
	{
		final int HEADER_INDENT_COUNT = 0;
		
		addRow(choiceItem, HEADER_INDENT_COUNT, createFirstLevelFont());
	}

	private void addSecondLevelRows(Vector<ChoiceItem> children) throws Exception
	{
		for(ChoiceItem firstLevelChild : children)
		{
			addThirdLevelRows(firstLevelChild);
		}
	}
	
	private void addThirdLevelRows(ChoiceItem choiceItem) throws Exception
	{
		final int SUBHEADER_INDENT_COUNT = 1;
		
		addRow(choiceItem, SUBHEADER_INDENT_COUNT, createSecondLevelFont());
	}

	private void addRow(ChoiceItem choiceItem, final int indentCount, Font font)
	{
		JComponent leftComponent = new PanelTitleLabel(choiceItem.getLabel());
		leftComponent.setFont(font);
		
		PanelTitleLabel rightComponent = new PanelTitleLabel(choiceItem.getAdditionalText());
		rightComponent.setFont(font);
		
		Box box = createHorizontalBoxWithIndents(indentCount);
		box.add(leftComponent);
		add(box);
		add(rightComponent);
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
	
	private Font createFirstLevelFont()
	{
		Font font = getRawFont();
		font = font.deriveFont(Font.BOLD);
		font = font.deriveFont((float)(font.getSize() * 1.5));
		
		return font;
	}
	
	private Font createSecondLevelFont()
	{
		Font font = getRawFont();
		font = font.deriveFont(Font.BOLD);
		
		return font;
	}
	
	private Font getRawFont()
	{
		return new PanelTitleLabel().getFont();
	}

	private DynamicChoiceWithRootChoiceItem question;
	protected static final int INDENT_PER_LEVEL = 25;
}
