-- phpMyAdmin SQL Dump
-- version 3.2.4
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 24-11-2012 a las 14:10:41
-- Versión del servidor: 5.1.41
-- Versión de PHP: 5.3.1

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `juego`
--
CREATE DATABASE IF NOT EXISTS juego;
USE juego;
-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `armas`
--

CREATE TABLE IF NOT EXISTS `armas` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(500) NOT NULL,
  `descripción` varchar(500) DEFAULT NULL,
  `dano` int(10) unsigned DEFAULT '5',
  `id_personaje` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_personaje` (`id_personaje`),
  KEY `fk_personaje_idx` (`id_personaje`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Volcar la base de datos para la tabla `armas`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `enemigos`
--

CREATE TABLE IF NOT EXISTS `enemigos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(500) DEFAULT NULL,
  `energia` int(10) unsigned DEFAULT '100',
  `dano` int(10) unsigned DEFAULT '10',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Volcar la base de datos para la tabla `enemigos`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `enemigo_pantalla`
--

CREATE TABLE IF NOT EXISTS `enemigo_pantalla` (
  `id_enemigo` int(10) unsigned NOT NULL DEFAULT '0',
  `id_pantalla` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_enemigo`,`id_pantalla`),
  KEY `fk_enemigo_idx` (`id_enemigo`),
  KEY `fk_pantalla_idx` (`id_pantalla`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `enemigo_pantalla`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pantallas`
--

CREATE TABLE IF NOT EXISTS `pantallas` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(500) NOT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  `dificultad` int(10) unsigned DEFAULT '1',
  `mundo` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Volcar la base de datos para la tabla `pantallas`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `personajes`
--

CREATE TABLE IF NOT EXISTS `personajes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(500) NOT NULL,
  `nivel` int(10) unsigned DEFAULT '10',
  `energia` int(10) unsigned DEFAULT '100',
  `puntos` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Volcar la base de datos para la tabla `personajes`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `personaje_pantalla`
--

CREATE TABLE IF NOT EXISTS `personaje_pantalla` (
  `id_personaje` int(10) unsigned NOT NULL DEFAULT '0',
  `id_pantalla` int(10) unsigned NOT NULL DEFAULT '0',
  `terminada` tinyint(4) DEFAULT '0',
  `fecha_inicio` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `fecha_fin` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_personaje`,`id_pantalla`),
  KEY `fk_personaje_pantalla_idx` (`id_personaje`),
  KEY `fk_pantalla_pantalla_idx` (`id_pantalla`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcar la base de datos para la tabla `personaje_pantalla`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `trofeos`
--

CREATE TABLE IF NOT EXISTS `trofeos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(500) NOT NULL,
  `puntos` int(10) unsigned DEFAULT '5',
  `id_pantalla` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_id_pantalla_idx` (`id_pantalla`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Volcar la base de datos para la tabla `trofeos`
--


--
-- Filtros para las tablas descargadas (dump)
--

--
-- Filtros para la tabla `armas`
--
ALTER TABLE `armas`
  ADD CONSTRAINT `fk_personaje_arma` FOREIGN KEY (`id_personaje`) REFERENCES `personajes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `enemigo_pantalla`
--
ALTER TABLE `enemigo_pantalla`
  ADD CONSTRAINT `fk_enemigo` FOREIGN KEY (`id_enemigo`) REFERENCES `enemigos` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_pantalla_personaje` FOREIGN KEY (`id_pantalla`) REFERENCES `pantallas` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `personaje_pantalla`
--
ALTER TABLE `personaje_pantalla`
  ADD CONSTRAINT `fk_pantalla_pantalla` FOREIGN KEY (`id_pantalla`) REFERENCES `pantallas` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_personaje_pantalla` FOREIGN KEY (`id_personaje`) REFERENCES `personajes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `trofeos`
--
ALTER TABLE `trofeos`
  ADD CONSTRAINT `fk_id_pantalla` FOREIGN KEY (`id_pantalla`) REFERENCES `pantallas` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
