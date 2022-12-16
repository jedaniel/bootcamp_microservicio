package com.bootcamp.bank.client.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"tipoDocumento","numeroDocumento"})
@Document(value = "client")
@CompoundIndex(def = "{'tipo_documento' : 1, 'numero_documento'  : 1}", unique = true)
public class ClientEntity {
    @Id
    private String id;

    @Field("tipo_cliente")
    private String tipoCliente;

    @Field("tipo_documento")
    private String tipoDocumento;

    @Field("numero_documento")
    private String numeroDocumento;

    @Field("razon_social")
    private String razonSocial;

    @Field("nombre")
    private String nombre;

    @Field("apellido_paterno")
    private String apellidoPaterno;

    @Field("apellido_materno")
    private String apellidoMaterno;

    @Field("fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Field("genero")
    private String genero;

    @Field("perfil")
    private String perfil;

}
