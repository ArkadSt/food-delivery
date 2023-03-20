drop table if exists weather;
drop table if exists stations;
create table if not exists stations (
    station_name varchar(256),
    wmo_code int,
    air_temp double,
    wind_speed double,
    weather_phenomenon varchar(256),
    timestamp bigint,
    primary key (wmo_code, timestamp)
);