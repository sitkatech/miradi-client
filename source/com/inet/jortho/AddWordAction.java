/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package com.inet.jortho;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

public class AddWordAction extends AbstractAction
{
	private Dictionary dictionary;
	private String word;
	private JTextComponent jText;
	
	public AddWordAction(JTextComponent jTextComponent, String wordToAdd)
	{
		this(jTextComponent, wordToAdd, Utils.getResource("addToDictionary"));
	}
	
	public AddWordAction(JTextComponent jTextComponent, String wordToAdd, String labelText)
	{
		super(labelText);
		dictionary = SpellChecker.getCurrentDictionary();
		word = wordToAdd;
		jText = jTextComponent;
	}
	
	public void actionPerformed(ActionEvent arg0)
	{
        UserDictionaryProvider provider = SpellChecker.getUserDictionaryProvider();
        if( provider != null ) {
            provider.addWord( word );
        }
        dictionary.add( word );
        dictionary.trimToSize();
        AutoSpellChecker.refresh( jText );
	}

}
