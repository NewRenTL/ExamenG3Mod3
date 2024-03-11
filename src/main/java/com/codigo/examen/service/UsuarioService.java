package com.codigo.examen.service;

import com.codigo.examen.entity.Usuario;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {
    ResponseEntity<Usuario> createUsuario(Usuario usuario);
    ResponseEntity<Usuario> getUsuarioById(Long id);
    ResponseEntity<Usuario> updateUsuario(Long id, Usuario usuario);
    ResponseEntity<Usuario> deleteUsuario(Long id);
}
