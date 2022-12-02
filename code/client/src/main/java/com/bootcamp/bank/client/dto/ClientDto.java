package com.bootcamp.bank.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDto {

    @JsonIgnore
    private String id;

    @NotBlank(message = "No puede ser vacío o nulo")
    private String tipoCliente;

    @NotBlank(message = "No puede ser vacío o nulo")
    @Length(min = 2, max = 3, message = "Debe tener una longitud de 2 a 3 caracteres como máximo")
    private String tipoDocumento;

    @NotBlank(message = "No puede ser vacío o nulo")
    @Length(min = 8, max = 11, message = "Debe tener una longitud de 8 a 11 caracteres como máximo")
    private String numeroDocumento;

    private String razonSocial;

    private String nombre;

    private String apellidoPaterno;

    private String apellidoMaterno;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    private String genero;
}
