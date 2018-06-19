/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.contrib.forum.test.po;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.test.ui.po.BaseElement;

/**
 * Represents the Answer add elements.
 * 
 * @version $Id$
 * @since 2.1
 */
public class FlagViewPage extends BaseElement
{
    @FindBy(css = ".answers answer:first-child .flag")
    private WebElement flagButton;

    // Hard to find a proper selector for this element.
    @FindBy(xpath = "//a[text() = 'View Flag Source']")
    private WebElement target;

    public static FlagViewPage goToPage()
    {
        DocumentReference reference = new DocumentReference("wiki", "Flags", "Flag");
        getUtil().gotoPage(reference);
        return new FlagViewPage();
    }

    public TopicViewPage clickTargetURL()
    {
        String url = target.getAttribute("href");
        target.click();
        return TopicViewPage.gotoPage(url);
    }
}
