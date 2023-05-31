package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig  implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Value("${allowed.origins}")
    private  String theAllowedOrigins;

    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager){
        this.entityManager = theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod [] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST,
                                                HttpMethod.DELETE, HttpMethod.PATCH};

        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);

        // disable HTTP methods for Product: PUT, POST and DELETE
        disableHttpMethods(Product.class, config, theUnsupportedActions);
        // disable HTTP methods for ProductCategory: PUT, POST and DELETE
        disableHttpMethods(ProductCategory.class,config, theUnsupportedActions);
        // disable HTTP methods for ProductCategory: PUT, POST and DELETE
        disableHttpMethods(Country.class,config, theUnsupportedActions);
        // disable HTTP methods for ProductCategory: PUT, POST and DELETE
        disableHttpMethods(State.class,config, theUnsupportedActions);

        disableHttpMethods(Order.class,config, theUnsupportedActions);
        exposeIds(config);

        //configure cors mapping
        cors.addMapping( config.getBasePath() + "/**").allowedOrigins(this.theAllowedOrigins);
    }

    private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
        .forDomainType(theClass).withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
        .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));
    }

    private void exposeIds(RepositoryRestConfiguration config)
    {

        Set<EntityType<?> > entities = entityManager.getMetamodel().getEntities(); //get the list of classes from the entity manager
        //create array of entity types
        List<Class> entityClass = new ArrayList<>();

        for(EntityType tempEntityType : entities){
            entityClass.add(tempEntityType.getJavaType());
        }

        Class[] domainTypes = entityClass.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
