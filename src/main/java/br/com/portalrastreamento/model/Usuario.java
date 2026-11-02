package br.com.portalrastreamento.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity // Diz que esta classe vai virar uma tabela no banco
@Table(name = "usuarios") // Nome da tabela no banco
public class Usuario implements Serializable {

    @Id // Diz que este é o campo chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento (1, 2, 3...)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 50)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING) // Salva o nome do Enum (ADMINISTRADOR/OPERADOR) em texto
    @Column(nullable = false)
    private PerfilUsuario perfil;

    // Clique com o botão direito no código > Insert Code > Getter and Setter...
    // Selecione todos os campos e clique em Generate. 
    // Cole eles aqui embaixo!
}