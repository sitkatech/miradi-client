/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.conservationmeasures.eam.utils.HtmlViewer;
import org.conservationmeasures.eam.utils.HyperlinkHandler;

public class DiagramWizardOverviewStep extends JPanel implements HyperlinkHandler
{
	public DiagramWizardOverviewStep() 
	{
		super(new BorderLayout());
		htmlViewer = new HtmlViewer("", this);
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		add(scrollPane);
	}

	public void refresh() throws Exception
	{
		String htmlText = OverviewText.build();
		htmlViewer.setText(htmlText);
		invalidate();
		validate();
	}

	boolean save() throws Exception
	{
		return true;
	}

	public void buttonPressed(String buttonName)
	{
	}

	public void linkClicked(String linkDescription)
	{
	}

	public void valueChanged(String widget, String newValue)
	{
	}

	HtmlViewer htmlViewer;

}

class OverviewText extends HtmlBuilder
{
	public static String build()
	{
		return font("Arial", 
			table(tableRow(
				tableCell(
						heading("Diagram") + 
						paragraph("The diagram below...") +
						paragraph(bold("Click next to continue with the modeling process.")) +
						newline() +
						indent(table(
							tableRow(
								tableCell(button("Back", "&lt; Previous")) +
								tableCell("&nbsp;") +
								tableCell(button("Next", "Next &gt;")) 
								)
							)) + 
						newline() +
						"") +
				tableCell(
						smallHeading("Guide to the Conceptual Model Diagram") + 
						smallParagraph("Each rectangle in the diagram below represents...") + 
					"")
				))
			);

	}
}
