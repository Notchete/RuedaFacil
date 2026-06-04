package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.Cliente;
import Exceptions.ElementoDuplicadoException;
import Conexion.ConexionBD;

public class DaoCliente {

    // Método para mapear (Convertir ResultSet a Objeto DTO)
    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getString("dni"),
            rs.getString("nombre"),
            rs.getString("telefono"),
            rs.getString("correo"),
            rs.getString("direccion"),
            rs.getString("numCarnet")
        );
    }

    // Insertar un nuevo cliente
    public boolean insertar(Cliente cliente) throws Exception {
        if (buscarPorDni(cliente.getDni()) != null) {
            throw new ElementoDuplicadoException("El cliente ya existe");
        }

        String sql = "INSERT INTO CLIENTE (dni, nombre, telefono, correo, direccion, numCarnet) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, cliente.getDni());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getDireccion());
            ps.setString(6, cliente.getNumCarnet());
            
            return ps.executeUpdate() > 0;
        }
    }

    // Buscar un cliente por su DNI
    public Cliente buscarPorDni(String dni) throws Exception {
        String sql = "SELECT * FROM CLIENTE WHERE dni = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    // Obtener todos los clientes registrados
    public List<Cliente> obtenerTodos() throws Exception {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM CLIENTE";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // Modificar los datos de un cliente
    public boolean modificar(Cliente cliente) throws Exception {
        if (buscarPorDni(cliente.getDni()) == null) {
            throw new Exception("El cliente no existe");
        }

        String sql = "UPDATE CLIENTE SET nombre = ?, telefono = ?, correo = ?, direccion = ?, numCarnet = ? WHERE dni = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getDireccion());
            ps.setString(5, cliente.getNumCarnet());
            ps.setString(6, cliente.getDni());
            
            return ps.executeUpdate() > 0;
        }
    }

    // Eliminar un cliente del sistema
    public boolean eliminar(String dni) throws Exception {
        if (buscarPorDni(dni) == null) {
            throw new Exception("El cliente no existe");
        }

        String sql = "DELETE FROM CLIENTE WHERE dni = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, dni);
            return ps.executeUpdate() > 0;
        }
    }
}