/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.utils;

import org.miradi.main.MiradiTestCase;

public class TestEditableHtmlPane extends MiradiTestCase
{
	public TestEditableHtmlPane(String name)
	{
		super(name);
	}
	
	public void testPrepareForSaving()
	{
		String htmlText = 
		  "<html>\n" +
		  "<head>\n" +
		  "	</head>\n" +
		  "	  <body>\n" +
		  "		<div>\n" +
		  "	 	 text on line 1 \n" +
		  "	 	 text on line 2\n" +
		  "     </div>\n" +
		  "     <div>\n" +
		  "      text on line 3\n" +
		  "		</div>\n" +
		  "	  </body>\n" +
		  "	</html>\n";

		assertEquals("wrong new lines inserted?", "text on line 1 text on line 2<br/>text on line 3", EditableHtmlPane.prepareForSaving(htmlText));
	}
}
