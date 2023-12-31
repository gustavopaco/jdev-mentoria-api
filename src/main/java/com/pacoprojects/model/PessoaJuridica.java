package com.pacoprojects.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pessoa_juridica",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_cnpj", columnNames = "cnpj"),
                @UniqueConstraint(name = "unique_inscricao_estadual", columnNames = "inscricao_estadual")})
@Entity
@PrimaryKeyJoinColumn(name = "pessoa_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "pessoa_juridica_fk", value = ConstraintMode.CONSTRAINT))
public class PessoaJuridica extends Pessoa {

    @CNPJ(message = "CNPJ obrigatório.")
    @NotBlank(message = "CNPJ obrigatório.")
    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @NotBlank(message = "Inscrição Estadual obrigatório.")
    @Column(name = "inscricao_estadual", nullable = false)
    private String inscricaoEstadual;

    @Column(name = "inscricao_municipal")
    private String inscricaoMunicipal;

    @NotBlank(message = "Nome Fantasia obrigatório.")
    @Column(name = "nome_fantasia", nullable = false)
    private String nomeFantasia;

    @NotBlank(message = "Razão Social obrigatório.")
    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @Column(name = "categoria")
    private String categoria;

}
