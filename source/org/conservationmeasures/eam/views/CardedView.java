/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
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
