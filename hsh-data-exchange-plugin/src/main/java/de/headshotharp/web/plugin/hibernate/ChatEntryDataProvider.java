package de.headshotharp.web.plugin.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import de.headshotharp.plugin.hibernate.GenericDataProvider;
import de.headshotharp.web.database.ChatEntry;

public class ChatEntryDataProvider extends GenericDataProvider<ChatEntry> {

    public ChatEntryDataProvider(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public ChatEntryDataProvider(Session session) {
        super(session);
    }

    @Override
    public Class<ChatEntry> getEntityClass() {
        return ChatEntry.class;
    }
}
