-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: localhost
-- Thời gian đã tạo: Th10 18, 2024 lúc 10:57 AM
-- Phiên bản máy phục vụ: 10.4.28-MariaDB
-- Phiên bản PHP: 8.1.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `xamplebackend`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `genders` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `descriptions` text DEFAULT NULL,
  `role` varchar(255) NOT NULL DEFAULT 'user',
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `remember_token` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `name`, `phone`, `password`, `genders`, `email`, `address`, `birthday`, `image`, `descriptions`, `role`, `email_verified_at`, `remember_token`, `created_at`, `updated_at`) VALUES
(14, 'Ngo Minh Quang', '0966593702', '37b97b6d477e63b2d6a26edbbe3f7d6a', 'Nam', 'quang1@gmail.com', 'My Dinh, Ha Noi', '2024-11-21', 'assets/images/anhtest8.jpeg', NULL, 'admin', NULL, NULL, NULL, NULL),
(15, 'Ngo Minh Quang', '0733984793', '58c55fe34ab59b425ee686b089da01e6', 'Nam', 'ngominh11w1@gmail.com', 'wHa Noi, Vietdưm', '2024-11-14', 'assets/images/rakkiu5.jpeg', NULL, 'user', NULL, NULL, NULL, NULL),
(29, 'Quang ', '0577362892', '32eb07e4e0bfcc38edf0807453deb38e', 'Nam', '31312@gmail.com', 'Ha Noi', '2024-11-28', 'assets/images/storeimage.jpeg', NULL, 'None', NULL, NULL, NULL, NULL),
(31, 'Quang ', '0577362894', '32eb07e4e0bfcc38edf0807453deb38e', 'Male', '3131332@gmail.com', 'Ha Noi', '2024-11-28', 'assets/images/shop.jpeg', NULL, 'user', NULL, NULL, NULL, NULL),
(33, 'Quang ', '0577362896', '32eb07e4e0bfcc38edf0807453deb38e', 'Female', '311332@gmail.com', 'Ha Noi', '2024-11-28', 'assets/images/storeimage.jpeg', NULL, 'user', NULL, NULL, NULL, NULL),
(34, 'Quang newwww', '0577362897', '32eb07e4e0bfcc38edf0807453deb38e', 'Female', '342442452@gmail.com', 'Ha Noi', '2024-11-28', 'assets/images/anhtest8.jpeg', NULL, 'Admin', NULL, NULL, NULL, NULL),
(36, 'NGO  MINH QUANG', '0966593702', '25d55ad283aa400af464c76d713c07ad', 'Nam', 'ngominhquang724@gmail.com', 'My Dinh, Ha Noi, Viet Nam', '2024-11-07', 'assets/images/anhtest7.jpeg', NULL, 'Admin', NULL, NULL, NULL, NULL);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `users_email_unique` (`email`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
