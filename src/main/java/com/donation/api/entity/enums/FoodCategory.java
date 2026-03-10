package com.donation.api.entity.enums;

public enum FoodCategory {
    GRAOS_CEREAIS("Grãos e Cereais"),
    HORTIFRUTI("Hortifruti"),
    LATICINIOS("Laticínios"),
    PROTEINAS("Proteínas"),
    ENLATADOS_CONSERVAS("Enlatados e Conservas"),
    BEBIDAS("Bebidas"),
    PADARIA_CONFEITARIA("Padaria e Confeitaria"),
    TEMPEROS_CONDIMENTOS("Temperos e Condimentos"),
    REFEICAO_PRONTA("Refeição Pronta"),
    OUTROS("Outros");

    private final String descricao;

    FoodCategory(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
