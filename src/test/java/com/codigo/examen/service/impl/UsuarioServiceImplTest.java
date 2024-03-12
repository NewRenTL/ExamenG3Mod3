package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.UsuarioRepository;
import com.codigo.examen.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsuarioServiceImplTest {
    private UsuarioService usuarioService;
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        usuarioService = new UsuarioServiceImpl(usuarioRepository);
    }

    @Test
    void createUsuarioTest() {
        Usuario usuario = new Usuario(); // crea un usuario de ejemplo
        when(usuarioRepository.save(any())).thenReturn(usuario);

        ResponseEntity<Usuario> response = usuarioService.createUsuario(usuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getUsuarioByIdTest() {
        Long userId = 1L;
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(new Usuario()));

        ResponseEntity<Usuario> response = usuarioService.getUsuarioById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateUsuarioTest() {
        Long userId = 1L;
        when(usuarioRepository.existsById(userId)).thenReturn(true);
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(new Usuario()));

        ResponseEntity<Usuario> response = usuarioService.updateUsuario(userId, new Usuario());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteUsuarioTest() {
        Long userId = 1L;
        when(usuarioRepository.existsById(userId)).thenReturn(true);
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(new Usuario()));

        ResponseEntity<Usuario> response = usuarioService.deleteUsuario(userId);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isAccountnonexpire());
    }



}