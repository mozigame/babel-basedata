package com.babel.basedata.mongo.dao;

import java.util.List;

import com.babel.basedata.model.LogDbPO;

public interface ILogDbDao {
	/**  
     * 新增  
     * <br>------------------------------<br>  
     * @param logDb  
     */  
    void insert(LogDbPO logDb);  
      
    /**  
     * 新增  
     * <br>------------------------------<br>  
     * @param logDbs  
     */  
    void insertAll(List<LogDbPO> logDbs);  
      
    /**  
     * 删除,主键id, 如果主键的值为null,删除会失败  
     * <br>------------------------------<br>  
     * @param id  
     */  
    void deleteById(Long id);  
      
    /**  
     * 按条件删除  
     * <br>------------------------------<br>  
     * @param criteriaLogDbPO  
     */  
    void delete(LogDbPO criteriaLogDbPO);  
      
    /**  
     * 删除全部  
     * <br>------------------------------<br>  
     */  
    void deleteAll();  
      
    /**  
     * 修改  
     * <br>------------------------------<br>  
     * @param logDb  
     */  
    void updateById(LogDbPO logDb);  
      
    /**  
     * 更新多条  
     * <br>------------------------------<br>  
     * @param criteriaLogDbPO  
     * @param logDb  
     */  
    void update(LogDbPO criteriaLogDbPO, LogDbPO logDb);  
      
    /**  
     * 根据主键查询  
     * <br>------------------------------<br>  
     * @param id  
     * @return  
     */  
    LogDbPO findById(String id);  
      
    /**  
     * 查询全部  
     * <br>------------------------------<br>  
     * @return  
     */  
    List<LogDbPO> findAll();  
      
    /**  
     * 按条件查询  
     * <br>------------------------------<br>  
     * @param criteriaLogDbPO  
     * @param skip  
     * @param limit  
     * @return  
     */  
    List<LogDbPO> find(LogDbPO criteriaLogDbPO, int skip, int limit);  
      
    /**  
     * 根据条件查询出来后 在去修改  
     * <br>------------------------------<br>  
     * @param criteriaLogDbPO  查询条件  
     * @param updateLogDbPO    修改的值对象  
     * @return  
     */  
    LogDbPO findAndModify(LogDbPO criteriaLogDbPO, LogDbPO updateLogDbPO);  
      
    /**  
     * 查询出来后 删除  
     * <br>------------------------------<br>  
     * @param criteriaLogDbPO  
     * @return  
     */  
    LogDbPO findAndRemove(LogDbPO criteriaLogDbPO);  
      
    /**  
     * count  
     * <br>------------------------------<br>  
     * @param criteriaLogDbPO  
     * @return  
     */  
    long count(LogDbPO criteriaLogDbPO);  
}
