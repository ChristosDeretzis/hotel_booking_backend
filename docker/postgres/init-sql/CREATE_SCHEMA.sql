CREATE TABLE public."user" (
                               id serial4 NOT NULL,
                               first_name varchar(250) NULL,
                               last_name varchar(250) NULL,
                               dob date NULL,
                               email varchar(250) NULL,
                               country varchar(250) NULL,
                               city varchar(250) NULL,
                               state varchar(250) NULL,
                               street_address varchar(250) NULL,
                               street_number varchar(250) NULL,
                               zip_code varchar(250) NULL,
                               username varchar(250) NULL,
                               "password" varchar(250) NULL,
                               CONSTRAINT user_pk PRIMARY KEY (id)
);


-- public.hotel definition

-- Drop table

-- DROP TABLE public.hotel;

CREATE TABLE public.hotel (
                              id serial4 NOT NULL,
                              hotel_name varchar(250) NULL,
                              description varchar(2000) NULL,
                              stars int4 NULL,
                              email varchar(250) NULL,
                              phone varchar(250) NULL,
                              country varchar(250) NULL,
                              city varchar(250) NULL,
                              state varchar(250) NULL,
                              street_address varchar(250) NULL,
                              street_number varchar(250) NULL,
                              zip_code varchar(250) NULL,
                              owner_id int4 NULL,
                              CONSTRAINT hotel_pk PRIMARY KEY (id),
                              CONSTRAINT hotel_fk FOREIGN KEY (owner_id) REFERENCES public."user"(id)
);


-- public.review definition

-- Drop table

-- DROP TABLE public.review;

CREATE TABLE public.review (
                               id serial4 NOT NULL,
                               description varchar(2000) NULL,
                               rating numeric NULL,
                               user_id int4 NULL,
                               hotel_id int4 NULL,
                               CONSTRAINT review_pk PRIMARY KEY (id),
                               CONSTRAINT review_fk FOREIGN KEY (user_id) REFERENCES public."user"(id),
                               CONSTRAINT review_fk_1 FOREIGN KEY (hotel_id) REFERENCES public.hotel(id)
);


-- public.room definition

-- Drop table

-- DROP TABLE public.room;

CREATE TABLE public.room (
                             id serial4 NOT NULL,
                             "name" varchar(250) NULL,
                             price numeric NULL,
                             capacity int4 NULL,
                             hotel_id int4 NULL,
                             CONSTRAINT room_pk PRIMARY KEY (id),
                             CONSTRAINT room_fk FOREIGN KEY (hotel_id) REFERENCES public.hotel(id)
);


-- public.reservation definition

-- Drop table

-- DROP TABLE public.reservation;

CREATE TABLE public.reservation (
                                    id serial4 NOT NULL,
                                    check_in date NULL,
                                    check_out date NULL,
                                    discount numeric NULL,
                                    final_price numeric NULL,
                                    user_id int4 NULL,
                                    hotel_id int4 NULL,
                                    CONSTRAINT reservation_pk PRIMARY KEY (id),
                                    CONSTRAINT reservation_fk_hotel FOREIGN KEY (hotel_id) REFERENCES public.hotel(id),
                                    CONSTRAINT reservation_fk_user FOREIGN KEY (user_id) REFERENCES public."user"(id)
);