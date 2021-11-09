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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.xwiki.contrib.forum.test.po.AnswerAddElement;
import org.xwiki.contrib.forum.test.po.FlagAddElement;
import org.xwiki.contrib.forum.test.po.FlagViewPage;
import org.xwiki.contrib.forum.test.po.ForumEditPage;
import org.xwiki.contrib.forum.test.po.ForumViewPage;
import org.xwiki.contrib.forum.test.po.ForumsHomePage;
import org.xwiki.contrib.forum.test.po.TopicAddElement;
import org.xwiki.contrib.forum.test.po.TopicViewPage;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.panels.test.po.ApplicationsPanel;
import org.xwiki.test.docker.junit5.UITest;
import org.xwiki.test.ui.TestUtils;
import org.xwiki.test.ui.po.ViewPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UI tests for the Forum application.
 *
 * @version $Id$
 * @since 2.9
 */
@UITest
class ForumIT
{
    private static final String FORUM_SPACE = "Forums";

    private static final String FORUM_TITLE = "MyForum";

    private static final String WEBHOME = "WebHome";

    private static final String FORUM_DESCRIPTION = "MyForum Description";

    private static final String TOPIC_TITLE = "MyTopic";

    private static final String TOPIC_DESCRIPTION = "MyTopic Description";

    private static final String FLAG_MESSAGE = "This message promotes hate or violence.";

    @BeforeAll
    void setUp(TestUtils setup)
    {
        setup.loginAsSuperAdmin();
    }

    @Test
    void applicationPanelLinksToForumsHomePage()
    {
        ApplicationsPanel applicationPanel = ApplicationsPanel.gotoPage();
        ViewPage vp = applicationPanel.clickApplication(FORUM_SPACE);

        DocumentReference reference = new DocumentReference("wiki", Arrays.asList(FORUM_SPACE), WEBHOME);

        assertEquals(reference.toString(), String.format("wiki:%s", vp.getMetaDataValue("document")));
    }

    @Test
    void createForumEntities(TestUtils setup) throws Exception
    {
        ForumsHomePage forumsHomePage = ForumsHomePage.gotoPage();
        // View Forums homepage tour.
        forumsHomePage.viewTour();
        waitUntilTourDisappears(setup);
        // Create new forum.
        forumsHomePage.clickAddForumButton();
        forumsHomePage.setAddForumEntryInput(FORUM_TITLE);

        ForumEditPage forumEditPage = forumsHomePage.clickAddForumEntryButton();
        forumEditPage.setDescription(FORUM_DESCRIPTION);
        forumEditPage.clickSaveAndView();

        ForumViewPage forumViewPage = new ForumViewPage();
        assertEquals(FORUM_TITLE, forumViewPage.getDocumentTitle());
        assertEquals(FORUM_DESCRIPTION, forumViewPage.getDescription());

        // View Forum tour.
        forumViewPage.viewTour();
        waitUntilTourDisappears(setup);
        // Create new topic.
        TopicAddElement topicAddForm = forumViewPage.clickAddTopicActivator();
        topicAddForm.getEditForm().setTitle(TOPIC_TITLE);
        topicAddForm.getEditForm().setDescription(TOPIC_DESCRIPTION);
        forumViewPage = topicAddForm.clickAddTopicButton();

        TopicViewPage topicViewPage = TopicViewPage.gotoPage();
        assertEquals(TOPIC_TITLE, topicViewPage.getDocumentTitle());
        assertEquals(TOPIC_DESCRIPTION, topicViewPage.getDescription());

        // View Topic tour.
        topicViewPage.viewTour();
        waitUntilTourDisappears(setup);

        // Create multiple answers.
        createAnswer(topicViewPage, "Hello\nworld!\n!@#$%^&*()_\"';:`~");
        createAnswer(topicViewPage, "Another message");
        assertEquals(2, topicViewPage.getAnswersNumber());

        // Flag the answer.
        FlagAddElement flagAddForm = topicViewPage.flagAnswer();
        flagAddForm.getEditForm().setReason();
        flagAddForm.getEditForm().setMessage(FLAG_MESSAGE);
        flagAddForm.getEditForm().clickSubmit();

        FlagViewPage flagViewPage = FlagViewPage.goToPage();
        TopicViewPage flagTopicView = flagViewPage.clickTargetURL();
        assertEquals(true, flagTopicView.checkFlaggedAnswer());

        // Add multiple comments.
        createComment(flagTopicView, "Hello\\nworld!\\n!@#$%^&*()_\\\"';:`~1");
        createComment(flagTopicView, "Another message");

        cleanUp(FORUM_TITLE);
    }

    private void createComment(TopicViewPage topicViewPage, String comment)
    {
        topicViewPage.addComment(comment);
    }

    void createAnswer(TopicViewPage topicViewPage, String answer)
    {
        AnswerAddElement answerAddForm = topicViewPage.clickAddAnswerActivator();
        answerAddForm.getEditForm().setAnswer(answer);
    }

    private void cleanUp(String page) throws Exception
    {
        ForumsHomePage forumsHomePage = ForumsHomePage.gotoPage();
        forumsHomePage.deleteForumPage(page);

        // Verify that the forum has been deleted.
        assertTrue(forumsHomePage.getNotification().contains("Forum deleted"));

    }

    private void waitUntilTourDisappears(TestUtils setup)
    {
        // Waiting for all the tour elements to disappear instead of the last one because the order varies.
        setup.getDriver().waitUntilElementDisappears(By.className("tour-backdrop"));
        setup.getDriver().waitUntilElementDisappears(By.className("tour-step-background"));
        setup.getDriver().waitUntilElementDisappears(By.cssSelector("id^='step-'"));

    }
}
