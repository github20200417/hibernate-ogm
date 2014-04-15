/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * JBoss, Home of Professional Open Source
 * Copyright 2014 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.hibernate.ogm.backendtck.id;

import static org.fest.assertions.Assertions.assertThat;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.ogm.utils.OgmTestCase;
import org.junit.Test;

/**
 * Test case for Auto identifier generator using the session.
 *
 * @author Nabeel Ali Memon <nabeel@nabeelalimemon.com>
 * @author Davide D'Alto
 */
public class AutoIdGeneratorWithSessionTest extends OgmTestCase {

	@Test
	public void testAutoIdentifierGenerator() throws Exception {
		final Session session = openSession();
		Transaction transaction = session.beginTransaction();

		DistributedRevisionControl git = new DistributedRevisionControl();
		git.setName( "Git" );
		session.persist( git );

		DistributedRevisionControl bzr = new DistributedRevisionControl();
		bzr.setName( "Bazaar" );
		session.persist( bzr );

		transaction.commit();
		session.clear();

		transaction = session.beginTransaction();
		DistributedRevisionControl dvcs = (DistributedRevisionControl) session.get( DistributedRevisionControl.class, git.getId() );
		assertThat( dvcs ).isNotNull();
		assertThat( dvcs.getId() ).isEqualTo( 1 );
		session.delete( dvcs );

		dvcs = (DistributedRevisionControl) session.get( DistributedRevisionControl.class, bzr.getId() );
		assertThat( dvcs ).isNotNull();
		assertThat( dvcs.getId() ).isEqualTo( 2 );
		transaction.commit();
		session.close();
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { DistributedRevisionControl.class };
	}
}
