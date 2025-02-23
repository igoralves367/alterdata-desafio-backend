INSERT INTO categorias (id, nome) VALUES
    (1, 'Ferramentas'),
    (2, 'Jardinagem'),
    (3, 'Equipamentos');

INSERT INTO produtos (id, nome, descricao, referencia, valor_unitario, categoria_id) VALUES
    (
        1,
        'Motosserra Semi Profissional Gasolina 50,2Cc Tcs53X-20',
        'As Motosserras Toyama Foram Desenvolvidas E Fabricadas Especialmente Para O Usuário Que Busca Qualidade De Equipamento Com Baixo Custo De Aquisição.',
        'TCS53H18',
        399.90,
        1 
    ),
    (
        2,
        'Tesoura Para Poda + Serrote de Poda',
        'Tesourão para Poda Linha Bronze, para podar frutíferas, flores e plantas ornamentais.',
        'KIT140',
        163.54,
        2 
    ),
    (
        3,
        'Kit para Jardinagem Horta Tramontina 04 Peças com Luva',
        'Kit completo para pequenas hortas, jardins e floricultura.',
        '55374237',
        57.34,
        3 
    );
