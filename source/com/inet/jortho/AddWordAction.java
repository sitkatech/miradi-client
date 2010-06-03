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

public class AddWordAction extends AbstractAction
{
	private Dictionary dictionary;
	
	public AddWordAction()
	{
		this(Utils.getResource("addToDictionary"));
	}
	
	public AddWordAction(String labelText)
	{
		super(labelText);
	}
	
	public void actionPerformed(ActionEvent arg0)
	{
//        UserDictionaryProvider provider = SpellChecker.getUserDictionaryProvider();
//        if( provider != null ) {
//            provider.addWord( oldWord );
//        }
//        dictionary.add( oldWord );
        dictionary.trimToSize();
	}

}
