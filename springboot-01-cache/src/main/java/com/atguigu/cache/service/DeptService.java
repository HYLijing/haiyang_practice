package com.atguigu.cache.service;

import com.atguigu.cache.bean.Department;
import com.atguigu.cache.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;


@Service
public class DeptService {

    @Autowired
    DepartmentMapper departmentMapper;

    @Qualifier("deptCacheManager")      //容器中有很多缓存管理器，可以通过这个注解指明使用哪个缓存管理器
    @Autowired
    RedisCacheManager deptCacheManager;


    /**
     *  缓存的数据能存入redis；
     *  第二次从缓存中查询就不能反序列化回来；
     *  存的是dept的json数据;CacheManager默认使用RedisTemplate<Object, Employee>操作Redis
     *
     *  原因是：存的是dept的数据，而我们默认使用的自己配置的empRedisTemplate,将我们存入的dept对象，序列化成了
     *          emp对象的格式，当我们取值的时候就会报错。
     *  解决方法：配置一个dept缓存管理器
     *
     * @param id
     * @return
     */
//    @Cacheable(cacheNames = "dept",cacheManager = "deptCacheManager")
//    public Department getDeptById(Integer id){
//        System.out.println("查询部门"+id);
//        Department department = departmentMapper.getDeptById(id);
//        return department;
//    }

    // 使用缓存管理器得到缓存，进行api调用

    /**
     * 以编码的方法操作缓存：使用缓存管理器获取缓存操作缓存。
     * @param id
     * @return
     */
    public Department getDeptById(Integer id){
        System.out.println("查询部门"+id);
        Department department = departmentMapper.getDeptById(id);

        //获取某个缓存
        Cache dept = deptCacheManager.getCache("dept");
        dept.put("dept:1",department);

        return department;
    }
}
