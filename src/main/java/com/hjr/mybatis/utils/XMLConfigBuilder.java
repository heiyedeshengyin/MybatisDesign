package com.hjr.mybatis.utils;

import com.hjr.mybatis.anno.Select;
import com.hjr.mybatis.cfg.Configuration;
import com.hjr.mybatis.cfg.Mapper;
import com.hjr.mybatis.io.Resources;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于解析XML文件<br>
 * 该类的作用就是为了获取Configuration对象
 */
public class XMLConfigBuilder {
    private XMLConfigBuilder() {}

    /**
     * 解析mybatis主配置文件,在本项目中为SqlMapConfig.xml<br>
     * 使用了dom4j + xpath技术
     * @param config 主配置文件的字节输入流
     * @return mybatis配置对象
     */
    public static Configuration loadConfiguration(InputStream config) {
        try {
            //定义mybatis配置对象
            Configuration cfg = new Configuration();

            //获取SAXReader对象
            SAXReader reader = new SAXReader();
            //获取Document对象,在本项目中为SqlMapConfig.xml
            Document document = reader.read(config);
            //获取根节点
            Element root = document.getRootElement();

            //使用xpath技术获取所有property节点
            List<Element> propertyElements = root.selectNodes("//property");
            //遍历节点
            for (Element propertyElement : propertyElements) {
                //获取name属性值
                String name = propertyElement.attributeValue("name");
                if ("driver".equals(name)) {
                    //数据库驱动
                    String driver = propertyElement.attributeValue("value");
                    cfg.setDriver(driver);
                }
                if ("url".equals(name)) {
                    //数据库url
                    String url = propertyElement.attributeValue("value");
                    cfg.setUrl(url);
                }
                if ("username".equals(name)) {
                    //数据库用户名
                    String username = propertyElement.attributeValue("value");
                    cfg.setUsername(username);
                }
                if ("password".equals(name)) {
                    //数据库密码
                    String password = propertyElement.attributeValue("value");
                    cfg.setPassword(password);
                }
            }

            //使用xpath技术获取所有mapper节点
            List<Element> mapperElements = root.selectNodes("//mappers/mapper");
            //遍历节点
            for (Element mapperElement : mapperElements) {
                //判断mapper节点是否有resource属性.若有,则表示使用XML配置;若没有,则表示使用注解配置
                Attribute attribute = mapperElement.attribute("resource");
                if (attribute != null) {
                    //mapper节点有resource属性,代表使用XML配置
                    System.out.println("使用的是XML");
                    //获取mapper配置文件的位置,在本项目中为com/hjr/dao/UserDao.xml
                    String mapperPath = attribute.getValue();
                    //把mapper配置文件中的内容读取出来
                    Map<String, Mapper> mappers = loadMapperConfiguration(mapperPath);
                    cfg.setMappers(mappers);
                } else {
                    //mapper节点有class属性,代表使用注解配置
                    System.out.println("使用的是注解");
                    //获取mapper节点class属性值,即dao接口全类名,在本项目中为com.hjr.dao.UserDao
                    String daoClassPath = mapperElement.attributeValue("class");
                    //把Select注解的内容和dao接口信息读取出来
                    Map<String, Mapper> mappers = loadMapperAnnotation(daoClassPath);
                    cfg.setMappers(mappers);
                }
            }

            return cfg;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                config.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析mapper配置文件
     * @param mapperPath mapper配置文件的位置,在本项目中为com/hjr/dao/UserDao.xml
     * @return 唯一标识(dao接口全类名.方法名)和Mapper对象
     * @throws IOException
     */
    private static Map<String, Mapper> loadMapperConfiguration(String mapperPath) throws IOException {
        InputStream in = null;
        try {
            Map<String, Mapper> mappers = new HashMap<>();
            //获取mapper配置文件的字节输入流
            in = Resources.getResourceAsStream(mapperPath);

            //解析mapper配置文件
            SAXReader reader = new SAXReader();
            Document document = reader.read(in);
            Element root = document.getRootElement();

            //获取mapper节点的namespace属性值,即dao接口全类名,在本项目中为com.hjr.dao.UserDao
            String namespace = root.attributeValue("namespace");
            //获取所有select节点
            List<Element> selectElements = root.selectNodes("//select");
            //遍历节点
            for (Element selectElement : selectElements) {
                //获取id属性值,即作用到的方法名,在本项目中为findAll
                String id = selectElement.attributeValue("id");
                //获取resultType属性值,即结果类型,在本项目中为com.hjr.domain.User
                String resultType = selectElement.attributeValue("resultType");
                //获取select节点中的文本,即SQL语句,在本项目中为select * from user
                String queryString = selectElement.getText();

                String key = namespace + "." + id;

                Mapper mapper = new Mapper();
                mapper.setQueryString(queryString);
                mapper.setResultType(resultType);

                mappers.put(key, mapper);
            }

            return mappers;
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } finally {
            in.close();
        }
    }

    /**
     * 解析Select注解
     * @param daoClassPath dao类全类名,在本项目中为com.hjr.dao.UserDao
     * @return 唯一标识(dao全类名.方法名)和Mapper对象
     * @throws Exception
     */
    private static Map<String, Mapper> loadMapperAnnotation(String daoClassPath) throws Exception {
        Map<String, Mapper> mappers = new HashMap<>();

        //获取dao类的字节码
        Class daoClass = Class.forName(daoClassPath);
        //获取dao类下的所有方法
        Method[] methods = daoClass.getMethods();

        //遍历所有方法
        for (Method method : methods) {
            //判断该方法上是否有Select注解
            boolean isAnnotated = method.isAnnotationPresent(Select.class);
            if (isAnnotated) {
                //若有,立即执行以下代码
                Mapper mapper = new Mapper();

                //获取Select注解的代理对象
                Select selectAnno = method.getAnnotation(Select.class);
                //获取Select注解的value属性,在本项目中为select * from user
                String queryString = selectAnno.value();
                mapper.setQueryString(queryString);

                //获取该方法的返回值类型,在本项目中为java.util.List<com.hjr.domain.User>
                Type type = method.getGenericReturnType();

                //判断该类型是否是泛型类型
                if (type instanceof ParameterizedType) {
                    ParameterizedType ptype = (ParameterizedType) type;
                    //获取该泛型类型上的所有实际类型参数
                    Type[] types = ptype.getActualTypeArguments();
                    //获取第一个实际类型参数,在本项目中为com.hjr.domain.User
                    Class domainClass = (Class) types[0];
                    String resultType = domainClass.getName();
                    mapper.setResultType(resultType);
                }

                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String key = className + "." + methodName;

                mappers.put(key, mapper);
            }
        }
        return mappers;
    }
}
