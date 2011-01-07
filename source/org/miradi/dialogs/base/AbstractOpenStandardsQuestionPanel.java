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

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.event.ListSelectionListener;

import org.miradi.actions.AbstractJumpMenuAction;
import org.miradi.dialogfields.DashboardCommentsField;
import org.miradi.dialogfields.DashboardFlagIconField;
import org.miradi.dialogfields.DashboardStatusIconField;
import org.miradi.dialogfields.DashboardStatusLabelField;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.dashboard.AbstractLongDescriptionProvider;
import org.miradi.dialogs.dashboard.DashboardRowDefinition;
import org.miradi.dialogs.dashboard.DashboardRowDefinitionManager;
import org.miradi.dialogs.fieldComponents.PanelLabelWithSelectableText;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.MiradiGridLayoutPlus;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.DynamicChoiceWithRootChoiceItem;
import org.miradi.questions.OpenStandardsDynamicProgressStatusQuestion;
import org.miradi.utils.FillerLabel;

import com.jhlabs.awt.GridLayoutPlus;

abstract public class AbstractOpenStandardsQuestionPanel extends AbstractObjectDataInputPanel
{
	public AbstractOpenStandardsQuestionPanel(Project projectToUse, DynamicChoiceWithRootChoiceItem questionToUse) throws Exception
	{
		super(projectToUse, getDashboard(projectToUse).getRef());
		
		setBackground(DASHBOARD_BACKGROUND_COLOR);
		setLayout(createLayoutManager());
		question = questionToUse;
		rowSelectionHandler = new DashboardSingleRowSelectionHandler();
		
		final int FIRST_LEVEL_INDENT_COUNT = 0;
		addRows(question.getHeaderChoiceItem(), FIRST_LEVEL_INDENT_COUNT);
		
		updateFieldsFromProject();
	}

	private GridLayoutPlus createLayoutManager()
	{
		final int TEXT_COLUMN = 0;
		final int RIGHT_COLUMN = 1;
		
		MiradiGridLayoutPlus gridLayout = new MiradiGridLayoutPlus(0, 2);
		gridLayout.doNotGrowColumn(TEXT_COLUMN);
		gridLayout.growToFillColumn(RIGHT_COLUMN);
		gridLayout.setGaps(0, 0);
		
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
	
	private void addRow(ChoiceItem choiceItem, int level) throws Exception
	{
		setRowWizardStep(choiceItem);
		addRowsWithLeftColumn(choiceItem, level);
		addRowsWithRightColumn(choiceItem, level);
	}

	private void addRowsWithLeftColumn(ChoiceItem choiceItem, int level) throws Exception
	{
		if (choiceItem.hasChildren())
			addRowWithoutIcon(choiceItem.getLabel(), EMPTY_COLUMN_TEXT, new HashMap<String, String>(), choiceItem.getLongDescriptionProvider(), level);
		else
			addRowWithStatusIcon(choiceItem, level);
	}

	private void addRowsWithRightColumn(ChoiceItem choiceItem, int level) throws Exception
	{
		Vector<DashboardRowDefinition> rowDefinitions = getDashboardRowDefinitionManager().getRowDefinitions(choiceItem.getCode());
		for (DashboardRowDefinition rowDefinition: rowDefinitions)
		{
			Vector<String> pseudoTags = rowDefinition.getPseudoTags();
			HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
			
			for (int index = 0; index < pseudoTags.size(); ++index)
			{
				tokenReplacementMap.put("%" + Integer.toString(index + 1), getDashboardData(pseudoTags.get(index)));
			}

			addRowWithoutIcon(EMPTY_COLUMN_TEXT, rowDefinition.getRightColumnTemplate(), tokenReplacementMap, choiceItem.getLongDescriptionProvider(), level);
		}
	}

	private void setRowWizardStep(ChoiceItem choiceItem)
	{
		AbstractLongDescriptionProvider longDescriptionProvider = choiceItem.getLongDescriptionProvider();
		AbstractJumpMenuAction action = getMainWindow().getActions().getJumpMenuAction(choiceItem.getCode());
		if (action != null)
		{
			String stepName = getMainWindow().getWizardManager().stripJumpPrefix(action.getClass());
			longDescriptionProvider.setWizardStepName(stepName);
		}
	}
	
	private void addRowWithStatusIcon(ChoiceItem choiceItem, int level) throws Exception
	{
		DashboardFlagIconField flagIconField = new DashboardFlagIconField(getProject(), getDashboard().getRef(), choiceItem.getCode());
		addUpdatedCustomField(flagIconField);
		
		ChoiceQuestion progressStatusQuestion = new OpenStandardsDynamicProgressStatusQuestion(getDashboard(), choiceItem.getCode());
		ObjectDataInputField statusIconField = new DashboardStatusIconField(getProject(), getDashboard().getRef(), choiceItem.getCode(), progressStatusQuestion);
		addUpdatedCustomField(statusIconField);
		
		ObjectDataInputField statusTextField = new DashboardStatusLabelField(getProject(), getDashboard().getRef(), choiceItem.getCode(), progressStatusQuestion);
		addUpdatedCustomField(statusTextField);
		
		
		PanelTitleLabel labelComponent = new PanelTitleLabel(choiceItem.getLabel());
		labelComponent.setOpaque(true);
		labelComponent.setBackground(getItemBackgroundColor());
		addRow(choiceItem.getLongDescriptionProvider(), level, flagIconField.getComponent(), statusIconField.getComponent(), labelComponent, statusTextField.getComponent());

		DashboardCommentsField commentsField = new DashboardCommentsField(getProject(), getDashboard().getRef(), choiceItem.getCode());
		if(commentsField.hasComments())
		{
			commentsField.getComponent().setFont(getCommentsFieldFont());
			addUpdatedCustomField(commentsField);
			addDefaultFontRow(choiceItem.getLongDescriptionProvider(), level, new FillerLabel(), new FillerLabel(), new FillerLabel(), commentsField.getComponent());
		}
	}

	private void addUpdatedCustomField(ObjectDataInputField field)
	{
		addFieldToList(field);
		field.updateFromObject();
	}
	
	private void addRowWithoutIcon(String leftColumnText, String rightColumnText, HashMap<String, String> tokenReplacementMap, AbstractLongDescriptionProvider longDescriptionProvider, int level) throws Exception
	{
		String rightColumnTranslatedText = EAM.substitute(rightColumnText, tokenReplacementMap);
		JComponent leftComponent = new PanelLabelWithSelectableText(leftColumnText);
		leftComponent.setOpaque(true);
		leftComponent.setBackground(getItemBackgroundColor());
		JComponent rightComponent = new PanelLabelWithSelectableText(rightColumnTranslatedText);
		rightComponent.setOpaque(true);
		rightComponent.setBackground(getItemBackgroundColor());
		
		addRow(longDescriptionProvider, level, new FillerLabel(), new FillerLabel(), leftComponent, rightComponent);
	}

	public static Color getItemBackgroundColor()
	{
		return AppPreferences.getWizardBackgroundColor();
	}

	private void addRow(AbstractLongDescriptionProvider longDescriptionProvider, int level,	JComponent flagIconComponent, JComponent iconComponent,	JComponent leftComponent, JComponent rightComponent)
	{
		Font font = getFontBasedOnLevel(level);
		leftComponent.setFont(font);
		rightComponent.setFont(font);
		addDefaultFontRow(longDescriptionProvider, level, flagIconComponent, iconComponent, leftComponent, rightComponent);
	}

	private void addDefaultFontRow(AbstractLongDescriptionProvider longDescriptionProvider, int level, JComponent flagIconComponent, JComponent iconComponent, JComponent leftComponent, JComponent rightComponent)
	{
		Box leftBox = createHorizontalBoxWithIndents(level);
		leftBox.add(flagIconComponent);
		leftBox.add(iconComponent);
		leftBox.add(Box.createHorizontalStrut(STRUT_WIDTH_BETWEEN_ICON_AND_TEXT));
		leftBox.add(leftComponent);

		Vector<JComponent> components = new Vector<JComponent>();
		components.add(leftBox);
		components.add(leftComponent);
		components.add(rightComponent);
		rowSelectionHandler.addSelectableRow(components, longDescriptionProvider);
		
		add(leftBox);
		add(rightComponent);
	}
	
	private Box createHorizontalBoxWithIndents(int level)
	{
		Box box = Box.createHorizontalBox();
		box.setOpaque(true);
		box.setBackground(getItemBackgroundColor());
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
	
	private Font getCommentsFieldFont()
	{
		Font font = getRawFont();
		font = font.deriveFont(Font.ITALIC);
		font = font.deriveFont((float)(font.getSize() * 0.8));
		
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
		return getDashboard(getProject());
	}
	
	private static Dashboard getDashboard(Project projectToUse)
	{
		ORef dashboardRef = projectToUse.getSingletonObjectRef(Dashboard.getObjectType());
		return Dashboard.find(projectToUse, dashboardRef);
	}
	
	protected DashboardRowDefinitionManager getDashboardRowDefinitionManager()
	{
		return getDashboard().getDashboardRowDefinitionManager();
	}

	public static final Color DASHBOARD_BACKGROUND_COLOR = Color.WHITE;
	
	private DynamicChoiceWithRootChoiceItem question;
	private SingleRowSelectionHandler rowSelectionHandler;
	protected static final int INDENT_PER_LEVEL = 25;
	private static final int STRUT_WIDTH_BETWEEN_ICON_AND_TEXT = 10;
	protected static final String EMPTY_COLUMN_TEXT = "";
}
