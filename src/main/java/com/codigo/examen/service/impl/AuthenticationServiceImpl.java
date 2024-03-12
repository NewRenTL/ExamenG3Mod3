package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.RolRepository;
import com.codigo.examen.repository.UsuarioRepository;
import com.codigo.examen.request.SignInRequest;
import com.codigo.examen.request.SignUpRequest;
import com.codigo.examen.response.AuthenticationResponse;
import com.codigo.examen.service.AuthenticationService;
import com.codigo.examen.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    @Override
    public Usuario signUpUser(SignUpRequest signUpRequest) {
        Usuario usuario = new Usuario();
        usuario.setUsername(signUpRequest.getEmail());
        usuario.setEmail(signUpRequest.getEmail());
        usuario.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));

        //Si no existe el rol USER se crea
        if(rolRepository.findByNombreRol("USER").isEmpty())
        {
            Rol newRol = new Rol();
            newRol.setNombreRol("USER");
            rolRepository.save(newRol);
        }

        //Setear nuestro set of del nombre de rol que rescatamos
        usuario.setRoles(Set.of(rolRepository.findByNombreRol("USER").get()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario signUpAdmin(SignUpRequest signUpRequest) {
        Usuario usuario = new Usuario();
        usuario.setUsername(signUpRequest.getUsuario());
        usuario.setEmail(signUpRequest.getEmail());
        usuario.setTelefono(signUpRequest.getTelefono());
        usuario.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
        if(rolRepository.findByNombreRol("ADMIN").isEmpty())
        {
            Rol newRol = new Rol();
            newRol.setNombreRol("ADMIN");
            rolRepository.save(newRol);
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public AuthenticationResponse signin(SignInRequest signInRequest) {
        System.out.println("Llego qui");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getUsername(),
                        signInRequest.getPassword()
                )
        );

        System.out.println("Llego aqui 2");

        var user =usuarioRepository.findByUsername(
                signInRequest.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("Invalid Username")
        );

        var jwt = jwtService.generateToken(user);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwt);
        return authenticationResponse;
    }
}
