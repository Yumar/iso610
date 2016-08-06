-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema cinedb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema cinedb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cinedb` ;
USE `cinedb` ;

-- -----------------------------------------------------
-- Table `cinedb`.`cine`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinedb`.`cine` (
  `idcine` INT NOT NULL,
  `nombre` VARCHAR(45) NULL,
  PRIMARY KEY (`idcine`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinedb`.`sala`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinedb`.`sala` (
  `idsala` INT NOT NULL,
  `nombre` VARCHAR(45) NULL,
  `idcine` INT NOT NULL,
  PRIMARY KEY (`idsala`),
  INDEX `fk_sala_cine_idx` (`idcine` ASC),
  CONSTRAINT `fk_sala_cine`
    FOREIGN KEY (`idcine`)
    REFERENCES `cinedb`.`cine` (`idcine`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinedb`.`pelicula`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinedb`.`pelicula` (
  `idpelicula` INT NOT NULL,
  `titulo` VARCHAR(45) NULL,
  `sinopsis` TEXT NULL,
  `ano_produccion` YEAR NULL,
  `nacionalidad` VARCHAR(45) NULL,
  PRIMARY KEY (`idpelicula`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinedb`.`tanda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinedb`.`tanda` (
  `idtanda` INT NOT NULL,
  `fecha` DATETIME NULL,
  `hora_inicio` TIME NULL,
  `hora_fin` TIME NULL,
  `idsala` INT NOT NULL,
  `idpelicula` INT NOT NULL,
  `costo` DECIMAL(7,2) NULL,
  PRIMARY KEY (`idtanda`),
  INDEX `fk_horarios_sala1_idx` (`idsala` ASC),
  INDEX `fk_horarios_pelicula1_idx` (`idpelicula` ASC),
  CONSTRAINT `fk_horarios_sala1`
    FOREIGN KEY (`idsala`)
    REFERENCES `cinedb`.`sala` (`idsala`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_horarios_pelicula1`
    FOREIGN KEY (`idpelicula`)
    REFERENCES `cinedb`.`pelicula` (`idpelicula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinedb`.`artista`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinedb`.`artista` (
  `idartista` INT NOT NULL,
  `nombre` VARCHAR(45) NULL,
  `fecha_nacimiento` DATE NULL,
  PRIMARY KEY (`idartista`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cinedb`.`rol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cinedb`.`rol` (
  `idrol` INT NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) NULL,
  `idartista` INT NOT NULL,
  `idpelicula` INT NOT NULL,
  `rolcol` VARCHAR(45) NULL,
  INDEX `fk_rol_artista1_idx` (`idartista` ASC),
  INDEX `fk_rol_pelicula1_idx` (`idpelicula` ASC),
  PRIMARY KEY (`idrol`),
  CONSTRAINT `fk_rol_artista1`
    FOREIGN KEY (`idartista`)
    REFERENCES `cinedb`.`artista` (`idartista`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_rol_pelicula1`
    FOREIGN KEY (`idpelicula`)
    REFERENCES `cinedb`.`pelicula` (`idpelicula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
