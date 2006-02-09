/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Random;

import org.conservationmeasures.eam.icons.ThreatPriorityIcon;
import org.conservationmeasures.eam.main.NodePropertiesDialog;
import org.conservationmeasures.eam.objects.ThreatPriority;
import org.martus.swing.ParagraphLayout;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;
import org.martus.swing.UiParagraphPanel;
import org.martus.swing.UiTextField;

public class ThreatRatingPanel extends UiParagraphPanel
{
	public ThreatRatingPanel()
	{
//		add(new UiLabel("Threat:"), ParagraphLayout.NEW_PARAGRAPH);
//		add(new UiLabel("Fishing"));
//		add(new UiLabel("Target:"), ParagraphLayout.NEW_PARAGRAPH);
//		add(new UiLabel("Coral reefs"));
//		addBlankLine();
		add(new UiLabel("Scope:"), ParagraphLayout.NEW_PARAGRAPH);
		add(createRatingDropdown());
		add(new UiLabel("Severity:"), ParagraphLayout.NEW_PARAGRAPH);
		add(createRatingDropdown());
		add(new UiLabel("Urgency:"), ParagraphLayout.NEW_PARAGRAPH);
		add(createRatingDropdown());
		add(new UiLabel("Rationale:"), ParagraphLayout.NEW_PARAGRAPH);
		add(new UiTextField(15));
		addBlankLine();
		add(new UiLabel("Rating:"), ParagraphLayout.NEW_PARAGRAPH);
		UiLabel ratingLabel = new UiLabel();
		ratingLabel.setText("High");
		ratingLabel.setIcon(new ThreatPriorityIcon(ThreatPriority.createPriorityHigh()));
		add(ratingLabel);
	}

	private UiComboBox createRatingDropdown()
	{
		UiComboBox dropDown = NodePropertiesDialog.createThreatDropDown();
		int choice = new Random().nextInt(dropDown.getItemCount());
		dropDown.setSelectedIndex(choice);
		return dropDown;
	}
}
