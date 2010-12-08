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
import org.miradi.dialogs.fieldComponents.PanelLabelWithSelectableText;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.OpenStandardsNoStartedIcon;
import org.miradi.layout.MiradiGridLayoutPlus;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.DynamicChoiceWithRootChoiceItem;

import com.jhlabs.awt.GridLayoutPlus;

abstract public class AbstractOpenStandardsQuestionPanel extends AbstractObjectDataInputPanel
{
	public AbstractOpenStandardsQuestionPanel(Project projectToUse, DynamicChoiceWithRootChoiceItem questionToUse) throws Exception
	{
		super(projectToUse, ORef.createInvalidWithType(Dashboard.getObjectType()));
		
		setLayout(createLayoutManager());
		question = questionToUse;
		rowSelectionHandler = new SingleRowSelectionHandler();
		
		final int FIRST_LEVEL_INDENT_COUNT = 0;
		addRows(question.getHeaderChoiceItem(), FIRST_LEVEL_INDENT_COUNT);
	}

	private GridLayoutPlus createLayoutManager()
	{
		final int TEXT_COLUMN = 0;
		final int RIGHT_COLUMN = 1;
		
		MiradiGridLayoutPlus gridLayout = new MiradiGridLayoutPlus(0, 2);
		gridLayout.doNotGrowColumn(TEXT_COLUMN);
		gridLayout.growToFillColumn(RIGHT_COLUMN);
		
		return gridLayout;
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
	
	private void addRow(String leftColumnTranslatedText, String rightColumnTranslatedText, AbstractLongDescriptionProvider longDescriptionProvider, int level) throws Exception
	{
		JComponent iconComponent = new PanelTitleLabel(new OpenStandardsNoStartedIcon());
		JComponent leftComponent = new PanelLabelWithSelectableText(leftColumnTranslatedText);
		JComponent rightComponent = new PanelLabelWithSelectableText(rightColumnTranslatedText);
		
		Font font = getFontBasedOnLevel(level);
		leftComponent.setFont(font);
		rightComponent.setFont(font);
		Box box = createHorizontalBoxWithIndents(level);
		box.add(iconComponent);
		box.add(leftComponent);
		rowSelectionHandler.addSelectableRow(leftComponent, rightComponent, longDescriptionProvider);

		add(box);
		add(rightComponent);
	}
	
	protected void addRow(ChoiceItem choiceItem, int level) throws Exception
	{
		addRow(choiceItem.getLabel(), EMPTY_LEFT_COLUMN_TEXT, choiceItem.getLongDescriptionProvider(), level);
	}
	
	protected void addRowWithNoPseudoTag(String rightColumnText, AbstractLongDescriptionProvider longDescriptionProvider, int level) throws Exception
	{
		addRow(EMPTY_LEFT_COLUMN_TEXT, rightColumnText, new HashMap<String, String>(), longDescriptionProvider, level);
	}
	
	protected void addRowWithLeftColumnText(String leftColumnText, AbstractLongDescriptionProvider longDescriptionProvider, int level, String pseudoTag) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%X", getDashboardData(pseudoTag));

		addRow(leftColumnText, EAM.text("%X"), tokenReplacementMap, longDescriptionProvider, level);
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
	
	private void addRow(HashMap<String, String> tokenReplacementMap, String text, AbstractLongDescriptionProvider longDescriptionProvider, int level) throws Exception
	{
		addRow(EMPTY_LEFT_COLUMN_TEXT, text, tokenReplacementMap, longDescriptionProvider, level);
	}
	
	private void addRow(String leftColumnText, String rightColumnText, HashMap<String, String> tokenReplacementMap, AbstractLongDescriptionProvider longDescriptionProvider, int level) throws Exception
	{
		String rightColumnTranslatedText = EAM.substitute(rightColumnText, tokenReplacementMap);
		addRow(leftColumnText, rightColumnTranslatedText, longDescriptionProvider, level);
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
	
	private Font getFontBasedOnLevel(int level)
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
	
	private Font getRawFont()
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
