ALTER TABLE produtos
    ADD COLUMN categoria_id BIGINT;

ALTER TABLE produtos
    ADD CONSTRAINT fk_categoria
        FOREIGN KEY (categoria_id) REFERENCES categorias(id);
