-- 0. CRIAÇÃO DO BANCO DE DADOS
CREATE DATABASE IF NOT EXISTS biblioteca_db;
USE biblioteca_db;

-- 1. CRIAÇÃO DAS TABELAS

CREATE TABLE grupos_usuarios (
    id_grupo INT PRIMARY KEY,
    nome_grupo VARCHAR(50) NOT NULL UNIQUE COMMENT 'Ex: Admin, Bibliotecario, Membro'
);

INSERT INTO grupos_usuarios (id_grupo, nome_grupo) VALUES
(1, 'Admin'),
(2, 'Bibliotecario'),
(3, 'Membro');

CREATE TABLE usuarios (
    id_usuario VARCHAR(20) PRIMARY KEY COMMENT 'ID gerado por função, ex: USR2025102012345',
    id_grupo INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    hash_senha VARCHAR(255) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_grupo) REFERENCES grupos_usuarios(id_grupo)
);

CREATE TABLE autores (
    id_autor INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

CREATE TABLE editoras (
    id_editora INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL
);

CREATE TABLE categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nome_categoria VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE livros (
    id_livro INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(13) NOT NULL UNIQUE,
    titulo VARCHAR(255) NOT NULL,
    id_editora INT,
    ano_publicacao INT,
    quantidade_estoque INT NOT NULL DEFAULT 0,
    FOREIGN KEY (id_editora) REFERENCES editoras(id_editora)
);

CREATE TABLE livro_autor (
    id_livro INT,
    id_autor INT,
    PRIMARY KEY (id_livro, id_autor),
    FOREIGN KEY (id_livro) REFERENCES livros(id_livro) ON DELETE CASCADE,
    FOREIGN KEY (id_autor) REFERENCES autores(id_autor)
);

CREATE TABLE livro_categoria (
    id_livro INT,
    id_categoria INT,
    PRIMARY KEY (id_livro, id_categoria),
    FOREIGN KEY (id_livro) REFERENCES livros(id_livro) ON DELETE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
);

CREATE TABLE emprestimos (
    id_emprestimo VARCHAR(20) PRIMARY KEY COMMENT 'ID gerado por função, ex: EMP20251020123',
    id_livro INT NOT NULL,
    id_usuario VARCHAR(20) NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_vencimento DATE NOT NULL,
    data_devolucao DATE DEFAULT NULL,
    FOREIGN KEY (id_livro) REFERENCES livros(id_livro),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_livro INT NOT NULL,
    id_usuario VARCHAR(20) NOT NULL,
    data_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status_reserva ENUM('Ativa', 'Cancelada', 'Atendida') DEFAULT 'Ativa',
    FOREIGN KEY (id_livro) REFERENCES livros(id_livro),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);


-- 2. FUNÇÕES
SET GLOBAL log_bin_trust_function_creators = 1;

DELIMITER $$
CREATE FUNCTION fnc_gerar_id_usuario()
RETURNS VARCHAR(20)
BEGIN
    DECLARE novo_id VARCHAR(20);
    SET novo_id = CONCAT('USR', DATE_FORMAT(NOW(), '%Y%m%d'), LPAD(FLOOR(RAND() * 100000), 5, '0'));
    RETURN novo_id;
END$$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION fnc_verificar_pendencias_usuario(p_id_usuario VARCHAR(20))
RETURNS BOOLEAN
BEGIN
    DECLARE pendencias INT DEFAULT 0;
    SELECT COUNT(*) INTO pendencias FROM emprestimos
    WHERE id_usuario = p_id_usuario AND data_devolucao IS NULL AND data_vencimento < CURDATE();
    IF pendencias > 0 THEN RETURN TRUE; ELSE RETURN FALSE; END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION fnc_gerar_id_emprestimo()
RETURNS VARCHAR(20)
BEGIN
    DECLARE novo_id VARCHAR(20);
    DECLARE hoje VARCHAR(8);
    DECLARE contador INT;
    SET hoje = DATE_FORMAT(NOW(), '%Y%m%d');
    SELECT COUNT(*) + 1 INTO contador FROM emprestimos WHERE id_emprestimo LIKE CONCAT('EMP', hoje, '%');
    SET novo_id = CONCAT('EMP', hoje, LPAD(contador, 4, '0'));
    RETURN novo_id;
END$$
DELIMITER ;


-- 3. PROCEDURES
DELIMITER $$
CREATE PROCEDURE sp_registrar_devolucao(IN p_id_emprestimo VARCHAR(20))
BEGIN
    START TRANSACTION;
    UPDATE emprestimos SET data_devolucao = CURDATE() WHERE id_emprestimo = p_id_emprestimo;
    UPDATE livros SET quantidade_estoque = quantidade_estoque + 1
    WHERE id_livro = (SELECT id_livro FROM emprestimos WHERE id_emprestimo = p_id_emprestimo LIMIT 1);
    COMMIT;
END$$
DELIMITER ;


-- 4. TRIGGERS
DELIMITER $$
CREATE TRIGGER trg_atualizar_estoque_emprestimo
AFTER INSERT ON emprestimos
FOR EACH ROW
BEGIN
    UPDATE livros SET quantidade_estoque = quantidade_estoque - 1 WHERE id_livro = NEW.id_livro;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER trg_impedir_exclusao_autor_com_livros
BEFORE DELETE ON autores
FOR EACH ROW
BEGIN
    DECLARE livros_count INT;
    SELECT COUNT(*) INTO livros_count FROM livro_autor WHERE id_autor = OLD.id_autor;
    IF livros_count > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Não é possível excluir um autor que ainda possui livros cadastrados.';
    END IF;
END$$
DELIMITER ;


-- 5. ÍNDICES
CREATE INDEX idx_livro_titulo ON livros(titulo);
CREATE INDEX idx_emprestimo_usuario_ativo ON emprestimos(id_usuario, data_devolucao);


-- 6. VIEWS
CREATE VIEW vw_livros_disponiveis_catalogo AS
SELECT 
    l.id_livro, l.titulo, l.isbn, l.quantidade_estoque, e.nome AS editora,
    GROUP_CONCAT(DISTINCT a.nome SEPARATOR ', ') AS autores,
    GROUP_CONCAT(DISTINCT c.nome_categoria SEPARATOR ', ') AS categorias
FROM livros l
LEFT JOIN editoras e ON l.id_editora = e.id_editora
LEFT JOIN livro_autor la ON l.id_livro = la.id_livro
LEFT JOIN autores a ON la.id_autor = a.id_autor
LEFT JOIN livro_categoria lc ON l.id_livro = lc.id_livro
LEFT JOIN categorias c ON lc.id_categoria = c.id_categoria
WHERE l.quantidade_estoque > 0
GROUP BY l.id_livro, e.nome;

CREATE VIEW vw_emprestimos_atrasados AS
SELECT e.id_emprestimo, u.nome AS nome_usuario, u.email AS email_usuario,
    l.titulo AS livro, e.data_emprestimo, e.data_vencimento,
    DATEDIFF(CURDATE(), e.data_vencimento) AS dias_atraso
FROM emprestimos e
JOIN usuarios u ON e.id_usuario = u.id_usuario
JOIN livros l ON e.id_livro = l.id_livro
WHERE e.data_devolucao IS NULL AND e.data_vencimento < CURDATE();


-- 7. USUÁRIOS E CONTROLE DE ACESSO
CREATE USER IF NOT EXISTS 'app_biblioteca'@'localhost' IDENTIFIED BY 'uma_senha_forte_123';
GRANT SELECT, INSERT, UPDATE, DELETE ON biblioteca_db.* TO 'app_biblioteca'@'localhost';
GRANT EXECUTE ON FUNCTION biblioteca_db.fnc_gerar_id_usuario TO 'app_biblioteca'@'localhost';
GRANT EXECUTE ON FUNCTION biblioteca_db.fnc_verificar_pendencias_usuario TO 'app_biblioteca'@'localhost';
GRANT EXECUTE ON FUNCTION biblioteca_db.fnc_gerar_id_emprestimo TO 'app_biblioteca'@'localhost';
GRANT EXECUTE ON PROCEDURE biblioteca_db.sp_registrar_devolucao TO 'app_biblioteca'@'localhost';

CREATE USER IF NOT EXISTS 'app_leitura'@'localhost' IDENTIFIED BY 'outra_senha_segura_456';
GRANT SELECT ON biblioteca_db.vw_livros_disponiveis_catalogo TO 'app_leitura'@'localhost';
GRANT SELECT ON biblioteca_db.vw_emprestimos_atrasados TO 'app_leitura'@'localhost';
GRANT SELECT ON biblioteca_db.livros TO 'app_leitura'@'localhost';
GRANT SELECT ON biblioteca_db.autores TO 'app_leitura'@'localhost';
GRANT SELECT ON biblioteca_db.editoras TO 'app_leitura'@'localhost';

FLUSH PRIVILEGES;

-- 8. DADOS DE TESTE (OPCIONAL, MAS RECOMENDADO)
INSERT INTO autores (nome) VALUES ('Frank Herbert'),('J.R.R. Tolkien'),('Robert C. Martin');
INSERT INTO editoras (nome) VALUES ('Aleph'),('HarperCollins'),('Alta Books');
INSERT INTO categorias (nome_categoria) VALUES ('Ficção Científica'),('Fantasia'),('Tecnologia');

INSERT INTO livros (isbn, titulo, id_editora, ano_publicacao, quantidade_estoque) VALUES
('9788576572975', 'Duna', 1, 2017, 10),
('9788595084128', 'O Hobbit', 2, 2019, 15),
('9788576082675', 'Código Limpo', 3, 2009, 5);

INSERT INTO livro_autor (id_livro, id_autor) VALUES (1, 1), (2, 2), (3, 3);
INSERT INTO livro_categoria (id_livro, id_categoria) VALUES (1, 1), (2, 2), (3, 3);