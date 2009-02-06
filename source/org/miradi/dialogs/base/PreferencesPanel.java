/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;
import org.miradi.diagram.DiagramConstants;
import org.miradi.dialogs.diagram.DiagramProjectPreferencesPanel;
import org.miradi.dialogs.fieldComponents.PanelCheckBox;
import org.miradi.dialogs.fieldComponents.PanelComboBox;
import org.miradi.dialogs.fieldComponents.PanelTabbedPane;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.threatrating.ThreatRatingPreferencesPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.FontFamiliyQuestion;
import org.miradi.questions.FontSizeQuestion;
import org.miradi.questions.QuestionManager;
import org.miradi.questions.TableRowHeightModeQuestion;
import org.miradi.utils.HyperlinkLabel;
import org.miradi.views.summary.SummaryPlanningPanel;

import com.jhlabs.awt.BasicGridLayout;

public class PreferencesPanel extends DataInputPanel implements ActionListener
{
	public PreferencesPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse.getProject());
		mainWindow = mainWindowToUse;
		project = mainWindow.getProject();
		add(createTabs(), BorderLayout.CENTER);
		
		setBackground(AppPreferences.getDarkPanelBackgroundColor());
		setBorder(BorderFactory.createEmptyBorder(0,3,3,3));
		
	}

	public void dispose()
	{
		diagramProjectPreferencesPanel.dispose();
		diagramProjectPreferencesPanel = null;
		
		if(summaryPlanningPanel != null)
			summaryPlanningPanel.dispose(); 
		if(threatRatingPreferencesPanel != null)
			threatRatingPreferencesPanel.dispose();
		threatRatingPreferencesPanel = null;
		super.dispose();
	}
	
	JTabbedPane createTabs()
	{
		JTabbedPane tabPane = new PanelTabbedPane();
		tabPane.addTab(EAM.text("Systemwide"), createSystemwideTab());
		tabPane.addTab(EAM.text("Diagram"), createDiagramTab());
		
		if(project.isOpen())
		{
			summaryPlanningPanel = new SummaryPlanningPanel(mainWindow, project.getMetadata().getRef());
			tabPane.addTab(EAM.text("Threat Ratings"), createThreatRatingTab());
			tabPane.addTab(EAM.text("Planning"), summaryPlanningPanel);
		}
		else
		{
			disableProjectSpecificCompoenents();
		}
		
		tabPane.addTab(EAM.text("Data Location"), createDataLocationTab());
		
		return tabPane;
	}
	
	private void disableProjectSpecificCompoenents()
	{
		panelFontFamilyCombo.setEnabled(false);
		panelFontSizeCombo.setEnabled(false);
	}

	private JPanel createDataLocationTab()
	{
		return new DataLocationChooserPanel(mainWindow);
	}
	
	private JPanel createSystemwideTab()
	{
		JPanel htmlTab = new JPanel(new BasicGridLayout(0,2));
		htmlTab.setBackground(AppPreferences.getDataPanelBackgroundColor());

		int panelFontSize = mainWindow.getDataPanelFontSize();
		String panelSizeAsString = Integer.toString(panelFontSize);
		panelFontSizeCombo = createAndAddLabelAndCombo(htmlTab, EAM.text("Font Size"), new FontSizeQuestion(), panelSizeAsString);
			
		String panelFontFamily = mainWindow.getDataPanelFontFamily();
		panelFontFamilyCombo = createAndAddLabelAndCombo(htmlTab, EAM.text("Font Family"), new FontFamiliyQuestion(), panelFontFamily);
		
		createAndAddBlankRow(htmlTab);
		String rowHeightMode = mainWindow.getRowHeightModeString();
		ChoiceQuestion rowHeightModeQuestion = QuestionManager.getQuestion(TableRowHeightModeQuestion.class);
		panelRowHeightModeCombo = createAndAddLabelAndCombo(htmlTab, EAM.text("Table Row Height Mode"), rowHeightModeQuestion, rowHeightMode);
		return htmlTab;
	}

	private void createAndAddBlankRow(JPanel htmlTab)
	{
		htmlTab.add(new JLabel(" "));
		htmlTab.add(new JLabel(" "));
	}

	private UiComboBox createAndAddLabelAndCombo(JPanel htmlTab, String label, ChoiceQuestion question, String sizeAsString)
	{
		UiComboBox combo = new PanelComboBox(question.getChoices());
		setSelectedItemQuestionBox(combo, sizeAsString);
		combo.addActionListener(this);
		htmlTab.add(new PanelTitleLabel(label));
		htmlTab.add(combo);
		return combo;
	}

	public void setSelectedItemQuestionBox(UiComboBox combo, String code)
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
		cellRatingsVisibleCheckBox.setSelected(mainWindow.getBooleanPreference(AppPreferences.TAG_CELL_RATINGS_VISIBLE));
		cellRatingsVisibleCheckBox.addActionListener(this);
		threatTab.add(cellRatingsVisibleCheckBox);
		
		threatRatingPreferencesPanel = new ThreatRatingPreferencesPanel(project); 
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

		diagramSystemPreferencesTab.add(new PanelTitleLabel(EAM.text("Strategy (Yellow)")));
		interventionDropdown = createColorsDropdown(DiagramConstants.strategyColorChoices);
		interventionDropdown.setSelectedItem(mainWindow.getColorPreference(AppPreferences.TAG_COLOR_STRATEGY));
		interventionDropdown.addActionListener(this);
		diagramSystemPreferencesTab.add(interventionDropdown);

		diagramSystemPreferencesTab.add(new PanelTitleLabel(EAM.text("Direct Threat (Pink)")));
		directThreatDropdown = createColorsDropdown(DiagramConstants.directThreatColorChoices);
		directThreatDropdown.setSelectedItem(mainWindow.getColorPreference(AppPreferences.TAG_COLOR_DIRECT_THREAT));
		directThreatDropdown.addActionListener(this);
		diagramSystemPreferencesTab.add(directThreatDropdown);

		diagramSystemPreferencesTab.add(new PanelTitleLabel(EAM.text("Contributing Factor (Orange)")));
		indirectFactorDropdown = createColorsDropdown(DiagramConstants.contributingFactorColorChoices);
		indirectFactorDropdown.setSelectedItem(mainWindow.getColorPreference(AppPreferences.TAG_COLOR_CONTRIBUTING_FACTOR));
		indirectFactorDropdown.addActionListener(this);
		diagramSystemPreferencesTab.add(indirectFactorDropdown);

		diagramSystemPreferencesTab.add(new PanelTitleLabel(EAM.text("Target (Light Green)")));
		targetDropdown = createColorsDropdown(DiagramConstants.targetColorChoices);
		targetDropdown.setSelectedItem(mainWindow.getColorPreference(AppPreferences.TAG_COLOR_TARGET));
		targetDropdown.addActionListener(this);
		diagramSystemPreferencesTab.add(targetDropdown);

		diagramSystemPreferencesTab.add(new PanelTitleLabel(EAM.text("Project Scope (Dark Green)")));
		scopeDropdown = createColorsDropdown(DiagramConstants.scopeColorChoices);
		scopeDropdown.setSelectedItem(mainWindow.getColorPreference(AppPreferences.TAG_COLOR_SCOPE));
		scopeDropdown.addActionListener(this);
		diagramSystemPreferencesTab.add(scopeDropdown);

		diagramSystemPreferencesTab.add(new PanelTitleLabel(EAM.text("Intermediate Result (Light Blue)")));
		intermediateResultDropDown = createColorsDropdown(DiagramConstants.intermediateResultChoices);
		intermediateResultDropDown.setSelectedItem(mainWindow.getColorPreference(AppPreferences.TAG_COLOR_INTERMEDIATE_RESULT));
		intermediateResultDropDown.addActionListener(this);
		diagramSystemPreferencesTab.add(intermediateResultDropDown);

		diagramSystemPreferencesTab.add(new PanelTitleLabel(EAM.text("Threat Reduction Result (Light Purple)")));
		threatReductionResultDropDown = createColorsDropdown(DiagramConstants.threatReductionResultChoices);
		threatReductionResultDropDown.setSelectedItem(mainWindow.getColorPreference(AppPreferences.TAG_COLOR_THREAT_REDUCTION_RESULT));
		threatReductionResultDropDown.addActionListener(this);
		diagramSystemPreferencesTab.add(threatReductionResultDropDown);

		diagramSystemPreferencesTab.add(new PanelTitleLabel(EAM.text("Show Diagram Grid")));
		gridVisibleCheckBox = new PanelCheckBox();
		gridVisibleCheckBox.setSelected(mainWindow.getBooleanPreference(AppPreferences.TAG_GRID_VISIBLE));
		gridVisibleCheckBox.addActionListener(this);
		diagramSystemPreferencesTab.add(gridVisibleCheckBox);
		
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

		diagramProjectPreferencesPanel = new DiagramProjectPreferencesPanel(mainWindow, project, project.getProjectInfo());
		diagramTab.add(diagramProjectPreferencesPanel);
		
		return diagramTab;
	}

	private UiComboBox createColorsDropdown(Color[] colorChoices)
	{
		UiComboBox dropdown = new PanelComboBox(colorChoices);
		dropdown.setRenderer(new ColorItemRenderer());
		return dropdown;
	}

	void update()
	{
		Color interventionColor = (Color)interventionDropdown.getSelectedItem();
		mainWindow.setColorPreference(AppPreferences.TAG_COLOR_STRATEGY, interventionColor);

		Color indirectFactorColor = (Color)indirectFactorDropdown.getSelectedItem();
		mainWindow.setColorPreference(AppPreferences.TAG_COLOR_CONTRIBUTING_FACTOR, indirectFactorColor);

		Color directThreatColor = (Color)directThreatDropdown.getSelectedItem();
		mainWindow.setColorPreference(AppPreferences.TAG_COLOR_DIRECT_THREAT, directThreatColor);

		Color targetColor = (Color)targetDropdown.getSelectedItem();
		mainWindow.setColorPreference(AppPreferences.TAG_COLOR_TARGET, targetColor);

		Color scopeColor = (Color)scopeDropdown.getSelectedItem();
		mainWindow.setColorPreference(AppPreferences.TAG_COLOR_SCOPE, scopeColor);

		Color intermediateResultColor = (Color) intermediateResultDropDown.getSelectedItem();
		mainWindow.setColorPreference(AppPreferences.TAG_COLOR_INTERMEDIATE_RESULT, intermediateResultColor);
		
		Color threatReductionResultColor = (Color) threatReductionResultDropDown.getSelectedItem();
		mainWindow.setColorPreference(AppPreferences.TAG_COLOR_THREAT_REDUCTION_RESULT, threatReductionResultColor);
		
		mainWindow.setBooleanPreference(AppPreferences.TAG_GRID_VISIBLE, gridVisibleCheckBox.isSelected());
		
		if(cellRatingsVisibleCheckBox != null)
			mainWindow.setBooleanPreference(AppPreferences.TAG_CELL_RATINGS_VISIBLE, cellRatingsVisibleCheckBox.isSelected());

		String panelFontSizeValue = getSelectedItemQuestionBox(panelFontSizeCombo);
		mainWindow.setDataPanelFontSize(Integer.parseInt(panelFontSizeValue));
		
		String panelFontFamilyValue = getSelectedItemQuestionBox(panelFontFamilyCombo);
		mainWindow.setDataPanelFontFamily(panelFontFamilyValue);
		
		String rowHeightMode = getSelectedItemQuestionBox(panelRowHeightModeCombo);
		mainWindow.setRowHeightMode(rowHeightMode);

		try
		{
			mainWindow.savePreferences();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unable to save preferences"));
		}
	}
	
	public String getSelectedItemQuestionBox(UiComboBox combo)
	{
		ChoiceItem selected = (ChoiceItem)combo.getSelectedItem();
		if(selected == null)
			return "";
		return selected.getCode();
	}
	

	static class ColorItemRenderer extends Component implements ListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			color = (Color)value;
			selected = isSelected;
			return this;
		}

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

		public Dimension getSize()
		{
			return new Dimension(48, 16);
		}

		public Dimension getPreferredSize()
		{
			return getSize();
		}

		public Dimension getMinimumSize()
		{
			return getSize();
		}

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
		update();
	}

	private Project project;
	private MainWindow mainWindow;
	private DiagramProjectPreferencesPanel diagramProjectPreferencesPanel;
	private ThreatRatingPreferencesPanel threatRatingPreferencesPanel;
	private SummaryPlanningPanel summaryPlanningPanel;
	
	private UiComboBox interventionDropdown;
	private UiComboBox directThreatDropdown;
	private UiComboBox indirectFactorDropdown;
	private UiComboBox targetDropdown;
	private UiComboBox scopeDropdown;
	private UiComboBox intermediateResultDropDown;
	private UiComboBox threatReductionResultDropDown;
	private UiCheckBox gridVisibleCheckBox; 
	private UiCheckBox cellRatingsVisibleCheckBox;
	
	private UiComboBox panelFontSizeCombo;
	private UiComboBox panelFontFamilyCombo;
	private UiComboBox panelRowHeightModeCombo;
}
