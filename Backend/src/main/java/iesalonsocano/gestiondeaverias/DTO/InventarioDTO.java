package iesalonsocano.gestiondeaverias.DTO;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String codigoQR;
    private String estado;       // Enum como String / We use a String to map the Enum.
    private LocalDateTime fechaIngreso;
    private Long aulaId;
}