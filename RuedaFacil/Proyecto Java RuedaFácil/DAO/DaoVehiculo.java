package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.Vehiculo;
import DTO.Categoria;
import DTO.Combustible;
import DTO.EstadoV;
import Exceptions.ElementoDuplicadoException;
import Conexion.ConexionBD;

public class DaoVehiculo {

    // Método para mapear (Convertir ResultSet a Objeto DTO)
    private Vehiculo mapear(ResultSet rs) throws SQLException {
        Vehiculo v = new Vehiculo();
        v.setMatricula(rs.getString("matricula"));
        v.setMarca(rs.getString("marca"));
        v.setModelo(rs.getString("modelo"));
        v.setAnioFabricacion(rs.getInt("anioFabricacion"));
        v.setAsientos(rs.getInt("asientos"));
        v.setPrecioPorDia(rs.getDouble("precioPorDia"));
        v.setCombustible(Combustible.valueOf(rs.getString("combustible")));
        v.setEstadoV(EstadoV.valueOf(rs.getString("estadoV")));
        
        Categoria cat = new Categoria();
        cat.setNombre(rs.getString("categoria"));
        v.setCategoria(cat);
        
        return v;
    }

    // Insertar un nuevo vehículo
    public boolean insertar(Vehiculo v) throws Exception {
        if (buscarPorMatricula(v.getMatricula()) != null) {
            throw new ElementoDuplicadoException("El vehículo ya existe");
        }

        String sql = "INSERT INTO VEHICULO (matricula, marca, modelo, anioFabricacion, combustible, asientos, precioPorDia, estadoV, categoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, v.getMatricula());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setInt(4, v.getAnioFabricacion());
            ps.setString(5, v.getCombustible().name()); 
            ps.setInt(6, v.getAsientos());
            ps.setDouble(7, v.getPrecioPorDia());
            ps.setString(8, v.getEstadoV().name()); 
            ps.setString(9, v.getCategoria().getNombre()); 
            
            return ps.executeUpdate() > 0;
        }
    }

    // Buscar un vehículo por su matrícula
    public Vehiculo buscarPorMatricula(String matricula) throws Exception {
        String sql = "SELECT * FROM VEHICULO WHERE matricula = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    // Consultar todos los vehículos disponibles de una categoría específica
    public List<Vehiculo> obtenerDisponiblesPorCategoria(String nombreCategoria) throws Exception {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM VEHICULO WHERE estadoV = 'Disponible' AND categoria = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nombreCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    // Obtener todos los vehículos del catálogo
    public List<Vehiculo> obtenerTodos() throws Exception {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM VEHICULO";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }
    
    // Modificar los datos de un vehículo
    public boolean modificar(Vehiculo v) throws Exception {
        if (buscarPorMatricula(v.getMatricula()) == null) {
            throw new Exception("El vehículo no existe");
        }

        String sql = "UPDATE VEHICULO SET precioPorDia = ?, estadoV = ?, combustible = ?, categoria = ? WHERE matricula = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setDouble(1, v.getPrecioPorDia());
            ps.setString(2, v.getEstadoV().name());
            ps.setString(3, v.getCombustible().name());
            ps.setString(4, v.getCategoria().getNombre());
            ps.setString(5, v.getMatricula());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    // Eliminar un vehículo del sistema
    public boolean eliminar(String matricula) throws Exception {
        if (buscarPorMatricula(matricula) == null) {
            throw new Exception("El vehículo no existe");
        }

        String sql = "DELETE FROM VEHICULO WHERE matricula = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, matricula);
            return ps.executeUpdate() > 0;
        }
    }
}