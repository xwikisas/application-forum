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

    @FindBy(css = "answer:target")
    private WebElement answerTarget;

    // Tour steps
    @FindBy(css = "#step-0 [data-role = 'next']")
    private WebElement step0;

    @FindBy(css = "#step-1 [data-role = 'next']")
    private WebElement step1;

    @FindBy(css = "#step-2 [data-role = 'next']")
    private WebElement step2;

    @FindBy(css = "#step-3 [data-role = 'next']")
    private WebElement step3;

    @FindBy(css = "#step-4 [data-role = 'next']")
    private WebElement step4;

    @FindBy(css = "#step-5 [data-role = 'end']")
    private WebElement step5;

    @Inject
    @Named("current")
    private static DocumentReferenceResolver<String> documentReferenceResolver;

    /**
     * @return the topic page
     */
    public static TopicViewPage gotoPage()
    {
        DocumentReference reference =
            new DocumentReference("wiki", Arrays.asList("Forums", "MyForum", "MyTopic"), "WebHome");
        getUtil().gotoPage(reference);
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
        AnswerAddElement answerAddForm = new AnswerAddElement();
        return answerAddForm;
    }

    /**
     * @return the form to enter new comment
     */
    public CommentAddElement clickAddCommentActivator()
    {
        CommentAddElement commentAddForm = new CommentAddElement();
        return commentAddForm;
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
        FlagAddElement flagAddForm = new FlagAddElement();
        return flagAddForm;
    }

    public boolean checkFlaggedAnswer()
    {
        return answerTarget != null ? true : false;
    }

    /**
     * Navigates through the Topic tour.
     */
    public void viewTour()
    {
        step0.click();
        step1.click();
        step2.click();
        step3.click();
        step4.click();
        step5.click();
    }
    
    /**
     * @return the form to enter new answer
     */
    public CommentAddElement clickAddCommentButton()
    {
        addCommentButton.click();
        CommentAddElement commentAddForm = new CommentAddElement();
        return commentAddForm;
    }
}
