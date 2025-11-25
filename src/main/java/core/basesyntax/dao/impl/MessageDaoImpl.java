package core.basesyntax.dao.impl;

import core.basesyntax.dao.MessageDao;
import core.basesyntax.exception.DataProcessingException;
import core.basesyntax.model.Message;
import core.basesyntax.model.MessageDetails;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class MessageDaoImpl extends AbstractDao implements MessageDao {
    public MessageDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Message create(Message entity) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't create message", e);
        }
        return entity;
    }

    @Override
    public Message get(Long id) {
        Transaction transaction = null;
        Message message = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            message = session.get(Message.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't get message", e);
        }
        return message;
    }

    @Override
    public List<Message> getAll() {
        Transaction transaction = null;
        List<Message> messages = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            messages = session.createQuery("FROM Message", Message.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't get all message", e);
        }
        return messages;
    }

    @Override
    public void remove(Message entity) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            Message message = session.get(Message.class, entity.getId());
            MessageDetails messageDetails = message.getMessageDetails();
            if (messageDetails != null) {
                session.remove(messageDetails);
            }
            session.remove(message);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't remove message", e);
        }
    }
}
