package com.kenzan.msl.login.edge.config;

import com.google.inject.AbstractModule;
import com.kenzan.msl.login.edge.services.LoginEdgeService;
import com.kenzan.msl.login.edge.services.LoginEdgeServiceStub;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import io.swagger.api.LoginEdgeApiService;
import io.swagger.api.factories.LoginEdgeApiServiceFactory;
import io.swagger.api.impl.LoginEdgeApiServiceImpl;

/**
 * @author Kenzan
 */
public class LocalLoginEdgeModule extends AbstractModule {
    @Override
    protected void configure() {
        requestStaticInjection(LoginEdgeApiServiceFactory.class);
        bind(LoginEdgeService.class).to(LoginEdgeServiceStub.class).in(LazySingletonScope.get());
        bind(LoginEdgeApiService.class).to(LoginEdgeApiServiceImpl.class).in(LazySingletonScope.get());
    }
}