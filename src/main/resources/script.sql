-- ETL
set @cnt = 0;
insert into Atributo
select (@cnt:=@cnt+1) as id, a.nome as valor from atributo a;

set @cnt = 0;
insert into Categoria
select (@cnt:=@cnt+1), c.nome from categoria c order by c.hierarquia;

select (@cnt:=@cnt+1), c.hierarquia from categoria c
where c.hierarquia is not null;

select * from categoriaatributo ca
join atributo a on a.id = ca.Atributo_Id
join categoria c on c.id = ca.Categoria_Id;

set @cnt = 0;
insert into Produto
select (@cnt:=@cnt+1), now(), p.descricao, '', p.nome, 1 from produto p;

select * from Produto;

insert into ProdutoCategoria
select pp.id, cc.id from produto p
join categoria c on p.Categoria_Id = c.id
join Categoria cc on c.nome = cc.nome
join Produto pp on pp.nome = p.nome;

select * from ProdutoCategoria;

select * from produto;

SELECT count(*), c.nome, c.id FROM ecommerce.Produto p
join ecommerce.ProdutoCategoria pa
on pa.produto_id = p.id
join ecommerce.Categoria c
on c.id = pa.categoria_id
group by c.nome
-- having count(*) < 10
order by count(*) desc;

delete from ProdutoCategoria where categoria_id in (7, 8, 10, 1);

delete from Categoria where id not in (
select categoria_id from ProdutoCategoria
);

select * from Produto p where id not in (
select produto_id from ProdutoCategoria
);

delete from Produto where id not in (
select produto_id from ProdutoCategoria
);

select count(*), pc.categoria_id from ecommerce.ProdutoCategoria pc
group by pc.categoria_id
order by count(*);

delete from ecommerce.ProdutoCategoria
where categoria_id in (
select pc.categoria_id from ecommerce.ProdutoCategoria pc
join ecommerce.Produto p on p.id = pc.produto_id
group by pc.categoria_id
having count(*) < 10);

-- order by count(*) desc;


select * from Categoria;
