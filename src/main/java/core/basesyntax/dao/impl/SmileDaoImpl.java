package core.basesyntax.dao.impl;

import core.basesyntax.dao.SmileDao;
import core.basesyntax.exception.DataProcessingException;
import core.basesyntax.model.Smile;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class SmileDaoImpl extends AbstractDao implements SmileDao {
    public SmileDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Smile create(Smile entity) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't smile", e);
        }
        return entity;
    }

    @Override
    public Smile get(Long id) {
        Transaction transaction = null;
        Smile smile = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            smile = session.get(Smile.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't get smile", e);
        }
        return smile;
    }

    @Override
    public List<Smile> getAll() {
        Transaction transaction = null;
        List<Smile> smiles = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            smiles = session.createQuery("FROM Smile", Smile.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't get all smile", e);
        }
        return smiles;
    }
}
