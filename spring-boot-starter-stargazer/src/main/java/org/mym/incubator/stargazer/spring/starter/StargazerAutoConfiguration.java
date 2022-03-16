package org.mym.incubator.stargazer.spring.starter;

import org.mym.incubator.stargazer.common.constants.Connector;
import org.mym.incubator.stargazer.common.exception.CreateConnectorFailedException;
import org.mym.incubator.stargazer.spi.StargazerConnector;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import java.util.function.Consumer;

/**
 * @author coxon
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({StargazerProperties.class})
@ConditionalOnClass({StargazerConnector.class})
@Order
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StargazerAutoConfiguration {

    StargazerProperties properties;

    Consumer<StargazerProperties> validate = properties -> {
        Assert.notNull(properties, "stargazer properties must be non-null");
        Assert.notNull(properties.getTopic(), "topic of sub/pub must be non-null");
        Assert.notNull(properties.getUrl(), "connector url must be non-null");
    };

    public StargazerAutoConfiguration(StargazerProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public StargazerConnector stargazerConnector() {
        validate.accept(properties);

        Connector connector = Connector.fetchConnector(properties.getUrl());
        try {
            StargazerConnector stargazerConnector = lookup(connector);
            stargazerConnector.initialize(properties.buildCreateRequest());
            return stargazerConnector;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw CreateConnectorFailedException.builder().message(e.getClass().getSimpleName() + " :: " + e.getLocalizedMessage()).build();
        }
    }

    private StargazerConnector lookup(Connector connector) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> type = this.getClass().getClassLoader().loadClass(connector.classPath());
        return (StargazerConnector) type.newInstance();
    }
}
