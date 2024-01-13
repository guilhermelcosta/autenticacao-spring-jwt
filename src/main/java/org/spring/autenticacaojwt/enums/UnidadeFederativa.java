package org.spring.autenticacaojwt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum UnidadeFederativa {

    ACRE("AC"),
    ALAGOAS("AL"),
    AMAPA("AP"),
    AMAZONAS("AM"),
    BAHIA("BA"),
    CEARA("CE"),
    DISTRITO_FEDERAL("DF"),
    ESPIRITO_SANTO("ES"),
    GOIAS("GO"),
    MARANHAO("MA"),
    MATO_GROSSO("MT"),
    MATO_GROSSO_DO_SUL("MS"),
    MINAS_GERAIS("MG"),
    PARA("PA"),
    PARAIBA("PB"),
    PARANA("PR"),
    PERNAMBUCO("PE"),
    PIAUI("PI"),
    RIO_DE_JANEIRO("RJ"),
    RIO_GRANDE_DO_NORTE("RN"),
    RIO_GRANDE_DO_SUL("RS"),
    RONDONIA("RO"),
    RORAIMA("RR"),
    SANTA_CATARINA("SC"),
    SAO_PAULO("SP"),
    SERGIPE("SE"),
    TOCANTINS("TO");

    private final String sigla;

    /**
     * Obtém a UnidadeFederativa relacionado a uma sigla UF
     *
     * @param sigla sigla da UF
     * @return objeto UnidadeFederativa relacionado a uma sigla UF
     */
    public UnidadeFederativa getEstadoDaSigla(String sigla) {
        return Arrays.stream(values())
                .filter(uf -> uf.sigla.equals(sigla))
                .findFirst()
                .orElse(null);
    }

}
