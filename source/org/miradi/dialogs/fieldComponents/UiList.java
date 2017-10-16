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
package org.miradi.dialogs.fieldComponents;


import org.martus.swing.UiLanguageDirection;

import javax.swing.JList;
import java.util.Vector;

public class UiList<E> extends JList<E>
{

    public UiList(E[] list)
    {
        super(list);
        initialize();
    }

    public UiList(Vector<E> list)
    {
        super(list);
        initialize();
    }

    private void initialize()
    {
        setComponentOrientation(UiLanguageDirection.getComponentOrientation());
    }

}
