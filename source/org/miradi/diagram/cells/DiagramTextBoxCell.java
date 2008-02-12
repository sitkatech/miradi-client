/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram.cells;

import java.awt.Color;

import org.miradi.diagram.DiagramConstants;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.TextBox;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.DiagramFactorBackgroundQuestion;

public class DiagramTextBoxCell extends FactorCell
{
	public DiagramTextBoxCell(TextBox cmFactor, DiagramFactor diagramFactorToUse)
	{
		super(cmFactor, diagramFactorToUse);
		
		diagramFactorBackgroundQuestion = new DiagramFactorBackgroundQuestion();
	}

	public Color getColor()
	{
		ChoiceItem choiceItem = diagramFactorBackgroundQuestion.findChoiceByCode(getDiagramFactor().getBackgroundColor());
		if (choiceItem == null)
			return DiagramConstants.TEXT_BOX_COLOR;
		
		return choiceItem.getColor();
	}
	
	private DiagramFactorBackgroundQuestion diagramFactorBackgroundQuestion;
}
