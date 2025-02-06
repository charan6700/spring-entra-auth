package com.charan6700.auth.dto;

import lombok.Data;

import java.util.List;

@Data
public class GraphUserDataResponse {

    private String odataContext;
    private List<User> value;
}
