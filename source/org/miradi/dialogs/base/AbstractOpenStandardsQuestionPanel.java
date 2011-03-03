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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.border.AbstractBorder;
import javax.swing.event.ListSelectionListener;

import org.miradi.actions.AbstractJumpMenuAction;
import org.miradi.dialogfields.DashboardCommentsField;
import org.miradi.dialogfields.DashboardFlagIconField;
import org.miradi.dialogfields.DashboardStatusIconField;
import org.miradi.dialogfields.DashboardStatusLabelField;
import org.miradi.dialogfields.ObjectDataField;
import org.miradi.dialogfields.TextAreaRightClickMouseHandler;
import org.miradi.dialogs.dashboard.AbstractLongDescriptionProvider;
import org.miradi.dialogs.dashboard.DashboardRowDefinition;
import org.miradi.dialogs.dashboard.DashboardRowDefinitionManager;
import org.miradi.dialogs.fieldComponents.PanelLabelWithSelectableText;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.MiradiGridLayoutPlus;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.CommandExecutedEvent;
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

abstract public class AbstractOpenStandardsQuestionPanel extends AbstractObjectDataInputPanel implements Scrollable, RowSelectionListener
{
	public AbstractOpenStandardsQuestionPanel(Project projectToUse, DynamicChoiceWithRootChoiceItem questionToUse) throws Exception
	{
		super(projectToUse, getDashboard(projectToUse).getRef());
		
		setBackground(DASHBOARD_BACKGROUND_COLOR);
		setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		setLayout(createLayoutManager());
		question = questionToUse;
		
		rebuild();
	}
	
	public boolean getScrollableTracksViewportWidth()
	{
		return true;
	}
	
	public boolean getScrollableTracksViewportHeight()
	{
		return false;
	}

	public Dimension getPreferredScrollableViewportSize()
	{
		// NOTE: Required by implements Scrollable, but default behavior is fine
		return super.getPreferredSize();
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction)
	{
		// NOTE: Required by implements Scrollable, but seems to have no effect
		return 0;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction)
	{
		// NOTE: Required by implements Scrollable, but seems to have no effect
		return 0;
	}

	private void rebuild() throws Exception
	{
		removeAll();
		getFields().clear();
		
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
		gridLayout.setGaps(0, 1);
		
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
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		try
		{
			if(eventForcesRebuild(event))
				rebuild();
		}
		catch(Exception e)
		{
			EAM.unexpectedErrorDialog(e);
		}
	}
	
	private boolean eventForcesRebuild(CommandExecutedEvent event)
	{
		if(event.isSetDataCommandWithThisTypeAndTag(Dashboard.getObjectType(), Dashboard.TAG_PROGRESS_CHOICE_MAP))
			return true;
		if(event.isSetDataCommandWithThisTypeAndTag(Dashboard.getObjectType(), Dashboard.TAG_COMMENTS_MAP))
			return true;
		if(event.isSetDataCommandWithThisTypeAndTag(Dashboard.getObjectType(), Dashboard.TAG_FLAGS_MAP))
			return true;

		return false;
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
		
		if(level == 1 && children.size() > 0)
		{
			Box left = createHorizontalBoxWithIndents(level);
			left.setBackground(DASHBOARD_BACKGROUND_COLOR);
			FillerLabel right = new FillerLabel();
			addRow(left, right);
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
		flagIconField.getComponent().setBackground(getItemBackgroundColor(level));
		addUpdatedCustomField(flagIconField);
		
		ChoiceQuestion progressStatusQuestion = new OpenStandardsDynamicProgressStatusQuestion(getProject().getCachedDashboardEffectiveMap(), choiceItem.getCode());
		ObjectDataField statusIconField = new DashboardStatusIconField(getProject(), getDashboard().getRef(), choiceItem.getCode(), progressStatusQuestion);
		statusIconField.getComponent().setBackground(getItemBackgroundColor(level));
		addUpdatedCustomField(statusIconField);
		
		ObjectDataField statusTextField = new DashboardStatusLabelField(getProject(), getDashboard().getRef(), choiceItem.getCode(), progressStatusQuestion);
		JComponent statusTextComponent = statusTextField.getComponent();
		statusTextComponent.setBackground(getItemBackgroundColor(level));
		addUpdatedCustomField(statusTextField);

		PanelLabelWithSelectableText labelComponent = createLabelWithSelectableText(level, choiceItem.getLabel());

		DashboardCommentsField commentsField = new DashboardCommentsField(getProject(), getDashboard().getRef(), choiceItem.getCode());
		if(commentsField.hasComments())
		{
			commentsField.getComponent().setBackground(getItemBackgroundColor(level));
			commentsField.getComponent().setFont(getCommentsFieldFont());
			addUpdatedCustomField(commentsField);
		}

		OneColumnPanel rightComponent = new OneColumnPanel();
		rightComponent.setOpaque(true);
		rightComponent.setBackground(getItemBackgroundColor(level));
		rightComponent.add(statusTextComponent);
		rightComponent.add(commentsField.getComponent());
		addRow(choiceItem.getLongDescriptionProvider(), level, flagIconField.getComponent(), statusIconField.getComponent(), labelComponent, rightComponent);
	}

	private PanelLabelWithSelectableText createLabelWithSelectableText(int level, String label)
	{
		PanelLabelWithSelectableText component = new PanelLabelWithSelectableText(label);
		new TextAreaRightClickMouseHandler(getMainWindow().getActions(), component);		
		component.setOpaque(true);
		component.setBackground(getItemBackgroundColor(level));
		
		return component;
	}

	private void addUpdatedCustomField(ObjectDataField field)
	{
		addFieldToList(field);
		field.updateFromObject();
	}
	
	private void addRowWithoutIcon(String leftColumnText, String rightColumnText, HashMap<String, String> tokenReplacementMap, AbstractLongDescriptionProvider longDescriptionProvider, int level) throws Exception
	{
		String rightColumnTranslatedText = EAM.substitute(rightColumnText, tokenReplacementMap);
		JComponent leftComponent = new PanelLabelWithSelectableText(leftColumnText);
		leftComponent.setOpaque(true);
		leftComponent.setBackground(getItemBackgroundColor(level));
		
		PanelLabelWithSelectableText rightComponent = createLabelWithSelectableText(level, rightColumnTranslatedText);
		
		addRow(longDescriptionProvider, level, new FillerLabel(), new FillerLabel(), leftComponent, rightComponent);
	}

	private Color getItemBackgroundColor(int level)
	{
		switch(level)
		{
			case 0: return AppPreferences.getWizardTitleBackground();
			case 1: return AppPreferences.getWizardBackgroundColor();
			default: return Color.decode(AppPreferences.getWizardSidebarBackgroundColorForCss());
		}
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
		flagIconComponent.setAlignmentY(TOP_ALIGNMENT);
		iconComponent.setAlignmentY(TOP_ALIGNMENT);
		leftComponent.setAlignmentY(TOP_ALIGNMENT);
		
		Box leftBox = createHorizontalBoxWithIndents(level);
		leftBox.setAlignmentY(TOP_ALIGNMENT);
		leftBox.add(flagIconComponent);
		leftBox.add(iconComponent);
		leftBox.add(Box.createHorizontalStrut(STRUT_WIDTH_BETWEEN_ICON_AND_TEXT));
		leftBox.add(leftComponent);
		leftBox.add(Box.createHorizontalStrut(STRUT_WIDTH_AFTER_LEFT_TEXT));

		Vector<JComponent> components = new Vector<JComponent>();
		components.add(leftBox);
		components.add(leftComponent);
		components.add(rightComponent);
		rowSelectionHandler.addSelectableRow(components, longDescriptionProvider);
		
		leftBox.setBorder(new LineBorderWithoutRightSide(Color.LIGHT_GRAY));
		rightComponent.setBorder(new LineBorderWithoutLeftSide(Color.LIGHT_GRAY));
		addRow(leftBox, rightComponent);
	}
	
	private void addRow(JComponent left, JComponent right)
	{
		add(left);
		add(right);
	}
	
	private Box createHorizontalBoxWithIndents(int level)
	{
		Box box = Box.createHorizontalBox();
		box.setOpaque(true);
		box.setBackground(getItemBackgroundColor(level));
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
	
	class PartialBorder extends AbstractBorder
	{
		public PartialBorder(Color colorToUse, Insets insetsToUse)
		{
			color = colorToUse;
			insets = insetsToUse;
		}

		@Override
		public Insets getBorderInsets(Component arg0)
		{
			return insets;
		}

		@Override
		public boolean isBorderOpaque()
		{
			return true;
		}

		@Override
		public void paintBorder(Component component, Graphics g, int x, int y, int width, int height)
		{
			g.setColor(color);
			if(insets.left > 0)
				g.drawLine(x, y, x, y+height);
			if(insets.right > 0)
				g.drawLine(x+width-1, y, x+width-1, y+height);
			if(insets.top > 0)
				g.drawLine(x, y, x+width, y);
			if(insets.bottom > 0)
				g.drawLine(x, y+height-1, x+width, y+height-1);
		}
		
		private Color color;
		private Insets insets;
	}
	
	class LineBorderWithoutRightSide extends PartialBorder
	{
		public LineBorderWithoutRightSide(Color colorToUse)
		{
			super(colorToUse, new Insets(1, 1, 1, 0));
		}
		
	}

	class LineBorderWithoutLeftSide extends PartialBorder
	{
		public LineBorderWithoutLeftSide(Color colorToUse)
		{
			super(colorToUse, new Insets(1, 0, 1, 1));
		}
		
	}
	public static final Color DASHBOARD_BACKGROUND_COLOR = Color.WHITE;
	
	private DynamicChoiceWithRootChoiceItem question;
	private SingleRowSelectionHandler rowSelectionHandler;
	protected static final int INDENT_PER_LEVEL = 25;
	private static final int STRUT_WIDTH_BETWEEN_ICON_AND_TEXT = 10;
	private static final int STRUT_WIDTH_AFTER_LEFT_TEXT = 5;
	protected static final String EMPTY_COLUMN_TEXT = "";
}
