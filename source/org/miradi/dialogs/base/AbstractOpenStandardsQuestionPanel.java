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
		
		addRows(question.getHeaderChoiceItem());
	}
	
	public void addRowSelectionListener(ListSelectionListener listener)
	{
		rowSelectionHandler.addSelectionListener(listener);
	}
	
	public void removeRowSelectionListener(ListSelectionListener listener)
	{
		rowSelectionHandler.removeSelectionListener(listener);
	}
	
	private void addRows(ChoiceItem choiceItem) throws Exception
	{
		Vector<ChoiceItem> children = choiceItem.getChildren();
		addRow(choiceItem);
		for (ChoiceItem thisChoiceItem : children)
		{
			addRows(thisChoiceItem);
		}
	}
	
	protected void addRow(ChoiceItem choiceItem) throws Exception
	{
		JComponent leftComponent = new PanelTitleLabel(choiceItem.getLabel());
		leftComponent.setFont(getRawFont());
		
		PanelTitleLabel rightComponent = new FillerLabel();
		rightComponent.setFont(getRawFont());
		
		addRow(leftComponent, rightComponent, 0, choiceItem.getLongDescriptionProvider());
	}

	

//	private void addFirstLevelRow(ChoiceItem choiceItem) throws Exception
//	{
//		final int FIRST_LEVEL_INDENT_COUNT = 0;
//		
//		addRow(choiceItem, FIRST_LEVEL_INDENT_COUNT, createFirstLevelFont());
//	}
//
//	private void addSecondLevelRows(Vector<ChoiceItem> secondLevelChildren) throws Exception
//	{
//		final int SECOND_LEVEL_INDENT_COUNT = 1;
//		for(ChoiceItem secondLevelChild : secondLevelChildren)
//		{
//			addRow(secondLevelChild, SECOND_LEVEL_INDENT_COUNT, createSecondLevelFont());
//			addThirdLevelRows(secondLevelChild.getChildren());
//		}
//	}
//	
//	private void addThirdLevelRows(Vector<ChoiceItem> thirdLevelChildren) throws Exception
//	{
//		for (ChoiceItem thirdLevelChild : thirdLevelChildren)
//		{
//			addThirdLevelRow(thirdLevelChild.getLabel(), thirdLevelChild.getLongDescriptionProvider());
//			addFourthLevelRow(thirdLevelChild.getCode(), thirdLevelChild.getLongDescriptionProvider());
//		}
//	}

//	private void addThirdLevelRow(String label, AbstractLongDescriptionProvider longDescriptionProvider) throws Exception
//	{
//		JComponent leftComponent = new PanelTitleLabel(label);
//		leftComponent.setFont(getRawFont());
//		
//		JComponent rightComponent = new FillerLabel();
//		rightComponent.setFont(getRawFont());
//		
//		final int THIRD_LEVEL_INDENT_COUNT = 2;
//		addRow(leftComponent, rightComponent, THIRD_LEVEL_INDENT_COUNT, longDescriptionProvider);
//	}

	protected void addFourthLevelRow(String code, AbstractLongDescriptionProvider longDescriptionProvider) throws Exception
	{
		addFourthLevelRow(new FillerLabel(), new FillerLabel(), longDescriptionProvider);
	}
	
	public void addFourthLevelRow(String leftColumnTranslatedText, String rightColumnTranslatedText, AbstractLongDescriptionProvider longDescriptionProvider) throws Exception
	{
		JComponent leftComponent = new PanelTitleLabel(leftColumnTranslatedText);
		JComponent rightComponent = new PanelTitleLabel(rightColumnTranslatedText);
		
		addFourthLevelRow(leftComponent, rightComponent, longDescriptionProvider);
	}

	private void addFourthLevelRow(JComponent leftComponent, JComponent rightComponent, AbstractLongDescriptionProvider longDescriptionProvider) throws Exception
	{
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
	
//	private Font createFirstLevelFont()
//	{
//		Font font = getRawFont();
//		font = font.deriveFont(Font.BOLD);
//		font = font.deriveFont((float)(font.getSize() * 1.5));
//		
//		return font;
//	}
//	
//	private Font createSecondLevelFont()
//	{
//		Font font = getRawFont();
//		font = font.deriveFont(Font.BOLD);
//		
//		return font;
//	}
	
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
