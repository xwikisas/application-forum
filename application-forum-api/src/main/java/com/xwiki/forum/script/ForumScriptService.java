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
package com.xwiki.forum.script;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.script.service.ScriptService;

import com.xpn.xwiki.api.Document;
import com.xwiki.forum.ForumManager;

/**
 * @version $Id$
 * @since 2.6
 */
@Component
@Named("forum")
@Singleton
public class ForumScriptService implements ScriptService
{
    @Inject
    private ForumManager manager;

    /**
     * Check if the current user has edit right on a comment: the user is the comment creator OR the user has edit right
     * on the current forum.
     * 
     * @param commentNb the comment number of the comment to be verified
     * @param answer the document location of the comment
     * @param forumReference the current forum
     * @return true of the conditions are met, false otherwise
     */
    public boolean hasCommentEdit(int commentNb, Document answer, DocumentReference forumReference)
    {
        return manager.hasCommentEdit(commentNb, answer, forumReference);
    }

    /**
     * Check if the current user has delete right on a comment: the user is the comment creator OR the user has delete
     * right on the current forum.
     * 
     * @param commentNb the comment number of the comment to be verified
     * @param answer the document location of the comment
     * @param forumReference the current forum
     * @return true of the conditions are met, false otherwise
     */
    public boolean hasCommentDelete(int commentNb, Document answer, DocumentReference forumReference)
    {
        return manager.hasCommentDelete(commentNb, answer, forumReference);
    }

    /**
     * Check if the current user has edit right on an answer: the user is the answer creator OR the user has edit right
     * on the current forum.
     * 
     * @param answer the answer document
     * @param forumReference the current forum
     * @return true of the conditions are met, false otherwise
     */
    public boolean hasAnswerEdit(Document answer, DocumentReference forumReference)
    {
        return manager.hasAnswerEdit(answer, forumReference);
    }

    /**
     * Check if the current user has delete right on an answer: the user is the answer creator and there aren't any
     * other users' comments in the page OR the user has delete right on the current forum.
     * 
     * @param answer the answer document
     * @param forumReference the current forum
     * @return true of the conditions are met, false otherwise
     */
    public boolean hasAnswerDelete(Document answer, DocumentReference forumReference)
    {
        return manager.hasAnswerDelete(answer, forumReference);
    }

    /**
     * Check if the current user has edit right on a topic: the user is the topic creator OR the user has edit right on
     * the current forum.
     * 
     * @param topic the topic document
     * @param forumReference the current forum
     * @return true of the conditions are met, false otherwise
     */
    public boolean hasTopicEdit(Document topic, DocumentReference forumReference)
    {
        return manager.hasTopicEdit(topic, forumReference);
    }

    /**
     * Check if the current user has delete right on a topic: the user is the topic creator and the topic doesn't have
     * any answers created by other users and there aren't any other users' comments in the answers page OR the user has
     * delete right on current forum.
     * 
     * @param topic the topic document
     * @param forumReference the current forum
     * @return true of the conditions are met, false otherwise
     */
    public boolean hasTopicDelete(Document topic, DocumentReference forumReference)
    {
        return manager.hasTopicDelete(topic, forumReference);
    }
}
