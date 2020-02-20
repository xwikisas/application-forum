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

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.xwiki.bridge.event.DocumentCreatedEvent;
import org.xwiki.component.annotation.Component;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.LocalDocumentReference;
import org.xwiki.model.reference.SpaceReference;
import org.xwiki.observation.AbstractEventListener;
import org.xwiki.observation.event.Event;

import com.xpn.xwiki.XWiki;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.BaseObject;

/**
 * Initialize forum.
 *
 * @version $Id$
 * @since 2.7.1
 */
@Component
@Named(ForumDocumentListener.NAME)
@Singleton
public class ForumDocumentListener extends AbstractEventListener
{
    /**
     * Listener name.
     */
    public static final String NAME = "ForumDocumentListener123";

    private static final List<Event> EVENTS = Arrays.<Event>asList(new DocumentCreatedEvent());

    private static final LocalDocumentReference FORUM_CLASSREFERENCE =
        new LocalDocumentReference("ForumCode", "ForumClass");

    private static final LocalDocumentReference PREFS_CLASSREFERENCE =
        new LocalDocumentReference("XWiki", "XWikiPreferences");

    @Inject
    private Logger logger;

    /**
     * Default constructor.
     */
    public ForumDocumentListener()
    {
        super(NAME, EVENTS);
    }

    @Override
    public void onEvent(Event event, Object source, Object data)
    {
        XWikiDocument forumDoc = (XWikiDocument) source;
        BaseObject forumObj = forumDoc.getXObject(FORUM_CLASSREFERENCE);

        if (forumObj == null) {
            return;
        }

        XWikiContext xcontext = (XWikiContext) data;
        XWiki xwiki = xcontext.getWiki();

        SpaceReference forumSpaceRef = forumDoc.getDocumentReference().getLastSpaceReference();
        DocumentReference forumPrefsDocRef = new DocumentReference("WebPreferences", forumSpaceRef);

        try {
            XWikiDocument forumPrefsDoc = xwiki.getDocument(forumPrefsDocRef, xcontext);
            BaseObject forumPrefsObj = forumPrefsDoc.newXObject(PREFS_CLASSREFERENCE, xcontext);
            forumPrefsObj.setIntValue("showRightPanels", 1);
            forumPrefsObj.setStringValue("rightPanels", "ForumCode.MostActiveTopicsPanel");
            forumPrefsDoc.setHidden(true);
            forumPrefsDoc.setAuthorReference(xcontext.getUserReference());
            forumPrefsDoc.setCreatorReference(xcontext.getUserReference());
            xwiki.saveDocument(forumPrefsDoc, "Display most active topics panel.", true, xcontext);
        } catch (XWikiException e) {
            logger.error("Forum Creation Exception: ", e);
        }
    }
}
