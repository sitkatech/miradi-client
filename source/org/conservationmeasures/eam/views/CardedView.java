/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views;

import java.awt.CardLayout;
import java.awt.Component;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

abstract public class CardedView extends UmbrellaView
{
	public CardedView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		
		cardLayout = new CardLayout();
		setLayout(cardLayout);
	}

	public void addCard(Component cardToAdd, String cardName)
	{
		add(cardToAdd, cardName);
	}
	
	public void showCard(String cardName)
	{
		cardLayout.show(this, cardName);
	}
	
	private CardLayout cardLayout;
}
