/*
 * Copyright (c) 2008, Itision Corporation. All Rights Reserved.
 *
 * The content of this file is copyrighted by Itision Corporation and can not be
 * reproduced, distributed, altered or used in any form, in whole or in part.
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