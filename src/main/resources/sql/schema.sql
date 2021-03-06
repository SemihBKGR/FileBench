CREATE TABLE IF NOT EXISTS `benches`
(
    `id`                  INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `access_token`        VARCHAR(9) UNIQUE  NOT NULL,
    `edit_token`          VARCHAR(9) UNIQUE  NOT NULL,
    `dirname`             VARCHAR(64) UNIQUE NOT NULL,
    `file_count`          INT UNSIGNED       NOT NULL,
    `file_size`           LONG               NOT NULL,
    `name`                VARCHAR(64),
    `description`         MEDIUMTEXT,
    `creation_time`       LONG               NOT NULL,
    `expiration_duration` LONG               NOT NULL,
    `view_count`          LONG               NOT NULL
);

CREATE TABLE IF NOT EXISTS `files`
(
    `id`             INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `bench_id`       INT UNSIGNED NOT NULL,
    `filename`       VARCHAR(64),
    `name`           VARCHAR(64),
    `description`    MEDIUMTEXT,
    `size`           LONG         NOT NULL,
    `download_count` LONG         NOT NULL,
    FOREIGN KEY (`bench_id`) references `benches` (`id`)
)
