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
import java.util.HashMap;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.dashboard.AbstractLongDescriptionProvider;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnGridLayout;
import org.miradi.main.EAM;
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
	
	private void addRows(ChoiceItem choiceItem, int level) throws Exception
	{
		Vector<ChoiceItem> children = choiceItem.getChildren();
		addRow(choiceItem, level);
		int childLevel = level + 1;
		for (ChoiceItem thisChoiceItem : children)
		{
			addRows(thisChoiceItem, childLevel);
		}
	}
	
	protected void addRow(ChoiceItem choiceItem, int level) throws Exception
	{
		addRow(choiceItem.getLabel(), EMPTY_LEFT_COLUMN_TEXT, choiceItem.getLongDescriptionProvider(), level);
	}
	
	protected void addRow(String leftColumnTranslatedText, String rightColumnTranslatedText, AbstractLongDescriptionProvider longDescriptionProvider, int level) throws Exception
	{
		JComponent leftComponent = new PanelTitleLabel(leftColumnTranslatedText);
		JComponent rightComponent = new PanelTitleLabel(rightColumnTranslatedText);
		
		Font font = getFontBasedForLevel(level);
		leftComponent.setFont(font);
		rightComponent.setFont(font);
		rowSelectionHandler.addSelectableRow(leftComponent, rightComponent, longDescriptionProvider);
		Box box = createHorizontalBoxWithIndents(level);
		box.add(leftComponent);
		add(box);
		add(rightComponent);
	}
	
	protected void addRow(HashMap<String, String> tokenReplacementMap, String text, AbstractLongDescriptionProvider longDescriptionProvider, int level) throws Exception
	{
		String rightColumnTranslatedText = EAM.substitute(text, tokenReplacementMap);		
		addRow(EMPTY_LEFT_COLUMN_TEXT, rightColumnTranslatedText, longDescriptionProvider, level);
	}
	
	protected void addRow(AbstractLongDescriptionProvider longDescriptionProvider,	int indentCount, String tag, String text) throws Exception
	{
		String rightColumnTranslatedText = EAM.substitute(text, getDashboardData(tag));
		addRow(EMPTY_LEFT_COLUMN_TEXT, rightColumnTranslatedText, longDescriptionProvider, indentCount);
	}
	
	protected void addRowWithTemplateAndPseudoField(String rightColumnTemplate, AbstractLongDescriptionProvider longDescriptionProvider, int level, String pseudoTag) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%X", getDashboardData(pseudoTag));
		addRow(tokenReplacementMap, rightColumnTemplate, longDescriptionProvider, level);
	}
	
	protected void addRowWithTemplateAndTwoPseudoFields(String rightColumnTemplate, AbstractLongDescriptionProvider longDescriptionProvider, int level, String pseudoTag1, String pseudoTag2) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%X", getDashboardData(pseudoTag1));
		tokenReplacementMap.put("%Y", getDashboardData(pseudoTag2));
		addRow(tokenReplacementMap, rightColumnTemplate, longDescriptionProvider, level);
	}

	private Box createHorizontalBoxWithIndents(int level)
	{
		Box box = Box.createHorizontalBox();
		for (int index = 0; index < level; ++index)
		{
			box.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		}
		
		return box;
	}
	
	private Font getFontBasedForLevel(int level)
	{
		if (level == 0)
			return createFirstLevelFont();
		
		if (level == 1)
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
	private static final String EMPTY_LEFT_COLUMN_TEXT = "";
}
