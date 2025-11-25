package co.edu.poli.encuestas.presentacion.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorExcepcionesGlobal {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String nombreCampo = ((FieldError) error).getField();
            String mensajeError = error.getDefaultMessage();
            errores.put(nombreCampo, mensajeError);
        });
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Error de validación");
        respuesta.put("errores", errores);
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        
        return ResponseEntity.badRequest().body(respuesta);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> manejarRuntimeException(RuntimeException ex) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", ex.getMessage());
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        
        return ResponseEntity.badRequest().body(respuesta);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarExcepcionGeneral(Exception ex) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Error interno del servidor");
        respuesta.put("detalle", ex.getMessage());
        respuesta.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}

