package org.spring.autenticacaojwt.componentes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Slf4j(topic = "INDICADOR_SAUDE_SISTEMA")
@Component
public class IndicadorSaudeSistemaComp implements HealthIndicator {

    @Override
    public Health health() {

        log.info(">>> indicadorSaudeSistema: retornando informações de saúde do sistema");
        long memoriaLiberada = Runtime.getRuntime().freeMemory();
        long memoriaTotal = Runtime.getRuntime().totalMemory();
        double percentualMemoriaLiberada = ((double) memoriaLiberada / (double) memoriaTotal) * 100;

        if (percentualMemoriaLiberada > 25)
            return Health.up()
                    .withDetail("memoria_liberada", String.format("%s bytes", memoriaLiberada))
                    .withDetail("memoria_total", String.format("%s bytes", memoriaTotal))
                    .withDetail("percentual_memoria_liberada", String.format("%s%%", percentualMemoriaLiberada))
                    .build();
        else
            return Health.down()
                    .withDetail("memoria_liberada", String.format("%s bytes", memoriaLiberada))
                    .withDetail("memoria_total", String.format("%s bytes", memoriaTotal))
                    .withDetail("percentual_memoria_liberada", String.format("%s%%", percentualMemoriaLiberada))
                    .build();
    }
}
