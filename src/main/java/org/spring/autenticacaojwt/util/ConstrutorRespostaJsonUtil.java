package org.spring.autenticacaojwt.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.spring.autenticacaojwt.excecao.lancaveis.ConstrutorRespostaJsonException;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

@UtilityClass
public class ConstrutorRespostaJsonUtil {

    /**
     * Constrói o Json de resposta
     *
     * @param chaves     chaves do Json
     * @param argumentos argumentos do Json
     * @return Json a partir das chaves e argumentos
     */
    public static Map<String, Object> construirRespostaJSON(List<String> chaves, List<Object> argumentos) {

        validarTamanhoListas(chaves, argumentos);

        return range(0, chaves.size())
                .boxed()
                .collect(toMap(chaves::get, argumentos::get));
    }

    /**
     * Valida se a quantidade de chaves é igual ao número de argumentos
     *
     * @param chaves     chaves do Json
     * @param argumentos argumentos do Json
     */
    private static void validarTamanhoListas(@NotNull List<String> chaves, @NotNull List<Object> argumentos) {
        if (chaves.size() != argumentos.size())
            throw new ConstrutorRespostaJsonException("O número de chaves deve ser igual ao número de argumentos.");
    }
}
