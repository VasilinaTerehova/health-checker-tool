/*
 * ******************************************************************************
 *  *
 *  * Pentaho Big Data
 *  *
 *  * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *  *
 *  *******************************************************************************
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  *****************************************************************************
 */
package com.epam.health.tool.common;

import javax.persistence.*;

/**
 * Abstract baseclass for all managed entities which using database generated identifier
 *
 * @author <a href="mailto: e.terehov@itision.com">Eugene Terehov</a>
 */
@MappedSuperclass
public abstract class AbstractManagedEntity extends AbstractEntity<Long> {

  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID)
  private Long id;

  protected AbstractManagedEntity() {
  }

  protected AbstractManagedEntity(Long id) {
    this.id = id;
  }

  /**
   * The id attribute used to uniquelly identify the instance of the subclass
   *
   * @return the unique id (primary key) of the instance
   */
  public Long getId() {
    return this.id;
  }

  public void setId( Long id ) {
    this.id = id;
  }

  @Override
  public String toString() {
    return getClass().getName() + "{" +
        "id=" + id +
        '}';
  }

}