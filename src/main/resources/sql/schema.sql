DROP TABLE IF EXISTS `benches`;
DROP TABLE IF EXISTS `files`;

CREATE TABLE IF NOT EXISTS `benches`
(
    `id`                  INT AUTO_INCREMENT PRIMARY KEY,
    `access_token`        VARCHAR(9) UNIQUE NOT NULL,
    `edit_token`          VARCHAR(9) UNIQUE NOT NULL,
    `name`                VARCHAR(64)       NOT NULL,
    `creation_time`       LONG              NOT NULL,
    `expiration_duration` LONG              NOT NULL,
    `view_count`          LONG              NOT NULL
);

CREATE TABLE IF NOT EXISTS `files`
(
    `id`             INT AUTO_INCREMENT PRIMARY KEY,
    `bench_id`       INT         NOT NULL,
    `name`           VARCHAR(64) NOT NULL,
    `size`           LONG        NOT NULL,
    `download_count` LONG        NOT NULL,
    FOREIGN KEY (`bench_id`) references `benches` (`id`)
)
