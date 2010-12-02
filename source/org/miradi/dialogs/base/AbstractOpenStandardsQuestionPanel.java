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
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.dashboard.AbstractLongDescriptionProvider;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnGridLayout;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.DynamicChoiceWithRootChoiceItem;
import org.miradi.utils.FillerLabel;

abstract public class AbstractOpenStandardsQuestionPanel extends AbstractObjectDataInputPanel
{
	public AbstractOpenStandardsQuestionPanel(Project projectToUse, DynamicChoiceWithRootChoiceItem questionToUse) throws Exception
	{
		super(projectToUse, ORef.createInvalidWithType(Dashboard.getObjectType()));
		
		setLayout(new TwoColumnGridLayout());
		question = questionToUse;
		rowSelectionHandler = new SingleRowSelectionHandler();
		
		final int FIRST_LEVEL_INDENT_COUNT = 0;
		addRows(question.getHeaderChoiceItem(), FIRST_LEVEL_INDENT_COUNT);
	}
	
	public void addRowSelectionListener(ListSelectionListener listener)
	{
		rowSelectionHandler.addSelectionListener(listener);
	}
	
	public void removeRowSelectionListener(ListSelectionListener listener)
	{
		rowSelectionHandler.removeSelectionListener(listener);
	}
	
	//NOTE this recursive method is still in progress.  Some of the commented code will be used later.
	private void addRows(ChoiceItem choiceItem, int indentCount) throws Exception
	{
		Vector<ChoiceItem> children = choiceItem.getChildren();
		addRow(choiceItem, indentCount);
		++indentCount; 
		for (ChoiceItem thisChoiceItem : children)
		{
			addRows(thisChoiceItem, indentCount);
		}
	}
	
	protected void addRow(ChoiceItem choiceItem, int indentCount) throws Exception
	{
		Font font = getFontBasedOnIndentation(indentCount);
		JComponent leftComponent = new PanelTitleLabel(choiceItem.getLabel());
		leftComponent.setFont(font);
		
		PanelTitleLabel rightComponent = new FillerLabel();
		rightComponent.setFont(font);
		
		addRow(leftComponent, rightComponent, indentCount, choiceItem.getLongDescriptionProvider());
	}
	
	protected void addFourthLevelRow(String leftColumnTranslatedText, String rightColumnTranslatedText, AbstractLongDescriptionProvider longDescriptionProvider) throws Exception
	{
		JComponent leftComponent = new PanelTitleLabel(leftColumnTranslatedText);
		JComponent rightComponent = new PanelTitleLabel(rightColumnTranslatedText);
		
		final int FORTH_LEVEL_INDENT_COUNT = 3;
		addRow(leftComponent, rightComponent, FORTH_LEVEL_INDENT_COUNT, longDescriptionProvider);
	}

//	private void addRow(ChoiceItem choiceItem, final int indentCount, Font font) throws Exception
//	{
//		JComponent leftComponent = new PanelTitleLabel(choiceItem.getLabel());
//		leftComponent.setFont(font);
//		
//		PanelTitleLabel rightComponent = new FillerLabel();
//		rightComponent.setFont(font);
//		
//		addRow(leftComponent, rightComponent, indentCount, choiceItem.getLongDescriptionProvider());
//	}

	private void addRow(JComponent leftComponent, JComponent rightComponent, final int indentCount, AbstractLongDescriptionProvider longDescriptionProvider) throws Exception
	{
		rowSelectionHandler.addSelectableRow(leftComponent, rightComponent, longDescriptionProvider);
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
	
	private Font getFontBasedOnIndentation(int indentCount)
	{
		if (indentCount == 0)
			return createFirstLevelFont();
		
		if (indentCount == 1)
			return createSecondLevelFont();
		
		return getRawFont();
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
	
	protected Font getRawFont()
	{
		return new PanelTitleLabel().getFont();
	}
	
	protected String getDashboardData(String tag)
	{
		return getDashboard().getData(tag);
	}
	
	private Dashboard getDashboard()
	{
		ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
		return Dashboard.find(getProject(), dashboardRef);
	}

	private DynamicChoiceWithRootChoiceItem question;
	private SingleRowSelectionHandler rowSelectionHandler;
	protected static final int INDENT_PER_LEVEL = 25;
}
