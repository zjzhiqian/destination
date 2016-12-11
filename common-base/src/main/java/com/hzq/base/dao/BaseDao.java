package com.hzq.base.dao;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.primitives.Ints;
import com.hzq.base.exception.DataAccessException;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class BaseDao<E, PK extends Serializable> extends SqlSessionDaoSupport implements Dao<E, PK> {
    public BaseDao() {
    }

    @Autowired
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public E getByPk(PK primaryKey) throws DataAccessException {
        String sqlId0 = this.getFindByPrimaryKeyStatement();
        return this.getSqlSession().selectOne(sqlId0, primaryKey);
    }


    @Override
    public E selectOne(String sqlId, Object param) throws DataAccessException {
        String sqlId0 = this.buildFullSqlId(sqlId);
        return this.getSqlSession().selectOne(sqlId0, param);
    }

    @Override
    public int insert(E entity) throws DataAccessException {
        this.prepareObjectBeforeSave(entity);
        int i = this.getSqlSession().insert(this.getInsertStatement(), entity);
        this.prepareObjectAfterSave(entity);
        return i;
    }

    @Override
    public int updateByParam(String sqlId, Object param) {
        String sqlId0 = this.buildFullSqlId(sqlId);
        return this.getSqlSession().update(sqlId0, param);
    }

    @Override
    public int update(E entity) throws DataAccessException {
        String sqlId0 = this.getUpdateStatement();
        this.prepareObjectBeforeUpdate(entity);
        int i = this.getSqlSession().update(sqlId0, entity);
        this.prepareObjectAfterUpdate(entity);
        return i;
    }


    @Override
    public PageResult<E> selectPage(String sqlId, Object param) {
        String sqlId0 = this.buildFullSqlId(sqlId);
        startPage(param);
        List<E> resultList = this.getSqlSession().selectList(sqlId0, param);
        PageInfo pageInfo = new PageInfo<>(resultList);
        return new PageResult<>(pageInfo.getPageNum(), pageInfo.getPageSize(), Ints.saturatedCast(pageInfo.getTotal()), pageInfo.getPages(), resultList);
    }

    @Override
    public PageResult<Object> selectPage2(String sqlId, Object param) {
        String sqlId0 = this.buildFullSqlId(sqlId);
        startPage(param);
        List<Object> resultList = this.getSqlSession().selectList(sqlId0, param);
        PageInfo pageInfo = new PageInfo<>(resultList);
        return new PageResult<>(pageInfo.getPageNum(), pageInfo.getPageSize(), Ints.saturatedCast(pageInfo.getTotal()), pageInfo.getPages(), resultList);

    }

    private void startPage(Object param) {
        Class<?> paramClass = param.getClass();
        try {
            Method method = paramClass.getMethod("getPage");
            PageHelper.startPage((Integer) method.invoke(param), 20);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("查询列表参数必须有getPage方法");
        }
    }


    private String getFindByPrimaryKeyStatement() {
        return this.buildFullSqlId(SqlMapStatementEnum.GET_BY_ID.getStatementId());
    }


    private String getInsertStatement() {
        return this.buildFullSqlId(SqlMapStatementEnum.INSERT_OBJECT.getStatementId());
    }

    private String getUpdateStatement() {
        return this.buildFullSqlId(SqlMapStatementEnum.INSERT_OBJECT.getStatementId());
    }


    private String buildFullSqlId(String sqlId) {
        String sqlId0 = sqlId;
        if (!sqlId.startsWith(".")) {
            sqlId0 = "." + sqlId;
        }

        return this.getIbatisSqlMapNamespace() + sqlId0;
    }

    public String getIbatisSqlMapNamespace() {
        return this.getClass().getInterfaces()[0].getName();
    }

    protected void prepareObjectAfterUpdate(E o) {
    }

    protected void prepareObjectBeforeUpdate(E o) {
    }

    protected void prepareObjectBeforeSave(E o) {
    }

    protected void prepareObjectAfterSave(E o) {
    }


}
