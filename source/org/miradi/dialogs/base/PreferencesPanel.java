/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;

import org.martus.swing.UiCheckBox;
import org.martus.swing.UiLabel;
import org.miradi.diagram.DiagramConstants;
import org.miradi.dialogs.NeverShowAgainPanel;
import org.miradi.dialogs.diagram.DiagramProjectPreferencesPanel;
import org.miradi.dialogs.fieldComponents.*;
import org.miradi.dialogs.threatrating.ThreatRatingPreferencesPanel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.questions.*;
import org.miradi.utils.ColorManager;
import org.miradi.utils.FillerLabel;
import org.miradi.utils.HyperlinkLabel;
import org.miradi.views.ProjectSettingsPanel;
import org.miradi.views.summary.PlanningPanel;

import com.jhlabs.awt.BasicGridLayout;

public class PreferencesPanel extends DataInputPanel implements ActionListener
{
	public PreferencesPanel(MainWindow mainWindowToUse, boolean showThemeOptions) throws Exception
	{
		super(mainWindowToUse.getProject());
		
		mainWindow = mainWindowToUse;
		add(createTabs(showThemeOptions), BorderLayout.CENTER);
		
		setBackground(AppPreferences.getDarkPanelBackgroundColor());
		setBorder(BorderFactory.createEmptyBorder(0,3,3,3));

		isUpdatingColorScheme = false;
		isUpdatingSystemTheme = false;
	}

	@Override
	public void dispose()
	{
		disposePanel(diagramProjectPreferencesPanel);
		diagramProjectPreferencesPanel = null;
		
		disposePanel(planningPanel);
		planningPanel = null;
		
		disposePanel(projectSettingsPanel);
		projectSettingsPanel = null;
		
		disposePanel(threatRatingPreferencesPanel);
		threatRatingPreferencesPanel = null;
		
		disposePanel(neverShowAgainPanel);
		neverShowAgainPanel = null;
		
		super.dispose();
	}
	
	private JTabbedPane createTabs(boolean showThemeOptions) throws Exception
	{
		JTabbedPane tabPane = new PanelTabbedPane();
		
		if(getProject().isOpen())
		{
			tabPane.addTab(EAM.text("Systemwide"), createSystemWideTab(showThemeOptions));
			tabPane.addTab(EAM.text("Diagram"), createDiagramTab());

			planningPanel = new PlanningPanel(getMainWindow(), getProject().getMetadata().getRef());
			tabPane.addTab(EAM.text("Threat Ratings"), createThreatRatingTab());
			tabPane.addTab(EAM.text("Planning"), planningPanel);
			
			projectSettingsPanel = new ProjectSettingsPanel(getProject());
			tabPane.addTab(EAM.text("Project Settings"), projectSettingsPanel);
			tabPane.addTab(EAM.text("Notifications"), createNeverShowAgainPanel());
			tabPane.addTab(EAM.text("Languages"), createLanguagesPanel());
		}
		else
		{
			tabPane.addTab(EAM.text("Languages"), createLanguagesPanel());
			tabPane.addTab(EAM.text("Data Location"), createDataLocationTab());
			tabPane.addTab(EAM.text("Notifications"), createNeverShowAgainPanel());
		}

		return tabPane;
	}

	private JPanel createDataLocationTab()
	{
		return new DataLocationChooserPanel(getMainWindow());
	}
	
	private JPanel createNeverShowAgainPanel()
	{
		return new NeverShowAgainPanel(getMainWindow());
	}

	private JPanel createLanguagesPanel()
	{
		return new LanguagesPanel(getMainWindow());
	}

	private JPanel createSystemWideTab(boolean showThemeOptions)
	{
		JPanel htmlTab = new JPanel(new BasicGridLayout(0,2, 2, 2, 2, 5));
		htmlTab.setBackground(AppPreferences.getDataPanelBackgroundColor());

		if (showThemeOptions)
		{
			String lookAndFeelTheme = getMainWindow().getLookAndFeelThemeName();
			lookAndFeelThemeCombo = createAndAddLabelAndCombo(htmlTab, EAM.text("System Theme"), StaticQuestionManager.getQuestion(LookAndFeelThemeQuestion.class), lookAndFeelTheme);
			htmlTab.add(new FillerLabel());
			htmlTab.add(new PanelTitleLabel("<html><i>" + EAM.text("" +
					"NOTE: Changes will be applied on restarting Miradi. <br>")));
			createAndAddBlankRow(htmlTab);
		}

		String panelSizeAsString = getDataPanelFontSizeCode();
		panelFontSizeCombo = createAndAddLabelAndCombo(htmlTab, EAM.text("Font Size"), StaticQuestionManager.getQuestion(FontSizeQuestion.class), panelSizeAsString);
			
		String panelFontFamily = getMainWindow().getDataPanelFontFamily();
		panelFontFamilyCombo = createAndAddLabelAndCombo(htmlTab, EAM.text("Font Family"), StaticQuestionManager.getQuestion(FontFamiliyQuestion.class), panelFontFamily);
		
		createAndAddBlankRow(htmlTab);
		String rowHeightMode = getMainWindow().getRowHeightModeString();
		ChoiceQuestion rowHeightModeQuestion = StaticQuestionManager.getQuestion(TableRowHeightModeQuestion.class);
		panelRowHeightModeCombo = createAndAddLabelAndCombo(htmlTab, EAM.text("Table Row Height Mode"), rowHeightModeQuestion, rowHeightMode);
		htmlTab.add(new FillerLabel());
		htmlTab.add(new PanelTitleLabel("<html><i>" + EAM.text("" +
				"NOTE: Automatic Table Row Height Mode may impact the speed of expand and <br>" +
				"collapse actions in the tables this setting affects. For better performance, <br>" +
				"select Manual Row Height Mode.")));
		
		createAndAddBlankRow(htmlTab);
		htmlTab.add(new PanelTitleLabel(EAM.text("Enable Spell Checking")));
		enableSpellCheckingCheckBox = new PanelCheckBox();
		enableSpellCheckingCheckBox.setBackground(AppPreferences.getDataPanelBackgroundColor());
		enableSpellCheckingCheckBox.setSelected(getMainWindow().getBooleanPreference(AppPreferences.TAG_IS_SPELL_CHECK_ENABLED));
		enableSpellCheckingCheckBox.addActionListener(this);
		htmlTab.add(enableSpellCheckingCheckBox);

		htmlTab.add(new FillerLabel());
		htmlTab.add(new PanelTitleLabel("<html><i>" + EAM.text("" +
				"NOTE: Spell checking is currently only available in English. <br>" +
				"If the 'Primary Project Data Language' in the Project tab of the Summary view <br>" +
				"is set to use a language other than English, spell check will not be available. <br>")));

		return htmlTab;
	}

	private String getDataPanelFontSizeCode()
	{
		final int panelFontSize = getMainWindow().getDataPanelFontSize();
		final ChoiceQuestion choiceQuestion = StaticQuestionManager.getQuestion(FontSizeQuestion.class);
		final String fontSizeAsCode = Integer.toString(panelFontSize);
		ChoiceItem choiceItem = choiceQuestion.findChoiceByCode(fontSizeAsCode);
		if (choiceItem == null)
			return FontSizeQuestion.DEFAULT_FONT_SIZE_CODE;
		
		return fontSizeAsCode;
	}

	private void createAndAddBlankRow(JPanel htmlTab)
	{
		htmlTab.add(new JLabel(" "));
		htmlTab.add(new JLabel(" "));
	}

	private UiComboBox createAndAddLabelAndCombo(JPanel htmlTab, String label, ChoiceQuestion question, String sizeAsString)
	{
		UiComboBox<ChoiceItem> combo = new PanelComboBox<>(question.getChoices());
		setSelectedItemQuestionBox(combo, sizeAsString);
		combo.addActionListener(this);
		htmlTab.add(new PanelTitleLabel(label));
		htmlTab.add(combo);
		return combo;
	}

	private void setSelectedItemQuestionBox(UiComboBox combo, String code)
	{
		for(int i = 0; i < combo.getItemCount(); ++i)
		{
			ChoiceItem choice = (ChoiceItem)combo.getItemAt(i);
			if(choice.getCode().equals(code))
			{
				combo.setSelectedIndex(i);
				return;
			}
		}
		combo.setSelectedIndex(-1);
	}

	private JPanel createThreatRatingTab()
	{
		JPanel threatTab = new JPanel(new BasicGridLayout(0,2));
		threatTab.setBackground(AppPreferences.getDataPanelBackgroundColor());

		threatTab.add(new PanelTitleLabel(EAM.text("Show Ratings in Cell")));
		cellRatingsVisibleCheckBox = new PanelCheckBox();
		cellRatingsVisibleCheckBox.setBackground(AppPreferences.getDataPanelBackgroundColor());
		cellRatingsVisibleCheckBox.setSelected(getMainWindow().getBooleanPreference(AppPreferences.TAG_CELL_RATINGS_VISIBLE));
		cellRatingsVisibleCheckBox.addActionListener(this);
		threatTab.add(cellRatingsVisibleCheckBox);
		
		threatRatingPreferencesPanel = new ThreatRatingPreferencesPanel(getProject()); 
		threatTab.add(threatRatingPreferencesPanel);
		
		return threatTab;
	}

	private JPanel createDiagramTab()
	{
		JPanel diagramTab = new JPanel(new BasicGridLayout(1,1));
		diagramTab.setBackground(AppPreferences.getDataPanelBackgroundColor());

		JPanel diagramSystemPreferencesTab = new JPanel(new BasicGridLayout(0,2));
		diagramSystemPreferencesTab.setBackground(AppPreferences.getDataPanelBackgroundColor());
		
		diagramTab.add(new UiLabel(" "));
		diagramTab.add(new PanelTitleLabel(EAM.text("Choose the colors that look best on your system:")));

		String colorScheme = getMainWindow().getColorScheme();
		colorSchemeCombo = createAndAddLabelAndCombo(diagramSystemPreferencesTab, EAM.text("Color Scheme"), StaticQuestionManager.getQuestion(ColorSchemeQuestion.class), colorScheme);

		interventionDropdown = createAndAddColorDropdown(diagramSystemPreferencesTab, EAM.text("Strategy (Yellow)"), DiagramConstants.strategyColorChoices, AppPreferences.TAG_COLOR_STRATEGY);
		directThreatDropdown = createAndAddColorDropdown(diagramSystemPreferencesTab, EAM.text("Direct Threat (Pink)"), DiagramConstants.directThreatColorChoices, AppPreferences.TAG_COLOR_DIRECT_THREAT);
		biophysicalFactorDropdown = createAndAddColorDropdown(diagramSystemPreferencesTab, EAM.text("Biophysical Factor (Olive)"), DiagramConstants.biophysicalFactorColorChoices, AppPreferences.TAG_COLOR_BIOPHYSICAL_FACTOR);
		indirectFactorDropdown = createAndAddColorDropdown(diagramSystemPreferencesTab, EAM.text("Contributing Factor (Orange)"), DiagramConstants.contributingFactorColorChoices, AppPreferences.TAG_COLOR_CONTRIBUTING_FACTOR);
		biodiversityTargetDropdown = createAndAddColorDropdown(diagramSystemPreferencesTab, EAM.text("Biodiversity Target (Lighter Green)"), DiagramConstants.targetColorChoices, AppPreferences.TAG_COLOR_TARGET);
		humanWelfareTargetDropdown = createAndAddColorDropdown(diagramSystemPreferencesTab, EAM.text("Human Wellbeing Target (Lighter Brown)"), DiagramConstants.humanWelfareTargetColorChoices, AppPreferences.TAG_COLOR_HUMAN_WELFARE_TARGET);
		biodiversityTargetScopeDropdown = createAndAddColorDropdown(diagramSystemPreferencesTab, EAM.text("Biodiversity Target Scope (Darker Green)"), DiagramConstants.biodiversityTargetScopeColorChoices, AppPreferences.TAG_COLOR_SCOPE_BOX);
		humanWelfareScopeDropDown = createAndAddColorDropdown(diagramSystemPreferencesTab, EAM.text("Human Wellbeing Target Scope (Darker Brown)"), DiagramConstants.humanWelfareScopeColorChoices, AppPreferences.TAG_COLOR_HUMAN_WELFARE_SCOPE_BOX);
		intermediateResultDropDown = createAndAddColorDropdown(diagramSystemPreferencesTab, EAM.text("Intermediate Result (Light Blue)"), DiagramConstants.intermediateResultChoices, AppPreferences.TAG_COLOR_INTERMEDIATE_RESULT);
		biophysicalResultDropdown = createAndAddColorDropdown(diagramSystemPreferencesTab, EAM.text("Biophysical Result (Lavender Blue)"), DiagramConstants.biophysicalResultColorChoices, AppPreferences.TAG_COLOR_BIOPHYSICAL_RESULT);
		threatReductionResultDropDown = createAndAddColorDropdown(diagramSystemPreferencesTab, EAM.text("Threat Reduction Result (Light Purple)"), DiagramConstants.threatReductionResultChoices, AppPreferences.TAG_COLOR_THREAT_REDUCTION_RESULT);

		diagramTab.add(new UiLabel(" "));
		diagramTab.add(new UiLabel(" "));
		
		JPanel bottomText = new JPanel();
		bottomText.setBackground(AppPreferences.getDataPanelBackgroundColor());
		bottomText.add(new HyperlinkLabel( 
				EAM.text("<div class='DataPanel'><p>Why are my choices limited to one color family for each type of factor?</p>"),
				EAM.text("We are trying to create a standard set of symbols that can be recognized " +
				"globally. Just like people the world over recognize a red octagon as a " +
				"stop sign, we hope that they will recognize a green oval as a target or " +
				"a yellow hexagon as a strategy")), BorderLayout.AFTER_LAST_LINE);
		bottomText.setBorder(BorderFactory.createEmptyBorder(25, 5, 25, 5));
		
		diagramTab.add(diagramSystemPreferencesTab);
		diagramTab.add(bottomText);
		
		TwoColumnPanel gridChoicePanel = new TwoColumnPanel();
		gridChoicePanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		gridVisibleCheckBox = new PanelCheckBox();
		gridVisibleCheckBox.setSelected(getMainWindow().getBooleanPreference(AppPreferences.TAG_GRID_VISIBLE));
		gridVisibleCheckBox.addActionListener(this);
		gridChoicePanel.add(new PanelTitleLabel(EAM.text("Show Diagram Grid")));
		gridChoicePanel.add(gridVisibleCheckBox);
		diagramTab.add(gridChoicePanel);

		diagramProjectPreferencesPanel = new DiagramProjectPreferencesPanel(getMainWindow());
		diagramTab.add(diagramProjectPreferencesPanel);
		
		return diagramTab;
	}

	private UiComboBox createAndAddColorDropdown(JPanel diagramSystemPreferencesTab, String label, Color[] colorChoices, String colorTag)
	{
		diagramSystemPreferencesTab.add(new PanelTitleLabel(label));
		UiComboBox<Color> dropdown = new PanelComboBox<>(colorChoices);
		dropdown.setRenderer(new ColorItemRenderer<>());
		dropdown.setSelectedItem(getMainWindow().getColorPreference(colorTag));
		dropdown.addActionListener(this);
		diagramSystemPreferencesTab.add(dropdown);
		
		return dropdown;
	}

	private void update()
	{
		String colorScheme = getSelectedItemQuestionBox(colorSchemeCombo);
		getMainWindow().setColorScheme(colorScheme);

		String lookAndColorTheme = getSelectedItemQuestionBox(lookAndFeelThemeCombo);
		getMainWindow().setLookAndFeelThemeName(lookAndColorTheme);

		setColorPreference(interventionDropdown, AppPreferences.TAG_COLOR_STRATEGY);
		setColorPreference(indirectFactorDropdown, AppPreferences.TAG_COLOR_CONTRIBUTING_FACTOR);
		setColorPreference(directThreatDropdown, AppPreferences.TAG_COLOR_DIRECT_THREAT);
		setColorPreference(biophysicalFactorDropdown, AppPreferences.TAG_COLOR_BIOPHYSICAL_FACTOR);
		setColorPreference(biophysicalResultDropdown, AppPreferences.TAG_COLOR_BIOPHYSICAL_RESULT);
		setColorPreference(biodiversityTargetDropdown, AppPreferences.TAG_COLOR_TARGET);
		setColorPreference(humanWelfareTargetDropdown, AppPreferences.TAG_COLOR_HUMAN_WELFARE_TARGET);
		setColorPreference(biodiversityTargetScopeDropdown, AppPreferences.TAG_COLOR_SCOPE_BOX);
		setColorPreference(humanWelfareScopeDropDown, AppPreferences.TAG_COLOR_HUMAN_WELFARE_SCOPE_BOX);
		setColorPreference(intermediateResultDropDown, AppPreferences.TAG_COLOR_INTERMEDIATE_RESULT);
		setColorPreference(threatReductionResultDropDown, AppPreferences.TAG_COLOR_THREAT_REDUCTION_RESULT);

		// set those that don't have an exposed preference choice
		getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_ACTIVITIES, (Color) interventionDropdown.getSelectedItem());
		if (isLegacyColorScheme())
		{
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_INDICATOR, DiagramConstants.LEGACY_DEFAULT_INDICATOR_COLOR);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_OBJECTIVE, DiagramConstants.LEGACY_DEFAULT_OBJECTIVE_COLOR);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_ALERT, ColorManager.LEGACY_RED);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_CAUTION, ColorManager.LEGACY_DARK_YELLOW);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_OK, ColorManager.LEGACY_LIGHT_GREEN);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_GREAT, ColorManager.LEGACY_DARK_GREEN);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_ABANDONED, ColorManager.LEGACY_DARK_GREY);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_PLANNED, ColorManager.LEGACY_LIGHT_BLUE);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_NOT_KNOWN, ColorManager.LEGACY_LIGHT_GREY);
		}
		else
		{
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_INDICATOR, DiagramConstants.DEFAULT_INDICATOR_COLOR);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_OBJECTIVE, DiagramConstants.DEFAULT_OBJECTIVE_COLOR);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_ALERT, ColorManager.RED);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_CAUTION, ColorManager.DARK_YELLOW);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_OK, ColorManager.LIGHT_GREEN);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_GREAT, ColorManager.DARK_GREEN);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_ABANDONED, ColorManager.DARK_GREY);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_PLANNED, ColorManager.LIGHT_BLUE);
			getMainWindow().setColorPreference(AppPreferences.TAG_COLOR_NOT_KNOWN, ColorManager.LIGHT_GREY);
		}

		getMainWindow().setBooleanPreference(AppPreferences.TAG_GRID_VISIBLE, gridVisibleCheckBox.isSelected());
		
		if(cellRatingsVisibleCheckBox != null)
			getMainWindow().setBooleanPreference(AppPreferences.TAG_CELL_RATINGS_VISIBLE, cellRatingsVisibleCheckBox.isSelected());

		getMainWindow().setDataPanelFontSize(getSelectedFontSize());
		
		String panelFontFamilyValue = getSelectedItemQuestionBox(panelFontFamilyCombo);
		getMainWindow().setDataPanelFontFamily(panelFontFamilyValue);
		
		String rowHeightMode = getSelectedItemQuestionBox(panelRowHeightModeCombo);
		getMainWindow().setRowHeightMode(rowHeightMode);
		
		getMainWindow().setBooleanPreference(AppPreferences.TAG_IS_SPELL_CHECK_ENABLED, enableSpellCheckingCheckBox.isSelected());

		getMainWindow().safelySavePreferences();
	}

	private int getSelectedFontSize()
	{
		String panelFontSizeValue = getSelectedItemQuestionBox(panelFontSizeCombo);
		if (panelFontSizeValue.equals(FontSizeQuestion.DEFAULT_FONT_SIZE_CODE))
			return MainWindow.getSystemFontSize();
		
		return Integer.parseInt(panelFontSizeValue);
	}

	private void setColorPreference(UiComboBox colorDropDown, String tagColor)
	{
		Color color = (Color)colorDropDown.getSelectedItem();
		getMainWindow().setColorPreference(tagColor, color);
	}
	
	private String getSelectedItemQuestionBox(UiComboBox combo)
	{
		ChoiceItem selected = (ChoiceItem)combo.getSelectedItem();
		if(selected == null)
			return "";
		
		return selected.getCode();
	}

	static class ColorItemRenderer<E> extends Component implements ListCellRenderer<E>
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			color = (Color)value;
			selected = isSelected;
			return this;
		}

		@Override
		public void paint(Graphics g)
		{
			super.paint(g);
			g.setColor(color);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, getWidth(), getHeight());			
			if(selected)
				g.drawRect(1, 1, getWidth()-2, getHeight()-2);
		}

		@Override
		public Dimension getSize()
		{
			return new Dimension(48, 16);
		}

		@Override
		public Dimension getPreferredSize()
		{
			return getSize();
		}

		@Override
		public Dimension getMinimumSize()
		{
			return getSize();
		}

		@Override
		public Dimension getMaximumSize()
		{
			return getSize();
		}

		boolean selected;
		Color color;
	}

	public String getPanelDescription()
	{
		return EAM.text("Preferences");
	}

	public void actionPerformed(ActionEvent e)
	{
		if (isUpdatingColorScheme || isUpdatingSystemTheme)
			return;

		boolean isColorSchemeChange = e.getSource() == colorSchemeCombo;
		if (isColorSchemeChange)
		{
			isUpdatingColorScheme = true;
			setColorScheme();
			isUpdatingColorScheme = false;
		}

		boolean isSystemThemeChange = e.getSource() == lookAndFeelThemeCombo;
		if (isSystemThemeChange)
		{
			isUpdatingSystemTheme = true;
			// no-op...updated preference will be applied on restart
			isUpdatingSystemTheme = false;
		}

		update();

		if (isColorSchemeChange)
			ColorManager.instance().clear();
	}

	private boolean isLegacyColorScheme()
	{
		String colorScheme = getSelectedItemQuestionBox(colorSchemeCombo);
		return colorScheme.equalsIgnoreCase(ColorSchemeQuestion.LEGACY_COLOR_SCHEME);
	}

	private void setColorScheme()
	{
		if (isLegacyColorScheme())
			setDefaultColorScheme();
		else
			setNewColorScheme();
	}

	private void setDefaultColorScheme()
	{
		interventionDropdown.setSelectedItem(DiagramConstants.LEGACY_DEFAULT_STRATEGY_COLOR);
		directThreatDropdown.setSelectedItem(DiagramConstants.LEGACY_DEFAULT_DIRECT_THREAT_COLOR);
		biophysicalFactorDropdown.setSelectedItem(DiagramConstants.LEGACY_DEFAULT_BIOPHYSICAL_FACTOR_COLOR);
		biophysicalResultDropdown.setSelectedItem(DiagramConstants.LEGACY_DEFAULT_BIOPHYSICAL_RESULT_COLOR);
		indirectFactorDropdown.setSelectedItem(DiagramConstants.LEGACY_DEFAULT_CONTRIBUTING_FACTOR_COLOR);
		biodiversityTargetDropdown.setSelectedItem(DiagramConstants.LEGACY_DEFAULT_TARGET_COLOR);
		humanWelfareTargetDropdown.setSelectedItem(DiagramConstants.LEGACY_DEFAULT_HUMAN_WELFARE_TARGET_COLOR);
		biodiversityTargetScopeDropdown.setSelectedItem(DiagramConstants.LEGACY_DEFAULT_BIODIVERSITY_TARGET_SCOPE_COLOR);
		humanWelfareScopeDropDown.setSelectedItem(DiagramConstants.LEGACY_DEFAULT_HUMAN_WELFARE_SCOPE_COLOR);
		intermediateResultDropDown.setSelectedItem(DiagramConstants.LEGACY_DEFAULT_INTERMEDIATE_RESULT_COLOR);
		threatReductionResultDropDown.setSelectedItem(DiagramConstants.LEGACY_DEFAULT_THREAT_REDUCTION_RESULT_COLOR);
	}

	private void setNewColorScheme()
	{
		interventionDropdown.setSelectedItem(DiagramConstants.DEFAULT_STRATEGY_COLOR);
		directThreatDropdown.setSelectedItem(DiagramConstants.DEFAULT_DIRECT_THREAT_COLOR);
		biophysicalFactorDropdown.setSelectedItem(DiagramConstants.DEFAULT_BIOPHYSICAL_FACTOR_COLOR);
		biophysicalResultDropdown.setSelectedItem(DiagramConstants.DEFAULT_BIOPHYSICAL_RESULT_COLOR);
		indirectFactorDropdown.setSelectedItem(DiagramConstants.DEFAULT_CONTRIBUTING_FACTOR_COLOR);
		biodiversityTargetDropdown.setSelectedItem(DiagramConstants.DEFAULT_TARGET_COLOR);
		humanWelfareTargetDropdown.setSelectedItem(DiagramConstants.DEFAULT_HUMAN_WELFARE_TARGET_COLOR);
		biodiversityTargetScopeDropdown.setSelectedItem(DiagramConstants.DEFAULT_BIODIVERSITY_TARGET_SCOPE_COLOR);
		humanWelfareScopeDropDown.setSelectedItem(DiagramConstants.DEFAULT_HUMAN_WELFARE_SCOPE_COLOR);
		intermediateResultDropDown.setSelectedItem(DiagramConstants.DEFAULT_INTERMEDIATE_RESULT_COLOR);
		threatReductionResultDropDown.setSelectedItem(DiagramConstants.DEFAULT_THREAT_REDUCTION_RESULT_COLOR);
	}

	private Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}

	private MainWindow mainWindow;
	private DiagramProjectPreferencesPanel diagramProjectPreferencesPanel;
	private ThreatRatingPreferencesPanel threatRatingPreferencesPanel;
	private PlanningPanel planningPanel;
	private ProjectSettingsPanel projectSettingsPanel;
	private NeverShowAgainPanel neverShowAgainPanel;

	private boolean isUpdatingColorScheme;
	private boolean isUpdatingSystemTheme;
	
	private UiComboBox colorSchemeCombo;
	private UiComboBox lookAndFeelThemeCombo;

	private UiComboBox interventionDropdown;
	private UiComboBox directThreatDropdown;
	private UiComboBox biophysicalFactorDropdown;
	private UiComboBox biophysicalResultDropdown;
	private UiComboBox indirectFactorDropdown;
	private UiComboBox biodiversityTargetDropdown;
	private UiComboBox humanWelfareTargetDropdown;
	private UiComboBox biodiversityTargetScopeDropdown;
	private UiComboBox humanWelfareScopeDropDown;
	private UiComboBox intermediateResultDropDown;
	private UiComboBox threatReductionResultDropDown;
	
	private UiCheckBox gridVisibleCheckBox; 
	private UiCheckBox cellRatingsVisibleCheckBox;
	private UiCheckBox enableSpellCheckingCheckBox;
	
	private UiComboBox panelFontSizeCombo;
	private UiComboBox panelFontFamilyCombo;
	private UiComboBox panelRowHeightModeCombo;
}
