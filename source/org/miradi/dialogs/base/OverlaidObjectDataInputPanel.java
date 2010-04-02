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
package org.miradi.dialogs.base;

import java.awt.CardLayout;
import java.awt.Rectangle;

import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public abstract class OverlaidObjectDataInputPanel extends AbstractObjectDataInputPanel
{
	public OverlaidObjectDataInputPanel(MainWindow mainWindowToUse, ORef orefToUse)
	{
		super(mainWindowToUse.getProject(), orefToUse);
		
		cardLayout = new CardLayout();
		setLayout(cardLayout);
	}

	protected void addPanel(AbstractObjectDataInputPanel panelToAdd)
	{
		addSubPanel(panelToAdd);
		add(panelToAdd, panelToAdd.getPanelDescription());
	}
	
	protected void deactivateCurrentCard()
	{
		if (currentCard != null)
			currentCard.becomeInactive();
	}

	protected void activateCurrentCard()
	{
		if (currentCard != null)
			currentCard.becomeActive();
	}

	@Override
	public void becomeActive()
	{
		super.becomeActive();
		isActive = true;
		activateCurrentCard();
	}

	@Override
	public void becomeInactive()
	{
		deactivateCurrentCard();
		isActive = false;
		super.becomeInactive();
	}

	@Override
	public void setFocusOnFirstField()
	{
		currentCard.setFocusOnFirstField();
	}
	
	protected boolean isMultiPropertiesPanelActive()
	{
		return isActive;
	}
	
	@Override
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);

		deactivateCurrentCard();
		
		currentCard = findPanel(orefsToUse);
		cardLayout.show(this, currentCard.getPanelDescription());
		if (isMultiPropertiesPanelActive())
			activateCurrentCard();
		
		scrollRectToVisible(new Rectangle(0,0,0,0));
		
		// NOTE: The following are an attempt to fix a reported problem 
		// where the screen was not fully repainted when switching objects
		// This code is duplicated in PlanningTreePropertiesPanel.java
		// and in TargetViabilityTreePropertiesPanel.java
		validate();
		repaint();
	}


		
	abstract protected AbstractObjectDataInputPanel findPanel(ORef[] orefsToUse);

	private boolean isActive;
	private CardLayout cardLayout;
	protected AbstractObjectDataInputPanel currentCard;
}
