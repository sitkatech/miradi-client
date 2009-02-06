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
package org.miradi.views;

import java.awt.CardLayout;
import java.awt.Component;

import org.miradi.main.MainWindow;
import org.miradi.views.umbrella.UmbrellaView;

abstract public class CardedView extends UmbrellaView
{
	public CardedView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		
		cardLayout = new CardLayout();
		setLayout(cardLayout);
	}
	
	public void becomeActive() throws Exception
	{
		super.becomeActive();
		createCards();
		showCurrentCard(getCurrentCardChoiceName());
		forceLayoutSoSplittersWork();
	}
	
	public void becomeInactive() throws Exception
	{
		super.becomeInactive();
		deleteCards();
	}
	
	public void addCard(Component cardToAdd, String cardName)
	{
		add(cardToAdd, cardName);
	}
	
	public void showCard(String cardName)
	{
		cardLayout.show(this, cardName);
	}
	
	abstract protected void showCurrentCard(String code);
		
	abstract protected void createCards() throws Exception;
	
	abstract protected String getCurrentCardChoiceName();
	
	abstract public void deleteCards() throws Exception;
	
	private CardLayout cardLayout;
}
