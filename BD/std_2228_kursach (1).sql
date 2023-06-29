-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Хост: std-mysql
-- Время создания: Июн 29 2023 г., 13:55
-- Версия сервера: 5.7.26-0ubuntu0.16.04.1
-- Версия PHP: 8.1.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `std_2228_kursach`
--

-- --------------------------------------------------------

--
-- Структура таблицы `Delivery_Center`
--

CREATE TABLE `Delivery_Center` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(150) DEFAULT NULL,
  `address` varchar(150) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `Delivery_Center`
--

INSERT INTO `Delivery_Center` (`id`, `name`, `address`) VALUES
(1, 'Пункт 1', 'Адрес 1'),
(2, 'Пункт 2', 'Адрес 2'),
(3, 'Пункт 3', 'Адрес 3'),
(4, 'Пункт 4', 'Адрес 4');

-- --------------------------------------------------------

--
-- Структура таблицы `Packs`
--

CREATE TABLE `Packs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `type_delivery` enum('Срочная','Обычная') NOT NULL,
  `weight` int(11) DEFAULT NULL,
  `user_from` int(10) UNSIGNED DEFAULT NULL,
  `user_to` int(10) UNSIGNED DEFAULT NULL,
  `id_dc_to` int(10) UNSIGNED DEFAULT NULL,
  `id_courier` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `Packs`
--

INSERT INTO `Packs` (`id`, `type_delivery`, `weight`, `user_from`, `user_to`, `id_dc_to`, `id_courier`) VALUES
(5, 'Обычная', 5, 16, 20, 1, 15),
(6, 'Срочная', 1, 20, 16, 1, 15);

-- --------------------------------------------------------

--
-- Структура таблицы `Roles`
--

CREATE TABLE `Roles` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `Roles`
--

INSERT INTO `Roles` (`id`, `name`) VALUES
(1, 'Директор'),
(2, 'Зам.директора'),
(3, 'Администратор'),
(4, 'Курьер'),
(5, 'Пользователь');

-- --------------------------------------------------------

--
-- Структура таблицы `Role_User`
--

CREATE TABLE `Role_User` (
  `id` int(10) UNSIGNED NOT NULL,
  `id_user` int(10) UNSIGNED DEFAULT NULL,
  `id_role` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `Role_User`
--

INSERT INTO `Role_User` (`id`, `id_user`, `id_role`) VALUES
(1, 1, 1),
(3, 3, 3),
(17, 15, 4),
(18, 16, 5),
(22, 20, 5);

-- --------------------------------------------------------

--
-- Структура таблицы `Users`
--

CREATE TABLE `Users` (
  `id` int(10) UNSIGNED NOT NULL,
  `first_name` varchar(150) NOT NULL,
  `last_name` varchar(150) NOT NULL,
  `second_name` varchar(150) DEFAULT NULL,
  `number_phone` varchar(25) NOT NULL,
  `address` varchar(150) DEFAULT NULL,
  `id_dc` int(10) UNSIGNED DEFAULT NULL,
  `login` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `Users`
--

INSERT INTO `Users` (`id`, `first_name`, `last_name`, `second_name`, `number_phone`, `address`, `id_dc`, `login`, `password`) VALUES
(1, 'Кирилл', 'Конопский', 'Сергеевич', '89604707981', 'москва', NULL, 'kirill.kk', '$2a$10$2ukdLIQjLjVXcZBTWvia7us/7HA1Q0T.d21OU0fXMWKhbIiV41yhq'),
(3, 'Никита', 'Беланй', 'Сергеевич', '89996967149', 'г. Ростов-на-Дону, ул. Вятская, 75/2 кв.28', NULL, 'Ice_One', '$2a$10$1O5HUS4caqFjawyNW2n5H.F.BIJc/pROFGB11fjKZDgZNIDWoZ4EW'),
(15, 'Никита', 'Голубев', 'Младший', '89956654213', 'Ад', 1, 'gruz', '$2a$10$NInzCh4jIXtffu96WHJYFu.UtxwGLolhbNZA5c.HrVQjgklm3oWYC'),
(16, 'Добряк', 'Голубев', 'Старшый', '89604707918', 'Москва', NULL, 'dobro', '$2a$10$pAq1puj2f5ti/Pb/.Ltc8ekhwouHtvwyLT.jWAPP0DgOQCv5pqZCe'),
(20, 'Андрей', 'Дорохин', 'Николаевич', '89604531564', 'Москоу', NULL, 'Android', '$2a$10$m1AdF8KVoylTlu7O4Z67s.SEowh9miHx7k0lm89xFplGdNSq5KbCq');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `Delivery_Center`
--
ALTER TABLE `Delivery_Center`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `Packs`
--
ALTER TABLE `Packs`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `user_from` (`user_from`),
  ADD KEY `user_to` (`user_to`),
  ADD KEY `id_dc_from` (`id_dc_to`),
  ADD KEY `id_courier` (`id_courier`);

--
-- Индексы таблицы `Roles`
--
ALTER TABLE `Roles`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `Role_User`
--
ALTER TABLE `Role_User`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_role` (`id_role`),
  ADD KEY `Role_User_ibfk_1` (`id_user`);

--
-- Индексы таблицы `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `login` (`login`),
  ADD KEY `id_dc` (`id_dc`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `Delivery_Center`
--
ALTER TABLE `Delivery_Center`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT для таблицы `Packs`
--
ALTER TABLE `Packs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT для таблицы `Roles`
--
ALTER TABLE `Roles`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT для таблицы `Role_User`
--
ALTER TABLE `Role_User`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT для таблицы `Users`
--
ALTER TABLE `Users`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `Packs`
--
ALTER TABLE `Packs`
  ADD CONSTRAINT `Packs_ibfk_1` FOREIGN KEY (`user_from`) REFERENCES `Users` (`id`),
  ADD CONSTRAINT `Packs_ibfk_2` FOREIGN KEY (`user_to`) REFERENCES `Users` (`id`),
  ADD CONSTRAINT `Packs_ibfk_3` FOREIGN KEY (`id_dc_to`) REFERENCES `Delivery_Center` (`id`),
  ADD CONSTRAINT `Packs_ibfk_4` FOREIGN KEY (`id_courier`) REFERENCES `Users` (`id`);

--
-- Ограничения внешнего ключа таблицы `Role_User`
--
ALTER TABLE `Role_User`
  ADD CONSTRAINT `Role_User_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `Users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `Role_User_ibfk_2` FOREIGN KEY (`id_role`) REFERENCES `Roles` (`id`);

--
-- Ограничения внешнего ключа таблицы `Users`
--
ALTER TABLE `Users`
  ADD CONSTRAINT `Users_ibfk_1` FOREIGN KEY (`id_dc`) REFERENCES `Delivery_Center` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
