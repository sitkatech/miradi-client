/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.utils.DialogGridPanel;
import org.conservationmeasures.eam.utils.HyperlinkLabel;
import org.martus.swing.UiCheckBox;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;

public class PreferencesPanel extends DataInputPanel implements ActionListener
{
	public PreferencesPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse.getProject());
		mainWindow = mainWindowToUse;


		JPanel bottomText = new JPanel();
		bottomText.add(new HyperlinkLabel( 
				"<p>Why are my choices limited to one color family for each type of factor?</p>",
				"We are trying to create a standard set of symbols that can be recognized " +
				"globally. Just like people the world over recognize a red octagon as a " +
				"stop sign, we hope that they will recognize a green oval as a target or " +
				"a yellow hexagon as a strategy"), BorderLayout.AFTER_LAST_LINE);
		bottomText.setBorder(BorderFactory.createEmptyBorder(25, 5, 25, 5));

		add(new UiLabel(EAM.text("Choose the colors that look best on your system:")), BorderLayout.BEFORE_FIRST_LINE);
		add(createColorPreferencesPanel(), BorderLayout.CENTER);
		add(bottomText, BorderLayout.AFTER_LAST_LINE);
	}

	DialogGridPanel createColorPreferencesPanel()
	{
		DialogGridPanel panel = new DialogGridPanel();

		panel.add(new UiLabel(EAM.text("Strategy (Yellow)")));
		interventionDropdown = createColorsDropdown(interventionColorChoices);
		interventionDropdown.setSelectedItem(mainWindow.getColorPreference(AppPreferences.TAG_COLOR_STRATEGY));
		interventionDropdown.addActionListener(this);
		panel.add(interventionDropdown);

		panel.add(new UiLabel(EAM.text("Direct Threat (Pink)")));
		directThreatDropdown = createColorsDropdown(directThreatColorChoices);
		directThreatDropdown.setSelectedItem(mainWindow.getColorPreference(AppPreferences.TAG_COLOR_DIRECT_THREAT));
		directThreatDropdown.addActionListener(this);
		panel.add(directThreatDropdown);

		panel.add(new UiLabel(EAM.text("Contributing Factor (Orange)")));
		indirectFactorDropdown = createColorsDropdown(indirectFactorColorChoices);
		indirectFactorDropdown.setSelectedItem(mainWindow.getColorPreference(AppPreferences.TAG_COLOR_CONTRIBUTING_FACTOR));
		indirectFactorDropdown.addActionListener(this);
		panel.add(indirectFactorDropdown);

		panel.add(new UiLabel(EAM.text("Target (Light Green)")));
		targetDropdown = createColorsDropdown(targetColorChoices);
		targetDropdown.setSelectedItem(mainWindow.getColorPreference(AppPreferences.TAG_COLOR_TARGET));
		targetDropdown.addActionListener(this);
		panel.add(targetDropdown);

		panel.add(new UiLabel(EAM.text("Project Scope (Dark Green)")));
		scopeDropdown = createColorsDropdown(scopeColorChoices);
		scopeDropdown.setSelectedItem(mainWindow.getColorPreference(AppPreferences.TAG_COLOR_SCOPE));
		scopeDropdown.addActionListener(this);
		panel.add(scopeDropdown);
		
		panel.add(new UiLabel(EAM.text("Intermediate Result (Blue)")));
		intermediateResultDropDown = createColorsDropdown(intermediateResultChoices);
		intermediateResultDropDown.setSelectedItem(mainWindow.getColorPreference(AppPreferences.TAG_COLOR_INTERMEDIATE_RESULT));
		intermediateResultDropDown.addActionListener(this);
		panel.add(intermediateResultDropDown);
		
		panel.add(new UiLabel(EAM.text("Threat Reduction Result (Purpule)")));
		threatReductionResultDropDown = createColorsDropdown(threatReductionResultChoices);
		threatReductionResultDropDown.setSelectedItem(mainWindow.getColorPreference(AppPreferences.TAG_COLOR_THREAT_REDUCTION_RESULT));
		threatReductionResultDropDown.addActionListener(this);
		panel.add(threatReductionResultDropDown);
		
		panel.add(new UiLabel(EAM.text(" ")));
		panel.add(new UiLabel(EAM.text(" ")));

		panel.add(new UiLabel(EAM.text("Show Diagram Grid")));
		gridVisibleCheckBox = new UiCheckBox();
		gridVisibleCheckBox.setSelected(mainWindow.getBooleanPreference(AppPreferences.TAG_GRID_VISIBLE));
		gridVisibleCheckBox.addActionListener(this);
		panel.add(gridVisibleCheckBox);

		
		panel.add(new UiLabel(EAM.text("Show Ratings in Cell")));
		cellRatingsVisibleCheckBox = new UiCheckBox();
		cellRatingsVisibleCheckBox.setSelected(mainWindow.getBooleanPreference(AppPreferences.TAG_CELL_RATINGS_VISIBLE));
		cellRatingsVisibleCheckBox.addActionListener(this);
		panel.add(cellRatingsVisibleCheckBox);
		
		return panel;
	}

	private UiComboBox createColorsDropdown(Color[] colorChoices)
	{
		UiComboBox dropdown = new UiComboBox(colorChoices);
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
		
		mainWindow.setBooleanPreference(AppPreferences.TAG_CELL_RATINGS_VISIBLE, cellRatingsVisibleCheckBox.isSelected());

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

	MainWindow mainWindow;
	UiComboBox interventionDropdown;
	UiComboBox directThreatDropdown;
	UiComboBox indirectFactorDropdown;
	UiComboBox targetDropdown;
	UiComboBox scopeDropdown;
	UiComboBox intermediateResultDropDown;
	UiComboBox threatReductionResultDropDown;
	UiCheckBox gridVisibleCheckBox; 
	UiCheckBox cellRatingsVisibleCheckBox;

	static final Color[] interventionColorChoices = {new Color(255, 255, 0), new Color(240, 240, 0), new Color(255, 255, 128)};
	static final Color[] directThreatColorChoices = {new Color(255, 150, 150), new Color(255, 128, 128), new Color(220, 150, 150), new Color(255, 200, 200)};
	static final Color[] indirectFactorColorChoices = {new Color(255, 190, 0), new Color(255, 128, 0), new Color(200, 128, 0), new Color(255, 220, 0), new Color(255, 190, 64), new Color(255, 240, 200)};
	static final Color[] targetColorChoices = {new Color(153, 255, 153), new Color(200, 255, 200), new Color(80, 255, 80), new Color(64, 220, 64)};
	static final Color[] scopeColorChoices = {new Color(0, 255, 0), new Color(128, 255, 128), new Color(0, 220, 0), new Color(0, 180, 0), new Color(0, 128, 0)};
	static final Color[] intermediateResultChoices = {new Color(150, 150, 255), new Color(130, 130, 235), new Color(110, 110, 215), new Color(90, 90, 195)};
	static final Color[] threatReductionResultChoices = {new Color(222, 100, 255), new Color(202, 80, 235), new Color(182, 60, 215), new Color(162, 40, 195)};
}
