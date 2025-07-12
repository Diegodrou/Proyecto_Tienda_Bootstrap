package prueba;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class AccesoBD {
	private static AccesoBD instanciaUnica = null;
	private Connection conexionBD = null;

	public Connection getConexionBD() {
		return conexionBD;
	}

	public static AccesoBD getInstance() {
		if (instanciaUnica == null) {
			instanciaUnica = new AccesoBD();
		}
		return instanciaUnica;
	}

    private AccesoBD() {
		abrirConexionBD();
	}

	public void abrirConexionBD() {
		if (conexionBD == null)
		{
			String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
			// daw es el nombre de la base de datos que hemos creado con anterioridad.
			String DB_URL = "jdbc:mariadb://localhost:3307/mi_tienda_base";
			// El usuario root y su clave son los que se puso al instalar MariaDB.
			String USER = "root";
			String PASS = "DawLab";
			try {
				Class.forName(JDBC_DRIVER);
				conexionBD = DriverManager.getConnection(DB_URL,USER,PASS);
			}
			catch(Exception e) {
				System.err.println("No se ha podido conectar a la base de datos");
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

    public boolean comprobarAcceso() {
		abrirConexionBD();
		return (conexionBD != null);
	}

	public List<ProductoBD> obtenerProductosBD() {
	abrirConexionBD();
	ArrayList<ProductoBD> productos = new ArrayList<>();

	try {
		String query = "SELECT codigo,descripcion,precio,existencias,imagen,nombreAlbum FROM productos"; // Hay que tener en cuenta las columnas de la tabla de productos
		PreparedStatement s = conexionBD.prepareStatement(query);
		ResultSet resultado = s.executeQuery();
		while(resultado.next()){
			ProductoBD producto = new ProductoBD();
			producto.setCodigo(resultado.getInt("codigo"));
			producto.setDescripcion(resultado.getString("descripcion"));
			producto.setPrecio(resultado.getFloat("precio"));
			producto.setStock(resultado.getInt("existencias"));
			producto.setImagen(resultado.getString("imagen"));
			producto.setNombreAlbum(resultado.getString("nombreAlbum"));
			productos.add(producto);
		}
	} catch(Exception e) {
		System.err.println("Error ejecutando la consulta a la base de datos");
		System.err.println(e.getMessage());
	}
	return productos;
}

public int comprobarUsuarioBD(String usuario, String clave) {
	abrirConexionBD();

	int codigo = -1;

	try{
		String con = "SELECT codigo FROM usuarios WHERE usuario=? AND clave=?";
		PreparedStatement s = conexionBD.prepareStatement(con);
		s.setString(1,usuario);
		s.setString(2,clave);

		ResultSet resultado = s.executeQuery();

		// El usuario/clave se encuentra en la BD

		if ( resultado.next() ) {
			codigo =  resultado.getInt("codigo");
		}
	}
	catch(Exception e) {

		// Error en la conexión con la BD
		System.err.println("Error verificando usuario/clave");
		System.err.println(e.getMessage());
		e.printStackTrace();
	}

	return codigo;
}

public boolean agregarUsuario(String nombre_de_usuario,String nombre,String apellidos,String password,String direccion, String telefono,StringBuilder errores){
	boolean agregadoExitosamente = false;
	// Primero verificamos si el usuario ya existe
    try {
        String checkQuery = "SELECT usuario FROM usuarios WHERE usuario = ?";
        PreparedStatement checkStatement = conexionBD.prepareStatement(checkQuery);
        checkStatement.setString(1, nombre_de_usuario);
        ResultSet result = checkStatement.executeQuery();
        
        if (result.next()) {
            // El usuario ya existe, no podemos agregarlo
            System.err.println("Error: Ya existe un usuario con ese nombre de usuario");
            return agregadoExitosamente;
        }
    } catch(Exception e) {
        System.err.println("Error verificando usuario existente");
        System.err.println(e.getMessage());
        e.printStackTrace();
		errores.append(e.getMessage());
        return agregadoExitosamente;
    }
	
	int codigo = 0;
	try{
		codigo = generarCodigoNumericoSecuencial();
	}catch(Exception e){
		// Error en la conexión con la BD
		System.err.println("Error registrando usuario");
		System.err.println(e.getMessage());
		e.printStackTrace();
		errores.append(e.getMessage());
		return agregadoExitosamente;
	}
	
	try{
		String q = "INSERT INTO usuarios (codigo,usuario,clave,nombre,apellidos,domicilio,telefono) VALUES (?,?,?,?,?,?,?)";
		PreparedStatement s = conexionBD.prepareStatement(q);
		s.setString(1,Integer.toString(codigo));
		s.setString(2,nombre_de_usuario);
		s.setString(3,password);
		s.setString(4,nombre);
		s.setString(5,apellidos);
		s.setString(6,direccion);
		s.setString(7,telefono);	
		
		int resultado = s.executeUpdate();

		if ( resultado > 0 ) {
			agregadoExitosamente = true;
		}

	}
	catch(Exception e){
		// Error en la conexión con la BD
		System.err.println("Error registrando usuario");
		System.err.println(e.getMessage());
		e.printStackTrace();
		errores.append(e.getMessage());
	}
	


	return agregadoExitosamente;
}

private int generarCodigoNumericoSecuencial() throws SQLException {
    // Primero intentamos obtener el máximo código existente
    int ultimoCodigo = obtenerUltimoCodigo();
    int nuevoCodigo = ultimoCodigo + 1;
    
    // Verificar disponibilidad (por si hay huecos)
    while(!esCodigoDisponible(nuevoCodigo) && nuevoCodigo < Integer.MAX_VALUE) {
        nuevoCodigo++;
    }
    
    if(nuevoCodigo >= Integer.MAX_VALUE) {
        throw new SQLException("No se pudo generar un código numérico disponible");
    }
    
    return nuevoCodigo;
}

private int obtenerUltimoCodigo() throws SQLException {
    try (PreparedStatement stmt = conexionBD.prepareStatement(
            "SELECT MAX(codigo) FROM usuarios WHERE codigo IS NOT NULL")) {
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getInt(1) : 0; // Si no hay registros, empezar desde 0
    }
}

private boolean esCodigoDisponible(int codigo) throws SQLException {
    try (PreparedStatement stmt = conexionBD.prepareStatement(
            "SELECT codigo FROM usuarios WHERE codigo = ?")) {
        stmt.setInt(1, codigo);
        return !stmt.executeQuery().next(); // True si está disponible
    }
}

/**
     * Obtiene las existencias actuales de un producto en la base de datos
     * @param codigo El ID del producto a consultar
     * @return La cantidad de existencias disponibles, o -1 si hay un error
     * @throws SQLException Si ocurre un error al acceder a la base de datos
     */
public int obtenerExistencias(int codigo) throws SQLException {
	// Consulta SQL para obtener las existencias del producto
	String sql = "SELECT existencias FROM productos WHERE codigo = ?";
        
	try (PreparedStatement stmt = conexionBD.prepareStatement(sql)) {
		stmt.setInt(1, codigo);
		
		try (ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("existencias");
			} else {
				// Producto no encontrado
				return -1;
			}
		}
	}
	}


public PreparedStatement prepareStatementWrap(String sql) throws SQLException{
	return conexionBD.prepareStatement(sql);
}

public List<String> getAtributosSession(int codigoUsuario) {
    List<String> atributosSession = new ArrayList<>(4); // Capacidad inicial para 4 elementos
    
    // Valores por defecto
    String nombre = "";
    String apellidos = "";
    String direccion = "";
    String telefono = "";

    try (
         PreparedStatement ps = conexionBD.prepareStatement(
             "SELECT nombre, apellidos, domicilio, telefono FROM usuarios WHERE codigo = ?")) {
        
        ps.setInt(1, codigoUsuario);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                nombre = rs.getString("nombre") != null ? rs.getString("nombre") : "";
                apellidos = rs.getString("apellidos") != null ? rs.getString("apellidos") : "";
                direccion = rs.getString("domicilio") != null ? rs.getString("domicilio") : "";
                telefono = rs.getString("telefono") != null ? rs.getString("telefono") : "";
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener atributos de sesión: " + e.getMessage());
        // Se mantienen los valores por defecto
    }
    
    // Añadir los valores a la lista en orden específico
    atributosSession.add(nombre);
    atributosSession.add(apellidos);
    atributosSession.add(direccion);
    atributosSession.add(telefono);
    
    return atributosSession;
}

}
