-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 04-06-2026 a las 23:32:05
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `ruedafacil`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `aplicar_descuento_categoria` (IN `p_cat` VARCHAR(50), IN `p_porcentaje` DECIMAL(5,2))   BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN SELECT 'Error al actualizar precios' AS mensaje; END;

    UPDATE VEHICULO SET precioPorDia = precioPorDia * (1 + (p_porcentaje/100)) 
    WHERE categoria = p_cat;
    SELECT CONCAT('Precios actualizados para la categoría: ', p_cat) AS mensaje;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `asignar_empleado_alquiler` (IN `p_idA` INT, IN `p_idE` INT)   BEGIN
    DECLARE v_existe INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN SELECT 'Error al reasignar empleado' AS mensaje; END;

    SELECT COUNT(*) INTO v_existe FROM EMPLEADO WHERE idE = p_idE;
    IF v_existe = 0 THEN
        SELECT 'El empleado no existe' AS mensaje;
    ELSE
        UPDATE ALQUILER SET idE = p_idE WHERE idA = p_idA;
        SELECT 'Empleado reasignado correctamente' AS mensaje;
    END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `finalizar_alquiler` (IN `p_idA` INT, IN `p_fechaDev` DATE)   BEGIN
    DECLARE v_dias INT;
    DECLARE v_precioDiario DECIMAL(10,2);
    DECLARE v_matricula VARCHAR(15);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN SELECT 'Error en la actualización del alquiler' AS mensaje; END;

    SELECT a.matricula, v.precioPorDia INTO v_matricula, v_precioDiario
    FROM ALQUILER a JOIN VEHICULO v ON a.matricula = v.matricula
    WHERE a.idA = p_idA;

    SET v_dias = DATEDIFF(p_fechaDev, (SELECT fechaInicio FROM ALQUILER WHERE idA = p_idA));
    IF v_dias < 1 THEN SET v_dias = 1; END IF;

    UPDATE ALQUILER 
    SET fechaDevolucion = p_fechaDev, 
        precio = (v_dias * v_precioDiario), 
        estadoA = 'Completado' 
    WHERE idA = p_idA;

    UPDATE VEHICULO SET estadoV = 'Disponible' WHERE matricula = v_matricula;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `listar_vehiculos_categoria` (IN `p_cat_nombre` VARCHAR(50))   BEGIN
    DECLARE v_mat VARCHAR(15);
    DECLARE v_modelo VARCHAR(50);
    DECLARE fin_cursor INT DEFAULT 0;
    DECLARE cur_v CURSOR FOR 
        SELECT v.matricula, v.modelo 
        FROM VEHICULO v JOIN CATEGORIA c ON v.categoria = c.nombre 
        WHERE c.nombre = p_cat_nombre;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET fin_cursor = 1;

    OPEN cur_v;
    bucle: LOOP
        FETCH cur_v INTO v_mat, v_modelo;
        IF fin_cursor = 1 THEN LEAVE bucle; END IF;
        SELECT CONCAT('Vehículo: ', v_mat, ' - Modelo: ', v_modelo) AS info;
    END LOOP;
    CLOSE cur_v;
END$$

--
-- Funciones
--
CREATE DEFINER=`root`@`localhost` FUNCTION `dias_alquiler_previstos` (`p_idA` INT) RETURNS INT(11) DETERMINISTIC BEGIN
    DECLARE f_inicio DATE;
    DECLARE f_fin DATE;
    SELECT fechaInicio, fechaDevolucionPrevista INTO f_inicio, f_fin FROM ALQUILER WHERE idA = p_idA;
    RETURN DATEDIFF(f_fin, f_inicio);
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `nombre_cliente_alquiler` (`p_idA` INT) RETURNS VARCHAR(100) CHARSET utf8mb4 COLLATE utf8mb4_general_ci DETERMINISTIC BEGIN
    DECLARE v_nombre VARCHAR(100);
    SELECT c.nombre INTO v_nombre 
    FROM CLIENTE c JOIN ALQUILER a ON c.dni = a.dni 
    WHERE a.idA = p_idA;
    RETURN IFNULL(v_nombre, 'No encontrado');
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `total_recaudado_categoria` (`p_cat` VARCHAR(50)) RETURNS DECIMAL(10,2) DETERMINISTIC BEGIN
    DECLARE v_total DECIMAL(10,2);
    SELECT SUM(a.precio) INTO v_total 
    FROM ALQUILER a 
    JOIN VEHICULO v ON a.matricula = v.matricula
    JOIN CATEGORIA c ON v.categoria = c.nombre
    WHERE c.nombre = p_cat AND a.estadoA = 'Completado';
    RETURN IFNULL(v_total, 0);
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `verificar_stock_vehiculo` (`p_matricula` VARCHAR(15)) RETURNS TINYINT(1) DETERMINISTIC BEGIN
    DECLARE v_estado VARCHAR(50);
    SELECT estadoV INTO v_estado FROM VEHICULO WHERE matricula = p_matricula;
    IF v_estado = 'Disponible' THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alquiler`
--

CREATE TABLE `alquiler` (
  `idA` int(11) NOT NULL,
  `fechaInicio` date NOT NULL,
  `fechaDevolucionPrevista` date NOT NULL,
  `fechaDevolucion` date DEFAULT NULL,
  `precio` decimal(10,2) DEFAULT NULL,
  `estadoA` varchar(50) DEFAULT NULL,
  `dni` varchar(15) DEFAULT NULL,
  `idE` int(11) DEFAULT NULL,
  `matricula` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `alquiler`
--

INSERT INTO `alquiler` (`idA`, `fechaInicio`, `fechaDevolucionPrevista`, `fechaDevolucion`, `precio`, `estadoA`, `dni`, `idE`, `matricula`) VALUES
(1, '2026-05-01', '2026-05-05', '2026-06-04', 1190.00, 'Completado', '12345678A', 1, '1111AAA'),
(2, '2026-05-20', '2026-05-25', NULL, 475.00, 'Activo', '91234567B', 2, '3333CCC'),
(3, '2026-06-01', '2026-06-03', NULL, 140.00, 'Activo', '89123456C', 3, '2222BBB');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria`
--

CREATE TABLE `categoria` (
  `nombre` varchar(50) NOT NULL,
  `descripcion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `categoria`
--

INSERT INTO `categoria` (`nombre`, `descripcion`) VALUES
('De lujo', 'Modelos de alta gama, máximas prestaciones y confort.'),
('Furgoneta', 'Vehículos de gran capacidad, perfectos para mudanzas, carga o grupos grandes.'),
('Motocicleta', 'Vehículos de dos ruedas, muy ágiles y económicos para desplazarse rápidamente por la ciudad.'),
('Todoterreno', 'Vehículos potentes con tracción 4x4, diseñados para circular por terrenos difíciles, arena o campo.'),
('Turismo', 'Vehículos estándar y compactos, ideales para el uso diario y urbano.'),
('Vehículo eléctrico', 'Coches 100% ecológicos con cero emisiones, tecnología avanzada y etiqueta ambiental Cero.');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `dni` varchar(15) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `correo` varchar(100) DEFAULT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `numCarnet` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`dni`, `nombre`, `telefono`, `correo`, `direccion`, `numCarnet`) VALUES
('12345678A', 'Juan Manuel Benítez', '645908567', 'juan.benitez@email.com', 'Calle Mayor 15, Sevilla', 'CARNET-ABC08'),
('89123456C', 'Sebastián Ramírez Santos', '669449573', 'sebastian.ramirez@email.com', 'Calle Betis 8, Triana', 'CARNET-DEF42'),
('91234567B', 'Clara Torres Marcos', '677420712', 'clara.torres@email.com', 'Av. Constitución 42, Dos Hermanas', 'CARNET-XYZ34');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

CREATE TABLE `empleado` (
  `idE` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `puesto` varchar(50) DEFAULT NULL,
  `oficina` varchar(50) DEFAULT NULL,
  `turno` varchar(50) DEFAULT NULL,
  `aniosExp` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empleado`
--

INSERT INTO `empleado` (`idE`, `nombre`, `puesto`, `oficina`, `turno`, `aniosExp`) VALUES
(1, 'Julio Hernández Rueda', 'Recepcionista', 'Oficina Central', 'Mañana', 4),
(2, 'Francisco Rodríguez Sancho', 'Gestor de Flota', 'Oficina Aeropuerto', 'Tarde', 7),
(3, 'Pilar Ortiz Blanco', 'Asesora Comercial', 'Oficina Estación', 'Noche', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `vehiculo`
--

CREATE TABLE `vehiculo` (
  `matricula` varchar(15) NOT NULL,
  `marca` varchar(50) NOT NULL,
  `modelo` varchar(50) NOT NULL,
  `anioFabricacion` int(11) DEFAULT NULL,
  `combustible` varchar(50) DEFAULT NULL,
  `asientos` int(11) DEFAULT NULL,
  `precioPorDia` decimal(10,2) NOT NULL,
  `estadoV` varchar(50) DEFAULT NULL,
  `categoria` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `vehiculo`
--

INSERT INTO `vehiculo` (`matricula`, `marca`, `modelo`, `anioFabricacion`, `combustible`, `asientos`, `precioPorDia`, `estadoV`, `categoria`) VALUES
('1111AAA', 'Seat', 'Ibiza', 2022, 'Gasolina', 5, 35.00, 'Disponible', 'Turismo'),
('2222BBB', 'Renault', 'Traffic', 2021, 'Diesel', 9, 70.00, 'Disponible', 'Furgoneta'),
('3333CCC', 'Tesla', 'Model Y', 2024, 'Eléctrico', 5, 95.00, 'Alquilado', 'Vehículo eléctrico');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `alquiler`
--
ALTER TABLE `alquiler`
  ADD PRIMARY KEY (`idA`),
  ADD KEY `dni` (`dni`),
  ADD KEY `idE` (`idE`),
  ADD KEY `matricula` (`matricula`);

--
-- Indices de la tabla `categoria`
--
ALTER TABLE `categoria`
  ADD PRIMARY KEY (`nombre`);

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`dni`);

--
-- Indices de la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD PRIMARY KEY (`idE`);

--
-- Indices de la tabla `vehiculo`
--
ALTER TABLE `vehiculo`
  ADD PRIMARY KEY (`matricula`),
  ADD KEY `categoria` (`categoria`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `alquiler`
--
ALTER TABLE `alquiler`
  MODIFY `idA` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `empleado`
--
ALTER TABLE `empleado`
  MODIFY `idE` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `alquiler`
--
ALTER TABLE `alquiler`
  ADD CONSTRAINT `alquiler_ibfk_1` FOREIGN KEY (`dni`) REFERENCES `cliente` (`dni`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `alquiler_ibfk_2` FOREIGN KEY (`idE`) REFERENCES `empleado` (`idE`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `alquiler_ibfk_3` FOREIGN KEY (`matricula`) REFERENCES `vehiculo` (`matricula`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `vehiculo`
--
ALTER TABLE `vehiculo`
  ADD CONSTRAINT `vehiculo_ibfk_1` FOREIGN KEY (`categoria`) REFERENCES `categoria` (`nombre`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
