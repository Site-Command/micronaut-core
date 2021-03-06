package io.micronaut.docs.config.immutable

import io.micronaut.context.ApplicationContext
import io.micronaut.context.exceptions.DependencyInjectionException
import spock.lang.Specification

class VehicleSpec extends Specification {

    void "test start vehicle"() {
        given:
        // tag::start[]
        ApplicationContext applicationContext = ApplicationContext.run(
                "my.engine.cylinders": "8",
                "my.engine.crank-shaft.rod-length": "7.0"
        )

        Vehicle vehicle = applicationContext.getBean(Vehicle)
        System.out.println(vehicle.start())
        // end::start[]

        expect:
        vehicle.start() == "Ford Engine Starting V8 [rodLength=7.0]"

        cleanup:
        applicationContext.close()
    }

    void "test start with invalid valid"() {
        when:
        def context = ApplicationContext.run(
                "my.engine.cylinders": "-10",
                "my.engine.crank-shaft.rod-length": "7.0"
        )
        context.getBean(Vehicle)

        then:
        def e = thrown(DependencyInjectionException)
        e.getCause().getMessage().contains("EngineConfig.cylinders - must be greater than or equal to 1")

        cleanup:
        context.close()
    }
}
