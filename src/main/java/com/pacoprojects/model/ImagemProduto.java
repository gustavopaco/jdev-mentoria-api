package com.pacoprojects.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "imagem_produto")
@Entity
public class ImagemProduto {

    @Id
    @SequenceGenerator(name = "sequence_imagem_produto", sequenceName = "sequence_imagem_produto", allocationSize = 1)
    @GeneratedValue(generator = "sequence_imagem_produto", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false)
    private Long id;

    @NotBlank(message = "Imagem original obrigatório.")
    @Column(name = "imagem_original", columnDefinition = "TEXT", nullable = false)
    private String imagemOriginal;

    @NotBlank(message = "Imagem miniatura obrigatório")
    @Column(name = "imagem_miniatura", columnDefinition = "TEXT", nullable = false)
    private String imagemMiniatura;

    @JsonIgnore
    @ManyToOne(targetEntity = Produto.class)
    @JoinColumn(
            name = "produto_id", nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "produto_id_fk", value = ConstraintMode.CONSTRAINT))
    private Produto produto;

    @JsonIgnore
    @ManyToOne(targetEntity = Pessoa.class)
    @JoinColumn(
            name = "empresa_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "empresa_id_fk", value = ConstraintMode.CONSTRAINT))
    private PessoaJuridica empresa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ImagemProduto that = (ImagemProduto) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
