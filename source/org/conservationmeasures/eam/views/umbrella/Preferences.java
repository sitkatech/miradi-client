/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.DialogGridPanel;
import org.conservationmeasures.eam.views.Doer;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;
import org.martus.swing.Utilities;

public class Preferences extends Doer
{

	public boolean isAvailable()
	{
		return true;
	}

	public void doIt() throws CommandFailedException
	{		
		String title = EAM.text("Title|Preferences");
		showPreferencesDialog(title, headerText);
	}
	
	void showPreferencesDialog(String title, String header)
	{
		JDialog dlg = new JDialog(EAM.mainWindow, title);
		dlg.setModal(true);
		
		Box textBox = Box.createHorizontalBox();
		textBox.add(Box.createHorizontalGlue());
		JLabel bodyComponent = new JLabel(header);
		textBox.add(bodyComponent);
		bodyComponent.setFont(Font.getFont("Arial"));
		textBox.add(Box.createHorizontalGlue());

		JButton ok = new JButton(new OkAction(dlg));
		JButton cancel = new JButton(new CancelAction(dlg));
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(ok);
		buttonBox.add(cancel);
		buttonBox.add(Box.createHorizontalGlue());
		
		Box box = Box.createVerticalBox();
		box.add(textBox);
		box.add(new UiLabel("Choose the colors that look best on your system"));
		box.add(createColorPreferencesPanel());
		box.add(buttonBox);
		box.add(Box.createVerticalGlue());
		JPanel panel = (JPanel)dlg.getContentPane();
		panel.add(box);
		dlg.pack();
		dlg.setLocation(Utilities.center(dlg.getSize(), Utilities.getViewableRectangle()));
		
		dlg.getRootPane().setDefaultButton(ok);
		ok.requestFocus(true);
		dlg.setVisible(true);
		
	}
	
	DialogGridPanel createColorPreferencesPanel()
	{
		DialogGridPanel panel = new DialogGridPanel();
		
		panel.add(new UiLabel("Intervention (Yellow)"));
		panel.add(createColorsDropdown(interventionColorChoices));
		panel.add(new UiLabel("Direct Threat (Pink)"));
		panel.add(createColorsDropdown(directThreatColorChoices));
		panel.add(new UiLabel("Indirect Factor (Orange)"));
		panel.add(createColorsDropdown(indirectFactorColorChoices));
		panel.add(new UiLabel("Target (Light Green)"));
		panel.add(createColorsDropdown(targetColorChoices));
		
		return panel;
	}

	private UiComboBox createColorsDropdown(Color[] colorChoices)
	{
		UiComboBox dropdown = new UiComboBox(colorChoices);
		
		dropdown.setRenderer(new ColorItemRenderer());
				
		return dropdown;
	}
	
	static class ColorItemRenderer extends UiLabel implements ListCellRenderer
	{

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			setIcon(new ColorIcon((Color)value));
			return this;
		}
		
	}
	
	static class ColorIcon implements Icon
	{
		public ColorIcon(Color colorToUse)
		{
			color = colorToUse;
		}

		public int getIconHeight()
		{
			return 16;
		}

		public int getIconWidth()
		{
			return 16;
		}

		public void paintIcon(Component c, Graphics g, int x, int y)
		{
			g.setColor(color);
			g.fillRect(0, 0,getIconWidth(), getIconHeight());
		}
		
		Color color;
	}

	static class OkAction extends AbstractAction
	{
		public OkAction(JDialog dialogToClose)
		{
			super("OK");
			dlg = dialogToClose;
		}
		
		public void actionPerformed(ActionEvent arg0)
		{
			dlg.dispose();
		}
		
		JDialog dlg;
	}

	static class CancelAction extends AbstractAction
	{
		public CancelAction(JDialog dialogToClose)
		{
			super("Cancel");
			dlg = dialogToClose;
		}
		
		public void actionPerformed(ActionEvent arg0)
		{
			dlg.dispose();
		}
		
		JDialog dlg;
	}

	static final String headerText = "<html><H2>e-Adaptive Management Preferences</H2></html>";
	static final Color[] interventionColorChoices = {new Color(255, 255, 0), new Color(255, 255, 128)};
	static final Color[] directThreatColorChoices = {new Color(255, 150, 150), new Color(255, 100, 150)};
	static final Color[] indirectFactorColorChoices = {new Color(255, 190, 0), new Color(255, 150, 0)};
	static final Color[] targetColorChoices = {new Color(153, 255, 153), new Color(80, 255, 80), new Color(200, 255, 200)};
}
