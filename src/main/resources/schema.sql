--drop table if exists stations;
--drop table if exists base_fee;
--drop table if exists WPEF;
--drop table if exists ATEF;
--drop table if exists WSEF;

create table if not exists stations (
    station_name varchar(256),
    wmo_code int,
    air_temp double,
    wind_speed double,
    weather_phenomenon varchar(256),
    timestamp bigint,
    primary key (wmo_code, timestamp)
);

create table if not exists base_fee (
    city varchar(256),
    vehicle varchar(256),
    fee double,
    primary key (city, vehicle)
);

create table if not exists WPEF (
    vehicle varchar(256),
    phenomenon varchar(256),
    fee double,
    primary key (vehicle, phenomenon)
);

create table if not exists ATEF (
    vehicle varchar(256),
    min_temp double,
    max_temp double,
    include_min boolean,
    include_max boolean,
    fee double,
    primary key (vehicle, min_temp, max_temp, include_min, include_max)
);

create table if not exists WSEF (
    vehicle varchar(256),
    min_wind_speed double,
    max_wind_speed double,
    include_min boolean,
    include_max boolean,
    fee double,
    primary key (vehicle, min_wind_speed, max_wind_speed, include_min, include_max)
);


