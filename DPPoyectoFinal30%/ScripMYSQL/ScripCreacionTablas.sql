
CREATE TABLE m_person ( -- Tabla Persona
	n_id_person int PRIMARY KEY AUTO_INCREMENT NOT NULL,
	c_name varchar(100) DEFAULT NULL,
	c_first_name varchar(100) DEFAULT NULL,
	c_last_name varchar(100) DEFAULT NULL,
	d_date_birth DATE  DEFAULT NULL,
	c_civil_status varchar(1) DEFAULT NULL,
	c_gender varchar(1) DEFAULT NULL COMMENT 'M=MAN F=WOMEN',
	c_state varchar(1) NOT NULL COMMENT 'A=ACTIVE I=INACTIVE'
);

CREATE TABLE m_ubigeo( -- Tabla Ubigeo
	n_id_ubigeo int PRIMARY KEY AUTO_INCREMENT NOT NULL,
	c_ubigeo_reniec varchar(6) DEFAULT NULL,
	c_ubigeo_inei varchar(6) DEFAULT NULL,
	c_departamento_inei varchar(2)  DEFAULT NULL,
	c_departamento varchar(50)   DEFAULT NULL,
	c_provincia_inei varchar(4)  DEFAULT NULL,
	c_provincia varchar(50)   DEFAULT NULL,
	c_distrito varchar(50)   DEFAULT NULL,
	c_region varchar(50)   DEFAULT NULL,
	c_macroregion_inei varchar(50)   DEFAULT NULL,
	c_macroregion_minsa varchar(50)   DEFAULT NULL,
	c_iso_3166_2 varchar(6)   DEFAULT NULL,
	c_fips varchar(4)   DEFAULT NULL,
	n_superficie double DEFAULT NULL,
	n_altitud double DEFAULT NULL,
	n_latitud double DEFAULT NULL,
	n_longitud double DEFAULT NULL
);

CREATE TABLE m_customer ( -- Tabla Clientes
	n_id_customer int PRIMARY KEY AUTO_INCREMENT NOT NULL
	,c_code varchar(20) UNIQUE
	,c_type_doc varchar(1) NOT NULL COMMENT '1=DNI 4=CARNET DE EXT.  6=RUC 7=PASSPORT'
	,c_num_doc varchar(12) DEFAULT NULL
	,c_business_name varchar(100) DEFAULT NULL
	,c_address varchar(200) DEFAULT NULL
	,c_phone_main varchar(9) DEFAULT NULL
	,c_email_main varchar(100) DEFAULT NULL
	,d_create_date DATETIME  DEFAULT CURRENT_TIMESTAMP COMMENT 'DATE CREATED'
	,c_create_user varchar(10) DEFAULT NULL COMMENT 'USER WHO CREATED'
	,d_audit_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'DATE LAST UPDATE'
	,c_audit_user varchar(10) DEFAULT NULL COMMENT 'USER WHO MADE THE LAST UPDATE'
	,c_state varchar(1) NOT NULL COMMENT 'A=ACTIVE I=INACTIVE'
	,n_id_ubigeo int DEFAULT NULL
	,n_id_person int DEFAULT NULL
	,CONSTRAINT fk_customer_ubigeo FOREIGN KEY (n_id_ubigeo) REFERENCES m_ubigeo(n_id_ubigeo)
	,CONSTRAINT fk_customer_person FOREIGN KEY (n_id_person) REFERENCES m_person(n_id_person)
);

CREATE TABLE m_profile( -- Tabla perfil
	n_id_profile int PRIMARY KEY AUTO_INCREMENT NOT NULL
	,c_description varchar(50)
	,c_state varchar(1) NOT NULL COMMENT 'A=ACTIVE I=INACTIVE' 
);
CREATE TABLE m_employes ( -- tabla de Epleados  --
	n_id_employe int PRIMARY KEY AUTO_INCREMENT NOT NULL
	,c_code varchar(20) UNIQUE
	,c_type_doc varchar(1) DEFAULT NULL
	,c_num_doc varchar(12) DEFAULT NULL
	,c_address varchar(200) DEFAULT NULL  
	,c_phone_main varchar(9) DEFAULT NULL
	,c_email_main varchar(100) DEFAULT NULL
	,c_username varchar(50) DEFAULT NULL
	,c_password varchar(50) DEFAULT NULL 
	,c_state varchar(1) DEFAULT NULL
	,n_id_ubigeo int DEFAULT NULL
	,n_id_person int DEFAULT NULL
	,n_id_profile int DEFAULT NULL
	,CONSTRAINT fk_m_employes_ubigeo FOREIGN KEY (n_id_ubigeo) REFERENCES m_ubigeo (n_id_ubigeo)
	,CONSTRAINT fk_m_employes_person FOREIGN KEY (n_id_person) REFERENCES m_person (n_id_person)
	,CONSTRAINT fk_m_employes_profile FOREIGN KEY (n_id_profile) REFERENCES m_profile (n_id_profile)
);



CREATE TABLE sale( -- Tabla de Ventas --
	n_id_sale int PRIMARY KEY AUTO_INCREMENT NOT NULL
	,c_type_doc varchar(1) COMMENT 'F=FACTURA, B=BOLETA'
	,c_serie varchar(4)
	,n_correlativo int NOT NULL
	,d_date_emi DATETIME
	,n_amount_net DECIMAL(20,2)
	,n_amount_igv DECIMAL(20,2)
	,n_amount_total DECIMAL(20,2)
	,c_state varchar(1) NOT NULL COMMENT 'A=ACTIVE I=INACTIVE' 
	,n_id_customer int
	,n_id_employe int
	,CONSTRAINT fk_sale_customer FOREIGN KEY (n_id_customer) REFERENCES m_customer (n_id_customer)
	,CONSTRAINT fk_sale_employe FOREIGN KEY (n_id_employe) REFERENCES m_employes (n_id_employe)
);

CREATE TABLE m_product( -- Tabla de productos
	n_id_product int PRIMARY KEY AUTO_INCREMENT NOT NULL
	,c_serie varchar(20)
	,c_name varchar(50)
	,c_description varchar(200)
	,n_price DECIMAL(20,2)
	,n_stock int
	,c_state varchar(1) NOT NULL COMMENT 'A=ACTIVE I=INACTIVE' 
);

CREATE TABLE sale_detail( -- Detalle de Venta
	n_id_sale_detail int PRIMARY KEY AUTO_INCREMENT NOT NULL
	,n_price DECIMAL(20,2)
	,n_quantity int
	,n_id_sale int
	,n_id_product int
	,CONSTRAINT fk_sale_detail_sale FOREIGN KEY (n_id_sale) REFERENCES sale (n_id_sale)
	,CONSTRAINT fk_sale_detail_product FOREIGN KEY (n_id_product) REFERENCES m_product (n_id_product)
);

