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
package org.xwiki.contrib.forum.test.ui;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.xwiki.contrib.forum.test.po.*;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.panels.test.po.ApplicationsPanel;
import org.xwiki.test.ui.AbstractTest;
import org.xwiki.test.ui.SuperAdminAuthenticationRule;
import org.xwiki.test.ui.po.ViewPage;

/**
 * UI tests for the Forum application.
 *
 * @version $Id$
 * @since 1.9.4
 */
public class ForumsTest extends AbstractTest
{
    @Rule
    public SuperAdminAuthenticationRule authenticationRule = new SuperAdminAuthenticationRule(getUtil());

    private static final String FORUM_SPACE = "Forums";

    private static final String FORUM_TITLE = "MyForum";

    private static final String WEBHOME = "WebHome";

    private static final String FORUM_DESCRIPTION = "MyForum Description";

    private static final String TOPIC_TITLE = "MyTopic";

    private static final String TOPIC_DESCRIPTION = "MyTopic Description";

    private static final String ANSWER = "Hello! This is my answer!";

    private static final String FLAG_MESSAGE = "This message promotes hate or violence.";

    @Test
    public void testApplicationPanelLinksToForumsHomePage()
    {
        ApplicationsPanel applicationPanel = ApplicationsPanel.gotoPage();
        ViewPage vp = applicationPanel.clickApplication(FORUM_SPACE);

        DocumentReference reference = new DocumentReference("wiki", Arrays.asList(FORUM_SPACE), WEBHOME);

        Assert.assertEquals(reference.toString(), String.format("wiki:%s", vp.getMetaDataValue("document")));
    }

    @Test
    public void testCreateForumEntities() throws Exception
    {
        ForumsHomePage forumsHomePage = ForumsHomePage.gotoPage();
        // View Forums homepage tour
        forumsHomePage.viewTour();
        waitUntilTourDisappears();
        // Create new forum
        forumsHomePage.clickAddForumButton();
        forumsHomePage.setAddForumEntryInput(FORUM_TITLE);

        ForumEditPage forumEditPage = forumsHomePage.clickAddForumEntryButton();
        forumEditPage.setDescription(FORUM_DESCRIPTION);
        forumEditPage.clickSaveAndView();

        ForumViewPage forumViewPage = new ForumViewPage();
        Assert.assertEquals(FORUM_TITLE, forumViewPage.getDocumentTitle());
        Assert.assertEquals(FORUM_DESCRIPTION, forumViewPage.getDescription());

        // View Forum tour
        forumViewPage.viewTour();
        waitUntilTourDisappears();
        // Create new topic
        TopicAddElement topicAddForm = forumViewPage.clickAddTopicActivator();
        topicAddForm.getEditForm().setTitle(TOPIC_TITLE);
        topicAddForm.getEditForm().setDescription(TOPIC_DESCRIPTION);
        forumViewPage = topicAddForm.clickAddTopicButton();

        TopicViewPage topicViewPage = TopicViewPage.gotoPage();
        Assert.assertEquals(TOPIC_TITLE, topicViewPage.getDocumentTitle());
        Assert.assertEquals(TOPIC_DESCRIPTION, topicViewPage.getDescription());

        // View Topic tour
        topicViewPage.viewTour();
        waitUntilTourDisappears();

        // Create new answer
        AnswerAddElement answerAddForm = topicViewPage.clickAddAnswerActivator();
        answerAddForm.getEditForm().setAnswer(ANSWER);
        Assert.assertEquals(1, topicViewPage.getAnswersNumber());

        // Flag the answer
        FlagAddElement flagAddForm = topicViewPage.flagAnswer();
        flagAddForm.getEditForm().setReason();
        flagAddForm.getEditForm().setMessage(FLAG_MESSAGE);
        flagAddForm.getEditForm().clickSubmit();

        FlagViewPage flagViewPage = FlagViewPage.goToPage();
        TopicViewPage flagTopicView = flagViewPage.clickTargetURL();
        Assert.assertEquals(true, flagTopicView.checkFlaggedAnswer());

        cleanUp(FORUM_TITLE);
    }

    private void cleanUp(String page) throws Exception
    {
        ForumsHomePage forumsHomePage = ForumsHomePage.gotoPage();
        forumsHomePage.deleteForumPage(page);

        // Verify that the forum has been deleted.
        Assert.assertTrue(forumsHomePage.getNotification().contains("Forum deleted"));

    }

    private void waitUntilTourDisappears()
    {
        // Waiting for all the tour elements to disappear instead of the last one because the order varies.
        getDriver().waitUntilElementDisappears(By.className("tour-backdrop"));
        getDriver().waitUntilElementDisappears(By.className("tour-step-background"));
        getDriver().waitUntilElementDisappears(By.cssSelector("id^='step-'"));

    }
}
