/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram.wizard;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;

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
		EAM.okDialog("Not implemented yet", new String[] {"Not implemented yet"});
	}

	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("Definition:ConceptualModel"))
		{
			EAM.okDialog("Definition: Conceptual Model", new String[] {"A conceptual model is..."});
		}
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
						paragraph("This view enables you to develop a " +
								definition("Definition:ConceptualModel", "conceptual model", "A conceptual model is...") +
								" of your project.  " +
								"A good conceptual model graphically depicts your project's targets, " +
								"direct threats, indirect factors, and interventions--and more importantly, " +
								"the relationships between them.") +
						paragraph("As in any modeling exercise, you should not try to show all factors and " +
								"relationships, but only the most important ones.") +
						paragraph("The Diagram interview is not functional yet, so you might want to " +
								"shrink this interview panel by dragging its lower border upward.") +
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
						smallHeading("Navigation Hints for the Diagram View") + 
						smallParagraph("To add a new target or other factor, " +
								"click the appropriate button on the tool bar " +
								"(hexagon, rectangle, or ellipse), " +
								"or use the drop down menu, or the right mouse button.  " +
								"The new factor will appear in the center of the page. " +
								" If a factor is selected, you can then use " +
								"the mouse or arrow keys to move it wherever you like.") + 
						smallParagraph("Double clicking on a factor will bring up a menu " +
								"in which you can name the factor, change its type, " +
								"link an indicator or objective to it, or type a comment.") + 
						smallParagraph("To link factors, indicating a causal relationship, " +
								"click on the first, then hold the shift key and then click on the second.  " +
								"Then click the toolbar relationship button (the line with an arrow) " +
								"to create the link.") + 
						smallParagraph("The zoom-in and zoom-out buttons can be used to " +
								"decrease or increase the size of the view.") + 
						smallParagraph("Note that the scope box automatically forms around " +
								"whatever targets you have active on the page. ") + 
					"")
				))
			);

	}
}
