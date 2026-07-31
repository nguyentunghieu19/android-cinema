package com.cinema.backend.dto.request;

public class AddComboRequest {

    private Integer comboId;

    private Integer quantity;

    public AddComboRequest() {
    }

    public Integer getComboId() {
        return comboId;
    }

    public void setComboId(Integer comboId) {
        this.comboId = comboId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}