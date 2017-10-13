/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.main;

import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.LocationHolder;
import org.miradi.utils.XmlUtilities2;

public class MiradiHtmlMenuItem extends JMenuItem implements LocationHolder
{
    public MiradiHtmlMenuItem(Action action, int mnemonic, KeyStroke accelerator)
    {
        this(action, mnemonic);
        setAccelerator(accelerator);
    }
    
    public MiradiHtmlMenuItem(Action action, KeyStroke accelerator)
    {
        this(action);
        setAccelerator(accelerator);
    }
    
    public MiradiHtmlMenuItem(Action action, int mnemonic)
    {
        this(action);
        setMnemonic(mnemonic);
    }
    
    public MiradiHtmlMenuItem(Action action)
    {
        super(action);
    }
    
	public boolean hasLocation()
	{
		return false;
	}

    @Override
    public String getText()
    {
        // TODO: there were problems with html in menus (markup visible on MacOS, items with no action not looking inactivated) so enter this convoluted workaround
        String htmlText = XmlUtilities2.convertXmlTextToHtml(super.getText());
        String taglessText = HtmlUtilities.stripAllHtmlTags(htmlText);
        String unescapedText = XmlUtilities2.getXmlDecoded(taglessText);
        return unescapedText;
    }

	final static KeyStroke KEY_CTL_P = KeyStroke.getKeyStroke(KeyEvent.VK_P ,KeyEvent.CTRL_MASK,true);
}
