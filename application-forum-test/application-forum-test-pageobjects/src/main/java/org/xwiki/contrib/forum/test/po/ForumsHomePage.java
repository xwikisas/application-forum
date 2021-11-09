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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.test.ui.po.LiveTableElement;
import org.xwiki.test.ui.po.ViewPage;

/**
 * Represents actions that can be done on the Forum.WebHome page.
 *
 * @version $Id$
 * @since 1.9.4
 */
public class ForumsHomePage extends ViewPage
{
    @FindBy(css = "#actionBox a")
    private WebElement addForumButton;

    @FindBy(xpath = "//div[@id = 'entryNamePopup']//input[@type = 'text']")
    private WebElement addForumEntryInput;

    @FindBy(xpath = "//div[@id = 'entryNamePopup']//input[@type = 'image']")
    private WebElement addForumEntryButton;

    @FindBy(xpath = "//tbody[@id='forums-display']//tr")
    private List<WebElement> forums;

    @FindBy(className = "xnotification-done")
    private WebElement notification;

    /**
     * @return the Forums homepage
     */
    public static ForumsHomePage gotoPage()
    {
        DocumentReference reference = new DocumentReference("wiki", "Forums", "WebHome");
        getUtil().gotoPage(reference);
        return new ForumsHomePage();
    }

    public ForumsHomePage()
    {
        // Make sure the forums live table is ready.
        getForumsLiveTable().waitUntilReady();
    }

    public LiveTableElement getForumsLiveTable()
    {
        return new LiveTableElement("forums");
    }

    /**
     * Opens the modal to enter new Forum entry name.
     */
    public void clickAddForumButton()
    {
        addForumButton.click();
    }

    /**
     * @param forumName the name of the new Forum entry
     */
    public void setAddForumEntryInput(String forumName)
    {
        this.addForumEntryInput.clear();
        this.addForumEntryInput.sendKeys(forumName);
    }

    /**
     * @return the new Forum entry page
     */
    public ForumEditPage clickAddForumEntryButton()
    {
        this.addForumEntryButton.click();
        return new ForumEditPage();
    }

    /**
     * Navigates through the Forums home page tour.
     */
    public void viewTour()
    {
        // Go through the tour steps.
        for (int i = 0; i < 3; i++) {
            getDriver().findElementById("bootstrap_tour_next").click();
        }
        // Finish the tour.
        getDriver().findElementById("bootstrap_tour_end").click();
    }

    /**
     * Deletes a forum from livetable.
     * 
     * @param forumName the name of the forum to be deleted
     */
    public void deleteForumPage(String forumName)
    {
        for (WebElement row : forums) {
            WebElement name = row.findElement(By.xpath("//td[contains(@class, 'doc_title')]/a"));
            if (forumName.equals(name.getText())) {
                row.findElement(By.xpath("//a[contains(@class, 'deleteforum') and contains(@space, 'Forums.MyForum')]"))
                    .click();
                getDriver().findElement(By.cssSelector(".xdialog-box-confirmation [value = 'Yes']")).click();
            }
        }
    }

    /**
     * @return the value of the notification
     */
    public String getNotification()
    {
        return notification.getText();
    }
}
