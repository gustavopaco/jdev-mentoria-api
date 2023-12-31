package com.pacoprojects.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoPessoa {

    FISICA("FISICA"),
    JURIDICA("JURIDICA"),
    JURIDICA_FORNECEDOR("FORNECEDOR");

    private final String descricao;

    @Override
    public String toString() {
        return this.descricao;
    }
}
