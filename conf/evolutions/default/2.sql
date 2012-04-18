# Roles schema

# --- !Ups

CREATE TABLE role (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user_role (
	user_id bigint(20) NOT NULL,
	role_id bigint(20) NOT NULL,
	FOREIGN KEY (user_id) REFERENCES user(id),
	FOREIGN KEY (role_id) REFERENCES role(id)
);

# --- !Downs

DROP TABLE role;
DROP TABLE user_role;