package com.hzq.base.dao;

import org.springframework.dao.DataAccessException;

import java.io.Serializable;

public interface Dao<E, PK extends Serializable> {
    E getByPk(PK pk) throws DataAccessException;

    E selectOne(String sqlId, Object param) throws DataAccessException;

    int insert(E entity) throws DataAccessException;

    int update(E entity) throws DataAccessException;

    int updateByParam(String sqlId, Object param);

    PageResult<E> selectPage(String sqlId, Object param);

    PageResult<Object> selectPage2(String sqlId, Object param);
}
