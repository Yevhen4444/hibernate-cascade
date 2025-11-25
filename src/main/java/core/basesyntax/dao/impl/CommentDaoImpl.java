package core.basesyntax.dao.impl;

import core.basesyntax.dao.CommentDao;
import core.basesyntax.exception.DataProcessingException;
import core.basesyntax.model.Comment;
import core.basesyntax.model.Smile;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class CommentDaoImpl extends AbstractDao implements CommentDao {
    public CommentDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Comment create(Comment entity) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            List<Smile> existingSmiles = new ArrayList<>();
            for (Smile smile : entity.getSmiles()) {
                Smile dbSmile = session.get(Smile.class, smile.getId());
                existingSmiles.add(dbSmile);
            }
            entity.setSmiles(existingSmiles);
            session.persist(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't create comment", e);
        }
    }

    @Override
    public Comment get(Long id) {
        Transaction transaction = null;
        Comment comment = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            comment = session.get(Comment.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't get comment with id " + id, e);
        }
        return comment;
    }

    @Override
    public List<Comment> getAll() {
        Transaction transaction = null;
        List<Comment> comments = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            comments = session.createQuery("FROM Comment", Comment.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't get all comments", e);
        }
        return comments;
    }

    @Override
    public void remove(Comment entity) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            Comment bdComment = session.get(Comment.class, entity.getId());
            bdComment.getSmiles().clear();
            session.remove(bdComment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't remove comment with id " + entity.getId(), e);
        }
    }
}
