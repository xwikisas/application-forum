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
import org.openqa.selenium.support.ui.Select;
import org.xwiki.test.ui.po.BaseElement;

/**
 * Represents the Topic edit elements. This po is shared by TopicAddElement and TopicEditPage
 * 
 * @version $Id$
 * @since 1.9.4
 */
public class FlagEditElement extends BaseElement
{
    @FindBy(id = "ForumCode.FlagClass_0_reason")
    private WebElement reason;

    @FindBy(id = "ForumCode.FlagClass_0_message")
    private WebElement message;

    @FindBy(css = ".button[type=submit]")
    private WebElement submitButton;

    public void setReason()
    {
        Select select = new Select(reason);
        select.selectByIndex(3);
    }

    public WebElement getMessage()
    {
        return message;
    }

    public void setMessage(String flagMessage)
    {
        message.clear();
        message.sendKeys(flagMessage);
    }

    public void clickSubmit()
    {
        submitButton.click();

    }
}
