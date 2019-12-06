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
package com.xwiki.forum.internal;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.DocumentReferenceResolver;
import org.xwiki.security.authorization.AuthorizationManager;
import org.xwiki.security.authorization.Right;

import com.xpn.xwiki.api.Object;
import com.xpn.xwiki.XWiki;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.api.Document;
import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.BaseObject;
import com.xwiki.forum.ForumManager;

/**
 * @version $Id$
 * @since 2.6
 */
@Component
@Singleton
public class DefaultForumManager implements ForumManager
{
    private static final String AUTHOR = "author";

    @Inject
    private AuthorizationManager authManager;

    @Inject
    private DocumentReferenceResolver<String> documentResolver;

    @Inject
    private Provider<XWikiContext> xcontext;

    @Override
    public boolean hasCommentEdit(int commentNb, Document answer, DocumentReference forumReference)
    {
        boolean hasCommentRight = checkCommentRight(commentNb, answer);
        boolean hasForumEdit =
            authManager.hasAccess(Right.EDIT, xcontext.get().getUserReference(), forumReference);

        return (hasCommentRight || hasForumEdit) ? true : false;
    }

    @Override
    public boolean hasCommentDelete(int commentNb, Document answer, DocumentReference forumReference)
    {
        boolean hasCommentRight = checkCommentRight(commentNb, answer);
        boolean hasForumDelete =
            authManager.hasAccess(Right.DELETE, xcontext.get().getUserReference(), forumReference);

        return (hasCommentRight || hasForumDelete) ? true : false;
    }

    private boolean checkCommentRight(int commentNb, Document answer)
    {
        // Comment class reference
        DocumentReference commentClass = new DocumentReference(xcontext.get().getWikiId(), XWiki.SYSTEM_SPACE,
            XWikiDocument.COMMENTSCLASS_REFERENCE.getName());

        Object commentObj = answer.getObject(commentClass.toString(), commentNb);
        String commentAuthor = (String) commentObj.getValue(AUTHOR);
        DocumentReference authorReference = documentResolver.resolve(commentAuthor);

        DocumentReference currentUserReference = xcontext.get().getUserReference();

        return authorReference.equals(currentUserReference);
    }

    @Override
    public boolean hasAnswerEdit(Document answer, DocumentReference forumReference)
    {
        boolean hasAnswerRight = checkDocumentRight(answer);
        boolean hasForumEdit =
            authManager.hasAccess(Right.EDIT, xcontext.get().getUserReference(), forumReference);

        return (hasAnswerRight || hasForumEdit) ? true : false;
    }

    @Override
    public boolean hasAnswerDelete(Document answer, DocumentReference forumReference)
    {
        boolean hasAnswerRight = checkDocumentRight(answer);
        boolean hasContributedComments = checkContributedComments(answer);
        boolean hasForumDelete =
            authManager.hasAccess(Right.DELETE, xcontext.get().getUserReference(), forumReference);
        return (hasAnswerRight && !hasContributedComments || hasForumDelete) ? true : false;
    }

    private boolean checkDocumentRight(Document document)
    {
        DocumentReference creatorReference = document.getCreatorReference();
        DocumentReference currentUserReference = xcontext.get().getUserReference();
        return creatorReference.equals(currentUserReference);
    }

    private boolean checkContributedComments(Document document)
    {
        DocumentReference documentCreatorReference = document.getCreatorReference();

        for (Object object : document.getComments()) {
            String commentCreator = object.get(AUTHOR).toString();
            DocumentReference commentCreatorReference = documentResolver.resolve(commentCreator);
            if (!documentCreatorReference.equals(commentCreatorReference)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasTopicEdit(Document topic, DocumentReference forumReference)
    {
        boolean hasTopicRight = checkDocumentRight(topic);
        boolean hasForumEdit =
            authManager.hasAccess(Right.EDIT, xcontext.get().getUserReference(), forumReference);

        return (hasTopicRight || hasForumEdit);
    }

    private boolean checkContributedAnswers(Document document)
    {
        DocumentReference documentCreatorReference = document.getCreatorReference();

        try {
            boolean hasContributedComments = false;

            for (String child : document.getChildren()) {
                DocumentReference childReference = documentResolver.resolve(child);
                XWikiDocument childXWikiDocument = xcontext.get().getWiki().getDocument(childReference, xcontext.get());
                DocumentReference childCreatorReference = childXWikiDocument.getCreatorReference();

                if (!documentCreatorReference.equals(childCreatorReference)) {
                    return true;
                }

                Document childDocument = new Document(childXWikiDocument, xcontext.get());
                hasContributedComments = checkContributedComments(childDocument);
                if (hasContributedComments) {
                    return true;
                }
            }
        } catch (XWikiException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean hasTopicDelete(Document topic, DocumentReference forumReference)
    {
        boolean hasTopicRight = checkDocumentRight(topic);
        boolean hasContributedAnswers = checkContributedAnswers(topic);
        boolean hasForumEdit =
            authManager.hasAccess(Right.DELETE, xcontext.get().getUserReference(), forumReference);

        return (hasTopicRight && !hasContributedAnswers || hasForumEdit);
    }
}
