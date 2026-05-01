package iesalonsocano.gestiondeaverias.Controllers;

import iesalonsocano.gestiondeaverias.DTO.InventarioDTO;
import iesalonsocano.gestiondeaverias.entity.InventarioEntity;
import iesalonsocano.gestiondeaverias.Services.InventarioService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión del inventario de equipamiento informático.
 * <p>
 * Proporciona endpoints para realizar operaciones CRUD sobre el inventario
 * de equipos del centro educativo (computadoras, impresoras, proyectores, etc.).
 * </p>
 *
 * <p>
 * Seguridad:
 * <ul>
 *   <li>GET - Accesible para cualquier usuario autenticado</li>
 *   <li>POST, PUT - Solo ADMIN o TECNICO</li>
 *   <li>DELETE - Solo ADMIN</li>
 * </ul>
 * </p>
 *
 * <p>
 * Base URL: {@code /api/inventario}
 * </p>
 *
 * @author IES Alonso Cano
 * @version 1.0.0
 * @see InventarioService
 * @see InventarioDTO
 */
@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Value("${app.frontend-base-url:${FRONTEND_BASE_URL:}}")
    private String frontendBaseUrl;

    /**
     * Obtiene la lista completa de artículos del inventario.
     *
     * @return ResponseEntity con lista de InventarioDTO
     */
    @GetMapping
    public ResponseEntity<List<InventarioDTO>> getAllInventario() {
        List<InventarioDTO> dtos = inventarioService.findAll().stream()
                .map(InventarioDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/export/csv")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<String> exportInventarioCsv() {
        List<InventarioDTO> items = inventarioService.findAll().stream()
                .map(InventarioDTO::fromEntity)
                .collect(Collectors.toList());

        StringJoiner csv = new StringJoiner("\n");
        csv.add("id,nombre,descripcion,codigoQR,estado,nombreAula,fechaIngreso");

        for (InventarioDTO item : items) {
            csv.add(String.join(",",
                    safeCsv(item.getId()),
                    safeCsv(item.getNombre()),
                    safeCsv(item.getDescripcion()),
                    safeCsv(item.getCodigoQR()),
                    safeCsv(item.getEstado()),
                    safeCsv(item.getNombreAula()),
                    safeCsv(item.getFechaIngreso())
            ));
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=activos.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv.toString());
    }

    private String safeCsv(Object value) {
        String text = value == null ? "" : String.valueOf(value);
        return '"' + text.replace("\"", "\"\"") + '"';
    }

    /**
     * Obtiene un artículo específico del inventario por su ID.
     *
     * @param id identificador del artículo
     * @return ResponseEntity con InventarioDTO si existe, o 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventarioDTO> getInventarioById(@PathVariable Long id) {
        return inventarioService.findById(id)
                .map(item -> ResponseEntity.ok(InventarioDTO.fromEntity(item)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/qr/{codigoQR}")
    public ResponseEntity<InventarioDTO> getInventarioByCodigoQr(@PathVariable String codigoQR) {
        InventarioEntity inventario = inventarioService.findByCodigo_QR(codigoQR);
        if (inventario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(InventarioDTO.fromEntity(inventario));
    }

    @GetMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> downloadQr(@PathVariable Long id) {
        return inventarioService.findById(id)
                .map(item -> {
                    try {
                        String codigoQR = ensureCodigoQr(item);
                        byte[] qr = generateQrPng(buildQrTargetUrl(codigoQR));
                        return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=qr-" + codigoQR + ".png")
                                .contentType(MediaType.IMAGE_PNG)
                                .body(qr);
                    } catch (WriterException | IOException e) {
                        return ResponseEntity.internalServerError().<byte[]>build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo artículo en el inventario.
     * <p>
     * Acceso: Solo ADMIN o TECNICO
     * </p>
     *
     * @param inventario entidad con los datos del artículo
     * @return ResponseEntity con InventarioDTO creado y código HTTP 201 Created
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<InventarioDTO> createInventario(@Valid @RequestBody InventarioEntity inventario) {
        if (inventario.getCodigoQR() == null || inventario.getCodigoQR().isBlank()) {
            inventario.setCodigoQR(generateUniqueCodigoQr());
        }
        InventarioEntity nuevoItem = inventarioService.save(inventario);
        return ResponseEntity.status(HttpStatus.CREATED).body(InventarioDTO.fromEntity(nuevoItem));
    }

    /**
     * Actualiza los datos de un artículo del inventario.
     * <p>
     * Acceso: Solo ADMIN o TECNICO
     * </p>
     *
     * @param id identificador del artículo
     * @param details entidad con los nuevos datos
     * @return ResponseEntity con InventarioDTO actualizado o 404 Not Found
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<InventarioDTO> updateInventario(@PathVariable Long id, @Valid @RequestBody InventarioEntity details) {
        return inventarioService.findById(id)
                .map(item -> {
                    item.setNombre(details.getNombre());
                    item.setDescripcion(details.getDescripcion());
                    item.setCodigoQR(details.getCodigoQR());
                    item.setEstado(details.getEstado());
                    item.setAula(details.getAula());

                    InventarioEntity actualizado = inventarioService.save(item);
                    return ResponseEntity.ok(InventarioDTO.fromEntity(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un artículo del inventario.
     * <p>
     * Acceso: Solo ADMIN
     * </p>
     *
     * @param id identificador del artículo a eliminar
     * @return ResponseEntity vacío con código 204 No Content o 404 Not Found
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInventario(@PathVariable Long id) {
        if (inventarioService.findById(id).isPresent()) {
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private String ensureCodigoQr(InventarioEntity item) {
        if (item.getCodigoQR() != null && !item.getCodigoQR().isBlank()) {
            return item.getCodigoQR();
        }

        item.setCodigoQR(generateUniqueCodigoQr());
        return inventarioService.save(item).getCodigoQR();
    }

    private String generateUniqueCodigoQr() {
        String codigo;
        do {
            codigo = "ACT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (inventarioService.findByCodigo_QR(codigo) != null);
        return codigo;
    }

    private byte[] generateQrPng(String value) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(value, BarcodeFormat.QR_CODE, 320, 320);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }

    private String buildQrTargetUrl(String codigoQR) {
        String qrPath = "/activos/qr/" + codigoQR;

        if (frontendBaseUrl != null && !frontendBaseUrl.isBlank()) {
            String base = frontendBaseUrl.trim().endsWith("/")
                    ? frontendBaseUrl.trim().substring(0, frontendBaseUrl.trim().length() - 1)
                    : frontendBaseUrl.trim();
            return base + qrPath;
        }

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .replacePath(qrPath)
                .build()
                .toUriString();
    }
}
