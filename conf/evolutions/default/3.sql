# Roles schema

# --- !Ups

CREATE TABLE player (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    user_id bigint(20) NOT NULL,
    name varchar(255) NOT NULL,
    about text,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);


# --- !Downs

DROP TABLE player;