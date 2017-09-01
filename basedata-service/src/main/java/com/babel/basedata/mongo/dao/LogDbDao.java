package com.babel.basedata.mongo.dao;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.babel.basedata.model.LogDbPO;
import com.babel.common.core.util.SpringContextUtil;
import com.mongodb.BasicDBObject;
@Component
public class LogDbDao  implements ILogDbDao{
//	@Autowired
    private MongoTemplate mongoTemplate;
    
    private MongoTemplate getMongoTemplate() {
        if(SpringContextUtil.containsBean("mongoTemplate")) {
            this.mongoTemplate=(MongoTemplate)SpringContextUtil.getBean("mongoTemplate");
        }
        return mongoTemplate;
    }
	
	 /**  
     * 新增  
     * <br>------------------------------<br>  
     * @param logDb  
     */   
    public void insert(LogDbPO logDb) {
        // TODO Auto-generated method stub  
        getMongoTemplate().insert(logDb);  
    }  
    /**  
     * 批量新增  
     * <br>------------------------------<br>  
     * @param logDbs  
     */  
    public void insertAll(List<LogDbPO> logDbs) {  
        // TODO Auto-generated method stub  
        getMongoTemplate().insertAll(logDbs);  
    }  
    /**  
     * 删除,按主键id, 如果主键的值为null,删除会失败  
     * <br>------------------------------<br>  
     * @param id  
     */  
    public void deleteById(Long id) {  
        // TODO Auto-generated method stub  
//        LogDbPO logDb = new LogDbPO(id, null, 0);  
        getMongoTemplate().remove(new BasicDBObject("cid", id));  
    }  
    /**  
     * 按条件删除  
     * <br>------------------------------<br>  
     * @param criteriaLogDbPO  
     */  
    public void delete(LogDbPO criteriaLogDbPO) {  
        // TODO Auto-generated method stub  
        Criteria criteria = Criteria.where("cid").is(criteriaLogDbPO.getCid());  
        Query query = new Query(criteria);  
        getMongoTemplate().remove(query, LogDbPO.class);  
    }  
    /**  
     * 删除全部  
     * <br>------------------------------<br>  
     */  
    public void deleteAll() {  
        // TODO Auto-generated method stub  
        getMongoTemplate().dropCollection(LogDbPO.class);  
    }  
    /**  
     * 按主键修改,  
     * 如果文档中没有相关key 会新增 使用$set修改器  
     * <br>------------------------------<br>  
     * @param logDb  
     */  
    public void updateById(LogDbPO logDb) {  
        // TODO Auto-generated method stub  
        Criteria criteria = Criteria.where("cid").is(logDb.getCid());  
        Query query = new Query(criteria);  
        Update update = Update.update("descs", logDb.getDescs()).set("title", logDb.getTitle());  
        getMongoTemplate().updateFirst(query, update, LogDbPO.class);  
    }  
    /**  
     * 修改多条  
     * <br>------------------------------<br>  
     * @param criteriaLogDbPO  
     * @param logDb  
     */  
    public void update(LogDbPO criteriaLogDbPO, LogDbPO logDb) {  
        // TODO Auto-generated method stub  
        Criteria criteria = Criteria.where("cid").gt(criteriaLogDbPO.getCid());;  
        Query query = new Query(criteria);  
        Update update = Update.update("descs", logDb.getDescs()).set("title", logDb.getTitle());  
        getMongoTemplate().updateMulti(query, update, LogDbPO.class);  
    }  
    /**  
     * 根据主键查询  
     * <br>------------------------------<br>  
     * @param id  
     * @return  
     */  
    public LogDbPO findById(String id) {  
        // TODO Auto-generated method stub  
        return getMongoTemplate().findById(id, LogDbPO.class);  
    }  
    /**  
     * 查询全部  
     * <br>------------------------------<br>  
     * @return  
     */  
    public List<LogDbPO> findAll() {  
        // TODO Auto-generated method stub  
        return getMongoTemplate().findAll(LogDbPO.class);  
    }  
    /**  
     * 按条件查询, 分页  
     * <br>------------------------------<br>  
     * @param criteriaLogDbPO  
     * @param skip  
     * @param limit  
     * @return  
     */  
    public List<LogDbPO> find(LogDbPO criteriaLogDbPO, int skip, int limit) {  
        // TODO Auto-generated method stub  
        Query query = getQuery(criteriaLogDbPO);  
        query.skip(skip);  
        query.limit(limit);  
        return getMongoTemplate().find(query, LogDbPO.class);  
    }  
    /**  
     * 根据条件查询出来后 再去修改  
     * <br>------------------------------<br>  
     * @param criteriaLogDbPO  查询条件  
     * @param updateLogDbPO    修改的值对象  
     * @return  
     */  
    public LogDbPO findAndModify(LogDbPO criteriaLogDbPO, LogDbPO updateLogDbPO) {  
        // TODO Auto-generated method stub  
        Query query = getQuery(criteriaLogDbPO);  
        Update update = Update.update("title", updateLogDbPO.getTitle()).set("descs", updateLogDbPO.getDescs());  
        return getMongoTemplate().findAndModify(query, update, LogDbPO.class);  
    }  
    /**  
     * 查询出来后 删除  
     * <br>------------------------------<br>  
     * @param criteriaLogDbPO  
     * @return  
     */  
    public LogDbPO findAndRemove(LogDbPO criteriaLogDbPO) {  
        // TODO Auto-generated method stub  
        Query query = getQuery(criteriaLogDbPO);  
        return getMongoTemplate().findAndRemove(query, LogDbPO.class);  
    }  
    /**  
     * count  
     * <br>------------------------------<br>  
     * @param criteriaLogDbPO  
     * @return  
     */  
    public long count(LogDbPO criteriaLogDbPO) {  
        // TODO Auto-generated method stub  
        Query query = getQuery(criteriaLogDbPO);  
        return getMongoTemplate().count(query, LogDbPO.class);  
    }  
    /**  
     *  
     * <br>------------------------------<br>  
     * @param criteriaLogDbPO  
     * @return  
     */  
    private Query getQuery(LogDbPO criteriaLogDbPO) {  
        if (criteriaLogDbPO == null) {  
            criteriaLogDbPO = new LogDbPO();  
        }  
        Query query = new Query();  
        if (criteriaLogDbPO.getCid() != null) {  
            Criteria criteria = Criteria.where("cid").is(criteriaLogDbPO.getCid());  
            query.addCriteria(criteria);  
        }  
//        if (criteriaLogDbPO.getAge() > 0) {  
//            Criteria criteria = Criteria.where("age").gt(criteriaLogDbPO.getAge());  
//            query.addCriteria(criteria);  
//        }  
        if (criteriaLogDbPO.getName() != null) {  
            Criteria criteria = Criteria.where("title").regex("^" + criteriaLogDbPO.getTitle());  
            query.addCriteria(criteria);  
        }  
        return query;  
    }  
}
