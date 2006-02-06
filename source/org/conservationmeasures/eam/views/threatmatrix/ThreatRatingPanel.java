/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import org.martus.swing.ParagraphLayout;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;
import org.martus.swing.UiParagraphPanel;
import org.martus.swing.UiTextField;

public class ThreatRatingPanel extends UiParagraphPanel
{
	public ThreatRatingPanel()
	{
		Object[] ratings = new Object[] {
			"HIGH",
			"MEDIUM",
			"LOW",
		};
		add(new UiLabel("Threat:"), ParagraphLayout.NEW_PARAGRAPH);
		add(new UiLabel("Fishing"));
		add(new UiLabel("Target:"), ParagraphLayout.NEW_PARAGRAPH);
		add(new UiLabel("Coral reefs"));
		addBlankLine();
		add(new UiLabel("Scope:"), ParagraphLayout.NEW_PARAGRAPH);
		add(new UiComboBox(ratings));
		add(new UiLabel("Severity:"), ParagraphLayout.NEW_PARAGRAPH);
		add(new UiComboBox(ratings));
		add(new UiLabel("Urgency:"), ParagraphLayout.NEW_PARAGRAPH);
		add(new UiComboBox(ratings));
		add(new UiLabel("Rationale:"), ParagraphLayout.NEW_PARAGRAPH);
		add(new UiTextField(40));
		addBlankLine();
		add(new UiLabel("Rating:"), ParagraphLayout.NEW_PARAGRAPH);
		add(new JLabel("HIGH"));
		setBorder(new LineBorder(Color.BLACK));
	}
}
