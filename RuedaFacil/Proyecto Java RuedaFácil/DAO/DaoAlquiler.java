package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Conexion.ConexionBD;
import DTO.Alquiler;
import DTO.Cliente;
import DTO.Empleado;
import DTO.EstadoA;
import DTO.EstadoV;
import DTO.Vehiculo;
import Exceptions.ValidacionException;

public class DaoAlquiler implements PrecioAlquiler {

    // Método para mapear (Convertir ResultSet a Objeto DTO)
    private Alquiler mapear(ResultSet rs) throws SQLException {
        Alquiler a = new Alquiler();
        a.setIdA(rs.getInt("idA"));
        a.setFechaInicio(rs.getDate("fechaInicio").toLocalDate());
        a.setFechaDevolucionPrevista(rs.getDate("fechaDevolucionPrevista").toLocalDate());
        if (rs.getDate("fechaDevolucion") != null) {
            a.setFechaDevolucion(rs.getDate("fechaDevolucion").toLocalDate());
        }
        a.setPrecio(rs.getDouble("precio"));
        a.setEstadoA(EstadoA.valueOf(rs.getString("estadoA")));
        
        Cliente c = new Cliente(); 
        c.setDni(rs.getString("dni")); 
        a.setCliente(c);
        
        Vehiculo v = new Vehiculo(); 
        v.setMatricula(rs.getString("matricula")); 
        a.setVehiculo(v);
        
        Empleado e = new Empleado(); 
        e.setIdE(rs.getInt("idE")); 
        a.setEmpleado(e);
        
        return a;
    }

    // Registrar un nuevo alquiler
    public boolean insertar(Alquiler alquiler) throws Exception {

        String sqlCheckCoche = "SELECT estadoV FROM VEHICULO WHERE matricula = ?";
        String sqlInsert = "INSERT INTO ALQUILER (fechaInicio, fechaDevolucionPrevista, precio, estadoA, dni, matricula, idE) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionBD.obtenerConexion()) {
            
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheckCoche)) {
                psCheck.setString(1, alquiler.getVehiculo().getMatricula());
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        String estadoActual = rs.getString("estadoV");
                        if (estadoActual.equalsIgnoreCase("Alquilado")) {
                            throw new ValidacionException("El vehículo con matrícula " + alquiler.getVehiculo().getMatricula() + " ya se encuentra alquilado.");
                        }
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
                ps.setDate(1, Date.valueOf(alquiler.getFechaInicio()));
                ps.setDate(2, Date.valueOf(alquiler.getFechaDevolucionPrevista()));
                ps.setDouble(3, alquiler.getPrecio());
                ps.setString(4, alquiler.getEstadoA().name()); 
                ps.setString(5, alquiler.getCliente().getDni());
                ps.setString(6, alquiler.getVehiculo().getMatricula());
                ps.setInt(7, alquiler.getEmpleado().getIdE()); 
                
                boolean insertado = ps.executeUpdate() > 0;
                if (insertado) {
                    actualizarEstadoVehiculo(alquiler.getVehiculo().getMatricula(), EstadoV.Alquilado, con);
                }
                return insertado;
            }
        }
    }

    // Buscar un contrato de alquiler por su ID
    public Alquiler buscarPorId(int idA) throws Exception {
        String sql = "SELECT * FROM ALQUILER WHERE idA = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idA);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    // Obtener el historial de alquileres de un cliente específico
    public List<Alquiler> obtenerHistorialPorCliente(String dni) throws Exception {
        List<Alquiler> historial = new ArrayList<>();
        String sql = "SELECT * FROM ALQUILER WHERE dni = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    historial.add(mapear(rs));
                }
            }
        }
        return historial;
    }

    // Obtener todos los alquileres globales del sistema
    public List<Alquiler> obtenerTodos() throws Exception {
        List<Alquiler> lista = new ArrayList<>();
        String sql = "SELECT * FROM ALQUILER";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // Modificar los datos de un alquiler (procesar devoluciones)
    public boolean modificar(Alquiler alquiler) throws Exception {
        if (buscarPorId(alquiler.getIdA()) == null) {
            throw new Exception("El alquiler no existe");
        }

        String sql = "UPDATE ALQUILER SET fechaDevolucion = ?, precio = ?, estadoA = ? WHERE idA = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            if (alquiler.getFechaDevolucion() != null) {
                ps.setDate(1, Date.valueOf(alquiler.getFechaDevolucion()));
            } else {
                ps.setNull(1, java.sql.Types.DATE);
            }
            ps.setDouble(2, alquiler.getPrecio());
            ps.setString(3, alquiler.getEstadoA().name());
            ps.setInt(4, alquiler.getIdA());
            
            boolean modificado = ps.executeUpdate() > 0;
            if (modificado && alquiler.getFechaDevolucion() != null) {
                actualizarEstadoVehiculo(alquiler.getVehiculo().getMatricula(), EstadoV.Disponible, con);
            }
            return modificado;
        }
    }

    // Eliminar un alquiler del sistema
    public boolean eliminar(int idA) throws Exception {
        if (buscarPorId(idA) == null) {
            throw new Exception("El alquiler no existe");
        }

        String sql = "DELETE FROM ALQUILER WHERE idA = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idA);
            return ps.executeUpdate() > 0;
        }
    }

    // Método auxiliar para actualizar el estado del vehículo de forma transaccional
    private void actualizarEstadoVehiculo(String matricula, EstadoV estado, Connection con) throws SQLException {
        String sql = "UPDATE VEHICULO SET estadoV = ? WHERE matricula = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ps.setString(2, matricula);
            ps.executeUpdate();
        }
    }
    
    @Override
    public double calcularPrecioAlquiler(double precioPorDia, int dias) {
        return precioPorDia * dias;
    }
}