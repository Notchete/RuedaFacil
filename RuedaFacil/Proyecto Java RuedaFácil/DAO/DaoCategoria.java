package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.Categoria;
import Exceptions.ElementoDuplicadoException;
import Conexion.ConexionBD;

public class DaoCategoria {

    // Método para mapear (Convertir ResultSet a Objeto DTO)
    private Categoria mapear(ResultSet rs) throws SQLException {
        Categoria cat = new Categoria();
        cat.setNombre(rs.getString("nombre"));
        cat.setDescripcion(rs.getString("descripcion"));
        return cat;
    }

    // Insertar una nueva categoría
    public boolean insertar(Categoria c) throws Exception {
        if (buscarPorNombre(c.getNombre()) != null) {
            throw new ElementoDuplicadoException("La categoría ya existe");
        }

        String sql = "INSERT INTO CATEGORIA (nombre, descripcion) VALUES (?, ?)";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            
            return ps.executeUpdate() > 0;
        }
    }

    // Buscar una categoría por su nombre
    public Categoria buscarPorNombre(String nombre) throws Exception {
        String sql = "SELECT * FROM CATEGORIA WHERE nombre = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    // Obtener todas las categorías registradas
    public List<Categoria> obtenerTodas() throws Exception {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM CATEGORIA";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // Modificar los datos de una categoría
    public boolean modificar(Categoria c) throws Exception {
        if (buscarPorNombre(c.getNombre()) == null) {
            throw new Exception("La categoría no existe");
        }

        String sql = "UPDATE CATEGORIA SET descripcion = ? WHERE nombre = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, c.getDescripcion());
            ps.setString(2, c.getNombre());
            
            return ps.executeUpdate() > 0;
        }
    }

    // Eliminar una categoría del sistema
    public boolean eliminar(String nombre) throws Exception {
        if (buscarPorNombre(nombre) == null) {
            throw new Exception("La categoría no existe");
        }

        String sql = "DELETE FROM CATEGORIA WHERE nombre = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nombre);
            return ps.executeUpdate() > 0;
        }
    }
}