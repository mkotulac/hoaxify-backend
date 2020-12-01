package com.hoaxify.hoaxify.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Created by Martin Kotulac
 * on 01/12/2020
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse {

    @NotNull
    private String message;

}
