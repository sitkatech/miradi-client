/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.diagram.cells;

import java.awt.Color;

import org.miradi.diagram.DiagramConstants;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.TextBox;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.DiagramFactorBackgroundQuestion;

public class DiagramTextBoxCell extends FactorCell
{
	public DiagramTextBoxCell(TextBox cmFactor, DiagramFactor diagramFactorToUse)
	{
		super(cmFactor, diagramFactorToUse);
		
		diagramFactorBackgroundQuestion = cmFactor.getProject().getQuestion(DiagramFactorBackgroundQuestion.class);
	}

	public Color getColor()
	{
		ChoiceItem choiceItem = diagramFactorBackgroundQuestion.findChoiceByCode(getDiagramFactor().getBackgroundColor());
		if (choiceItem == null)
			return DiagramConstants.TEXT_BOX_COLOR;
		
		return choiceItem.getColor();
	}
	
	private ChoiceQuestion diagramFactorBackgroundQuestion;
}
