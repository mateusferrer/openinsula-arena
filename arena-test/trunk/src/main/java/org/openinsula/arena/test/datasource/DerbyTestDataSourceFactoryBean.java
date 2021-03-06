/*
 *  (C) Copyright 2007 Insula Tecnologia da Informacao Ltda.
 * 
 *  This file is part of Arena Test.
 *
 *  Arena Test is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Arena Test is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Arena Test.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openinsula.arena.test.datasource;

import java.io.File;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

/**
 * Factory bean que cria um DataSource para ser utilizado nos TestCases de banco
 * de dados. <br>
 * Esta implementa��o � espec�fica para o <strong>Derby</strong>, e cria o
 * banco de dados no diretorio apontado pela propriedade "java.io.tmpdir". O
 * diretorio � removido ao se executar o m�todo destroy().
 * @author yanaga
 */
public class DerbyTestDataSourceFactoryBean extends AbstractTestDataSourceFactoryBean implements DisposableBean {

	private static final long serialVersionUID = 1L;

	protected SingleConnectionDataSource dataSource = null;

	protected File databaseDir;

	public Object getObject() throws Exception {
		dataSource = doCreateDataSource();

		fireBeforeDropDatabase(dataSource);
		fireAfterDropDatabase(dataSource);
		fireBeforeCreateDatabase(dataSource);
		fireAfterCreateDatabase(dataSource);

		return dataSource;
	}

	private SingleConnectionDataSource doCreateDataSource() {
		SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

		dataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
		dataSource.setUrl(getJdbcUrl());
		dataSource.setSuppressClose(true);

		return dataSource;
	}

	public void destroy() throws Exception {
		dataSource.setSuppressClose(false);
		dataSource.getConnection().close();

		deleteDir(databaseDir);
	}

	public boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}

	protected File createDatabaseDirectory() {
		return new File(System.getProperty("java.io.tmpdir"), databaseName + (int) (Math.random() * 1000 * 1000 * 1000));
	}

	protected String getJdbcUrl() {
		databaseDir = createDatabaseDirectory();

		return String.format("jdbc:derby:%s;create=true", databaseDir.getAbsolutePath());
	}

}
