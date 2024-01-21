package org.spring.autenticacaojwt.componentes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import static org.spring.autenticacaojwt.util.constantes.ConstantesNumUtil.CEM;
import static org.spring.autenticacaojwt.util.constantes.ConstantesNumUtil.VINTE_E_CINCO;
import static org.spring.autenticacaojwt.util.constantes.ConstantesTopicosUtil.INDICADOR_SAUDE_SISTEMA_COMP;


@Slf4j(topic = INDICADOR_SAUDE_SISTEMA_COMP)
@Component
public class IndicadorSaudeSistemaComp implements HealthIndicator {

    /**
     * Fornece informações sobre a saúde da aplicação (endpoint exposto)
     *
     * @return informações sobre a saúde da aplicação
     */
    @Override
    public Health health() {

        log.info(">>> indicadorSaudeSistema: retornando informações de saúde do sistema");
        long memoriaLiberada = Runtime.getRuntime().freeMemory();
        long memoriaTotal = Runtime.getRuntime().totalMemory();
        double percentualMemoriaLiberada = ((double) memoriaLiberada / (double) memoriaTotal) * CEM;

        if (percentualMemoriaLiberada > VINTE_E_CINCO)
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
