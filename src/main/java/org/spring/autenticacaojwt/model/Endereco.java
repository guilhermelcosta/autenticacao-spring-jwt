package org.spring.autenticacaojwt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.spring.autenticacaojwt.enums.UnidadeFederativa;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import static org.spring.autenticacaojwt.util.constantes.ConstantesErroValidadorUtil.MSG_ERRO_CEP;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_ENDERECO")
public class Endereco implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private UUID id;

    @ToString.Exclude
    @OneToOne(mappedBy = "endereco")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Usuario usuario;

    @Column(name = "RUA", nullable = false)
    private String rua;

    @Column(name = "NUMERO", nullable = false)
    private String numero;

    @Column(name = "BAIRRO", nullable = false)
    private String bairro;

    @Column(name = "COMPLEMENTO")
    private String complemento;

    @Column(name = "CEP", nullable = false)
    @Pattern(regexp = "(^[0-9]{5})-([0-9]{3}$)", message = MSG_ERRO_CEP)
    private String cep;

    @Column(name = "CIDADE", nullable = false)
    private String cidade;

    @Column(name = "ESTADO", nullable = false)
    @Enumerated(EnumType.STRING)
    private UnidadeFederativa estado;
}
