package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.UsuarioRepository;
import com.codigo.examen.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return usuarioRepository.findByUsername(username).orElseThrow(
                        ()-> new UsernameNotFoundException("User not found")
                );
            }
        };
    }

    @Override
    public ResponseEntity<Usuario> createUsuario(Usuario usuario) {
        Usuario usersave = usuarioRepository.save(usuario);
        return ResponseEntity.ok(usersave);
    }

    @Override
    public ResponseEntity<Usuario> getUsuarioById(Long id) {
        Optional<Usuario> existUser = usuarioRepository.findById(id);
        if(existUser.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(existUser.get());
    }

    @Override
    public ResponseEntity<Usuario> updateUsuario(Long id, Usuario usuario) {
        boolean exists = usuarioRepository.existsById(id);
        if(exists)
        {
            Usuario entity = usuarioRepository.findById(id).get();
            //So I'll update this entity
            entity.setUsername(usuario.getUsername());
            entity.setTelefono(usuario.getTelefono());
            entity.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
            entity.setEmail(usuario.getEmail());
            usuarioRepository.save(entity);
        }
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<Usuario> deleteUsuario(Long id) {
        boolean exists = usuarioRepository.existsById(id);
        if(exists)
        {
            Usuario entity =usuarioRepository.findById(id).get();
            //La cuenta expiro
            entity.setAccountnonexpire(false);
            usuarioRepository.save(entity);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(entity);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
