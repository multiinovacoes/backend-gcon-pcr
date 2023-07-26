package br.com.multiinovacoes.gcon.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Classe responsável por obter os erros da API
 */
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ErroDTO  {

    private int status;
    private String message;
    private Date timestamp;

}