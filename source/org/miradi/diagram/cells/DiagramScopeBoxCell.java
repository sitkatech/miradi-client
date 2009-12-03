/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.objects.DiagramFactor;
import org.miradi.objects.ScopeBox;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ScopeBoxColorQuestion;

public class DiagramScopeBoxCell extends FactorCell
{
	public DiagramScopeBoxCell(ScopeBox scopeBox, DiagramFactor diagramFactorToUse)
	{
		super(scopeBox, diagramFactorToUse);
	}

	@Override
	public Color getColor()
	{
		ScopeBox scopeBox = (ScopeBox) getWrappedFactor();
		ChoiceQuestion colorQuestion = scopeBox.getProject().getQuestion(ScopeBoxColorQuestion.class);
		String scopeBoxColorCode = scopeBox.getScopeBoxColorCode();
		ChoiceItem currentScopeBoxChoice = colorQuestion.findChoiceByCode(scopeBoxColorCode);
		
		return currentScopeBoxChoice.getColor();
	}
	
	@Override
	public boolean isScopeBox()
	{
		return true;
	}
}
