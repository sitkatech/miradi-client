/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Color;
import java.util.Random;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.main.NodePropertiesDialog;
import org.conservationmeasures.eam.objects.ThreatPriority;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.BasicGridLayout;

public class ThreatRatingPanel extends JPanel
{
	public ThreatRatingPanel()
	{
		Box scopeCell = Box.createVerticalBox();
		scopeCell.add(new UiLabel("Scope:"));
		scopeCell.add(createRatingDropdown());
		scopeCell.setBorder(new LineBorder(Color.BLACK, 3));
		
		Box severityCell = Box.createVerticalBox();
		severityCell.add(new UiLabel("Severity:"));
		severityCell.add(createRatingDropdown());
		severityCell.setBorder(new LineBorder(Color.BLACK, 3));
		
		Box urgencyCell = Box.createVerticalBox();
		urgencyCell.add(new UiLabel("Urgency:"));
		urgencyCell.add(createRatingDropdown());
		urgencyCell.setBorder(new LineBorder(Color.BLACK, 3));
		
		Box extraCell = Box.createVerticalBox();
		extraCell.add(new UiLabel("Criterion 4:"));
		extraCell.add(createRatingDropdown());
		extraCell.setBorder(new LineBorder(Color.BLACK, 3));

		
		ThreatPriority priority = getRandomPriority();
		UiLabel ratingLabel = new UiLabel();
		ratingLabel.setText(priority.toString());
		ratingLabel.setBackground(Project.getPriorityColor(priority));
		JPanel ratingPanel = new JPanel();
		ratingPanel.add(ratingLabel);
		ratingPanel.setBorder(new LineBorder(Color.BLACK, 3));
		ratingPanel.setBackground(Project.getPriorityColor(priority));

		JPanel top = new JPanel();
		top.setLayout(new BasicGridLayout(2, 2));
		top.add(scopeCell);
		top.add(severityCell);
		
		top.add(urgencyCell);
		top.add(extraCell);

		Box vBox = Box.createVerticalBox();
		vBox.add(top);
		vBox.add(ratingPanel);
		
		add(vBox);
	}

	private UiComboBox createRatingDropdown()
	{
		UiComboBox dropDown = NodePropertiesDialog.createThreatDropDown();
		int choice = new Random().nextInt(dropDown.getItemCount());
		dropDown.setSelectedIndex(choice);
		return dropDown;
	}
	
	private ThreatPriority getRandomPriority()
	{
		return ThreatPriority.createFromInt(new Random().nextInt(4));
	}
}
