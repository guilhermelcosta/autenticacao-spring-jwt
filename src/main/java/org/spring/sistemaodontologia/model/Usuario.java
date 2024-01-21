package org.spring.sistemaodontologia.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.spring.sistemaodontologia.util.constantes.ConstantesMsgErroUtil.*;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_USUARIO")
public class Usuario extends RepresentationModel<Usuario> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private UUID id;

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_ENDERECO")
    private Endereco endereco;

    @Column(name = "PERFIL_USUARIO", nullable = false)
    @JsonProperty("perfil_usuario")
    private Integer perfilUsuario;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "RG", unique = true, nullable = false)
    @Pattern(regexp = "(^\\d{1,2}).?(\\d{3}).?(\\d{2})-?(\\d|X|x$)", message = MSG_ERRO_RG)
    private String rg;

    @CPF
    @Column(name = "CPF", unique = true, nullable = false)
    @Pattern(regexp = "(^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$)", message = MSG_ERRO_CPF)
    private String cpf;

    @Column(name = "DATA_NASCIMENTO", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "EMAIL", unique = true, nullable = false)
    @Pattern(regexp = "^[a-z0-9.]+@[a-z0-9]+\\.[a-z]+\\.?([a-z]+)?$", message = MSG_ERRO_EMAIL)
    private String email;

    @Column(name = "SENHA", nullable = false)
    @Size(min = 6, max = 200, message = MSG_ERRO_SENHA)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @Column(name = "TELEFONE", nullable = false)
    @Pattern(regexp = "(^[0-9]{2})?(\\s|-)?(9?[0-9]{4})-?([0-9]{4}$)", message = MSG_ERRO_TELEFONE)
    private String telefone;

    @Column(name = "DATA_CRIACAO", nullable = false, updatable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "DATA_ULTIMA_MODIFICACAO", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("data_ultima_modificacao")
    private LocalDateTime dataUltimaModificacao;
}
