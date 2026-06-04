package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Conexion.ConexionBD;
import DTO.Empleado;
import DTO.Turno;

public class DaoEmpleado {

    // Método para mapear (Convertir ResultSet a Objeto DTO)
    private Empleado mapear(ResultSet rs) throws SQLException {
        Empleado e = new Empleado();
        e.setIdE(rs.getInt("idE"));
        e.setNombre(rs.getString("nombre"));
        e.setPuesto(rs.getString("puesto"));
        e.setOficina(rs.getString("oficina"));
        e.setTurno(Turno.valueOf(rs.getString("turno")));
        e.setAniosExp(rs.getInt("aniosExp"));
        return e;
    }

    // Insertar un nuevo empleado
    public boolean insertar(Empleado e) throws Exception {
        String sql = "INSERT INTO EMPLEADO (nombre, puesto, oficina, turno, aniosExp) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getPuesto());
            ps.setString(3, e.getOficina());
            ps.setString(4, e.getTurno().name());
            ps.setInt(5, e.getAniosExp());
            
            return ps.executeUpdate() > 0;
        }
    }

    // Buscar un empleado por su ID
    public Empleado buscarPorId(int idE) throws Exception {
        String sql = "SELECT * FROM EMPLEADO WHERE idE = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idE);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    // Obtener todos los empleados
    public List<Empleado> obtenerTodos() throws Exception {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM EMPLEADO";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // Modificar los datos de un empleado
    public boolean modificar(Empleado e) throws Exception {
        if (buscarPorId(e.getIdE()) == null) {
            throw new Exception("El empleado no existe");
        }

        String sql = "UPDATE EMPLEADO SET nombre = ?, puesto = ?, oficina = ?, turno = ?, aniosExp = ? WHERE idE = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getPuesto());
            ps.setString(3, e.getOficina());
            ps.setString(4, e.getTurno().name());
            ps.setInt(5, e.getAniosExp());
            ps.setInt(6, e.getIdE());
            
            return ps.executeUpdate() > 0;
        }
    }

    // Eliminar un empleado del sistema
    public boolean eliminar(int idE) throws Exception {
        if (buscarPorId(idE) == null) {
            throw new Exception("El empleado no existe");
        }

        String sql = "DELETE FROM EMPLEADO WHERE idE = ?";
        
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idE);
            return ps.executeUpdate() > 0;
        }
    }
}