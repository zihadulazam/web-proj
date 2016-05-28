CREATE TABLE USERS (
ID INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
NAME VARCHAR(255) NOT NULL,
SURNAME VARCHAR(255) NOT NULL,
NICKNAME VARCHAR(25) NOT NULL,
EMAIL VARCHAR(254) NOT NULL,
PASSWORD VARCHAR(255) NOT NULL,
AVATAR_PATH VARCHAR(255) NOT NULL,
REVIEWS_COUNTER INTEGER NOT NULL DEFAULT 0 CONSTRAINT REVIEWS_COUNTER_CHECK CHECK (REVIEWS_COUNTER >=0),
REVIEWS_POSITIVE INTEGER NOT NULL DEFAULT 0 CONSTRAINT REVIEWS_POSITIVE_CHECK CHECK (REVIEWS_POSITIVE >=0),
REVIEWS_NEGATIVE INTEGER NOT NULL DEFAULT 0 CONSTRAINT REVIEWS_NEGATIVE_CHECK CHECK (REVIEWS_NEGATIVE >=0),
USERTYPE INTEGER NOT NULL DEFAULT 0,
PRIMARY KEY (ID),
UNIQUE (NICKNAME),
UNIQUE (EMAIL)
);

CREATE TABLE PRICE_RANGES (
ID INTEGER NOT NULL,
NAME VARCHAR(25) NOT NULL,
MIN_VALUE FLOAT,
MAX_VALUE FLOAT,
PRIMARY KEY (ID),
UNIQUE (NAME),
CONSTRAINT PRICE_RANGE_CHECK CHECK ((MIN_VALUE IS NULL AND MAX_VALUE IS NOT
NULL) OR (MIN_VALUE IS
NOT NULL AND MAX_VALUE IS NULL) OR (MIN_VALUE < MAX_VALUE))
);

CREATE TABLE RESTAURANTS (
ID INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
NAME VARCHAR(255) NOT NULL,
DESCRIPTION VARCHAR(32000),
WEB_SITE_URL VARCHAR(255),
GLOBAL_VALUE INTEGER CONSTRAINT GLOBAL_VALUE_CHECK CHECK (GLOBAL_VALUE
>= 0 AND
GLOBAL_VALUE <= 5),ID_OWNER INTEGER,
ID_CREATOR INTEGER NOT NULL,
ID_PRICE_RANGE INTEGER,
REVIEWS_COUNTER INTEGER NOT NULL DEFAULT 0,
VALIDATED BOOLEAN NOT NULL DEFAULT FALSE,
PRIMARY KEY (ID),
FOREIGN KEY (ID_OWNER) REFERENCES USERS (ID),
FOREIGN KEY (ID_CREATOR) REFERENCES USERS (ID),
FOREIGN KEY (ID_PRICE_RANGE) REFERENCES PRICE_RANGES (ID)
);

CREATE TABLE PHOTOS (
ID INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
NAME VARCHAR(25) NOT NULL,
DESCRIPTION VARCHAR (32000),
PATH VARCHAR(255) NOT NULL,
ID_RESTAURANT INTEGER NOT NULL,
ID_OWNER INTEGER NOT NULL,
PRIMARY KEY (ID),
FOREIGN KEY (ID_RESTAURANT) REFERENCES RESTAURANTS (ID),
FOREIGN KEY (ID_OWNER) REFERENCES USERS (ID)
);

CREATE TABLE COORDINATES (
ID INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
LATITUDE FLOAT NOT NULL,
LONGITUDE FLOAT NOT NULL,
ADDRESS VARCHAR(255) NOT NULL,
CITY VARCHAR(255) NOT NULL,
STATE VARCHAR(255) NOT NULL,
COMPLETE_LOCATION VARCHAR(255) NOT NULL,
PRIMARY KEY (ID)
);

CREATE TABLE RESTAURANT_COORDINATE (
ID_RESTAURANT INTEGER NOT NULL,
ID_COORDINATE INTEGER NOT NULL,
PRIMARY KEY (ID_RESTAURANT, ID_COORDINATE),
FOREIGN KEY (ID_RESTAURANT) REFERENCES RESTAURANTS (ID),
FOREIGN KEY (ID_COORDINATE) REFERENCES COORDINATES (ID));

CREATE TABLE OPENING_HOURS_RANGES (
ID INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
DAY_OF_THE_WEEK INTEGER CONSTRAINT DAY_OF_THE_WEEK_CHECK CHECK
(DAY_OF_THE_WEEK >= 1 AND
DAY_OF_THE_WEEK <= 7),
START_HOUR TIME NOT NULL,
END_HOUR TIME NOT NULL,
PRIMARY KEY (ID),
CONSTRAINT RANGE_CHECK CHECK (START_HOUR < END_HOUR)
);

CREATE TABLE OPENING_HOURS_RANGE_RESTAURANT (
ID_RESTAURANT INTEGER NOT NULL,
ID_OPENING_HOURS_RANGE INTEGER NOT NULL,
PRIMARY KEY (ID_RESTAURANT, ID_OPENING_HOURS_RANGE),
FOREIGN KEY (ID_RESTAURANT) REFERENCES RESTAURANTS (ID),
FOREIGN KEY (ID_OPENING_HOURS_RANGE) REFERENCES OPENING_HOURS_RANGES (ID)
);

CREATE TABLE CUISINES (
ID INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
NAME VARCHAR(25) NOT NULL,
PRIMARY KEY (ID)
);

CREATE TABLE RESTAURANT_CUISINE (
ID_RESTAURANT INTEGER NOT NULL,
ID_CUISINE INTEGER NOT NULL,
PRIMARY KEY (ID_RESTAURANT, ID_CUISINE),
FOREIGN KEY (ID_RESTAURANT) REFERENCES RESTAURANTS (ID),
FOREIGN KEY (ID_CUISINE) REFERENCES CUISINES (ID)
);

CREATE TABLE REVIEWS (
ID INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
GLOBAL_VALUE INTEGER NOT NULL CONSTRAINT REVIEW_GLOBAL_VALUE_CHECK CHECK
(GLOBAL_VALUE >= 1
AND GLOBAL_VALUE <= 5),
FOOD INTEGER CONSTRAINT REVIEW_FOOD_CHECK CHECK (FOOD IS NULL OR (FOOD
>= 1 AND FOOD <=
5)),
SERVICE INTEGER CONSTRAINT REVIEW_SERVICE_CHECK CHECK (SERVICE IS NULL
OR (SERVICE >= 1 AND
SERVICE <= 5)),
VALUE_FOR_MONEY INTEGER CONSTRAINT REVIEW_VALUE_FOR_MONEY_CHECK CHECK
(VALUE_FOR_MONEY IS
NULL OR (VALUE_FOR_MONEY >= 1 AND VALUE_FOR_MONEY <= 5)),
ATMOSPHERE INTEGER CONSTRAINT REVIEW_ATMOSPHERE_CHECK CHECK (ATMOSPHERE
IS NULL OR
(ATMOSPHERE >= 1 AND ATMOSPHERE <= 5)),
NAME VARCHAR(25),
DESCRIPTION VARCHAR(32000),
DATE_CREATION TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ID_RESTAURANT INTEGER NOT NULL,
ID_CREATOR INTEGER NOT NULL,
ID_PHOTO INTEGER,
LIKES INTEGER NOT NULL DEFAULT 0 CONSTRAINT LIKES_COUNTER_CHECK CHECK (LIKES >=0),
DISLIKES INTEGER NOT NULL DEFAULT 0 CONSTRAINT DISLIKES_COUNTER_CHECK CHECK (DISLIKES >=0),
PRIMARY KEY (ID),
FOREIGN KEY (ID_RESTAURANT) REFERENCES RESTAURANTS (ID),
FOREIGN KEY (ID_CREATOR) REFERENCES USERS (ID),
FOREIGN KEY (ID_PHOTO) REFERENCES PHOTOS (ID)
);

CREATE TABLE REPLIES (
ID INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
DESCRIPTION VARCHAR(32000) NOT NULL,
DATE_CREATION TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ID_REVIEW INTEGER NOT NULL,
ID_OWNER INTEGER NOT NULL,
DATE_VALIDATION TIMESTAMP,
ID_VALIDATOR INTEGER,
VALIDATED BOOLEAN NOT NULL DEFAULT FALSE,
PRIMARY KEY (ID),
FOREIGN KEY (ID_REVIEW) REFERENCES REVIEWS (ID),FOREIGN KEY (ID_OWNER) REFERENCES USERS (ID),
FOREIGN KEY (ID_VALIDATOR) REFERENCES USERS (ID)
);

CREATE TABLE USER_REVIEW_LIKES (
ID_USER INTEGER NOT NULL,
ID_REVIEW INTEGER NOT NULL,
ID_CREATOR INTEGER NOT NULL,
LIKE_TYPE INTEGER NOT NULL CONSTRAINT LIKE_TYPE_CHECK CHECK (LIKE_TYPE
>= 0 AND LIKE_TYPE
<= 1),
DATE_CREATION TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (ID_USER, ID_REVIEW, ID_CREATOR),
FOREIGN KEY (ID_USER) REFERENCES USERS (ID),
FOREIGN KEY (ID_REVIEW) REFERENCES REVIEWS (ID),
FOREIGN KEY (ID_CREATOR) REFERENCES USERS (ID)
);

CREATE TABLE REPLIES_TO_BE_CONFIRMED (
ID INTEGER NOT NULL,
DATE_ADMIN_TOOK TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(ID),
FOREIGN KEY(ID) REFERENCES REPLIES(ID)
);

CREATE TABLE REPORTED_REVIEWS (
ID INTEGER NOT NULL,
DATE_ADMIN_TOOK TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(ID),
FOREIGN KEY(ID) REFERENCES REVIEWS(ID)
);

CREATE TABLE REPORTED_PHOTOS (
ID INTEGER NOT NULL,
DATE_ADMIN_TOOK TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(ID),
FOREIGN KEY(ID) REFERENCES PHOTOS(ID)
);


CREATE TABLE NOTIFICATIONS (
ID INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
USER_ID INTEGER NOT NULL,
DESCRIPTION VARCHAR(1000),
PRIMARY KEY(ID),
FOREIGN KEY(USER_ID) REFERENCES USERS(ID)
);

CREATE TABLE RESTAURANTS_REQUESTS (
ID_USER INTEGER NOT NULL,
ID_RESTAURANT INTEGER NOT NULL,
DATE_ADMIN_TOOK TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
CREATION_CLAIM_BOTH_FLAG INTEGER NOT NULL,
USERTEXTCLAIM VARCHAR(1000),
PRIMARY KEY(ID_USER,ID_RESTAURANT),
FOREIGN KEY(ID_USER) REFERENCES USERS(ID),
FOREIGN KEY(ID_RESTAURANT) REFERENCES RESTAURANTS(ID)
);


CREATE INDEX REVIEWS_REST_LIKES_DESC ON REVIEWS(ID_RESTAURANT,LIKES);
CREATE INDEX REVIEWS_REST_VALUE_DESC ON REVIEWS(ID_RESTAURANT,GLOBAL_VALUE);
CREATE INDEX REVIEWS_REST_DATA_DESC ON REVIEWS(ID_RESTAURANT,DATE_CREATION);
CREATE INDEX REVIEWS_USER_DATA_DESC ON REVIEWS(ID_CREATOR,DATE_CREATION);

CREATE INDEX PHOTO_RESTAURANT ON PHOTOS(ID_RESTAURANT);
CREATE INDEX PHOTO_OWNER ON PHOTOS(ID_OWNER);

CREATE INDEX REPLIES_REVIEW ON REPLIES(ID_REVIEW);

CREATE INDEX RESTAURANTS_NAME ON RESTAURANTS(NAME);
CREATE INDEX RESTAURANTS_VALUE_DESC ON RESTAURANTS(GLOBAL_VALUE);

CREATE INDEX COORDINATES_ADDRESS ON COORDINATES(ADDRESS);
CREATE INDEX COORDINATES_LAT_LONG ON COORDINATES(LATITUDE,LONGITUDE);

CREATE INDEX USER_ID ON NOTIFICATIONS(ID);

