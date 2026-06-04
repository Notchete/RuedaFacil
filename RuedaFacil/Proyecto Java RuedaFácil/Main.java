import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;

import DAO.DaoAlquiler;
import DAO.DaoCategoria;
import DAO.DaoCliente;
import DAO.DaoEmpleado;
import DAO.DaoVehiculo;
import DTO.Alquiler;
import DTO.Categoria;
import DTO.Cliente;
import DTO.Combustible;
import DTO.Empleado;
import DTO.EstadoA;
import DTO.EstadoV;
import DTO.Turno;
import DTO.Vehiculo;
import Exceptions.ElementoDuplicadoException;
import Exceptions.ValidacionException;

public class Main {

    private static DaoCliente daoCliente = new DaoCliente();
    private static DaoVehiculo daoVehiculo = new DaoVehiculo();
    private static DaoAlquiler daoAlquiler = new DaoAlquiler();
    private static DaoCategoria daoCategoria = new DaoCategoria();
    private static DaoEmpleado daoEmpleado = new DaoEmpleado();
    
    private static Scanner scanner = new Scanner(System.in);
    // MENU PRINCIPAL
    public static void main(String[] args) {
        int opcion = -1;
        
        do {
            System.out.println("\n|==== RUEDAFACIL - MENU PRINCIPAL ====|");
            System.out.println("1. Gestión de Clientes");
            System.out.println("2. Gestión de Vehículos");
            System.out.println("3. Gestión de Alquileres");
            System.out.println("4. Gestión de Categorías");
            System.out.println("5. Gestión de Empleados");
            System.out.println("0. Salir del Sistema");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1: menuClientes(); break;
                    case 2: menuVehiculos(); break;
                    case 3: menuAlquileres(); break;
                    case 4: menuCategorias(); break;
                    case 5: menuEmpleados(); break;
                    case 0: 
                        System.out.println("Cerrando Sistema RuedaFácil..."); 
                        scanner.close();
                        break;
                    default: System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, introduzca una opción válida.");
            }
        } while (opcion != 0);
    }

    // 1. SUBMENU CLIENTES
    private static void menuClientes() {
        int op = -1;
        do {
            System.out.println("\n|-- GESTION DE CLIENTES --|");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Buscar cliente por DNI");
            System.out.println("3. Listar todos los clientes");
            System.out.println("4. Modificar cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            try {
                op = Integer.parseInt(scanner.nextLine());
                switch (op) {
                    case 1:
                        System.out.print("DNI: "); String dni = scanner.nextLine();
                        System.out.print("Nombre Completo: "); String nombre = scanner.nextLine();
                        System.out.print("Teléfono: "); String tlf = scanner.nextLine();
                        System.out.print("Correo: "); String correo = scanner.nextLine();
                        System.out.print("Dirección: "); String dir = scanner.nextLine();
                        System.out.print("Número de Carnet: "); String carnet = scanner.nextLine();
                        try {
                            if (daoCliente.insertar(new Cliente(dni, nombre, tlf, correo, dir, carnet))) {
                                System.out.println("Cliente registrado con éxito.");
                            }
                        } catch (ElementoDuplicadoException e) { 
                            System.out.println("Error: " + e.getMessage()); 
                        }
                        break;
                    case 2:
                        System.out.print("DNI a buscar: ");
                        Cliente cBusq = daoCliente.buscarPorDni(scanner.nextLine());
                        if(cBusq != null) {
                            System.out.println("DNI: " + cBusq.getDni() + " | Nombre: " + cBusq.getNombre() + 
                                               " | Teléfono: " + cBusq.getTelefono() + " | Correo: " + cBusq.getCorreo() + 
                                               " | Dirección: " + cBusq.getDireccion() + " | Carnet: " + cBusq.getNumCarnet());
                        } else System.out.println("Cliente no encontrado.");
                        break;
                    case 3:
                        List<Cliente> clientes = daoCliente.obtenerTodos();
                        for(Cliente c : clientes) {
                            System.out.println("DNI: " + c.getDni() + " | Nombre: " + c.getNombre() + 
                                               " | Teléfono: " + c.getTelefono() + " | Correo: " + c.getCorreo() + 
                                               " | Dirección: " + c.getDireccion() + " | Carnet: " + c.getNumCarnet());
                        }
                        break;
                    case 4:
                        System.out.print("DNI del cliente a modificar: "); String dniMod = scanner.nextLine();
                        Cliente cMod = daoCliente.buscarPorDni(dniMod);
                        if(cMod != null) {
                            System.out.print("Nuevo Nombre: "); cMod.setNombre(scanner.nextLine());
                            System.out.print("Nuevo Teléfono: "); cMod.setTelefono(scanner.nextLine());
                            System.out.print("Nuevo Correo: "); cMod.setCorreo(scanner.nextLine());
                            System.out.print("Nueva Dirección: "); cMod.setDireccion(scanner.nextLine());
                            System.out.print("Nuevo Número de Carnet: "); cMod.setNumCarnet(scanner.nextLine());
                            try {
                                if (daoCliente.modificar(cMod)) {
                                    System.out.println("Datos del cliente actualizados con éxito.");
                                }
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else System.out.println("El cliente indicado no existe.");
                        break;
                    case 5:
                        System.out.print("DNI del cliente a eliminar: ");
                        try {
                            if (daoCliente.eliminar(scanner.nextLine())) {
                                System.out.println("Cliente eliminado con éxito.");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, introduzca una opción válida.");
            } catch (Exception e) { 
                System.err.println("Error crítico en Clientes: " + e.getMessage()); 
            }
        } while (op != 0);
    }

    // 2. SUBMENU VEHICULOS
    private static void menuVehiculos() {
        int op = -1;
        do {
            System.out.println("\n|-- GESTION DE VEHICULOS --|");
            System.out.println("1. Registrar vehículo");
            System.out.println("2. Buscar vehículos disponibles por categoría");
            System.out.println("3. Buscar vehículo por matricula");
            System.out.println("4. Listar flota de vehículos");
            System.out.println("5. Modificar vehículo");
            System.out.println("6. Eliminar vehículo");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            try {
                op = Integer.parseInt(scanner.nextLine());
                switch (op) {
                    case 1:
                        Vehiculo v = new Vehiculo();
                        System.out.print("Matrícula: "); v.setMatricula(scanner.nextLine());
                        System.out.print("Marca: "); v.setMarca(scanner.nextLine());
                        System.out.print("Modelo: "); v.setModelo(scanner.nextLine());
                        System.out.print("Año de fabricación: "); v.setAnioFabricacion(Integer.parseInt(scanner.nextLine()));
                        System.out.print("Asientos: "); v.setAsientos(Integer.parseInt(scanner.nextLine()));
                        System.out.print("Precio/día (EUR): "); v.setPrecioPorDia(Double.parseDouble(scanner.nextLine()));
                        System.out.print("Combustible (Gasolina, Diesel, Electrico, Hibrido): "); v.setCombustible(Combustible.valueOf(scanner.nextLine()));
                        System.out.print("Estado (Disponible, Alquilado, Mantenimiento, Baja): "); v.setEstadoV(EstadoV.valueOf(scanner.nextLine()));
                        System.out.print("Categoría: "); 
                        Categoria cat = new Categoria(); cat.setNombre(scanner.nextLine()); v.setCategoria(cat);
                        try { 
                            if (daoVehiculo.insertar(v)) {
                                System.out.println("Vehículo registrado con éxito.");
                            }
                        } catch (ElementoDuplicadoException e) { 
                            System.out.println("Error: " + e.getMessage()); 
                        }
                        break;
                    case 2:
                        System.out.print("Categoria a consultar: ");
                        List<Vehiculo> libres = daoVehiculo.obtenerDisponiblesPorCategoria(scanner.nextLine());
                        for (Vehiculo l : libres) {
                            System.out.println("Matrícula: " + l.getMatricula() + " | Marca: " + l.getMarca() + 
                                               " | Precio/Día: " + l.getPrecioPorDia() + " | Estado: " + l.getEstadoV());
                        }
                        break;
                    case 3:
                        System.out.print("Matrícula a buscar: ");
                        Vehiculo vBusq = daoVehiculo.buscarPorMatricula(scanner.nextLine());
                        if(vBusq != null) {
                            System.out.println("Matrícula: " + vBusq.getMatricula() + " | Marca: " + vBusq.getMarca() + 
                                               " | Modelo: " + vBusq.getModelo() + " | Año: " + vBusq.getAnioFabricacion() + 
                                               " | Precio/Día: " + vBusq.getPrecioPorDia() + " | Combustible: " + vBusq.getCombustible() + 
                                               " | Estado: " + vBusq.getEstadoV() + " | Categoría: " + vBusq.getCategoria().getNombre());
                        } else System.out.println("Vehículo no encontrado.");
                        break;
                    case 4:
                        List<Vehiculo> flota = daoVehiculo.obtenerTodos();
                        for(Vehiculo f : flota) {
                            System.out.println("Matrícula: " + f.getMatricula() + " | Marca: " + f.getMarca() + " | Modelo: " + f.getModelo() + 
                                               " | Estado: " + f.getEstadoV() + " | Categoría: " + f.getCategoria().getNombre());
                        }
                        break;
                    case 5:
                        System.out.print("Matrícula del vehículo a modificar: ");
                        Vehiculo vMod = daoVehiculo.buscarPorMatricula(scanner.nextLine());
                        if (vMod != null) {
                            System.out.print("Nuevo Precio/día: "); vMod.setPrecioPorDia(Double.parseDouble(scanner.nextLine()));
                            System.out.print("Nuevo Estado (Disponible, Alquilado, Mantenimiento, Baja): "); vMod.setEstadoV(EstadoV.valueOf(scanner.nextLine()));
                            System.out.print("Nuevo Combustible: "); vMod.setCombustible(Combustible.valueOf(scanner.nextLine()));
                            System.out.print("Nueva Categoría: "); vMod.getCategoria().setNombre(scanner.nextLine());
                            try {
                                if (daoVehiculo.modificar(vMod)) {
                                    System.out.println("Datos del vehículo actualizados con éxito.");
                                }
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else System.out.println("El vehículo indicado no existe.");
                        break;
                    case 6:
                        System.out.print("Matrícula a eliminar: "); 
                        try {
                            if (daoVehiculo.eliminar(scanner.nextLine())) {
                                System.out.println("Vehículo eliminado con éxito.");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, introduzca una opción válida.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: El valor introducido para Estado o Combustible no es válido.");
            } catch (Exception e) { 
                System.err.println("Error crítico en Vehículos: " + e.getMessage()); 
            }
        } while (op != 0);
    }

    // 3. SUBMENU ALQUILERES
    private static void menuAlquileres() {
        int op = -1;
        do {
            System.out.println("\n|-- GESTION DE ALQUILERES --|");
            System.out.println("1. Registrar alquiler");
            System.out.println("2. Consultar historial de cliente");
            System.out.println("3. Buscar alquiler por ID");
            System.out.println("4. Listar todos los alquileres");
            System.out.println("5. Modificar alquiler (Devolucion)");
            System.out.println("6. Eliminar alquiler");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            try {
                op = Integer.parseInt(scanner.nextLine());
                switch (op) {
                case 1:
                    Alquiler a = new Alquiler();
                    Cliente c = new Cliente(); 
                    Empleado eResp = new Empleado();
                    
                    System.out.print("DNI del Cliente: "); c.setDni(scanner.nextLine());
                    System.out.print("Matricula del Vehiculo: "); String mat = scanner.nextLine();
                    System.out.print("ID del Empleado responsable: "); eResp.setIdE(Integer.parseInt(scanner.nextLine()));
                    System.out.print("Dias de alquiler previstos: "); int dias = Integer.parseInt(scanner.nextLine());
                    
                    Vehiculo vEncontrado = daoVehiculo.buscarPorMatricula(mat);
                    if (vEncontrado == null) {
                        System.out.println("El vehículo indicado no existe.");
                        break;
                    }
                    
                    double precioFinal = daoAlquiler.calcularPrecioAlquiler(vEncontrado.getPrecioPorDia(), dias);
                    System.out.println("Precio total calculado automáticamente (EUR): " + precioFinal);
                    
                    a.setPrecio(precioFinal);
                    a.setFechaInicio(LocalDate.now());
                    a.setFechaDevolucionPrevista(LocalDate.now().plusDays(dias));
                    a.setEstadoA(EstadoA.Activo);
                    a.setCliente(c); 
                    a.setVehiculo(vEncontrado); 
                    a.setEmpleado(eResp);
                    
                    try { 
                        if (daoAlquiler.insertar(a)) {
                            System.out.println("El alquiler ha sido registrado exitosamente en el sistema.");
                        }
                    } catch (ValidacionException e) { 
                        System.out.println("Error de validación: " + e.getMessage()); 
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("DNI del cliente: ");
                    List<Alquiler> historial = daoAlquiler.obtenerHistorialPorCliente(scanner.nextLine());
                    if (historial.isEmpty()) {
                        System.out.println("No hay registros de alquiler para este cliente.");
                    } else {
                        for (Alquiler h : historial) {
                            System.out.println("ID: " + h.getIdA() + " | Vehículo: " + h.getVehiculo().getMatricula() + 
                                               " | Previsto: " + h.getFechaDevolucionPrevista() + " | Real: " + h.getFechaDevolucion() + 
                                               " | Estado: " + h.getEstadoA());
                        }
                    }
                    break;
                case 3:
                    System.out.print("ID del alquiler a buscar: ");
                    Alquiler aBusq = daoAlquiler.buscarPorId(Integer.parseInt(scanner.nextLine()));
                    if(aBusq != null) {
                        System.out.println("ID Alquiler: " + aBusq.getIdA() + " | DNI Cliente: " + aBusq.getCliente().getDni() + 
                                           " | Matrícula: " + aBusq.getVehiculo().getMatricula() + " | ID Empleado: " + aBusq.getEmpleado().getIdE() +
                                           " | Inicio: " + aBusq.getFechaInicio() + " | Devolución: " + aBusq.getFechaDevolucion() + 
                                           " | Precio: " + aBusq.getPrecio() + " | Estado: " + aBusq.getEstadoA());
                    } else {
                        System.out.println("Alquiler no encontrado.");
                    }
                    break;
                case 4:
                    List<Alquiler> todos = daoAlquiler.obtenerTodos();
                    if (todos.isEmpty()) {
                        System.out.println("No hay alquileres registrados en el sistema.");
                    } else {
                        for(Alquiler al : todos) {
                            System.out.println("ID: " + al.getIdA() + " | DNI Cliente: " + al.getCliente().getDni() + 
                                               " | Matrícula: " + al.getVehiculo().getMatricula() + " | Estado: " + al.getEstadoA());
                        }
                    }
                    break;
                    case 5:
                        System.out.print("ID del alquiler a procesar devolución: ");
                        Alquiler aMod = daoAlquiler.buscarPorId(Integer.parseInt(scanner.nextLine()));
                        if (aMod != null) {
                            aMod.setFechaDevolucion(LocalDate.now());
                            aMod.setEstadoA(EstadoA.Finalizado);
                            try {
                                if (daoAlquiler.modificar(aMod)) {
                                    System.out.println("Devolución procesada con éxito y vehículo liberado.");
                                }
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else System.out.println("Alquiler inexistente.");
                        break;
                    case 6:
                        System.out.print("ID del alquiler a eliminar: "); 
                        try {
                            if (daoAlquiler.eliminar(Integer.parseInt(scanner.nextLine()))) {
                                System.out.println("Alquiler eliminado con éxito.");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, introduzca una opción, ID o número de días válido.");
            } catch (Exception e) { 
                System.err.println("Error crítico en Alquileres: " + e.getMessage()); 
            }
        } while (op != 0);
    }

    // 4. SUBMENU CATEGORIAS
    private static void menuCategorias() {
        int op = -1;
        do {
            System.out.println("\n|-- GESTION DE CATEGORIAS --|");
            System.out.println("1. Registrar categoría");
            System.out.println("2. Buscar categoría por nombre");
            System.out.println("3. Listar todas las categorías");
            System.out.println("4. Modificar categoría");
            System.out.println("5. Eliminar categoría");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            try {
                op = Integer.parseInt(scanner.nextLine());
                switch (op) {
                    case 1:
                        Categoria cat = new Categoria();
                        System.out.print("Nombre de la categoría: "); cat.setNombre(scanner.nextLine());
                        System.out.print("Descripción: "); cat.setDescripcion(scanner.nextLine());
                        try { 
                            if (daoCategoria.insertar(cat)) {
                                System.out.println("Categoría añadida con éxito.");
                            }
                        } catch (ElementoDuplicadoException e) { 
                            System.out.println("Error: " + e.getMessage()); 
                        }
                        break;
                    case 2:
                        System.out.print("Nombre a buscar: ");
                        Categoria cBusq = daoCategoria.buscarPorNombre(scanner.nextLine());
                        if(cBusq != null) {
                            System.out.println("Nombre: " + cBusq.getNombre() + " | Descripción: " + cBusq.getDescripcion());
                        } else System.out.println("Categoría no encontrada.");
                        break;
                    case 3:
                        List<Categoria> cats = daoCategoria.obtenerTodas();
                        for(Categoria c : cats) {
                            System.out.println("Nombre: " + c.getNombre() + " | Descripción: " + c.getDescripcion());
                        }
                        break;
                    case 4:
                        System.out.print("Nombre de la categoría a modificar: ");
                        Categoria cMod = daoCategoria.buscarPorNombre(scanner.nextLine());
                        if (cMod != null) {
                            System.out.print("Nueva descripción: "); cMod.setDescripcion(scanner.nextLine());
                            try {
                                if (daoCategoria.modificar(cMod)) {
                                    System.out.println("Datos de la categoría actualizados con éxito.");
                                }
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else System.out.println("Categoría no encontrada.");
                        break;
                    case 5:
                        System.out.print("Nombre de categoría a eliminar: "); 
                        try {
                            if (daoCategoria.eliminar(scanner.nextLine())) {
                                System.out.println("Categoría eliminada con éxito.");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, introduzca una opción válida.");
            } catch (Exception e) { 
                System.err.println("Error crítico en Categorías: " + e.getMessage()); 
            }
        } while (op != 0);
    }

    // 5. SUBMENU EMPLEADOS
    private static void menuEmpleados() {
        int op = -1;
        do {
            System.out.println("\n|-- GESTION DE EMPLEADOS --|");
            System.out.println("1. Registrar empleado");
            System.out.println("2. Buscar empleado por ID");
            System.out.println("3. Listar todos los empleados");
            System.out.println("4. Modificar empleado");
            System.out.println("5. Eliminar empleado");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            try {
                op = Integer.parseInt(scanner.nextLine());
                switch (op) {
                    case 1:
                        Empleado emp = new Empleado();
                        System.out.print("Nombre Completo: "); emp.setNombre(scanner.nextLine());
                        System.out.print("Puesto: "); emp.setPuesto(scanner.nextLine());
                        System.out.print("Oficina: "); emp.setOficina(scanner.nextLine());
                        System.out.print("Turno (Mañana, Tarde, Noche): "); emp.setTurno(Turno.valueOf(scanner.nextLine()));
                        System.out.print("Años de experiencia: "); emp.setAniosExp(Integer.parseInt(scanner.nextLine()));
                        try {
                            if (daoEmpleado.insertar(emp)) {
                                System.out.println("Empleado registrado con éxito.");
                            }
                        } catch (ElementoDuplicadoException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 2:
                        System.out.print("ID del Empleado a buscar: "); 
                        Empleado eBusq = daoEmpleado.buscarPorId(Integer.parseInt(scanner.nextLine()));
                        if(eBusq != null) {
                            System.out.println("ID: " + eBusq.getIdE() + " | Nombre: " + eBusq.getNombre() + 
                                               " | Puesto: " + eBusq.getPuesto() + " | Oficina: " + eBusq.getOficina() + 
                                               " | Turno: " + eBusq.getTurno() + " | Años Experiencia: " + eBusq.getAniosExp());
                        } else System.out.println("Empleado no encontrado.");
                        break;
                    case 3:
                        List<Empleado> emps = daoEmpleado.obtenerTodos();
                        for(Empleado e : emps) {
                            System.out.println("ID: " + e.getIdE() + " | Nombre: " + e.getNombre() + " | Puesto: " + e.getPuesto() + 
                                               " | Turno: " + e.getTurno());
                        }
                        break;
                    case 4:
                        System.out.print("ID del Empleado a modificar: "); 
                        Empleado eMod = daoEmpleado.buscarPorId(Integer.parseInt(scanner.nextLine()));
                        if(eMod != null) {
                            System.out.print("Nuevo Nombre: "); eMod.setNombre(scanner.nextLine());
                            System.out.print("Nuevo Puesto: "); eMod.setPuesto(scanner.nextLine());
                            System.out.print("Nueva Oficina: "); eMod.setOficina(scanner.nextLine());
                            System.out.print("Nuevo Turno (Mañana, Tarde, Noche): "); eMod.setTurno(Turno.valueOf(scanner.nextLine()));
                            System.out.print("Nuevos Años de experiencia: "); eMod.setAniosExp(Integer.parseInt(scanner.nextLine()));
                            try {
                                if (daoEmpleado.modificar(eMod)) {
                                    System.out.println("Datos del empleado actualizados con éxito.");
                                }
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else System.out.println("El empleado indicado no existe.");
                        break;
                    case 5:
                        System.out.print("ID del Empleado a eliminar: "); 
                        try {
                            if (daoEmpleado.eliminar(Integer.parseInt(scanner.nextLine()))) {
                                System.out.println("Empleado eliminado con éxito.");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, introduzca una opción válida.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: El valor introducido para Turno no es válido.");
            } catch (Exception e) { 
                System.err.println("Error crítico en Empleados: " + e.getMessage()); 
            }
        } while (op != 0);
    }
}