/*
 * Copyright 2016 Ratha Long
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nz.co.testamation.hibernate;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import nz.co.testamation.common.util.ReflectionUtil;
import nz.co.testamation.core.waiting.Task;
import nz.co.testamation.core.waiting.WaitFor;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;

public class HibernateEntityTemplate {

    private final SessionFactory sessionFactory;

    private final EntityManagerFactory entityManagerFactory;

    public HibernateEntityTemplate( SessionFactory sessionFactory, EntityManagerFactory entityManagerFactory ) {
        this.sessionFactory = sessionFactory;
        this.entityManagerFactory = entityManagerFactory;
    }


    public interface Work<T> {
        T execute( Session session );
    }

    public interface WorkNoResult {
        void execute( Session session );
    }

    public interface EntityWork {
        void execute( EntityManager entityManager );
    }

    public void doWithNewEntity( EntityWork work ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        work.execute( entityManager );
        entityManager.close();
    }

    public <T> T doInHibernate( final Work<T> work ) {
        return new HibernateTemplate( sessionFactory ).execute(
            new HibernateCallback<T>() {
                @Override
                public T doInHibernate( Session session ) throws HibernateException {
                    Transaction transaction = session.beginTransaction();
                    T result = work.execute( session );
                    session.flush();
                    transaction.commit();
                    return result;
                }
            } );
    }

    public void doInHibernateNoResult( final WorkNoResult work ) {
        new HibernateTemplate( sessionFactory ).execute(
            new HibernateCallback<Object>() {
                @Override
                public Object doInHibernate( Session session ) throws HibernateException {
                    Transaction transaction = session.beginTransaction();
                    work.execute( session );
                    session.flush();
                    transaction.commit();
                    return null;
                }
            } );
    }

    public <T> void waitForInHibernate( final Work<T> work, Predicate<T> predicate ) {
        new WaitFor<>( predicate )
            .when( new Task<T>() {
                @Override
                public T execute() {
                    return doInHibernate( work );
                }
            } )
            .run();
    }

    public <T> T reload( Session session, T obj ) {
        Field field = Iterables.getOnlyElement( Iterables.filter( FieldUtils.getAllFieldsList( obj.getClass() ), new Predicate<Field>() {
            @Override
            public boolean apply( Field field ) {
                return field.getAnnotation( Id.class ) != null;
            }
        } ) );

        return (T) session.load( obj.getClass(), ReflectionUtil.getFieldValue( obj, field.getName(), Serializable.class ) );
    }
}
