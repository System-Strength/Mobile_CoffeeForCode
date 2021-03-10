-- MySQL Workbench Synchronization
-- Generated: 2021-03-7 23:20
-- Model: Coffee For Code
-- Version: 1.1
-- Project: Sync with restAPI by SystemStrength
-- Author: SystemStrength

/****** Comandos abaixo devem ser utilizados apenas em conexao direta com a api  ******/
drop database heroku_88863b9257990d2;

create database `heroku_88863b9257990d2`  DEFAULT CHARACTER SET utf8  DEFAULT COLLATE utf8_general_ci ;

use heroku_88863b9257990d2;

/******	Para test local utilizar os comandos a baixo ******/
create database `db_cfc` DEFAULT CHARACTER SET utf8  DEFAULT COLLATE utf8_general_ci ;
use db_cfc;
/**********************************************************************************/

create table tbl_account(
    id_user int primary key auto_increment,
    email varchar(256) not null,
    rg_user varchar(12) not null,
    phone_user VARCHAR(15) ,
    password varchar(256) not null,
    partner BIT default 0
)DEFAULT CHARACTER SET utf8  DEFAULT COLLATE utf8_general_ci;

create table tbl_category(
    cd_cat int primary key auto_increment,
    nm_cat varchar(50) not null
)DEFAULT CHARACTER SET utf8  DEFAULT COLLATE utf8_general_ci;

create table tbl_menu(
    cd_prod int primary key auto_increment,
    img_prod mediumblob not null,
    nm_prod varchar(50) not null,
    price_prod decimal(6, 2) not null,
    qntd_prod int not null,
    cat_prod varchar(50) not null,
    date_prod date
)DEFAULT CHARACTER SET utf8  DEFAULT COLLATE utf8_general_ci;
insert into tbl_menu (nm_prod, price_prod, cat_prod, qntd_prod, img_prod) values ( "Expresso", "5.00", "Café", "1", "/expresso.jpg");


create table tbl_purchase(
    cd_purchase int primary key auto_increment,
    cpf_user varchar(14), 
    address_user varchar(50),
    HomeNumber_user varchar(40),
    apt_block_user varchar(10),
    PayFormat_user varchar(20) not null
)DEFAULT CHARACTER SET utf8  DEFAULT COLLATE utf8_general_ci;

create table tbl_WorkWithUS(
    cd_user int not null primary key auto_increment,
    nm_user varchar(50) not null,
    phone_user varchar(20) not null,
    email_user varchar(40) not null
)DEFAULT CHARACTER SET utf8  DEFAULT COLLATE utf8_general_ci;


create table tbl_manager(
    nm_manager varchar(50) not null,
    cpf_manager varchar(14) not null,
    password_manager varchar(12)  primary key not null
)DEFAULT CHARACTER SET utf8  DEFAULT COLLATE utf8_general_ci;

create table tbl_employees(
    id_employees int primary key auto_increment,
    nm_employees varchar(50) not null,
    cg_employees varchar(50) not null,
    email_employees varchar(40) not null,
    cpf_employees varchar(14) not null,
    addres_employees varchar(50) not null,
    phone_employees varchar(15) not null
)DEFAULT CHARACTER SET utf8  DEFAULT COLLATE utf8_general_ci;

/*
create table tbl_partners(
    cd_parc int not null primary key auto_increment,
    nm_parc varchar(50) not null,
    empresa_parc varchar(50) not null,
    cnpj_parc varchar(17) not null,
    email_parc varchar(40) not null,
    site_parc varchar(40),
    end_parc varchar(50),
    tel_parc varchar(30) not null,
    redeSocial_parc varchar(40),
    descr_parc varchar(2000)
); */



/******************** Inserts ********************/
insert into tbl_category (nm_cat) values ("Café");
insert into tbl_category (nm_cat) values ("Chocolate");
insert into tbl_category (nm_cat) values ("MilkShake");
insert into tbl_category (nm_cat) values ("Sanduíche");
insert into tbl_category (nm_cat) values ("Cookies");
insert into tbl_category (nm_cat) values ("Hambúrguer");

/******************** Selects  ********************/
select * from tbl_account;
select * from tbl_employees;
select * from tbl_menu;
select * from tbl_category;
