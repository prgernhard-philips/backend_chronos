package com.agenciacronos.siteinstitucional.Services;

import com.agenciacronos.siteinstitucional.Repositories.UsuarioRepository;
import com.agenciacronos.siteinstitucional.exceptions.UsuarioCadastradoException;
import com.agenciacronos.siteinstitucional.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioDetailService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
        return User
                .builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getTipoUsuario().toString()).build();
    }

    public Usuario salvar(Usuario usuario) {
        boolean exists = usuarioRepository.existsByUsername(usuario.getUsername());
        if (exists){
            throw new UsuarioCadastradoException(usuario.getUsername());
        }
        return usuarioRepository.save(usuario);
    }
}
