CREATE TABLE public.users (
                               id uuid NOT NULL,
                               first_name varchar(250) NULL,
                               last_name varchar(250) NULL,
                               email varchar(250) NULL,
                               username varchar(250) NULL,
                               dob date NULL,
                               CONSTRAINT user_pk PRIMARY KEY (id)
);