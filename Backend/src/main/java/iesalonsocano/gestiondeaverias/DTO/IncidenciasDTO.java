package iesalonsocano.gestiondeaverias.DTO;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidenciasDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String estado; // Usamos String para mapear el Enum /We use a String to map the Enum.
    private LocalDateTime fechaReporte;
    private LocalDateTime fechaCierre;

    private Long usuarioId;
    private Long aulaId;
}