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

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.DocumentReferenceResolver;
import org.xwiki.test.ui.po.ViewPage;

/**
 * Represents the Topic view page.
 * 
 * @version $Id$
 * @since 2.1
 */
public class TopicViewPage extends ViewPage
{
    @FindBy(css = ".addconversation-activator")
    private WebElement addAnswerActivator;

    @FindBy(css = ".topic-description>p")
    private WebElement description;

    @FindBy(css = ".topic-answers")
    private WebElement summaryAnswersNb;

    @FindBy(css = ".answers .answer")
    private List<WebElement> pageAnswersElements;

    @FindBy(css = ".answers .answer:first-child .flag")
    private WebElement flagButton;

    @FindBy(css = ".answers .answer:first-child .comments .comment:first-of-type .button[type=submit]")
    private WebElement addCommentButton;

    @FindBy(css = ".answer:target .answer-reply")
    private WebElement answerTarget;

    // Tour steps
    @FindBy(css = "#bootstrap_tour_next")
    private WebElement step0;

    @FindBy(css = "#bootstrap_tour_next")
    private WebElement step1;

    @FindBy(css = "#bootstrap_tour_next")
    private WebElement step2;

    @FindBy(css = "#bootstrap_tour_next")
    private WebElement step3;

    @FindBy(css = "#bootstrap_tour_next")
    private WebElement step4;

    @FindBy(css = "#bootstrap_tour_end")
    private WebElement step5;

    @Inject
    @Named("current")
    private static DocumentReferenceResolver<String> documentReferenceResolver;

    /**
     * @return the topic page
     */
    public static TopicViewPage gotoPage()
    {
        getUtil().gotoPage(List.of("Forums", "MyForum", "MyTopic"), "WebHome", "view", "");
        return new TopicViewPage();
    }

    public static TopicViewPage gotoPage(String url)
    {
        getUtil().gotoPage(url);
        return new TopicViewPage();
    }

    /**
     * @return the form to enter new answer
     */
    public AnswerAddElement clickAddAnswerActivator()
    {
        addAnswerActivator.click();
        return new AnswerAddElement();
    }

    /**
     * @return the topic description
     */
    public String getDescription()
    {
        return description.getText();
    }

    public int getAnswersNumber()
    {
        // Get the answers number by counting the elements in the page.
        int first = pageAnswersElements.size();
        // Get the answers number by parsing the topic summary "1 answer".
        String asnwersNb = summaryAnswersNb.getText().split(" ")[0];
        int second = Integer.parseInt(asnwersNb);
        if (first == second) {
            return first;
        }
        return -1;
    }

    public FlagAddElement flagAnswer()
    {
        flagButton.click();
        return new FlagAddElement();
    }

    public boolean checkFlaggedAnswer()
    {
        return answerTarget != null;
    }

    /**
     * Navigates through the Topic tour.
     */
    public void viewTour()
    {
        // TODO: Figure why the tour is not showing up when running the test. (it shows when manually testing on
        //  instance)
//        step0.click();
//        step1.click();
//        step2.click();
//        step3.click();
//        step4.click();
//        step5.click();
    }

    public void addComment(String comment)
    {
        CommentAddElement commentAddForm = new CommentAddElement();
        commentAddForm.getEditForm().setComment(comment);
        addCommentButton.click();
    }
}
