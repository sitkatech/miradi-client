/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.DiagramConstants;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.TextBox;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.DiagramFactorBackgroundQuestion;

public class DiagramTextBoxCell extends FactorCell
{
	public DiagramTextBoxCell(TextBox cmFactor, DiagramFactor diagramFactorToUse)
	{
		super(cmFactor, diagramFactorToUse);
		
		diagramFactorBackgroundQuestion = new DiagramFactorBackgroundQuestion(DiagramFactor.TAG_BACKGROUND_COLOR);
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
