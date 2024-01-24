package org.spring.autenticacaojwt.componentes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import static java.lang.Runtime.getRuntime;
import static java.lang.String.format;
import static org.spring.autenticacaojwt.util.constantes.ConstantesNumUtil.CEM;
import static org.spring.autenticacaojwt.util.constantes.ConstantesNumUtil.VINTE_E_CINCO;
import static org.spring.autenticacaojwt.util.constantes.ConstantesTopicosUtil.INDICADOR_SAUDE_SISTEMA_COMP;
import static org.springframework.boot.actuate.health.Health.down;
import static org.springframework.boot.actuate.health.Health.up;


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
        long memoriaLiberada = getRuntime().freeMemory();
        long memoriaTotal = getRuntime().totalMemory();
        double percentualMemoriaLiberada = ((double) memoriaLiberada / (double) memoriaTotal) * CEM;

        if (percentualMemoriaLiberada > VINTE_E_CINCO)
            return up()
                    .withDetail("memoria_liberada", format("%s bytes", memoriaLiberada))
                    .withDetail("memoria_total", format("%s bytes", memoriaTotal))
                    .withDetail("percentual_memoria_liberada", format("%s%%", percentualMemoriaLiberada))
                    .build();
        else
            return down()
                    .withDetail("memoria_liberada", format("%s bytes", memoriaLiberada))
                    .withDetail("memoria_total", format("%s bytes", memoriaTotal))
                    .withDetail("percentual_memoria_liberada", format("%s%%", percentualMemoriaLiberada))
                    .build();
    }
}
